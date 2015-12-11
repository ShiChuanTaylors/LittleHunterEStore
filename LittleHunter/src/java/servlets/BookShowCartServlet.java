package servlets;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import database.*;
import cart.*;
import exception.*;
import javax.xml.ws.WebServiceRef;
import org.me.currencyexchange.CurrencyExWS_Service;
import RMIgst.*;

public class BookShowCartServlet extends HttpServlet {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/192.168.43.15_58526/CurrencyExWS/CurrencyExWS.wsdl")
    private CurrencyExWS_Service service;
    private BookDBAO bookDB;
    @Override
    public void init() throws ServletException {
        bookDB = (BookDBAO) getServletContext().getAttribute("bookDB");
        
        if (bookDB == null) throw new UnavailableException("Couldn't get database.");
    }
    @Override
    public void destroy() {bookDB = null;}
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        response.setContentType("text/html");
        response.setBufferSize(8192);
        String contextPath = request.getContextPath();
        PrintWriter out = response.getWriter();
        //out.println("<html><head><title>Shopping Cart</title></head>");
        out.println(BannerServlet.htmlHeader);
        getServletContext().getRequestDispatcher("/Banner").include(request, response);
        String bookId = request.getParameter("Remove");
        BookDetails bd;
        if (bookId != null) {
            try {
                bd = bookDB.getBookDetails(bookId);
                cart.remove(bookId);
                out.println("<font color='red' size='+2'>You just removed <strong>" +
                    bd.getShirtName() + "</strong> <br> &nbsp; <br></font>");
            } catch (BookNotFoundException ex) {
                response.reset();
                throw new ServletException(ex);
            }
        } else if (request.getParameter("Clear") != null) {
            cart.clear();
            //out.println("<font color='red' size='+2'><strong></strong> <br>&nbsp; <br> </font>");
            out.println("<script>\n" +
                    "      function toastNotification(){\n" +
                    "           Materialize.toast('You just cleared your shopping cart!', 3000);" +
                    "      }\n" +
                    "      \n" +
                    "      setTimeout(toastNotification, 1000);\n" +
                    "    </script>");
        } else if (request.getParameter("CurrencyEx") != null) {
            // Rate exchange, calling web services
            String countryTo = request.getParameter("CurrencyEx");
            float value = 1;
            //currencyExchange("myr",countryTo);
            value = convert("myr", countryTo);
            
            if(countryTo.equals("usd")){
                cart.setMoneySymbol("USD");
            }
            else if(countryTo.equals("aud")){
                cart.setMoneySymbol("AUD");
            }
            else{
                cart.setMoneySymbol("MYR");
            }
            cart.setCurrencyExRate(value);
            //out.println(countryTo + cart.getCurrencyExRate());
            
        }

        // Print a summary of the shopping cart
        int num = cart.getNumberOfItems();
        if (num > 0) {
            out.println("<font size='+2'>Your shopping cart contains " + num +
                ((num == 1) ? " item":" items") + "</font><br>&nbsp;");

            // Return the Shopping Cart
            out.println("<table class=\"striped\" summary='layout'><thead><tr>" +
                "<th align='left'>Quantity</th>" + 
                "<th align='left'>Title</th>" + 
                "<th align='left'>Price</th></thead></tr>");

            Iterator i = cart.getItems().iterator();
            while (i.hasNext()) {
                ShoppingCartItem item = (ShoppingCartItem) i.next();
                bd = (BookDetails) item.getItem();
                out.println("<tr>" +
                    "<td align='right' bgcolor='white'>" + item.getQuantity() + "</td>" + 
                    "<td bgcolor='#ffffaa'><strong><a href='" +
                        response.encodeURL(contextPath+"/BookDetails?Id=" + bd.getId()) + "'>" +
                        bd.getShirtName() + "</a></strong></td>" +
                    "<td bgcolor='#ffffaa' align='right'>"+ cart.getMoneySymbol() +"&nbsp;" + String.format("%.2f",bd.getPrice() * cart.getCurrencyExRate())  +"</td>" + 
                    "<td bgcolor='#ffffaa'><strong><a href='" + response.encodeURL(contextPath+
                        "/BookShowCart?Remove=" + bd.getId()) + "'>Remove Item</a></strong></td></tr>");
            }

            // Print the total at the bottom of the table
            out.println("<tr><td colspan='5' bgcolor='white'><br></td></tr>" + 
                    "<tr><td colspan='2' align='right' bgcolor='white'>Subtotal</td>" +
                           "<td bgcolor='#ffffaa' align='right'>" + cart.getMoneySymbol() +"&nbsp;" + String.format("%.2f",cart.getTotal() * cart.getCurrencyExRate())+ "</td>" + 
                           ""
                    + "<td><a href='" +
                response.encodeURL(contextPath+ "/BookShowCart?CurrencyEx=usd") +
                "' class=\"btn-large waves-effect waves-light black lighten-1\">Convert to US Dollar</a>"
                    + " &nbsp; <a href='" +
                response.encodeURL(contextPath+ "/BookShowCart?CurrencyEx=aud") +
                "' class=\"btn-large waves-effect waves-light black lighten-1\">Convert to Australian Dollar</a>"
                    );
            if(!cart.getMoneySymbol().equals("MYR")) {
                out.println("<a href='" +
                response.encodeURL(contextPath+ "/BookShowCart?CurrencyEx=myr") +
                "' class=\"btn-large waves-effect waves-light black lighten-1\">Convert to MYR</a>");
            }
            
            
            
            /* RMI to check if gst rate is needed to display */
            double gstRate =0;//= new GstClient().
            gstRate = new GstClient().calc(cart.getMoneySymbol());
            
            System.out.println(gstRate);
            if(gstRate != 0) {
                out.println("<tr>"
                        + "     <td>GST Rate</td>"
                        + "     <td>" + (gstRate * 100) + "%</td>" 
                        + "<tr>"
                        + "<tr>"
                        + "     <td>Total</td>"
                        + "     <td>"+ cart.getMoneySymbol() + "&nbsp;" + (cart.getTotal() * (gstRate + 1)) + "</td>" 
                        + "<tr>");
            }
            
            out.println("</td></tr></table>");
            
            out.println("<p> &nbsp; <p><a href='" +
                response.encodeURL(contextPath+ "/BookCatalog") +
                "' class=\"btn-large waves-effect waves-light teal lighten-1\">Continue Shopping</a> &nbsp; &nbsp; &nbsp;<a href='" +
                response.encodeURL(contextPath+ "/BookCashier") +
                "' class=\"btn-large waves-effect waves-light teal lighten-1\">Check Out</a> &nbsp; &nbsp; &nbsp;" + "<a href='" +
                response.encodeURL(contextPath+"/BookShowCart?Clear=clear") + 
                "' class=\"btn-large waves-effect waves-light teal lighten-1\">Clear Cart</a></strong>");
        } else {// Shopping cart is empty!
            out.println("<script>\n" +
                    "      function toastNotification(){\n" +
                    "           Materialize.toast('Your cart is empty!', 3000);" +
                    "      }\n" +
                    "      \n" +
                    "      setTimeout(toastNotification, 1000);\n" +
                    "    </script>");
            //out.println("<font size='+2'>Your cart is empty.</font><br> &nbsp; <br><center>"
                    out.println("<a href='" +
                response.encodeURL(contextPath+ "/BookCatalog") +"' class=\"btn-large waves-effect waves-light teal lighten-1\">Back to the Catalog</a></center>");
        }
        
        out.println("</body> </html>");
        out.close();
    }
    @Override
    public String getServletInfo() {
        return "Returns information about the books that the user is in the process of ordering.";
    }

    private float convert(java.lang.String country1, java.lang.String country2) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        org.me.currencyexchange.CurrencyExWS port = service.getCurrencyExWSPort();
        return port.convert(country1, country2);
    }

}

package servlets;

import RMIgst.GstClient;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import cart.*;

public class ShirtCashierServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        HttpSession session = request.getSession();
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        response.setContentType("text/html");
        String contextPath = request.getContextPath();
        PrintWriter out = response.getWriter();
        
        out.println(BannerServlet.htmlHeader);
        getServletContext().getRequestDispatcher("/Banner").include(request, response);
         // Go back to catalog
         out.println("<p> &nbsp; <p><a href='" +response.encodeURL(
                 contextPath+"/ShirtCatalog") +"' class=\"btn-large waves-effect waves-light teal lighten-1\">Back to Catalog</a>" );                

         /* RMI to check if gst rate is needed to display */
        double gstRate = 0;
        gstRate = new GstClient().calc(cart.getMoneySymbol());
        // Print out the total and the form for the user
        out.println("<p>Your total purchase amount is:<strong>"+ cart.getMoneySymbol() + "&nbsp;" +
            String.format("%.2f",cart.getTotal() * cart.getCurrencyExRate()* (gstRate + 1)) + "<p>To purchase the items in your shopping cart, please provide us with the following information:<form action='" +
            response.encodeURL(contextPath+ "/ShirtReceipt") +
            "' method='post'><table class=\"striped\" summary='layout'><tr>" +
            "<td><strong>Name:</strong></td>" +
            "<td><input type='text' name='cardname'" +
            "value='Mr. Leng' size='19'></td></tr>" + 
            "<tr><td><strong>Credit Card Number:</strong></td>" +
            "<td><input type='text' name='cardnum' " +
            "value='xxxx xxxx xxxx xxxx' size='19'></td></tr>" +
                /*"<tr><td><strong>RMI Transaction Date:</strong></td>" +
                "<td>" + cart.getTransactionDate() + "</td></tr>" +*/
            "<tr><td></td><td><input type='submit' class=\"btn-large waves-effect waves-light teal lighten-1\" value='Submit Information'></td></tr></table>" +
            "</form></body></html>");
        out.close();
    }
    @Override
    public String getServletInfo() {
        return "Takes the user's name and credit card number so that the user can buy the books.";
    }
}

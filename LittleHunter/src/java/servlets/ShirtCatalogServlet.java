package servlets;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import database.*;
import cart.*;
import exception.*;

public class ShirtCatalogServlet extends HttpServlet {
    
    private ShirtDBAO shirtDB;
    @Override
    public void init() throws ServletException {
        shirtDB = (ShirtDBAO) getServletContext().getAttribute("shirtDB");
        if (shirtDB == null) throw new UnavailableException("Couldn't get database.");
    }
    @Override
    public void destroy() {shirtDB = null;}
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        System.out.println(cart);
        response.setContentType("text/html");
        response.setBufferSize(8192);
        String contextPath = request.getContextPath();
        PrintWriter out = response.getWriter();
        out.println(BannerServlet.htmlHeader);
        getServletContext().getRequestDispatcher("/Banner").include(request, response);
        String bookId = request.getParameter("Id");
        if (bookId != null) {
            try {
                ShirtDetails book = shirtDB.getShirtDetails(bookId);
                cart.add(bookId, book);
                out.println("<script>\n" +
                    "      function toastNotification(){\n" +
                    "           Materialize.toast('Added " + book.getShirtName() + " into cart!', 4000);" +
                    "      }\n" +
                    "      \n" +
                    "      setTimeout(toastNotification, 1000);\n" +
                    "    </script>");
                        /*+ "<p><h3><font color='red'>You added <i>" + book.getShirtName() +
                    "</i> to your shopping cart.</font></h3>")*/
            } catch (BookNotFoundException ex) {
                response.reset();
                throw ex;
            }
        }
        //Give the option of checking cart or checking out if cart not empty
        if (cart.getNumberOfItems() > 0) {
            out.println("<div ><p class=\"col s12 m6\"><a href='" +
                response.encodeURL(contextPath+ "/ShirtShowCart") +
                "' class=\"btn-large waves-effect waves-light teal lighten-1\">"
                    + "Check Shopping Cart</a>&nbsp;&nbsp;&nbsp;<a href='" +
                response.encodeURL(contextPath+ "/ShirtCashier")+"'class=\"btn-large waves-effect waves-light teal lighten-1\">"
                    + "Purchase Shirts</a></p></div>");
        }
        // Always prompt the user to buy more -- get and show the catalog
        out.println("<h1 class=\"header center teal-text text-lighten-2\">Our Store</h1>");
        
       //try {
            Collection coll = shirtDB.getBooks();
            Iterator i = coll.iterator();
            out.println(" <div class=\"row\" id=\"catalog\">\n");
            while (i.hasNext()) {
                ShirtDetails book = (ShirtDetails) i.next();
                bookId = book.getId();
                //Print out info on each book in its own two rows
               
                out.println(
"        <div class=\"col s12 m4\">\n" +
"          <div class=\"card\">\n" +
"            <div class=\"card-image\">\n" +
"              <img src=\"images/" + book.getImageUrl() +"\">\n" +
"            </div>" +
        
                    "<div class=\"card-content\"><p>" +
        "              <span class=\"card-title\" style=\"text-align:center; color:black; font-size: 1.3em;\"><a href='" +
                    response.encodeURL(contextPath+"/ShirtDetails?Id=" + bookId) + 
                    "' class=\"teal-text\">" +book.getShirtName()+"&nbsp;</a></span>\n" 
                    + "<br/><br/>Price: RM&nbsp;&nbsp;" + "<span class=\"price\">" + book.getPrice() + "</span>" +
                    "<br/>" + book.getDescription() + "</em><br/><br/></p> " +
        "<p>Inventory:&nbsp;&nbsp;" + "<span class=\"price\">" + book.getInventory()+ "</span>" + "&nbsp;qty</p></div>\n" +
"           <div class=\"card-action\">  <a href='" +
                    response.encodeURL(contextPath+"/ShirtCatalog?Id=" + bookId) + 
                    "'  class=\"waves-effect waves-light lighten-1\">&nbsp;Add to Cart&nbsp;</a></div></div>\n" +
"        </div>\n");
            }
            out.println(" </div>");
        /*} catch (BooksNotFoundException ex) {
            response.reset();
            throw ex;
        }*/

        out.println("</body></html>");
        out.close();
    }
    @Override
    public String getServletInfo() {return "Adds books to the user's shopping cart and prints the catalog.";}
}

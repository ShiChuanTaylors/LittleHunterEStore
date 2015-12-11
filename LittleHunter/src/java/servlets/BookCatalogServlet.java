package servlets;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import database.*;
import cart.*;
import exception.*;

public class BookCatalogServlet extends HttpServlet {
    
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
                BookDetails book = bookDB.getBookDetails(bookId);
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
                response.encodeURL(contextPath+ "/BookShowCart") +
                "' class=\"btn-large waves-effect waves-light teal lighten-1\">"
                    + "Check Shopping Cart</a>&nbsp;&nbsp;&nbsp;<a href='" +
                response.encodeURL(contextPath+ "/BookCashier")+"'class=\"btn-large waves-effect waves-light teal lighten-1\">"
                    + "Purchase Shirts</a></p></div>");
        }
        // Always prompt the user to buy more -- get and show the catalog
        out.println("<h1 class=\"header center teal-text text-lighten-2\">Our Store</h1>");
        
       //try {
            Collection coll = bookDB.getBooks();
            Iterator i = coll.iterator();
            out.println(" <div class=\"row\" id=\"catalog\">\n");
            while (i.hasNext()) {
                BookDetails book = (BookDetails) i.next();
                bookId = book.getId();
                //Print out info on each book in its own two rows
               
                out.println(
"        <div class=\"col s12 m4\">\n" +
"          <div class=\"card\">\n" +
"            <div class=\"card-image\">\n" +
"              <img src=\"images/" + book.getImageUrl() +"\">\n" +
"              <span class=\"card-title\" style=\"text-align:center; font-size: 1.3em;\"><a href='" +
                    response.encodeURL(contextPath+"/BookDetails?Id=" + bookId) + 
                    "' class=\"white-text\">" +book.getShirtName()+"&nbsp;</a></span>\n" +
"            </div>" +
                    "<div class=\"card-content\"><p>"
                    + "Price: RM&nbsp;&nbsp;" + "<span class=\"price\">" + book.getPrice() + "</span>" +
                    "<br/>" + book.getDescription() + "</em> </p></div>\n" +
"           <div class=\"card-action\">  <a href='" +
                    response.encodeURL(contextPath+"/BookCatalog?Id=" + bookId) + 
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

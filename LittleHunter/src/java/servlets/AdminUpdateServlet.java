package servlets;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import database.*;
import cart.*;
import exception.*;

public class AdminUpdateServlet extends HttpServlet {
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
        response.setContentType("text/html");
        response.setBufferSize(8192);
        String contextPath = request.getContextPath();
        PrintWriter out = response.getWriter();
        out.println(BannerServlet.htmlHeader);
        getServletContext().getRequestDispatcher("/Banner").include(request, response);
        String shirtId = request.getParameter("Id");
        if (shirtId != null) {
            try {
                if(shirtDB.deleteBook(shirtId))
                  out.println("<p><h3><font color='red'>You updated Shirt with ID=<i>" + shirtId +
                          "</i></font></h3>");
                else
                  out.println("<p><h3><font color='red'>Cannot update Shirt with ID=<i>" + shirtId +
                          "</i></font></h3>");                      
            } catch (BookNotFoundException ex) {
                response.reset();
                throw ex;
            }
        }
        //Give the option of checking cart or checking out if cart not empty
        if (cart.getNumberOfItems() > 0) {
            out.println("<p><strong><a href='" +
                response.encodeURL(contextPath+ "/ShirtShowCart") +
                "'>Check Shopping Cart</a>&nbsp;&nbsp;&nbsp;<a href='" +
                response.encodeURL(contextPath+ "/ShirtCashier")+"'>Buy Your Books</a></p></strong>");
        }
        // Always prompt the user to buy more -- get and show the catalog
        out.println("<h3>Please choose shirt to update:</h3><center><table class=\"striped\" summary='layout'>");
        try {
            Collection coll = shirtDB.getBooks();
            Iterator i = coll.iterator();
            while (i.hasNext()) {
                ShirtDetails book = (ShirtDetails) i.next();
                shirtId = book.getId();
                //Print out info on each book in its own two rows
                out.println("<tr><td bgcolor='#ffffaa'><strong>"+book.getShirtName()+"&nbsp;</strong></td>" +
                    "<td bgcolor='#ffffaa' rowspan='2'>MYR&nbsp;"+ book.getPrice() +
                    "&nbsp; </td><td bgcolor='#ffffaa' rowspan='2'><a href='" +
                    response.encodeURL(contextPath+"/AdminBookEntry?Mode=Update&ID=" + shirtId) + 
                    "'> &nbsp;Update&nbsp;</a></td></tr>" +
                    "<tr><td bgcolor='white'>&nbsp; &nbsp;&nbsp;<em>" + book.getDescription()+"</em></td></tr>");
            }
        } catch (BooksNotFoundException ex) {
            response.reset();
            throw ex;
        }
        out.println("</table></center></body></html>");
        out.close();
    }
    @Override
    public String getServletInfo() {return "Admin Function to update existing book.";}
}

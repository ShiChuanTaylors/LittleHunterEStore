package servlets;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import database.*;
import exception.*;
public class ShirtDetailsServlet extends HttpServlet {
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
        response.setContentType("text/html");
        response.setBufferSize(8192);
        PrintWriter out = response.getWriter();
        String shirtId = request.getParameter("Id");
        //out.println("<html><head><title>Book Details:" +shirtId+ "</title></head><body>");
        out.println(BannerServlet.htmlHeader);
        getServletContext().getRequestDispatcher("/Banner").include(request, response);
        if (shirtId != null) {
            try {
                ShirtDetails ts = shirtDB.getShirtDetails(shirtId);
                out.println("<br/>"+
                    "<table class=\"striped\" border='1'>"+
                    "<thead><tr><th colspan='2'>T-Shirt Details</th><tr></thead>"+
                    "<tr><td>ID</td><td>"+shirtId+"</td></tr>"+
                    "<tr><td>T-shirt Name</td><td>"+ts.getShirtName()+"</td></tr>"+
                    "<tr><td>Description</td><td>"+ts.getDescription()+"</td></tr>"+
                    "<tr><td>Price</td><td>MYR&nbsp;"+ts.getPrice()+"</td></tr></table>");
            // Go back to catalog
            out.println("<p> &nbsp; <p><a href='" +
                response.encodeURL(request.getContextPath() + "/ShirtCatalog") +
                "' class=\"btn-large waves-effect waves-light teal lighten-1\">Continue Shopping</a>" );                
            } catch (BookNotFoundException ex) {
                out.println("<center><h1>T-shirt Not Found</h1></center>");
            }
            finally{
                out.println("</body></html>");
                out.close();
            }
        }        
    }
    @Override
    public String getServletInfo() {return "Returns information about book";}
}

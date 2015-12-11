package servlets;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import database.*;
import exception.*;

public class ShirtStoreServlet extends HttpServlet {
    private ShirtDBAO shirtDB;
    @Override
    public void init() throws ServletException {
        shirtDB = (ShirtDBAO) getServletContext().getAttribute("shirtDB");
        if (shirtDB == null) throw new UnavailableException("Couldn't get database.");
    }
    @Override
    public void destroy() { shirtDB = null; }
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        response.setContentType("text/html");
        response.setBufferSize(8192);
        String contextPath = request.getContextPath();
        PrintWriter out = response.getWriter();
        //out.println("<html><head><title>Amazon.COM</title></head><body>");
        out.println(BannerServlet.htmlHeader);
        getServletContext().getRequestDispatcher("/Banner").include(request, response);
        try {
            String bookOfTheDayID="1";
            shirtDB = (ShirtDBAO) getServletContext().getAttribute("shirtDB");            
            ShirtDetails bd = shirtDB.getShirtDetails(bookOfTheDayID);
            out.println("<b>Book of the day</b>:<a href='" +
                response.encodeURL(contextPath+"/ShirtDetails?Id="+bookOfTheDayID+"'>") + 
                bd.getShirtName() +"</a><p><a href=\'" +response.encodeURL(contextPath+ "/ShirtCatalog") +
                "'><b>Start Shopping</b></a></font><br/>");
        } catch (BookNotFoundException ex) {
             out.println("<center><h1>"+ex.getMessage()+"</h1></center>");
       }
       finally{ 
           out.println("</body></html>");
           out.close();
       }
    }
    @Override
    public String getServletInfo() {return "The Amazon.COM ShirtStore Main Servlet";}
}

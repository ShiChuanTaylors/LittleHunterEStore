package servlets;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import cart.ShoppingCart;
import database.*;

public class ShirtReceiptServlet extends HttpServlet {
    private ShirtDBAO shirtDB;
    @Override
    public void init() throws ServletException {
        shirtDB = (ShirtDBAO) getServletContext().getAttribute("shirtDB");
        if (shirtDB == null)  throw new UnavailableException("Couldn't get database.");
    }
    @Override
    public void destroy() { shirtDB = null;}

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean orderCompleted = true;
        HttpSession session = request.getSession(true);
        String contextPath = request.getContextPath();
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        orderCompleted = false;
        // Update the inventory
        //try {
            shirtDB.buyBooks(cart);
        //} catch (OrderException e) {
        //    System.err.println(e.getMessage());
        //    orderCompleted = false;
        //}
        orderCompleted = true;

        // Payment received -- invalidate the session
        session.invalidate();

        // set content type header before accessing the Writer
        response.setContentType("text/html");
        response.setBufferSize(8192);

        PrintWriter out = response.getWriter();
        //out.println("<html><head><title>Receipt</title></head><body>");
        out.println(BannerServlet.htmlHeader);
        getServletContext().getRequestDispatcher("/Banner").include(request, response);

        if (orderCompleted) {
            out.println("<h3>"+request.getParameter("cardname")+", <br/>Thank you for purchasing your shirts from us.");
        } else {
            out.println("<h3>Your order could not be completed due to insufficient inventory.");
        }
        out.println("<p><a href='" +response.encodeURL(contextPath+ "/ShirtCatalog")+
                "' class=\"btn-large waves-effect waves-light teal lighten-1\">Continue Shopping</a> &nbsp; &nbsp; &nbsp;</body></html>");
        out.close();
    }
    @Override
    public String getServletInfo() {
        return "Updates the inventory, invalidates the user session, thanks the user for the order.";
    }
}

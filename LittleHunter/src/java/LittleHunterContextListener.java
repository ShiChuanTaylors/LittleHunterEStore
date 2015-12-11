import database.ShirtDBAO;
import javax.servlet.*;
public final class LittleHunterContextListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent event)  {
        ServletContext context = event.getServletContext();
         try {
            context.setAttribute("shirtDB", new ShirtDBAO());
            
        } catch (Exception ex) {
            throw new UnsupportedOperationException(
                    "Couldn't create ShirtDBAO object:" +ex.getMessage());
       }
    }
    public void contextDestroyed(ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        ShirtDBAO shirtDB = (ShirtDBAO) context.getAttribute("shirtDB");
        if (shirtDB != null) shirtDB.remove();
        context.removeAttribute("shirtDB");
    }
}

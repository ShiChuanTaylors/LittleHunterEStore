import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import cart.*;

public class AmazonSessionListener implements HttpSessionListener {
    
    //public static GstClient gc; 
    
    public void sessionCreated(HttpSessionEvent arg0) {
        //transactionDate = new DateClient().getRMIdate();
        
        // Initialize RMI connection
        arg0.getSession().setAttribute("cart", new ShoppingCart());
        
        //double gstRate = new GstClient().getGST("MYR");
        //System.out.println("------" + gstRate + "-------");
        
        
        
    }
   public void sessionDestroyed(HttpSessionEvent arg0) {
        arg0.getSession().removeAttribute("cart");
   }
}
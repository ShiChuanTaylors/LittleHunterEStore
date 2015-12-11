package RMIgst;


import java.rmi.*;
import java.net.*;
import java.math.BigInteger;

public class GstClient {

    public double calc(String currency) {
        String sRMI = "rmi://localhost:1099/gst";        
        try {      
          GstSuper gst = (GstSuper) Naming.lookup(sRMI);
          double gstRate = gst.getGSTrate(currency);
          System.out.println("The rate is : " + gstRate);
          return gstRate;
        }
        catch (MalformedURLException ex) {
          System.err.println(sRMI + " is not a valid RMI URL");
        }
        catch (RemoteException ex) {
          System.err.println("Remote object threw exception " + ex);
        }
        catch (NotBoundException ex) {
          System.err.println("Could not find the requested remote object on the server");
        } 
        return 0.0;
    }
  public static void main(String args[]) {
    String sRMI = "rmi://localhost:1099/gst";        
    try {      
      GstSuper gst = (GstSuper) Naming.lookup(sRMI);
      double gstRate = gst.getGSTrate("myr");
      System.out.println("The rate is : " + gstRate);
    }
    catch (MalformedURLException ex) {
      System.err.println(sRMI + " is not a valid RMI URL");
    }
    catch (RemoteException ex) {
      System.err.println("Remote object threw exception " + ex);
    }
    catch (NotBoundException ex) {
      System.err.println("Could not find the requested remote object on the server");
    } 
  }
}

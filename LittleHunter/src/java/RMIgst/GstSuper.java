package RMIgst;


import java.rmi.*;

public interface GstSuper extends Remote {

  public double getGSTrate(String currency) throws RemoteException;

}

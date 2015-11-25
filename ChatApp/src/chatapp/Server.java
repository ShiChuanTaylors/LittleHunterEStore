/*
 * Taylor's SoCIT - Bachelor's of Software Engineering (Hons)
 * Distributed Application Development Assignment 1 - Chat System
 * Author: Ting Shi Chuan (0313664)
 */
package chatapp;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author ting
 */
public class Server {
    
    private final int port;
    private static int nClients;    // total clients in the server
    private boolean serverStatus;  // the boolean that will be turned of to stop the server
    private final ServerSocket serverSocket;
    private static ArrayList<ClientThread> allClients;
    
    Server(ServerSocket ss, int port) {
        this.serverSocket = ss;
        this.port = port;
    }
    
    public static void main (String [] args) {
        allClients = new ArrayList<>();
        // Get connection
        System.out.println("Requesting Server Connection..");
        
        // Connect to a available server socket port
        ServerSocket ss = new PortScanner().getServerSocketConnection();
        
        if(ss == null) {
            System.out.println("Connection Failed..");
            return;
        }
        System.out.println("Connected!");
        
        Server server = new Server(ss, ss.getLocalPort());
        server.start();
        
    }
    
    public void start() {
        serverStatus = true;
        
        // infinite loop to wait for connections
        while(serverStatus) 
        {
            // format message saying we are waiting
            System.out.println("Server waiting for Clients on port " + port + ".");

            Socket socket; 
            try {
                socket = serverSocket.accept(); // accept connection
                if(!serverStatus)   // if server is stopped
                    break;
                
                ClientThread ct = new ClientThread(socket);  // make a thread of it
                allClients.add(ct);
                new Thread(ct).start();
                
            } catch (IOException e) {
                System.err.println("Error Connecting Client: " + e);
            }
            
        }
    }
    
    /* Remove Client based on their ID */
    private void removeClient(int uniqueId) {   
        for(ClientThread ct : allClients) {
            if(ct.getUniqueId() == uniqueId) {
                allClients.remove(ct);
                break;
            }
        }
    }
    
    private ClientThread getClientById(int id) {
        for(ClientThread ct : allClients) {
            if(ct.getUniqueId() == id) {
                return ct;
            }
        }
        return null;
    }
    
    
    
    /* Each client is a thread */
    /* One instance of this thread will run for each client */
    class ClientThread extends ClientConnection implements Runnable, Serializable {

        private ChatMessage cm; // the only type of message a will receive

        ClientThread(Socket socket) {
            // assign a unique id
            super(socket, ++(Server.nClients));
            try
            {
                // read the username
                String username = getSInput().readObject().toString();
                setUserName(username);
                getSOutput().writeObject(getUniqueId());        // reply client of their id
                System.out.println(username + " just connected.");
            }
            catch (IOException e) {
                System.err.println("Exception creating new Input/output Streams: " + e);
                return;
            }
            catch (ClassNotFoundException e) {
                System.err.println(e);
            }
            setConnectedDate(new Date().toString() + "\n");
        }

        @Override
        public void run() {
            // to loop until LOGOUT
            boolean connStatus = true;
            while(connStatus) {
                try {
                    cm = (ChatMessage) getSInput().readObject();
                }
                catch (IOException | ClassNotFoundException e) {    
                    break;
                }
                switch(cm.getType()) {
                    case ChatMessage.MESSAGETO:
                        writeMsgToClient(cm);
                        break;
                    case ChatMessage.LOGOUT:
                        connStatus = false;
                        break;
                    case ChatMessage.WHOISIN:
                        writeWhoIsIn(cm, this);
                        break;
                    case ChatMessage.BOARDCAST:
                        System.out.println("Detected Broadcast signal");
                        broadcastUsers(cm);
                        break;
                    case ChatMessage.NEW_USER_ARRIVED:
                        newUserArrivalUpdateCall(cm);
                        break;
                    case ChatMessage.GROUP_CHAT: 
                        groupMsg(cm);
                }
            }
            removeClient(getUniqueId());
            closeConnections();
            if(!checkIfAnyUsersLeft()) {
                serverStatus = false;
                //send myself a fake client to close the server
                try {
                    Socket s = new Socket(serverSocket.getInetAddress(), port);
                    s.close();
                } catch (IOException ex) {
                    
                }
            } else {
                // Update all clients online user list
                newUserArrivalUpdateCall(new ChatMessage(ChatMessage.WHOISIN, ""));
            }
        }

        private boolean writeMsgToClient(ChatMessage cm) {
            System.out.println("Finding Client " + 
                    cm.getTo().getUniqueId() + " " + 
                    cm.getTo().getUserName());
            for(ClientThread ct : allClients) {
                try {
                    System.out.println("Finding Client" + ct.getUniqueId() + " " + 
                    ct.getUserName());
                    if(ct.getUniqueId() == cm.getTo().getUniqueId()) {
                        System.out.println("Writting...");
                        ct.getSOutput().writeObject(cm);
                        System.out.println("Sent! Server:" + cm.getMessage());
                        return true;
                    }
                } catch (IOException e) {
                    return false;
                }
            }
            return false;
        }
        
        private void closeConnections() {
            // Close all connections
            try {
                if(getSOutput() != null) 
                    getSOutput().close();
                if(getSInput() != null) 
                    getSInput() .close();
                if(getSocket() != null) 
                    getSocket().close();
            }
            catch(Exception e) {
                System.err.println("Close Error: " + e);
            }
        }
        
        private void writeWhoIsIn(ChatMessage cm, ClientThread curThread) {
            try {
                curThread.getSOutput().writeObject(cm);
                for(ClientThread ct : allClients) {
                    curThread.getSOutput().writeObject(new Client(ct.getUniqueId(), ct.getUserName(), ct.getConnectedDate()));
                }
                curThread.getSOutput().writeObject(new Client(null));        // Send empty client to trigger end of list
            } catch (IOException e) {

            }
        }

        private synchronized void broadcastUsers(ChatMessage cm) {
            try {
                for(ClientThread ct : allClients) {
                    System.out.println("Writing message to " + ct.getUniqueId() + " " + ct.getUserName());
                    ct.getSOutput().writeObject(cm);
                }
            } catch (IOException ex) {
                System.err.println("Error Broadcasting to users");
            }
        }

        private void newUserArrivalUpdateCall(ChatMessage cm) {
            for(ClientThread ct : allClients) {
                writeWhoIsIn(cm, ct);
            }
        }

        private boolean checkIfAnyUsersLeft() {
            return !(allClients.isEmpty());
        }

        private void groupMsg(ChatMessage cm) {
            try {
                
                for(Client c : cm.getChatGroup().getGroupTo()) {
                    ClientThread ct = getClientById(c.getUniqueId());
                    if(ct != null) {
                        ct.getSOutput().writeObject(cm);
                    }
                }
            } catch (IOException ex) {
                System.err.println("Error Broadcasting to users");
            }
        }
    }
    
}

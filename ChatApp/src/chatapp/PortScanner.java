/*
 * Taylor's SoCIT - Bachelor's of Software Engineering (Hons)
 * Distributed Application Development Assignment 1 - Chat System
 * Author: Ting Shi Chuan (0313664)
 */
package chatapp;

import java.io.IOException;
import java.net.ServerSocket;
import javax.swing.JOptionPane;

/**
 *
 * @author ting
 */
public class PortScanner {
    
    private final String host;
    private int nThreads;
    private static ServerSocket connection;
    
    private static Boolean isNotified = false;     
    
    public PortScanner(String host, int nThreads) {
        this.host = host;
        this.nThreads = nThreads;
    }
    
    public PortScanner(String host) {
        this(host, 500);        // Create 500 threads to scan port as default
    }
    
    public PortScanner() {
        this("localhost", 500); // Localhost as default host
    }
    
    
    
    
    public ServerSocket getServerSocketConnection() {
        multiThreadScanning(); 
        
        synchronized(this){
            while(connection == null){
                try {
                    wait();
                } catch (InterruptedException ex) {
                    
                }
            }
        }
        System.out.println("Port Scan successfull");
        return connection;
    }

    private void multiThreadScanning() {
        int rep = (65536/nThreads) + (((65536 % nThreads)==0)? 0 : 1);
        for(int i = 0; i < nThreads; i++) {
            PortScannerThread t= new PortScannerThread(this, host, i * rep + 1, (i + 1) * rep);
            t.start();
        }
    }
    
    class PortScannerThread extends Thread{

        //start and end port
        private final int startPort;
        private final int endPort;
        //host
        private final String host;
        //connected port, socket
        private int connectedPort;

        private final PortScanner tParentThread;

        //Constructor
        public PortScannerThread(PortScanner tParentThread, String host, int startPort, int endPort) {
            if(startPort < 1) 
                startPort = 1;
            if(endPort > 65535) {
                endPort = 65535;
            }
            if(startPort > 65535) {
                startPort = 65535;
            }
            this.startPort = startPort;
            this.endPort = endPort;
            //System.out.println(this.startPort + " and " + this.endPort);
            this.host = host;
            this.tParentThread = tParentThread;

        }

        @Override
        public void run() {
            int port = startPort;

            while(PortScanner.connection != null || port <= endPort) {
                connectServer(port);
                if(PortScanner.connection != null) {    //Connection found
                    synchronized(tParentThread) {
                        if(!isNotified) {
                            tParentThread.notifyAll();  //Tell parent that connection is successful
                            isNotified = true;
                        }
                    }
                    return;
                }
                port++;
            }
        }

        public int getConnectedPort() {
            return this.connectedPort;
        }

        private void connectServer(int port) {
            ServerSocket connection = null;
            try {
                connection = new ServerSocket(port);
                synchronized(this) {
                    if(PortScanner.connection == null) {
                        System.out.println("Connected to " + port);
                        this.connectedPort = port;
                        PortScanner.connection = connection;
                        int jop = JOptionPane.showConfirmDialog(null, 
                                "Connected to " + port, 
                                "Server Connection", JOptionPane.OK_OPTION);
                        
                    }
                    else if(connection != null){
                        connection.close();
                    }
                }
            } catch (IOException ex) {
            } finally {
                try {
                    if(connection != null && PortScanner.connection == null) {
                        connection.close();
                    }
                } catch(IOException e) {

                }
            }
        }
    }
    
}


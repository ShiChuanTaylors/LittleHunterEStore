/*
 * Taylor's SoCIT - Bachelor's of Software Engineering (Hons)
 * Distributed Application Development Assignment 1 - Chat System
 * Author: Ting Shi Chuan (0313664)
 */
package chatapp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JTextArea;

/**
 *
 * @author ting
 */
public class ClientConnection extends Client implements Serializable{
    
    private String serverName;
    private Socket socket;
    private int port;
    
    private ObjectInputStream sInput;		// to read from the socket
    private ObjectOutputStream sOutput;		// to write on the socket
    
    private ServerListener sl;
    
    
    public ClientConnection(String server, String userName, int port, Socket socket) {
        super(userName);
        this.serverName = server;
        this.port = port;
        this.socket = socket;
        
        try {
            sOutput = new ObjectOutputStream(socket.getOutputStream());
            sInput  = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            
        }
        
    }
    
    public ClientConnection(Socket socket, int uniqueId) {
        super(uniqueId);
        this.socket = socket;
        try {
            sOutput = new ObjectOutputStream(socket.getOutputStream());
            sInput  = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            
        }   
    }
    
    public ObjectOutputStream getSOutput() {
        return this.sOutput;
    }
    
    public ObjectInputStream getSInput() {
        return this.sInput;
    }
    
    
    public Socket getSocket() {
        return this.socket;
    }
    
    public String getServerName() {
        return this.serverName;
    }
    
    public int getPortNumber() {
        return this.port;
    }
    
    public ServerListener getServerListener() {
        return this.sl;
    }
    
    public void startListenFromServer(Chatroom chatRoom, JTextArea jta, int toId) {
        sl = new ServerListener(this, chatRoom, new MessageBox(jta, toId));
        sl.start();
    }
    
    public void addMessageBoxToListener(MessageBox box) {
        sl.addNewBox(box);
    }
    
    public void addGroupBoxToListener(MessageBox box) {
        sl.addNewGroupBox(box);
    }
    
    protected class ServerListener extends Thread {
        
        private final ClientConnection cc;
        private final ArrayList<MessageBox> nMsgBoxes;
        private final ArrayList<MessageBox> groupBoxes;
        private final Chatroom cr;
        
        ServerListener(ClientConnection cc, Chatroom cr, MessageBox box) {
            this.cc = cc;
            this.cr = cr;
            nMsgBoxes = new ArrayList<>();
            groupBoxes = new ArrayList<>();
            
            nMsgBoxes.add(box);             // Add the first chatbox -> broadcast box
        }
        
        @Override
        public void run() {
            while(true) {
                try {
                    if(!cc.getSocket().isConnected()) {
                        return;
                    }
                    ChatMessage cm = (ChatMessage) cc.getSInput().readObject();
                    
                    //Check what type of message is received
                    switch(cm.getType()) {
                        case ChatMessage.WHOISIN : case ChatMessage.NEW_USER_ARRIVED :
                            updateChatroomUserList();
                            break;
                        case ChatMessage.MESSAGETO : case ChatMessage.BOARDCAST :
                            findBoxToAppend(cm);
                            break;
                        case ChatMessage.GROUP_CHAT :
                            findBoxToAppend(cm);
                            break;
                    }
                    
                }
                catch(IOException e) {
                    break;
                }
                // can't happen with a String object but need the catch anyhow
                catch(ClassNotFoundException e2) {
                }
            }
        }
        
        private void findBoxToAppend(ChatMessage cm) {
            if(cm.getTo() != null) { 
                if(cm.getTo().getUniqueId() == 0) {
                    nMsgBoxes.get(0).appendBox(cm.getFrom().getUserName()+ ": " +
                                cm.getMessage() + "\n> ");
                    return;
                }
                   
                for(MessageBox b : nMsgBoxes) {
                    if(b.getToId() == cm.getFrom().getUniqueId()) {
                        b.appendBox(cm.getFrom().getUserName()+ ": " +
                                cm.getMessage() + "\n> ");
                        return;
                    }
                }
                
            } else if(cm.getChatGroup() != null) {
                for(MessageBox gb : groupBoxes) {
                    if(gb.getGroupName().equals(cm.getChatGroup().getGroupName())) {
                        gb.appendBox(cm.getFrom().getUserName()+ ": " +
                                cm.getMessage() + "\n> ");
                        return;
                    }
                }
                
            }
            addNewBoxArrival(cm);        // instantiate ChatBox if is new chatbox
             
        }

        // Add new convo when someone send message to you
        private void addNewBoxArrival(ChatMessage cm) {
            
            // INDIVIDUAL BOX: create new chat box with the from
            if(cm.getType() != ChatMessage.GROUP_CHAT) {
                ChatBox cb = new ChatBox(cm.getFrom(), this.cc, cm.getType(), true);
                cb.setVisible(true);
                //add New box
                MessageBox newBox = new MessageBox(cb.getMessageTextArea(), 
                        cm.getFrom().getUniqueId());

                nMsgBoxes.add(newBox);

                newBox.appendBox(cm.getFrom().getUserName()+ ": " +
                                cm.getMessage() + "\n> ");
            } 
            else if(cm.getType() == ChatMessage.GROUP_CHAT) {
                ChatBox cb = new ChatBox(cm.getChatGroup(), this.cc, cm.getType(), true);
                cb.setVisible(true);
                //add New Group box
                MessageBox newBox = new MessageBox(cb.getMessageTextArea(), 
                        cm.getChatGroup().getGroupName());

                groupBoxes.add(newBox);

                newBox.appendBox(cm.getFrom().getUserName()+ ": " +
                                cm.getMessage() + "\n> ");
            }
        }
        
        // Add new convo when you intent to send message to a new person
        private void addNewBox(MessageBox newBox) {
            nMsgBoxes.add(newBox);
        }

        private void addNewGroupBox(MessageBox newBox) {
            groupBoxes.add(newBox);
        }
        
        private void updateChatroomUserList() {
            cr.renewClientList();
            DefaultListModel<String> users = new DefaultListModel<>();
            Client c;
            try {
                while(true) {

                    c = (Client)cc.getSInput().readObject();

                    if(c.getUserName() == null) {       // trigger end of list
                        break;
                    }
                    if(c.getUserName().equals(cc.getUserName())) {
                        c.setUserName("You");
                    }
                    cr.getClientList().add(c);
                    users.addElement("(" + "ID: " + c.getUniqueId() + 
                            " - " + c.getConnectedDate() + ") " +
                            c.getUserName());
                }
            } catch (IOException | ClassNotFoundException e) {

            }
            cr.getUserList().setModel(users);
        }
        
        public void closeDialogue(Client to) {
            synchronized(nMsgBoxes) {
                for(MessageBox box : nMsgBoxes) {
                    if(box.getToId() == to.getUniqueId()) {
                        nMsgBoxes.remove(box);
                    }
                }
            }
        }
        
        public void closeGroupDialogue(ChatGroup cg) {
            synchronized(groupBoxes) {
                for(MessageBox box : groupBoxes) {
                    if(box.getGroupName().equals(cg.getGroupName())) {
                        groupBoxes.remove(box);
                    }
                }
            }
        }
    }
}

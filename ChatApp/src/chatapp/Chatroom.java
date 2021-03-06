package chatapp;


import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/*
 * Taylor's SoCIT - Bachelor's of Software Engineering (Hons)
 * Distributed Application Development Assignment 1 - Chat System
 * Author: Ting Shi Chuan (0313664)
 */
/**
 *
 * @author ting
 */
public class Chatroom extends javax.swing.JFrame {

    /**
     * Creates new form Chatroom
     */
    private ClientConnection client;
    private ArrayList<Client> connectedClients;
    private ArrayList<Integer> chatToIndexes;
    
    public Chatroom() {
        initComponents();
    }
    
    public Chatroom(ClientConnection client) {
        initComponents();
        this.setTitle("Chatroom");
        this.client = client;
        client.startListenFromServer(this, BoardcastBox, 0);  // broadcast id = 0
        unLbl.setText(client.getUserName());
        initUserList();
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                System.out.println("Closing Window");
                closeConnections();
            }
        });
    }

    private void closeConnections() {
        // Close all connections
        
        try {
            
            // Send logout signal to server to close the thread
            client.getSOutput().writeObject(new ChatMessage(ChatMessage.LOGOUT, ""));
            
            // close local's connections
            if(client.getSOutput() != null) 
                client.getSOutput().close();
            if(client.getSInput() != null) 
                client.getSInput() .close();
            if(client.getSocket() != null) 
                client.getSocket().close();
        }
        catch(Exception e) {
            System.err.println("Close Error: " + e);
        } finally {
            int replyToChat = JOptionPane.showConfirmDialog(null, 
                    "Closed Successfully!", "INFO", 
                    JOptionPane.YES_OPTION);
            if (replyToChat == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jButton2 = new javax.swing.JButton();
        onlineUserLbl = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        userList = new javax.swing.JList();
        refreshBtn = new javax.swing.JButton();
        broadcastLbl = new javax.swing.JLabel();
        broadcastTb = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        BoardcastBox = new javax.swing.JTextArea();
        welcomeLbl = new javax.swing.JLabel();
        unLbl = new javax.swing.JLabel();
        logoutBtn = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        chatNowBtn = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        howToChatBtn = new javax.swing.JButton();

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        jList2.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList2);

        jButton2.setText("jButton2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        onlineUserLbl.setText("Online Users");

        userList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(userList);

        refreshBtn.setText("Refresh");
        refreshBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshBtnActionPerformed(evt);
            }
        });

        broadcastLbl.setText("To All The Users:");

        broadcastTb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                broadcastTbActionPerformed(evt);
            }
        });

        BoardcastBox.setEditable(false);
        BoardcastBox.setColumns(20);
        BoardcastBox.setRows(5);
        BoardcastBox.setText("> ");
        jScrollPane4.setViewportView(BoardcastBox);

        welcomeLbl.setText("Welcome,");

        unLbl.setText("<<username>>");

        logoutBtn.setText("Logout");
        logoutBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutBtnActionPerformed(evt);
            }
        });

        chatNowBtn.setText("Chat now!");
        chatNowBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chatNowBtnActionPerformed(evt);
            }
        });

        howToChatBtn.setText("How to Chat?");
        howToChatBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                howToChatBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addGap(103, 103, 103))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(broadcastTb, javax.swing.GroupLayout.PREFERRED_SIZE, 469, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 469, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(broadcastLbl)
                                    .addGap(362, 362, 362))
                                .addComponent(chatNowBtn, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 469, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(onlineUserLbl)
                                    .addGap(110, 110, 110)
                                    .addComponent(refreshBtn)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(welcomeLbl)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(unLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addGap(91, 91, 91))
                                    .addComponent(howToChatBtn, javax.swing.GroupLayout.Alignment.TRAILING))))))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(198, 198, 198)
                .addComponent(logoutBtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(welcomeLbl)
                    .addComponent(unLbl)
                    .addComponent(howToChatBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(onlineUserLbl)
                    .addComponent(refreshBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chatNowBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(broadcastLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(broadcastTb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(logoutBtn)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void refreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshBtnActionPerformed
        try {
            client.getSOutput().writeObject(new ChatMessage(ChatMessage.WHOISIN, ""));
        } catch (IOException ex) {
            Logger.getLogger(Chatroom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_refreshBtnActionPerformed

    private void broadcastTbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_broadcastTbActionPerformed
        try {
            client.getSOutput().writeObject(new ChatMessage(new Client(0), 
                    new Client(client.getUniqueId(),client.getUserName()), 
                    ChatMessage.BOARDCAST, broadcastTb.getText()));
            broadcastTb.setText("");
        } catch (IOException ex) {
            
        }
    }//GEN-LAST:event_broadcastTbActionPerformed

    private void logoutBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutBtnActionPerformed
        closeConnections();
    }//GEN-LAST:event_logoutBtnActionPerformed

    private void chatNowBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chatNowBtnActionPerformed
        
        if(chatToIndexes.isEmpty()) {
            int replyToChat = JOptionPane.showConfirmDialog(null, 
                "Please select a user to chat!", 
                "Sorry, try again!", 
                JOptionPane.YES_OPTION);
            if (replyToChat == JOptionPane.YES_OPTION) {}
        }
        else if(chatToIndexes.size() == 1) {
            int replyToChat = JOptionPane.showConfirmDialog(null, 
                    "Do you wish to chat with " + connectedClients.get(chatToIndexes.get(0)).getUserName(), 
                    "Continue to Chat", 
                    JOptionPane.YES_NO_OPTION);
            if (replyToChat == JOptionPane.YES_OPTION) {

                new ChatBox(connectedClients.get(chatToIndexes.get(0)), client, 
                        ChatMessage.MESSAGETO, false).setVisible(true);
            }
            else {

            }
        }
        else if(chatToIndexes.size() > 1) {         // this is a group chat
            
            // ask for group name
            String groupName = JOptionPane.showInputDialog(null, 
                    "Enter a group name", 
                    "Create New Group", 
                    JOptionPane.OK_CANCEL_OPTION);
            
            // group the selected users into a new list
            ArrayList<Client> groupUserList = new ArrayList<>();
            
            int isMyselfAdded = 0;
            for(Integer index : chatToIndexes) {
                if(connectedClients.get(index).getUserName().equals("You"))
                    isMyselfAdded = 1;
                groupUserList.add(connectedClients.get(index));
            }
            
            if(isMyselfAdded == 0) {
                groupUserList.add(connectedClients.get(client.getUniqueId()-1));
                isMyselfAdded = 1;
            }
            /*for(int i = 0; i < connectedClients.size(); i++) {
                for(Integer index : chatToIndexes) {
                   if(i == index || 
                           connectedClients.get(i).getUserName().equals("You")) {
                       groupUserList.add(connectedClients.get(i));
                   } 
                }
            }*/
            
            // add myself in
            //groupUserList.add(new Client(client.getUniqueId(),client.getUserName(), client.getConnectedDate()));
            for(Client test : groupUserList) {
                System.out.println("Test " + test.getUniqueId() + ". "+ test.getUserName());
            }
            
            ChatGroup cg = new ChatGroup(groupName, groupUserList);
            new ChatBox(cg, client, 
                        ChatMessage.GROUP_CHAT, false).setVisible(true);
        }
    }//GEN-LAST:event_chatNowBtnActionPerformed

    private void howToChatBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_howToChatBtnActionPerformed
        JOptionPane.showConfirmDialog(null, "2 ways to chat: \n" +
                "1) Click the user to start a private chat\n" + 
                "2) Click multiple users to start a group chat\n", "How to Chat?", JOptionPane.OK_OPTION);
    }//GEN-LAST:event_howToChatBtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Chatroom.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Chatroom.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Chatroom.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Chatroom.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Chatroom().setVisible(true);
            }
        });
        
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea BoardcastBox;
    private javax.swing.JLabel broadcastLbl;
    private javax.swing.JTextField broadcastTb;
    private javax.swing.JButton chatNowBtn;
    private javax.swing.JButton howToChatBtn;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JButton logoutBtn;
    private javax.swing.JLabel onlineUserLbl;
    private javax.swing.JButton refreshBtn;
    private javax.swing.JLabel unLbl;
    private javax.swing.JList userList;
    private javax.swing.JLabel welcomeLbl;
    // End of variables declaration//GEN-END:variables

    private void initUserList() {
        
        loadUsersFromServer();
        
        userList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                
                chatToIndexes = new ArrayList<>();
                
                JList lsm = (JList)e.getSource();
                // Find out which indexes are selected.
                int minIndex = lsm.getMinSelectionIndex();
                int maxIndex = lsm.getMaxSelectionIndex();
                for (int i = minIndex; i <= maxIndex; i++) {
                    if (lsm.isSelectedIndex(i)) {
                        chatToIndexes.add(i);
                    }
                }
            }
            
        });
    }

    private void loadUsersFromServer() {
        try {
            client.getSOutput().writeObject(new ChatMessage(ChatMessage.NEW_USER_ARRIVED, ""));
        } catch (IOException ex) {
            Logger.getLogger(Chatroom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void renewClientList() {
        connectedClients = new ArrayList<>();
    }
    
    
    public ArrayList<Client> getClientList() {
        return this.connectedClients;
    }
    
    public JList getUserList() {
        return this.userList;
    }
}

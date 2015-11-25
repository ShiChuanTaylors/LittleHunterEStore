/*
 * Taylor's SoCIT - Bachelor's of Software Engineering (Hons)
 * Distributed Application Development Assignment 1 - Chat System
 * Author: Ting Shi Chuan (0313664)
 */
package chatapp;

import javax.swing.JTextArea;

/**
 *
 * @author ting
 */
public class MessageBox {
    
    public static int totalBoxes;
    private int boxId;
    private JTextArea box;
    private int toId;            // 0 means to all
    private String groupName;
    
    
    public MessageBox(JTextArea box, int toId) {
        this.box = box;
        this.toId = toId;
        if(toId == 0) {
            boxId = 0;
        }
        else {
            boxId = ++totalBoxes;
        }
    }
    
    public MessageBox(JTextArea box, String groupName) {
        this.box = box;
        this.groupName = groupName;
        if(toId == 0) {
            boxId = 0;
        }
        else {
            boxId = ++totalBoxes;
        }
    }
    
    public JTextArea getBox() {
        return this.box;
    }
    
    public void appendBox(String msg) {
        box.append(msg);
    }
    
    public int getToId() {
        return this.toId;
    }
    
    public String getGroupName() {
        return this.groupName;
    }
}

/*
 * Taylor's SoCIT - Bachelor's of Software Engineering (Hons)
 * Distributed Application Development Assignment 1 - Chat System
 * Author: Ting Shi Chuan (0313664)
 */
package chatapp;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author ting
 */
public class ChatGroup implements Serializable {
    
    private int uniqueId;
    private String groupName;
    
    public ArrayList<Client> chatToIDs = new ArrayList<>();
    
    public ChatGroup(int uniqueId) {
        this.uniqueId = uniqueId;
    }
    
    public ChatGroup(String groupName) {
        this.groupName = groupName;
    }
    
    public ChatGroup(String groupName, ArrayList<Client> chatToIDs) {
        this.groupName = groupName;
        this.chatToIDs = chatToIDs;
    }
    
    public ChatGroup(int uniqueId, String groupName) {
        this.uniqueId = uniqueId;
        this.groupName = groupName;
    }
    
    public String getGroupName() {
        return this.groupName;
    }
    
    public int getId() {
        return this.uniqueId;
    }
    
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
    public void setId(int uniqueId) {
        this.uniqueId = uniqueId;
    }
    
    public void setGroupTo(ArrayList<Client> chatToIDs) {
        this.chatToIDs = chatToIDs;
    }
    
    public ArrayList<Client> getGroupTo() {
        return this.chatToIDs;
    }
    
}

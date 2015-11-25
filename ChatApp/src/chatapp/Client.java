/*
 * Taylor's SoCIT - Bachelor's of Software Engineering (Hons)
 * Distributed Application Development Assignment 1 - Chat System
 * Author: Ting Shi Chuan (0313664)
 */
package chatapp;
import java.io.Serializable;
import java.util.HashMap;
/**
 *
 * @author ting
 */
public class Client implements Serializable {
    
    private String userName;
    private int uniqueId;     //unique id for each client connection
    private String connectedDate;    // connected date
    
    
    private int msgBoxId;
    
    public Client(String userName) {
        this.userName = userName;
    }
    
    public Client(int uniqueId) {
        this.uniqueId = uniqueId;
    }
    
    public Client(int uniqueId, String userName) {
        this.uniqueId = uniqueId;
        this.userName = userName;
    }
    
    public Client(int uniqueId, String userName, String date) {
        this.uniqueId = uniqueId;
        this.userName = userName;
        this.connectedDate = date;
    }
    
    public void setId(int id) {
        this.uniqueId = id;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public int getUniqueId() {
        return this.uniqueId;
    }
    
    public String getUserName() {
        return this.userName;
    }
    
    public String getConnectedDate() {
        return this.connectedDate;
    }
    
    public void setConnectedDate(String date) {
        this.connectedDate = date;
    }
}

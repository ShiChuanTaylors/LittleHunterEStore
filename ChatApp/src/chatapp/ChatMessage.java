/*
 * Taylor's SoCIT - Bachelor's of Software Engineering (Hons)
 * Distributed Application Development Assignment 1 - Chat System
 * Author: Ting Shi Chuan (0313664)
 */
package chatapp;

import java.io.*;

/**
 *
 * @author ting
 */
public class ChatMessage implements Serializable {
    
    static final int WHOISIN = 0, 
            MESSAGETO = 1, 
            LOGOUT = 2, 
            BOARDCAST = 3, 
            CLOSING_DIALOGUE = 4,
            NEW_USER_ARRIVED = 5,
            GROUP_CHAT = 6;
    
    private int type;       // TO check which type of message: e.g logout
    private String message;
    private Client from;
    private Client to;
    private ChatGroup toGroup;
    
    ChatMessage(int type, String message) {
        this.type = type;
        this.message = message;
    }
    
    ChatMessage(Client to, int type, String message) {
        this(type, message);
        this.to = to;
    }
    
    // Individual chat constructor
    ChatMessage(Client to, Client from, int type, String message) {
        this(type, message);
        this.to = to;
        this.from = from;
    }
    
    // group chat constructor
    ChatMessage(ChatGroup toGroup, Client from, int type, String message) {
        this(type, message);
        this.toGroup = toGroup;
        this.from = from;
    }

    public int getType() {
        return this.type;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public Client getTo() {
        return this.to;
    }
    
    public Client getFrom() {
        return this.from;
    }
    
    public ChatGroup getChatGroup() {
        return this.toGroup;
    }
}

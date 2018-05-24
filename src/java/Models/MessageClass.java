/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.util.ArrayList;

/**
 *
 * @author Eugene
 */
public class MessageClass {
    private ArrayList<String> message = new ArrayList<String>();

    /**
     * @return the message
     */
    public ArrayList<String> getAllMessages() {
        return new ArrayList<>(this.message);
    }

    /**
     * @param message the message to set
     */
    public void addMessage(String message) {
        this.message.add(message);
    }
}

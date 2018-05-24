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
public class ExampleClass {
    private ArrayList<String> message = new ArrayList<String>();
    private transient String temporary; // This field will not be serialized to json

    /**
     * @return the id
     */

    /**
     * @return the temporary
     */
    public String getTemporary() {
        return temporary;
    }

    /**
     * @param temporary the temporary to set
     */
    public void setTemporary(String temporary) {
        this.temporary = temporary;
    }

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

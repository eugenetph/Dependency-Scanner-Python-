/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

/**
 *
 * @author Eugene Tan
 */
public class Libraries {
    
    /**
     * A string variable to store library name
     */
    private String librariesName;
    
    /**
     * A string variable to store library version
     */
    private String librariesVersion;

    /**
     * A contructor for Libraries class
     * @param librariesName
     * @param librariesVersion 
     */
    public Libraries(String librariesName, String librariesVersion) {
        this.librariesName = librariesName;
        this.librariesVersion = librariesVersion;
    }

    /**
     * Getter to get library name
     * @return library name
     */
    public String getLibrariesName() {
        return librariesName;
    }

    /**
     * Getter to get library version
     * @return library version
     */
    public String getLibrariesVersion() {
        return librariesVersion;
    }
}

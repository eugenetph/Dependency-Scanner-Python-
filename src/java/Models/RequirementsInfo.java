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
public class RequirementsInfo {
    
    /**
     * A string variable to store library name of the dependency
     */
    private String libName;
    
    /**
     * A string variable to store library version of the dependency
     */
    private String version;
    
    /**
     * A string variable to store the affected version of a dependency
     */
    private String affectedVersion;
    
    /**
     * A string variable to store the adversary description of the dependency
     */
    private String adversary;
    
    /**
     * A constructor for RequirementsInfo Class
     * @param libName
     * @param version
     * @param affectedVersion
     * @param adversary 
     */
    public RequirementsInfo(String libName, String version, String affectedVersion, String adversary){
        this.libName = libName;
        this.version = version;
        this.affectedVersion = affectedVersion;
        this.adversary = adversary;
    }

    /**
     * Getter to get the library name of the dependency
     * @return the library name
     */
    public String getLibName() {
        return libName;
    }

    /**
     * Getter to get the library version of the dependency
     * @return the library version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Getter to get the affected version of the dependency
     * @return the affected version
     */
    public String getAffectedVersion() {
        return affectedVersion;
    }

    /**
     * Getter to get the adversary description of the dependency
     * @return an adversary description
     */
    public String getAdversary() {
        return adversary;
    }
    
}

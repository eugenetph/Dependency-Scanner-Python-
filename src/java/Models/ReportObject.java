/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.util.ArrayList;

/**
 *
 * @author Eugene Tan
 */
public class ReportObject {
    
    /**
     * A variable to store project name
     */
    private String projectName;
    
    /**
     * A variable to store scanned information
     */
    private ArrayList<String> scannedInformation;
    
    /**
     * A variable to store dependency object
     */
    private ArrayList<DependencyObject> dependencyObject;
    
    /**
     * A variable to store cve object
     */
    private ArrayList<CveObject> cveObject;
    
    /**
     * Hash value of the project file (MD5)
     */
    private String hashValue;
    /**
     * Empty constructor
     */
    public ReportObject(){}
    
    /**
     * Constructor used for GSON
     */
    public ReportObject(String projectName, String hashValue, ArrayList<String> scannedInformation, ArrayList<DependencyObject> dependencyObject, ArrayList<CveObject> cveObject){
        this.projectName = projectName;
        this.hashValue = hashValue;
        this.scannedInformation = scannedInformation;
        this.dependencyObject = dependencyObject;
        this.cveObject = cveObject;
    }    

    /**
     * Getter to get the project name uploaded
     * @return the project name
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Getter to get an ArrayList of scanned information by Safety scanner
     * @return an ArrayList of scanned information
     */
    public ArrayList<String> getScannedInformation() {
        return scannedInformation;
    }

    /**
     * Getter to get an ArrayList of Dependency Object 
     * @return an ArrayList of Dependency Object 
     */
    public ArrayList<DependencyObject> getDependencyObject() {
        return dependencyObject;
    }

    /**
     * Getter to get an ArrayList of Cve Object
     * @return an ArrayList of Cve Object
     */
    public ArrayList<CveObject> getCveObject() {
        return cveObject;
    } 
    
    /**
     * Getter to get hash value of the file/project
     * @return the hash value of the file/project
     */
    public String getHashValue() {
        return hashValue;
    }
}

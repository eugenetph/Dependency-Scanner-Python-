/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import ReportModels.ReportDependency;
import com.google.gson.Gson;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Eugene Tan
 */
public class ReportController {
    
    /**
     * Initialize Gson
     */
    private static Gson gson = new Gson();
 
    /**
     * Static variable for mapping of object to its identifier
     */
    private static HashMap<String, ReportDependency> map = new HashMap();
    
    /**
     * Method to add the dependency object to its hash value
     * @param hash hash value of the project file
     * @param reportObject the Dependency object
     */
    public static void addReportObject(String hash, ReportDependency reportObject){
        map.put(hash, reportObject);
    }
    
    /**
     * Method to generate all JSON to object
     * @param path the path contains JSON file
     * @return 
     */
    public static ReportDependency generateReport(String path){
        ReportDependency dep = null;
        
        try(FileReader file = new FileReader(path)){
            dep = gson.fromJson(file, ReportDependency.class);
        }
        catch(Exception e){
            System.out.println("Error: " + e);
        }
        return dep;
    }
    
    /**
     * Method to get the dependency object
     * @param hash the hash value of the project file
     * @return a specified dependency object with it hash value 
     */
    public static ReportDependency getReportObject(String hash){
        return map.get(hash);
    }
    
    /**
     * Method to clear HashMap
     */
    public static void clearAll(){
        map.clear();
    }
}

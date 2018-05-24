/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.util.Comparator;
import java.util.HashMap;

/**
 *
 * @author Eugene Tan
 */
public class DependencyObject{
    
    /**
     * The name of the dependency
     */
    private String dependencyName;
    
    /**
     * The cpe type
     */
    private String cpe;
    
    /**
     * The coordinate
     */
    private String coordinates;
    
    /**
     * The highest severity level
     */
    private String severityLevel;
    
    /**
     * The CVE count
     */
    private int cveCount;
    
    /**
     * The CPE confidence level
     */
    private String cpeConfidence;
    
    /**
     * The evidence count
     */
    private int evidenceCount;
    
    /**
     * The dependency constructor
     */
    public DependencyObject (String dependencyName, String cpe, String coordinates, String severityLevel, 
            int cveCount, String cpeConfidence, int evidenceCount) {
        this.dependencyName = dependencyName;
        this.cpe = cpe;
        this.coordinates = coordinates;
        this.severityLevel = severityLevel;
        this.cveCount = cveCount;
        this.cpeConfidence = cpeConfidence;
        this.evidenceCount = evidenceCount;
    }

    /**
     * @return the dependencyName
     */
    public String getDependencyName() {
        return dependencyName;
    }

    /**
     * @return the cpe
     */
    public String getCpe() {
        return cpe;
    }

    /**
     * @return the coordinates
     */
    public String getCoordinates() {
        return coordinates;
    }

    /**
     * @return the severityLevel
     */
    public String getSeverityLevel() {
        return severityLevel;
    }

    /**
     * @return the cveCount
     */
    public int getCveCount() {
        return cveCount;
    }

    /**
     * @return the cpeConfidence
     */
    public String getCpeConfidence() {
        return cpeConfidence;
    }

    /**
     * @return the evidenceCount
     */
    public int getEvidenceCount() {
        return evidenceCount;
    }   

    /**
     * Method Comparator to sort in cve number in asc or desc 
     */
    public static class cveCountComparator implements Comparator{

        @Override
        public int compare(Object o1, Object o2) {

            DependencyObject dependenciesOne = (DependencyObject)o1;
            DependencyObject dependenciesTwo = (DependencyObject)o2;
            
            return dependenciesOne.cveCount - dependenciesTwo.cveCount;
        }
    }
    
    /**
     * Method Comparator to sort the evidence number in asc or desc
     */
    public static class evidenceCountComparator implements Comparator{

        @Override
        public int compare(Object o1, Object o2) {

            DependencyObject dependenciesOne = (DependencyObject)o1;
            DependencyObject dependenciesTwo = (DependencyObject)o2;
            
            return dependenciesOne.evidenceCount - dependenciesTwo.evidenceCount;
        }
        
    }
    
    /**
     * Method Comparator to sort the cpe confidence level in asc or desc
     */
    public static class cpeConfidenceComparator implements Comparator{

        @Override
        public int compare(Object o1, Object o2) {
            HashMap <String, Integer> map = new HashMap();
            map.put("highest", 3);
            map.put("medium", 2);
            map.put("low", 1);
            
            DependencyObject dependenciesOne = (DependencyObject)o1;
            DependencyObject dependenciesTwo = (DependencyObject)o2;
            
            return map.get(dependenciesOne.cpeConfidence.toLowerCase()) - map.get(dependenciesTwo.cpeConfidence.toLowerCase()) ;
        }   
    }
    
    /**
     * Method Comparator to sort the severity level in asc or desc
     */
    public static class severityComparator implements Comparator{

        @Override
        public int compare(Object o1, Object o2) {

        DependencyObject dependenciesOne = (DependencyObject)o1;
        DependencyObject dependenciesTwo = (DependencyObject)o2;
        HashMap<String, Integer> map = new HashMap();
        map.put("high", 3);
        map.put("medium", 2);
        map.put("low", 1);

        int thisSeverity = map.get(dependenciesOne.severityLevel.toLowerCase());
        int otherSeverity = map.get(dependenciesTwo.severityLevel.toLowerCase());
        
        return thisSeverity - otherSeverity; 
        }
    }
}

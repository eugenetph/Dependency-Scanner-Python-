/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.CveObject;
import Models.DependencyObject;
import Models.ReportObject;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Eugene Tan
 */
public class ReportExtractionController {
    
    /**
     * Constant variable for project name extraction
     */
    private static final String PROJECTHEADER = "Project";
    
    /**
     * Use regex patterns when extracting cve number.
     */
    private static final Pattern CVENUMBER = Pattern.compile("(CVE-\\d{4}-\\d{4}) "); 
    
    /**
     * Use regex patterns when extracting cvss score number.
     */
    private static final Pattern SCORE = Pattern.compile(" (\\d.\\d) "); 
    
    /**
     * Use regex patterns when extracting severity level number.
     */
    private static final Pattern SEVERITY = Pattern.compile(" (Low|Medium|High) ");
    
    /**
     * Use regex patterns when extracting file path
     */
    private static final Pattern FILE_PATH = Pattern.compile("([a-zA-Z]:(\\\\([a-zA-Z0-9_-]|\\s)+)+([a-zA-Z.]+))");
    
    /**
     * Use regex patterns when extract MD5 string
     */
    private static final Pattern MD5 = Pattern.compile("(MD5:\\s[a-z0-9]+)");
    
    /**
     * Use regex patterns when extract SHA1 string
     */
    private static final Pattern SHA1 = Pattern.compile("(SHA1:\\s[a-z0-9]+)");
    
    /**
     * Project name variable
     */
    private String projectName;
    
    /**
     * ArrayList to store multiple cveObject
     */
    private ArrayList<CveObject> cveObject;
    
    /**
     * ReportObject to store report summary
     */
    private ReportObject reportModel;
    
    /**
     * ArrayList to store multiple dependencyObject
     */
    private ArrayList<DependencyObject> dependencyObject;
    
    /**
     * ArrayList that store scanned information from the report
     */
    private ArrayList scannedInformation;
    
    /**
     * Store OWASP dependency check report path
     */
    private String path;
    
    /**
     * Store hash value of project file (MD5)
     */
    private String hashValue;
    
    /**
     * The constructor for this class
     * @param path 
     */
    public ReportExtractionController(String path, String hashValue){
//        this.path = path + OWASP_ReportName;
           this.path = path;
           this.hashValue = hashValue;
    }
    /**
     * A method for input html file path
     * @param path the file path
     * @return
     * @throws IOException 
     */
    private Document DOM(String path) throws IOException{
        File input = new File(path);
        Document d = Jsoup.parse(input, "UTF-8");
        return d;
    }
    /**
     * Method to trimmer your desire sentence/text from a full string
     * @param fullPart the full string
     * @param projectHeader the project header to be search in a full text
     * @param separator the separator in the full string
     * @return specific string from a full string
     */
    private String Trimmer(String fullPart, String projectHeader, String separator) {
        String partHeader = fullPart;
        for (String content : partHeader.split(separator)) {
            if (content.trim().startsWith(projectHeader)) {
                return content.substring(
                        content.indexOf(":") + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
    
    /**
     * Method to extract the project name
     * @param d the document
     * @param projectHeader the project header to be search in a full text
     * @return the project name
     */
    private String projectName(Document d, String projectHeader){
        String projName = "";
        String extractedData;
        
        Elements elements = d.select("div.wrapper");
        for(Element element: elements){
            extractedData = element.select("h2").first().text();
            projName = Trimmer(extractedData, projectHeader, ";");
        }
        return projName;
    }
    
    /**
     * Method to extract the scan information
     * @param d the DOM document
     * @return Array of scan information
     */
    private ArrayList<String> scanInformation(Document d){
        ArrayList<String> extractedArrayData = new ArrayList<>();
        String extractedData;
        Elements elements = d.select("div.wrapper");
        
        for(int i = 0; i<6; i++){
            extractedData = elements.select("div ul.indent li").get(i).text();
            extractedArrayData.add(extractedData);
        }
        return extractedArrayData;
    }
    
    /**
     * Method to extracting desired each cve details into an CveObject java class
     * @param d the DOM document
     * @return ArrayList of CveObject
     */
    private ArrayList<CveObject> cveDetails(Document d) {
        ArrayList<CveObject> cve = new ArrayList<>();
        Matcher match;
        String cveNumber;
        String severity;
        String cvssScore;
        String description;
        String number ="";
        String sev = "";
        double score = 0;

        Elements elements = d.select("div#content5 p");

        for (int i = 1; i < elements.size(); i += 6) {
            cveNumber = elements.eq(i - 1).text();
            match = CVENUMBER.matcher(cveNumber);
            if (match.find()) {
                number = match.group(1);
            }

            severity = elements.eq(i).text();
            match = SEVERITY.matcher(severity);
            if (match.find()) {
                sev = match.group(1);
            }

            cvssScore = elements.eq(i).text();
            match = SCORE.matcher(cvssScore);
            if (match.find()) {
                score = Double.parseDouble(match.group(1));
            }
            
            description = elements.eq(i + 1).text();
            
            cve.add(new CveObject(number, score, sev, description));
        }
        return cve;
    }
    
    /**
     * Method to extracting of dependency object into DependencyObject java class (Currently not in used)
     * @param d the DOM document
     * @return the Dependency Object
     */
    private ArrayList<DependencyObject> setDependencyDetail(Document d){
        ArrayList<DependencyObject> dependency = new ArrayList<>();;
        String dependencyName;
        String cpe;
        String coordinate;
        String highestSeverity;
        String cpeConfidence;       
        int cveCount;
        int evidenceCount;
  
        Elements elements = d.select("table#summaryTable tbody tr.vulnerable td");
        
        for (int i = 0; i < elements.size(); i+=7) {
            dependencyName = elements.select("a").get(i).text();
            cpe = elements.select("a").get(i+1).text();
            coordinate = elements.get(i+2).text();
            highestSeverity = elements.get(i+3).text();
            cveCount = Integer.parseInt(elements.get(i+4).text());
            cpeConfidence = elements.get(i+5).text();
            evidenceCount = Integer.parseInt(elements.get(i+6).text());
      
            dependency.add(new DependencyObject(dependencyName, cpe, coordinate, highestSeverity, 
                    cveCount, cpeConfidence, evidenceCount));
        }
        return dependency;
    }
    
    /**
     * Method to extract project description from OWASP report
     * @param d
     * @return the project description
     */
    private String projectDescription(Document d){
        Elements elements = d.select("div.subsectioncontent p");
        return elements.get(1).text();
    } 
    
    /**
     * Method to extract project file path from OWASP report
     * @param d the DOM document
     * @return the project file path
     */
    private String filePath(Document d){
        Elements elements = d.select("div.subsectioncontent");
        String path;
        String extractedData;
        
        extractedData = elements.get(0).text();
        Matcher match = FILE_PATH.matcher(extractedData);
        if (match.find()) {
            path = match.group(1);
        }
        else{
            path = "";
        }
        return path;
    } 
    
    /**
     * Method to create Report Summary Model
     * @param OWN_URL_PATH the given report path for html extraction
     * @throws IOException 
     */
    public void createASummaryReport()
            throws IOException {
        Document d = DOM(path);
        projectName = projectName(d, PROJECTHEADER);
        scannedInformation = scanInformation(d);
        cveObject = cveDetails(d);
//        dependencyObject = dependencyDetail(d);
        reportModel = new ReportObject(projectName, hashValue, scannedInformation, dependencyObject, cveObject);
    }
    
    /**
     * Get the Report Summary Report created by this class
     * @return the report summary report
     */
    public ReportObject getReportSummaryReport(){
        return reportModel;
    }
}

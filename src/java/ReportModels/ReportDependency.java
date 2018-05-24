/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReportModels;

import Models.DependencyObject;
import Models.Libraries;
import Models.RequirementsInfo;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author Eugene Tan
 */
public class ReportDependency {
    private ScanInfo scanInfo;

    private Dependencies[] dependencies;

    private ProjectInfo projectInfo;

    private String reportSchema;
    
    private ArrayList<String> extractedArrayData;
    
    private DependencyObject[] vulDependency;
    
    private DependencyObject[] notVulDependency;

    private RequirementsInfo[] requirementsInfo;
    
    private ArrayList<Libraries> missing_Libraries;
    
    private ArrayList<Libraries> unaffected_Libraries;
    
    private String packageNumber;
    
    private String fileName;
    
    private String filePath;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    
    public ScanInfo getScanInfo ()
    {
        return scanInfo;
    }

    public void setScanInfo (ScanInfo scanInfo)
    {
        this.scanInfo = scanInfo;
    }

    public Dependencies[] getDependencies ()
    {
        return dependencies;
    }

    public void setDependencies (Dependencies[] dependencies)
    {
        this.dependencies = dependencies;
    }

    public ProjectInfo getProjectInfo ()
    {
        return projectInfo;
    }

    public void setProjectInfo (ProjectInfo projectInfo)
    {
        this.projectInfo = projectInfo;
    }

    public String getReportSchema ()
    {
        return reportSchema;
    }

    public void setReportSchema (String reportSchema)
    {
        this.reportSchema = reportSchema;
    }
    
    public ArrayList<String> getScanInformation(){
        return extractedArrayData;
    }
    
    public void setScanInformation(String path)throws IOException{
        File input = new File(path);
        Document d = Jsoup.parse(input, "UTF-8");
        extractedArrayData = new ArrayList<>();
        String extractedData;
        Elements elements = d.select("div.wrapper");
        
        for(int i = 0; i<6; i++){
            extractedData = elements.select("div ul.indent li").get(i).text();
            extractedArrayData.add(extractedData);
        }
    }
    
    public void setDependencyDetail(String path)throws IOException{
        File input = new File(path);
        Document d = Jsoup.parse(input, "UTF-8");
        String dependencyName;
        String cpe;
        String coordinate;
        String highestSeverity;
        String cpeConfidence;
        
        int cveCount;
        int evidenceCount;
        int counter = 0;
  
        Elements elements = d.select("table#summaryTable tbody tr.vulnerable td");
        vulDependency = new DependencyObject[(elements.size()/7)];
        for (int i = 0; i < elements.size(); i+=7) {
            dependencyName = elements.get(i).text();
            cpe = elements.get(i+1).text();
            coordinate = elements.get(i+2).text();
            highestSeverity = elements.get(i+3).text();
            cveCount = Integer.parseInt(elements.get(i+4).text());
            cpeConfidence = elements.get(i+5).text();
            evidenceCount = Integer.parseInt(elements.get(i+6).text());
      
            vulDependency[counter++] = new DependencyObject(dependencyName, cpe, coordinate, highestSeverity, 
                    cveCount, cpeConfidence, evidenceCount);
        }
    }
    
    public void setUnvulnerableDependencyDetail(String path)throws IOException{
        File input = new File(path);
        Document d = Jsoup.parse(input, "UTF-8");
        String dependencyName;
        String cpe;
        String coordinate;
        String highestSeverity;
        String cpeConfidence;
        
        int cveCount;
        int evidenceCount;
        int counter = 0;
  
        Elements elements = d.select("table#summaryTable tbody tr.notvulnerable td");
        notVulDependency = new DependencyObject[(elements.size()/7)];
        for (int i = 0; i < elements.size(); i+=7) {
            dependencyName = elements.get(i).text();
            cpe = elements.get(i+1).text();
            coordinate = elements.get(i+2).text();
            highestSeverity = elements.get(i+3).text();
            cveCount = Integer.parseInt(elements.get(i+4).text());
            cpeConfidence = elements.get(i+5).text();
            evidenceCount = Integer.parseInt(elements.get(i+6).text());
      
            notVulDependency[counter++] = new DependencyObject(dependencyName, cpe, coordinate, highestSeverity, 
                    cveCount, cpeConfidence, evidenceCount);
        }
    }
    
    public DependencyObject[] getDependencyDetail(){
        return vulDependency;
    }
    
    public DependencyObject[] getNotVulDependencyDetail(){
        return notVulDependency;
    }
    
    public ArrayList<Libraries> getMissingLibraries() {
        return missing_Libraries;
    }

    public void setMissingLibraries(ArrayList<Libraries> missing_Libraries) {
        this.missing_Libraries = missing_Libraries;
    }
    
    public ArrayList<Libraries> getUnaffectedLibraries() {
        return unaffected_Libraries;
    }

    public void setUnaffectedLibraries(ArrayList<Libraries> unaffected_Libraries) {
        this.unaffected_Libraries = unaffected_Libraries;
    }
    
    public RequirementsInfo[] getRequirementsInfo() {
        return requirementsInfo;
    }

    public void setRequirementsInfo(RequirementsInfo[] requirementsInfo) {
        this.requirementsInfo = requirementsInfo;
    }
    
    public String getPackageNumber() {
        return packageNumber;
    }

    public void setPackageNumber(String packageNumber) {
        this.packageNumber = packageNumber;
    }
    

    @Override
    public String toString()
    {
        return "ClassPojo [scanInfo = "+scanInfo+", dependencies = "+dependencies+", projectInfo = "+projectInfo+", reportSchema = "+reportSchema+"]";
    }
}

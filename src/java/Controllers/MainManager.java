/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.ReportObject;
import ReportModels.ReportDependency;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Eugene Tan
 */
public class MainManager {

    /**
     * A variable to store file path for scanning
     */
    private ArrayList<String> filePath = new ArrayList<>();

    /**
     * A variable to store file name
     */
    private ArrayList<String> fileName = new ArrayList<>();

    /**
     * Set HttpServletRequest request
     */
    private HttpServletRequest request;

    /**
     * Get HttpServletResponse response
     */
    private HttpServletResponse response;

    /**
     * The constructor of this class
     */
    public MainManager(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    /**
     * Method to invoke scanners and fileController
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void mainManager()
            throws ServletException, IOException {
        try {
            FileController.jobProcessForUploadFiles(request, response);
            SScannerController.clearAll();
            ReportController.clearAll();
            filePath = FileController.getFilePathForScanner();
            fileName = FileController.getFileNameForScanner();
            for (int i = 0; i < fileName.size(); i++) {
                    if (!FileController.getFileExistFlag().get(i)) {
                        System.out.println("Dscanner in progress");
                        DScannerController.invokeDScanner(filePath.get(i), fileName.get(i), "ALL");
                        System.out.println("DScanner complete!");
                    } else {
                        System.out.println("DScanner is not invoked");
                    }
                    SScannerController.invokeSScanner(fileName.get(i), filePath.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error in MainManager: " + e);
        }
    }
    
    /**
     * Method to retrieve the report
     * @return the report
     */
    public ReportDependency getReportSummary(int num) {
        String JSON_FILE_PATH = filePath.get(num) + "\\dependency-check-report.json";
        return ReportController.generateReport(JSON_FILE_PATH);
    }
    
    /**
     * Method to get the number of file
     * @return the number of file
     */
    public int numberOfFile(){
        return fileName.size();
    }
    
    /**
     * Method to get an ArrayList of the file name of the projects
     * @return an ArrayList of the file name
     */
    public ArrayList<String> fileName(){
        return fileName;
    }
    
    /**
     * Method to get the an ArrayList of filePath to the projects
     * @return an ArrayList of filePath
     */
    public ArrayList<String> filePath(){
        return filePath;
    }
}

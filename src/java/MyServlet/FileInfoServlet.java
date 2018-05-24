/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyServlet;

import Controllers.FileController;
import Controllers.ReportController;
import Controllers.MainManager;
import Controllers.SScannerController;
import Models.DependencyObject;
import Models.RequirementsInfo;
import ReportModels.ReportDependency;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.MultipartConfig;
import java.util.ArrayList;
import com.google.gson.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Eugene Tan
 */
@MultipartConfig
public class FileInfoServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet FileInfoServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet FileInfoServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DateTimeFormatter dtf;
        dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println("Before: " + dtf.format(now));
        // Creates response object
        MainManager main_Manager = new MainManager(request, response);
        RequirementsInfo[] reqInfo;
        ArrayList<String> serialized = new ArrayList<>();
        Models.ExampleClass obj = new Models.ExampleClass();
        String s;

        PrintWriter out = response.getWriter();
        String fileType = request.getContentType();
        try {
            if (fileType != null && fileType.contains("multipart/form-data")) {
                main_Manager.mainManager();
                for (int j = 0; j < main_Manager.numberOfFile(); j++) {
                    System.out.println("we are at number: " + j);
                    ReportDependency reportSummary;
                    reportSummary = main_Manager.getReportSummary(j);
                    System.out.println("see size:" + SScannerController.getRequirementsObject().size());
                    reqInfo = SScannerController.getRequirementsObject().get(j);
                    s = FileController.getFilePathForScanner().get(j) + "\\dependency-check-report.html";
                    reportSummary.setScanInformation(s);
                    reportSummary.setDependencyDetail(s);
                    reportSummary.setUnvulnerableDependencyDetail(s);
                    reportSummary.setRequirementsInfo(reqInfo);
                    reportSummary.setMissingLibraries(SScannerController.missingLibraries().get(j));
                    reportSummary.setUnaffectedLibraries(SScannerController.unaffectedLibraries().get(j));
                    reportSummary.setPackageNumber(SScannerController.getNumberOfPackage().get(j));
                    ReportController.addReportObject(FileController.getDirHashValue().get(j), reportSummary);
                }
                obj.addMessage("File Upload and Scanning Complete! ");
                dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                now = LocalDateTime.now();
                System.out.println("After: " + dtf.format(now));
            } else {
                obj.addMessage("File Upload Failure!");
            }
        } catch (IOException | ServletException e) {
            obj.addMessage("File Upload Failure!");
            System.out.println("Error in Servlet: " + e);
        }
        // Serialize response object to JSON string
        Gson gson = new Gson();
        serialized.add(gson.toJson(obj));
        serialized.add(gson.toJson(FileController.getDirHashValue().get(0)));
        serialized.add(gson.toJson(FileController.getFileNameForScanner().get(0)));

        // Send response back to client
        out.println(serialized);
//      processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

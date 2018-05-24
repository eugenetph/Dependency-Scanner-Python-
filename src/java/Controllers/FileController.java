/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import ConstantsVariable.FinalConstants;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author Eugene Tan
 */
public class FileController {

    /**
     * Boolean directory flag indicator
     */
    private static ArrayList<Boolean> dirFlag = new ArrayList<>();

    /**
     * The project file path for scanning
     */
    private static ArrayList<String> filePath = new ArrayList<>();

    /**
     * An ArrayList that contains file name of the projects
     */
    private static ArrayList<String> fileName = new ArrayList<>();

    /**
     * An ArrayList that contains hash value of the projects
     */
    private static ArrayList<String> hashDirName = new ArrayList<>();

    /**
     * An ArrayList that contains the flag for file that exist
     */
    private static ArrayList<Boolean> fileExistFlag = new ArrayList<Boolean>();

    /**
     * Method to call method writeFileIntoDir to duplicate uploaded file into a
     * specified directory
     *
     * @throws ServletException
     * @throws IOException
     */
    public static void jobProcessForUploadFiles(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        clearAll();
        ArrayList<Part> filePart = new ArrayList<Part>();

        //Get file from FormData in Ajax
        for (int j = 0; request.getPart("file" + j) != null; j++) {
            filePart.add(request.getPart("file" + j));

//        filePart = request.getPart("file1");
            ArrayList<String> pathName = getCompletionDirPathName();
            String pathToCompletionDir;
            File targetPath = null;
            PrintWriter out = response.getWriter();

            try {
                for (int i = 0; i < pathName.size(); i++) {
                    pathToCompletionDir = pathName.get(i);
                    targetPath = new File(pathToCompletionDir);

                    //Validation of Completion directory path
                    if (targetPath.exists()) {
                        writeFileIntoDir(pathToCompletionDir, filePart.get(j));
                        setDirFlag(true);
                        break;
                    } else {
                        setDirFlag(false);
                    }
                }
            } catch (Exception e) {
                out.println("However, Directory was not created successfully! Error while creating directory in Java " + e);
            }
        }
    }

    /**
     * Method invocation to retrieve the directory path for completion directory
     * uploaded file
     *
     * @return the directory path
     */
    private static ArrayList<String> getCompletionDirPathName() {
        ArrayList<String> pathName = new ArrayList<String>();
        pathName.add("C:\\Users\\Eugene\\Desktop\\GitRepository\\Dependency-Scanner-Python\\DependencyCheck\\web\\Completion\\");
        pathName.add("C:\\Users\\Eugene Tan\\Desktop\\GitRepository\\DependencyCheck\\DependencyCheck\\web\\Completion\\");
        return pathName;
    }

    /**
     * Getter to get project file path
     *
     * @return the project file path
     */
    public static ArrayList<String> getFilePathForScanner() {
        return filePath;
    }

    /**
     * Getter to get the project name for dependency scanner/check
     *
     * @return the project name
     */
    public static ArrayList<String> getFileNameForScanner() {
        return fileName;
    }

    /**
     * Method to extract the
     *
     * @param part
     * @return the extracted project name
     */
    public static String getFileName(Part part) {
        String partHeader = part.getHeader("Content-Disposition");
        for (String content : partHeader.split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf("=") + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    /**
     * Method to extract the file extensive name (Currently not in used)
     *
     * @param part
     * @return the file extensive name Currently unused
     */
    private static String fileExtensionName(Part part) {
        String partHeader = part.getHeader("Content-Disposition");
        for (String content : partHeader.split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf(".") + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    /**
     * Method to duplicate uploaded file into a specified directory
     *
     * @param pathToCompletionDir the directory path that store the project
     * @param filePart
     * @param output
     */
    private static void writeFileIntoDir(String pathToCompletionDir, Part filePart) {
        File targetFile = null;
        File file = null;
        InputStream inputStream;
        String localFilePath = null;
        String localFileName = null;
        String localHashDirName = null;
        FileOutputStream outputStream;
        byte[] buffer = new byte[8 * 1024];
        byte[] hash = null;
        int bytesRead;

        try {
            inputStream = filePart.getInputStream();
            localFileName = getFileName(filePart);
            fileName.add(localFileName);
            System.out.println("Get part Name: " + localFileName);
            localHashDirName = hashFileContent(filePart);
            hashDirName.add(localHashDirName);
            localFilePath = pathToCompletionDir + localHashDirName;
            filePath.add(localFilePath);
            targetFile = new File(localFilePath);
            if (!targetFile.exists()) {
                setFileExistFlag(false);
                createADirectory(localFilePath);
                file = new File(localFilePath, localFileName);
                file.createNewFile();
                outputStream = new FileOutputStream(file);
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
                outputStream.close();
            }
            else{
                setFileExistFlag(true);
            }
        } catch (IOException e) {
            System.out.println("File error at FileController class: " + e);
        }
    }

    /**
     * Method to physically create a directory for storing duplicated uploaded
     * project
     *
     * @param filePath
     */
    private static void createADirectory(String filePath) {
        File Dir = new File(filePath);

        if (!Dir.exists()) {
            try {
                Dir.mkdir();
            } catch (SecurityException se) {
                System.out.println("Message error at FileController Class: " + se);
            }
        }
    }


    
    /**
     * Method to get the hash value of the project file
     *
     * @param filePart
     * @param output
     * @return the string of hash value
     */
    private static String hashFileContent(Part filePart) {
        MessageDigest digest;
        InputStream inputStream;
        byte[] buffer = new byte[8 * 1024];
        byte[] hash = null;
        String hashValue;
        int bytesRead;

        try {
            inputStream = filePart.getInputStream();
            digest = MessageDigest.getInstance("SHA-256");
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
            hash = digest.digest();
            hashValue = base16Encoder(hash);

            return hashValue;
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * Method to convert desirable type of base encoder for the hash value
     *
     * @param hash
     * @return the hash value of a project
     */
    private static String base16Encoder(byte[] hash) {
        int quotient;
        int remainder;
        String hexadecimal = "";
        String reverse = "";
        String[] hexAlpha = {"a", "b", "c", "d", "e", "f"};
        for (Byte b : hash) {
            if (b < 0) {
                quotient = b + 256;
            } else {
                quotient = b;
            }
            while (quotient != 0) {
                remainder = quotient % 16;
                if (remainder > 9) {
                    reverse += hexAlpha[remainder - 10];

                } else {
                    reverse += Integer.toString(remainder);
                }
                quotient /= 16;
            }
            hexadecimal += new StringBuffer(reverse).reverse().toString();
            reverse = "";
        }
        return hexadecimal;
    }

    /**
     * Setter to set the directory flag
     *
     * @param dirFlag
     */
    private static void setDirFlag(boolean flag) {
        dirFlag.add(flag);
    }

    /**
     * A method to clear all the content in an ArrayList
     */
    private static void clearAll() {
        filePath.clear();
        fileName.clear();
        hashDirName.clear();
        fileExistFlag.clear();
    }

    /**
     * Getter to get the directory flag status
     *
     * @return the directory flag status
     */
    public static ArrayList<Boolean> getDirFlag() {
        return dirFlag;
    }

    /**
     * Getter to get an ArrayList of hash value
     * @return an ArrayList of hash value
     */
    public static ArrayList<String> getDirHashValue() {
        return hashDirName;
    }

    /**
     * Getter to get the path to OWASP report
     * @return 
     */
    public static String getReportPathOWASP() {
        return getFilePathForScanner() + FinalConstants.OWASP_report;
    }

    /**
     * setter to add flag status to the fileExistFlag ArrayList
     * @param flag 
     */
    public static void setFileExistFlag(boolean flag) {
        fileExistFlag.add(flag);
    }

    /**
     * Getter to get the Flag status of the file
     * @return 
     */
    public static ArrayList<Boolean> getFileExistFlag() {
        return fileExistFlag;
    }

}

    /**
     * Method invocation to stores all the file or sub-directories names in the
     * parent directory
     *
     * @param pathToCompletionDir the directory path that stores the project
     * before dependency scan
     * @return
     */
//    private static Set<String> storeFolderNameInHashSet(String pathToCompletionDir){
//        String dirPath = pathToCompletionDir;
//        Set<String> files = new HashSet<String>();
//        File directory = new File(dirPath);
//        File[] directoryFiles = directory.listFiles();
//        String filename;
//        
//        for(int i = 0; i < directoryFiles.length; i++){
//            filename = directoryFiles[i].getName();
//            files.add(filename);
//        }
//        return files;
//    }
    /**
     * Method to check whether the directory stores the specific file
     *
     * @param filename
     * @return true if found, otherwise false
     */
//    private static boolean searchFile(String filename){
//        return fileSet.contains(filename);
//    }
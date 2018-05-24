/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import ConstantsVariable.FinalConstants;
import Models.Libraries;
import Models.RequirementsInfo;
import ReportModels.ReportDependency;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.simple.JSONObject;

/**
 *
 * @author Eugene Tan
 */
public class SScannerController {

    /**
     * Constant input command for safety check
     */
    private static final String SAFETY_CHECK = "safety check";

    /**
     * Constant input command for requirements file
     */
    private static final String REQ_TXT = " -r \"@directory\"";

    /**
     * Constant input command to generate full report
     */
    private static final String FULL_REPORT = " --full-report";

    /**
     * Searching regex pattern for the path that contain requirements.txt
     */
    private static final Pattern FILE_PATH = Pattern.compile("([a-zA-Z]:(\\\\([a-zA-Z0-9_-]|\\s)+)+(.txt))");

    /**
     * Searching regex pattern for the currently installed version of dependency
     */
    private static final Pattern VERSION_PATTERN = Pattern.compile("(installed\\s([0-9]+[.]?)+)");

    /**
     * Searching regex pattern for the installed version
     */
    private static final Pattern INSTALLED_VERSION = Pattern.compile("(([0-9]+[.]?)+)");
    
    /**
     * Searching regex pattern for the affected library for report
     */
    private static final Pattern AFFECTED_LIBRARY = Pattern.compile("(([<]|(<=))([0-9]+[.]?)+)");

    /**
     * Searching regex pattern for the package scanned by Safety scanner
     */
    private static final Pattern PACKAGE_SCANNED = Pattern.compile("([0-9]+\\spackages)");

    /**
     * Searching regex for the adversary description of a dependency
     */
    private static final Pattern ADVERSARY_INFO_1 = Pattern.compile("((id\\s[0-9]+.*))");

    /**
     * Searching regex pattern for extension file name of zip
     */
    private static final Pattern EXTENSION_FILE_NAME = Pattern.compile("(zip)");

    /**
     * Searching regex pattern for the library name in the requirements.txt file
     */
    private static final Pattern LIBRARIES_NAME = Pattern.compile("(([a-zA-Z0-9]-?)+)");

    /**
     * Searching regex pattern for the library version in the requirements.txt file
     */
    private static final Pattern LIBRARIES_VERSION = Pattern.compile("(([0-9]+[.]?)+)");

    /**
     * An ArrayList to store the requirements information in requirements.txt
     */
    private static ArrayList<RequirementsInfo[]> ri = new ArrayList<RequirementsInfo[]>();

    /**
     * An ArrayList to store the missing libraries in the safety database
     */
    private static ArrayList<ArrayList<Libraries>> missingLibraries = new ArrayList<ArrayList<Libraries>>();
    
    /**
     * An ArrayList to store the unaffected Libraries in the requirements.txt file
     */
    private static ArrayList<ArrayList<Libraries>> unaffectedLibraries = new ArrayList<ArrayList<Libraries>>();
    
    /**
     * An ArrayList to store the package scanned information
     */
    private static ArrayList<String> packageScanned = new ArrayList<>();
    

    /**
     * A method to call safety check scanner
     */
    public static void invokeSScanner(String fileName, String filePath) {
        String localRequirementsFilePath = null;
        RequirementsInfo[] reqInfo;
        String input = "";
        try {
            String extension = getFileExtension(fileName);
            System.out.println("fileName: " + fileName);
            if (extension.equals("zip")) {
                System.out.println("Extension file name: " + extension);
            } else {
                System.out.println("Extension file name is not a zip file");
            }
            localRequirementsFilePath = getRequirementsPath(fileName, filePath);
            System.out.println("Return!");
            if (localRequirementsFilePath != null) {
                while (input.equals("")) {
                    System.out.println("loop");
                    System.out.println("check: " + SAFETY_CHECK + REQ_TXT.replace("@directory", localRequirementsFilePath) + FULL_REPORT);
                    Process process = Runtime.getRuntime().exec(SAFETY_CHECK + REQ_TXT.replace("@directory", localRequirementsFilePath) + FULL_REPORT);
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                        String line;
                        while (((line = reader.readLine()) != null)) {
                            input += line;
                        }
                        System.out.println("input:" + input);
                        if (!input.equals("")) {
                            reqInfo = generateRequirementsObject(input);
                            ri.add(reqInfo);
                            setMissingAndVulLibraries(localRequirementsFilePath, reqInfo);
                            packageScanned.add(generatedPackageScanned(input));
                        }
                    }
                }
            } else {
                ri.add(null);
                missingLibraries.add(null);
                unaffectedLibraries.add(null);
                packageScanned.add(null);
                System.out.println("Requirements.txt path does not exist!");
            }
            System.out.println("Count now: " + ri.size());
        } catch (IOException e) {
            System.out.println("Error message from SScannerController: " + e);
        }
        System.out.println("SScanner complete!");
    }

    /**
     * Method to get the path that contain requirements.txt
     *
     * @param fileName the project file name
     * @param filePath the path that contain the project name
     * @return the path that contain requirements.txt
     */
    private static String getRequirementsPath(String fileName, String filePath) {
        String path = null;
        try {
            Process process = Runtime.getRuntime().exec("cmd /c " + FinalConstants.batchPath + " " + fileName, null, new File(filePath));
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String input = "";
                String line;
                while ((line = reader.readLine()) != null) {
                    input += line;
                }
                Matcher match = FILE_PATH.matcher(input);
                if (match.find()) {
                    path = match.group(1);
                } else {
                    path = null;
                }
                System.out.println("Regex path result for requirements.txt: " + path);
            }
        } catch (IOException e) {
            System.out.println("Error message from getRequirementsPath method: " + e);
        }
        return path;
    }

    /**
     * Method to retrieve the extension name of the file
     *
     * @param fileName the project file name
     * @return just the extension name of the file
     */
    private static String getFileExtension(String fileName) {
        String extension;
        Matcher extensionName = EXTENSION_FILE_NAME.matcher(fileName);
        if (extensionName.find()) {
            extension = extensionName.group(1);
        } else {
            extension = "";
        }
        return extension;
    }

    /**
     * Method that set the missing and vulnerable libraries to an ArrayList
     * @param requirementsFilePath
     * @param reqInfo 
     */
    public static void setMissingAndVulLibraries(String requirementsFilePath, RequirementsInfo[] reqInfo) {
        Map<String, Object> databaseMap = getDatabase();
        ArrayList<Libraries> missingLibrariesPt1 = new ArrayList<>();
        ArrayList<Libraries> unaffectedLibrariesPt1 = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(requirementsFilePath))) {
            String line = null;
            String librariesName = null;
            String librariesVersion = null;
            while ((line = reader.readLine()) != null) {
                Matcher libName = LIBRARIES_NAME.matcher(line);
                Matcher libVersion = LIBRARIES_VERSION.matcher(line);
                if (libName.find()) {
                    librariesName = libName.group(1);
                    if(libVersion.find()){
                        librariesVersion = libVersion.group(1);
                    }else{
                        librariesVersion = "";
                    }

                    if (!databaseMap.keySet().contains(librariesName)) {
                        missingLibrariesPt1.add(new Libraries(librariesName, librariesVersion));
                    } else {
                        boolean unaffectedFlag = true;
                        for (RequirementsInfo r : reqInfo) {
                            if (r.getLibName().equals(librariesName)) {
                                unaffectedFlag = false;
                                break;
                            }
                        }
                        if (unaffectedFlag) {
                            unaffectedLibrariesPt1.add(new Libraries(librariesName, librariesVersion));
                        }
                    }
                }
            }
            missingLibraries.add(missingLibrariesPt1);
            unaffectedLibraries.add(unaffectedLibrariesPt1);
        } catch (IOException e) {
            System.out.println("Error message from getMissingLibraries Method: " + e);
        }

    }

    /**
     * Get an ArrayList of missing libraries
     * @return an ArrayList of missing libraries
     */
    public static ArrayList<ArrayList<Libraries>> missingLibraries() {
            return missingLibraries;
    }

    /**
     * Get an ArrayList of unaffected libraries 
     * @return an ArrayList of unaffected libraries
     */
    public static ArrayList<ArrayList<Libraries>> unaffectedLibraries() {
            return unaffectedLibraries;
    }

    /**
     * Method to get the safety database
     * @return the database
     */
    private static Map<String, Object> getDatabase() {
        Map<String, Object> mapping = null;
        Gson engine = new Gson();

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(FinalConstants.safetyDbPath))) {
            mapping = engine.fromJson(reader, Map.class);
        } catch (IOException e) {
            System.out.println("Error message from getDatabase Method: " + e);
        }

        return mapping;
    }

    /**
     * Method to get an ArrayList safety scanner result (Multiple)
     * @param fullPart
     * @param projectHeader
     * @param separator
     * @return an ArrayList safety scanner result
     */
    private static ArrayList<String> trimmerMul(String fullPart, String projectHeader, String separator) {
        String partHeader = fullPart;
        ArrayList<String> getString = new ArrayList<>();
        for (String content : partHeader.split(separator)) {
            if (content.trim().startsWith(projectHeader)) {
                getString.add(content.substring(
                        content.indexOf(">") + 1).trim().replace("\"", ""));
            }
        }
        return getString;
    }

    /**
     * Method to get an ArrayList of specified sentence
     * @param fullPart
     * @param projectHeader
     * @param separator
     * @return an ArrayList of specified sentence
     */
    private static String trimmer(String fullPart, String projectHeader, String separator) {
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
     * Getter to get an ArrayList of vulnerable dependencies
     * @param inputLine
     * @return an ArrayList of vulnerable dependencies
     */
    private static ArrayList<String> getVulString(String inputLine) {
        ArrayList<String> getStringText = new ArrayList<>();
        if (!inputLine.contains("No known security vulnerabilities found") && !inputLine.equals("")) {
            getStringText.add(trimmerMul(inputLine, "-", "---").get(0));
            getStringText.addAll(trimmerMul(inputLine, "-", "--"));
        }else if(inputLine.equals("")) {
            System.out.println("Empty String");
        }
        return getStringText;
    }

    /**
     * Getter to get an ArrayList of current Library Version of the dependencies in requirements.txt file
     * @param input
     * @return an ArrayList of Library Version
     */
    private static String getLibVersion(String input) {
        String s = null;

        Matcher m = VERSION_PATTERN.matcher(input);
        if (m.find()) {
            s = m.group(1);
            m = INSTALLED_VERSION.matcher(s);
            if (m.find()) {
                s = m.group(1);
            }
        } else {
            s = "";
        }
        return s;
    }

    /**
     * Getter to get an ArrayList of affected Library Version of the dependencies in requirements.txt file
     * @param input
     * @return 
     */
    private static String getAffectedLibVersion(String input) {
        String s = null;

        Matcher m = AFFECTED_LIBRARY.matcher(input);
        if (m.find()) {
            s = m.group(1);
        } else {
            s = "";
        }
        return s;
    }

    /**
     * Method to get the requirements information in a Model form
     * @param input
     * @return the requirements information in a Model form
     */
    private static RequirementsInfo[] generateRequirementsObject(String input) {
        ArrayList<String> getVul = new ArrayList<>();
        getVul = getVulString(input);
        RequirementsInfo[] reqI = new RequirementsInfo[getVul.size()];
        String version = null;
        String vulName = null;
        String affectedLib = null;
        String adversary = null;
        for (String s : getVul) {
            String forgeString = "Name: ";
            forgeString += s;
            vulName = trimmer(forgeString, "Name", ",");
            version = getLibVersion(s);
            affectedLib = getAffectedLibVersion(s);
            adversary = getAdversary(s);
            reqI[getVul.indexOf(s)] = new RequirementsInfo(vulName, version, affectedLib, adversary);
        }
        return reqI;
    }

    /**
     * Getter to get the adversary description of the affected dependencies
     * @param input
     * @return the adversary description
     */
    private static String getAdversary(String input) {
        String adversary = null;
        Matcher adversaryMatch = ADVERSARY_INFO_1.matcher(input);
        if (adversaryMatch.find()) {
            adversary = adversaryMatch.group(1);
            adversary = adversary.substring(8, adversary.length());
        } else {
            adversary = "";
        }
        return adversary;
    }

    /**
     * Getter to get the package scanned for each project/file
     * @param input
     * @return the package scanned
     */
    private static String generatedPackageScanned(String input) {
        String s = null;

        Matcher m = PACKAGE_SCANNED.matcher(input);
        if (m.find()) {
            s = m.group(1);
        } else {
            s = "";
        }
        return s;
    }
    
    /**
     * Getter to get the requirements information model 
     * @return requirements information model 
     */
    public static ArrayList<RequirementsInfo[]> getRequirementsObject() {
            return ri;
    }

    /**
     * Getter to get the total number of package in the requirements.txt file
     * @return 
     */
    public static ArrayList<String> getNumberOfPackage() {
        return packageScanned;
    }
    
    /**
     * Method to clear all the ArrayList 
     */
    public static void clearAll(){
        ri.clear();
        missingLibraries.clear();
        unaffectedLibraries.clear();
        packageScanned.clear();
    }
}

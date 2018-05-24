/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import ConstantsVariable.FinalConstants;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author Eugene Tan
 */
public class DScannerController {

    /**
     * A method to call dependency scanner
     */
    public static void invokeDScanner(String path, String fileName, String format) {
        try{
            String thePath = "\"" + path + "\"";
            Process process = Runtime.getRuntime().exec(FinalConstants.executeBatchPath + " " 
                    + fileName + " " + thePath  + " " + thePath + " " + format, null, new File(FinalConstants.BATCH_PATH));
            
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))){
                String line;
                while((line = reader.readLine()) != null){
//                    System.out.println(line);
                }
            }catch(IOException e){
                System.out.println("Error message from invokerDScanner Method: " + e);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
            e.printStackTrace();
        }
    }
//    public static boolean readyFlag()
}

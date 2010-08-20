/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.mobile.sms;


public class SMSUtils {
    
    public static String getSMSFileNameFromXMLFileName(String xmlFileName){
        int offset;
        int xmlFileLength = xmlFileName.length();
        //it means the file has been sent already;
        if(xmlFileName.startsWith("s_")){
            offset = 4;
        }
        //this is the original filename of Results in filesystem
        else if(xmlFileName.startsWith("r_")){
            offset = 2;
        }
        else{
            offset = 0;
        }
        return "sms_" + xmlFileName.substring(offset, xmlFileLength - 4) + ".txt";
    }

}

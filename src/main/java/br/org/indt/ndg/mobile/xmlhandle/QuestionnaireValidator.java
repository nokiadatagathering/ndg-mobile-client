package br.org.indt.ndg.mobile.xmlhandle;

import br.org.indt.ndg.mobile.logging.Logger;
import com.twmacinta.util.MD5;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

/**
 *
 * @author Alexandre Martini
 */
public class QuestionnaireValidator {
    private InputStreamReader is;
    private int character;
    private ByteArrayOutputStream outputArray;
    private OutputStreamWriter out;

    public QuestionnaireValidator(){
        outputArray = new ByteArrayOutputStream();
        try{
            out = new OutputStreamWriter(outputArray, "UTF-8");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public boolean isValid(String fullPath){
        try {
            FileConnection fc = (FileConnection) Connector.open(fullPath);
            InputStream inputStream = fc.openInputStream();
            is = new InputStreamReader(inputStream, "UTF-8");

            while( (character = is.read()) != -1){
                out.write(character);
            }
            inputStream.close();
            fc.close();
            return isValidSurvey();
            
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        
    }

    private boolean isValidSurvey() {
        String beforeChecksum = null, afterChecksum = null;
        String checksumContent = null;
        int surveyTagIndex = 0;
        try {
            String s = new String(outputArray.toByteArray(), "UTF-8");
            
            //Remove header if found
            if(s.startsWith("<?xml")){
                surveyTagIndex = s.indexOf("\n") + 1;
                s = s.substring(surveyTagIndex);
                surveyTagIndex = 0;
            }

            // Remove any other chars after </survey> tag
            String strLastTag = "</survey>";
            int surveyLastTagIndex = s.indexOf(strLastTag) + strLastTag.length();
            s = s.substring(surveyTagIndex, surveyLastTagIndex);

            int checkSumContentIndex = s.indexOf("checksum=\"") + 10;
            beforeChecksum = s.substring(surveyTagIndex, checkSumContentIndex);
            int endOfChecksumContentIndex = s.indexOf(">") - 1;
            checksumContent = s.substring(checkSumContentIndex, endOfChecksumContentIndex);
            afterChecksum = s.substring(endOfChecksumContentIndex);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }

        String surveyContent = beforeChecksum + afterChecksum;
        String expectedChecksum = checksumContent;//mountStringFromBuffer(checksumKey);
     //   try {
        byte[] surveyContentBuf = null;//outputArray.toByteArray();
        try {
            surveyContentBuf = surveyContent.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
            MD5 md5 = new MD5(surveyContentBuf);

            byte[] result = md5.doFinal();

            String hashResult = MD5.toHex(result);
            if (expectedChecksum.equals(hashResult)) {
                return true;
            }

            return false; 
    }

    //verify if, so far, buffer is equal to target word
    boolean checkBuffer(Vector buffer, String targetWord){
        int size = buffer.size();
        if(size == 0)
            return true;
        else{
            String bufferStr = mountStringFromBuffer(buffer);
            String subString = targetWord.substring(0, size);
            if(bufferStr.equals(subString))
                return true;
            else
                return false;
        }
    }

    String mountStringFromBuffer(Vector buffer){
        StringBuffer sb = new StringBuffer();
        int size = buffer.size();
        for(int i = 0; i < size; i++){
            Integer temp = (Integer) buffer.elementAt(i);
            sb.append((char)temp.intValue());
        }
        return sb.toString();

    }

    boolean isInTargetWord(int charac, String targetWord){
        int size = targetWord.length();
        for(int i = 0; i < size; i++){
            //Integer temp = (Integer) checksum.elementAt(i);
            int tempInt = targetWord.charAt(i);
            if (tempInt == charac){
                return true;
            }
        }
        return false;
    }
}

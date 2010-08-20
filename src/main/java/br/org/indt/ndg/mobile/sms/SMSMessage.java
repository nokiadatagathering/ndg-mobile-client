

package br.org.indt.ndg.mobile.sms;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.logging.Logger;
import br.org.indt.ndg.mobile.sms.SMSSender;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.Connector;
import com.jcraft.jzlib.*;


public class SMSMessage {
    public static final int HEADER_LENGTH = 36;
    private static int sms_length;// = 100;//characters
    private static int msg_body_length;// = SMS_LENGTH - HEADER_LENGTH;
    public static final int MSG_SURVEYID_START_INDEX = 1;
    public static final int MSG_SURVEYID_LENGTH = 10;
    public static final int MSG_NUMBER_OFFSET = MSG_SURVEYID_START_INDEX + MSG_SURVEYID_LENGTH;//11;
    public static final int MSG_RESULTID_START_INDEX = 13;
    public static final int MSG_RESULTID_LENGTH = 8;
    
    public static final byte MSG_TYPE_RESULT = 1;
    
    private StringBuffer content = new StringBuffer();
    private String path;
    
    private Vector messagesToSend = new Vector();
    
    public SMSMessage(){
       sms_length = AppMIDlet.getInstance().getSettings().getStructure().getSMSLength();
       msg_body_length = sms_length - HEADER_LENGTH;
    }
    
    public void append(String text){
        content.append(text);
    }
    
    public void append(char character){
        content.append(character);
    }
    
    public void append(double number){
        content.append(number);
    }
    
    public void append(byte number){
        content.append(number);
    }
    
    public void save(String path){
         try {
            //String surveyDir = AppMIDlet.getInstance().getFileSystem().getSurveyDirName();
            FileConnection smsConnection = (FileConnection) Connector.open(path);
            
            if(smsConnection.exists()){                
                smsConnection.delete();                
            }
            smsConnection.create();
            
            DataOutputStream output = (DataOutputStream) smsConnection.openDataOutputStream();
            output.write(content.toString().getBytes("UTF-8"));
            
            output.close();
            smsConnection.close();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public StringBuffer getContent(){
        return content;
    }
    
    private void setPath(String path){
        this.path = path;
    }
    
    public static SMSMessage loadSMSMessage(String path){
        SMSMessage msg = new SMSMessage();
        msg.setPath(path);
        return msg;
    }    
    
    public Vector getMessages(){
        if(path == null){
            throw new NullPointerException("Unable to get Messages. Null path");
        }        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayOutputStream headerOS = new ByteArrayOutputStream();
        try {            
           FileConnection smsConnection = (FileConnection) Connector.open(path);

           DataInputStream input = (DataInputStream) smsConnection.openDataInputStream();           

           for(int headerCount = 0, data = 0; (data = input.read()) != -1; headerCount++){               
               if( headerCount< HEADER_LENGTH)
                   headerOS.write(data);
               else
                   baos.write(data);
           }

           ByteArrayOutputStream out=new ByteArrayOutputStream();
           out.reset();

           ZOutputStream zOut=new ZOutputStream(out, JZlib.Z_BEST_COMPRESSION);
           DataOutputStream objOut=new DataOutputStream(zOut);

           byte [] bytes = baos.toByteArray();//buffer.getBytes("UTF-8");
           objOut.write(bytes);
           zOut.close();        
      
           byte[] zippedBytes = out.toByteArray();          
           
           byte[]header = headerOS.toByteArray();//new byte[HEADER_LENGTH];
           byte[] smsBuf = new byte[sms_length];

           //saves header data into a buffer
           int zippedBufferLength = zippedBytes.length;
           for(int dataRead = 0; dataRead < zippedBufferLength;){
               int remaining = zippedBufferLength - dataRead;
               int bytesToBeReadCount;
               if(remaining <= msg_body_length) // last part
               {
                   bytesToBeReadCount = remaining;
                   smsBuf = new byte[HEADER_LENGTH + remaining + 1];
                   smsBuf[HEADER_LENGTH + remaining] = (byte) '#'; // 3 is ETX in ascii table
               }
               else
               {
                   bytesToBeReadCount = msg_body_length;
                   smsBuf = new byte[sms_length];
               }

               for(int i = 0; i < HEADER_LENGTH; i++){
                   smsBuf[i] = header[i];
               }          
               for(int j = 0, index = HEADER_LENGTH; j < bytesToBeReadCount; j++, dataRead++, index++){
                   smsBuf[index] = zippedBytes[dataRead];
               }
               messagesToSend.addElement(smsBuf);
           }
           
           //puts info about msg cardinality (1,2,3,...)
           int offset = MSG_NUMBER_OFFSET;
           int numberOfMsgs = (char) messagesToSend.size();
           for(int i = 1; i <= numberOfMsgs; i++)
           {
               byte[] temp = (byte[]) messagesToSend.elementAt(i - 1);
               if(i < 10){
                   temp[offset] = (byte) '0';
                   temp[offset + 1] = (byte) Integer.toString(i).charAt(0);
               }
               else{
                   temp[offset] = (byte) Integer.toString(i).charAt(0);
                   temp[offset + 1] = (byte) Integer.toString(i).charAt(1);
               }
           }           
           
           input.close();           
           smsConnection.close();
           baos.close();
           out.close();
           
           return messagesToSend;

        } catch (IOException ex) {            
            Logger.getInstance().log(ex.getMessage());
            return null;
        }
    }
}

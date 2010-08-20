
package br.org.indt.ndg.mobile.sms;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.logging.Logger;
import br.org.indt.ndg.mobile.submit.TestConnection;
import br.org.indt.ndg.mobile.xmlhandle.kParserSMS;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.TimerTask;
import java.util.Vector;
import javax.microedition.io.*;
import javax.microedition.io.file.FileConnection;

import javax.microedition.lcdui.Display;
import javax.wireless.messaging.*;

public class SMSReceiver implements MessageListener {
    //public static String RECEIVING_PORT;
    private static SMSReceiver instance = new SMSReceiver();
    
    
    /** Connections detected at start up. */
    String[] connections;
    
    /** SMS message connection for inbound text messages. */
    MessageConnection smsconn;   

    StringBuffer buf = new StringBuffer();
    
    private Receiver receiver; 
    
    public static SMSReceiver getInstance(){
        return instance;
    }
    
    private SMSReceiver() {         
    }

    public void end(){
        try {
            if(smsconn != null)
                smsconn.close();
        } catch (IOException ex) {
            Logger.getInstance().log(ex.getMessage());
        }
    }
    public void init(){
        if (smsconn == null) {
            try {
                smsconn = (MessageConnection)Connector.open("sms://:" + AppMIDlet.getInstance().getSettings().getStructure().getReceivingPort());
                smsconn.setMessageListener(this);      
                
                receiver = new Receiver();

                new Thread(receiver).start();

                //    initiated = true;
            } catch (IOException ioe) {
                Logger.getInstance().log("SMSReceiver IOException: " + ioe.getMessage());
            }
        }
        
    }
    
    /**
     * Notification that a message arrived.
     * @param conn the connection with messages available
     */
    public void notifyIncomingMessage(MessageConnection conn) {
        try {
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        receiver.handleMessage(conn);
    }
    
    private TextMessage textMessage;

    public void test(String string) {
        receiver.handleMessage(string);
    }
 
    MessageConnection getConnection(){
        return smsconn;
    }    
    TextMessage getTextMessage(){
        return textMessage;
    }
    
    private String surveyTitle = "";
    
    public void setSurveyTitle(String _title) {
        surveyTitle = _title;
    }
    
    public String getSurveyTitle() {
        return surveyTitle;
    }
    
}
class Receiver implements Runnable{
    /*the position of the digit that indicates the number of the directory of the sms just received */
    private static final int BEGIN_OF_CONTENT_FOR_SURVEY_PROTOCOL = 13;
    private static final String ACK_FROM_SERVER_MESSAGE_TYPE = "2";
    private static final String SURVEY_MESSAGE_TYPE = "3";
    private static final String SERVER_GENERIC_MESSAGE_TYPE = "4";
    private static final String ACK_FROM_SERVER_CONN_TEST = "6";
    
    Message msg;
    SMSReceiver receiver;
    
    private static Hashtable messages;
    
    private String surveyDir;
    
    private String surveyId;
    
    private StringBuffer messageParts;
    
    private MessageConnection msgConn;
    
    private boolean newMessage;
    
    private String testMessage;
    
    private Vector messagesVector;
    
//    public Receiver(SMSReceiver receiver){
//        this.receiver = receiver;
//        messages = new Hashtable();
//        messageParts = new StringBuffer();       
//    }
    public Receiver(){
        messages = new Hashtable();
        messageParts = new StringBuffer();
        messagesVector = new Vector();
    }
    public synchronized void handleMessage(MessageConnection conn){                       
        msgConn = conn;            
        newMessage = true;
        notifyAll();      
    }
    public synchronized void handleMessage(String str){
        testMessage = str;
        newMessage = true;
        notifyAll();
    }
    public void run() {
        while(true){ 
            synchronized(this){
                while (!newMessage) {
                    try {
                        wait();                                
                    }
                    catch(InterruptedException ie){
                        Logger.getInstance().logException("Ex on wait: " + ie.getMessage());
                    }
                    catch(Throwable t) {
                        Logger.getInstance().logException("Ex on Throwable: " + t.getMessage());
                    }
                }                
            }

            newMessage = false;
            try {                    
                msg = msgConn.receive();                      
                handleMessage(msg);                
            } 
            catch (Exception e) {
                Logger.getInstance().log("Exception on receive: " + e.toString());
            }
        }
    }
    private void handleMessage(Message msg){
        //Logger.getInstance().log("msg: " + msg);
        if (msg != null) {                
            if (msg instanceof TextMessage) {
                //Logger.getInstance().log("Msg is TextMessage");                        
                String text = ((TextMessage)msg).getPayloadText();
                //Logger.getInstance().log("Content: " + text);
                String type = text.substring(0,1);
                //Logger.getInstance().log("Type: " + type);
                if(type.equals(SURVEY_MESSAGE_TYPE)){
                    handleIncomingSurvey(text);
                }
                else if(type.equals(ACK_FROM_SERVER_MESSAGE_TYPE)){                    
                    handleIncomingACK(text);
                }                        
                else if(type.equals(SERVER_GENERIC_MESSAGE_TYPE)) {
                    handleIncomingGenericServerNotification(text);
                }
                else if (type.equals(ACK_FROM_SERVER_CONN_TEST)) {
                    handleIncomingACKConnectionTest(text);
                }
            }
            else if(msg instanceof BinaryMessage){
                try {
                    //Logger.getInstance().log("Msg is BinMessage");
                    byte[] content = ((BinaryMessage) msg).getPayloadData();
                    int length = content.length;
                    
//                    for (int i = 0; i < length; i++) {
//                        Logger.getInstance().log("Byte: " + (byte) content[i]);
//                    }
                    String text = new String(content, "UTF-8");
                    String text2 = new String(content, "ISO-8859-1");

                    String text4 = new String(content);
                    Logger.getInstance().log("Content utf: " + text);
                    //Logger.getInstance().log("Content iso: " + text2);
                    //Logger.getInstance().log("Content default: " + text4);
                    String type = text.substring(0, 1);
                    
                    if (type.equals(SURVEY_MESSAGE_TYPE)) {
                        handleIncomingSurvey(text);
                    } else if (type.equals(ACK_FROM_SERVER_MESSAGE_TYPE)) {
                        handleIncomingACK(text);
                    }
                    else if(type.equals(SERVER_GENERIC_MESSAGE_TYPE)) {
                        handleIncomingGenericServerNotification(text);
                    }
                    else if (type.equals(ACK_FROM_SERVER_CONN_TEST)) {
                        handleIncomingACKConnectionTest(text);
                    }
                } catch (UnsupportedEncodingException ex) {
                    Logger.getInstance().log(ex.getMessage());
                }
            }
            else{                        
            }
        }
    }
    
    private void handleIncomingGenericServerNotification(String text) {
        // If initialized via PushRegistry, we must wait until splash screen is gone and 
        // "no survey" notification is gone.
        if (!AppMIDlet.getInstance().isInitializedManually()) {
            try {
                AppMIDlet.getInstance().setInitializedManually(true);
                Thread.sleep(6000);
            } catch (InterruptedException ex) {
                Logger.getInstance().logException("Exception on sleeping: " + ex.getMessage());                
            }
        }
        String strMessage = text.substring(1, text.length());
        AppMIDlet.getInstance().getGeneralAlert().showAlert(Resources.WARNING, strMessage);        
    }
    
    private void handleIncomingACK(String text){
        Logger.getInstance().log("Handling ack: " + text);
        surveyId = text.substring(SMSMessage.MSG_SURVEYID_START_INDEX, SMSMessage.MSG_SURVEYID_START_INDEX + SMSMessage.MSG_SURVEYID_LENGTH);
        surveyDir = "SURVEY" + surveyId + "/";

        StringBuffer bufFileName = new StringBuffer();
        bufFileName.append(surveyId).append("_").append(AppMIDlet.getInstance().getIMEI()).append("_")
                .append(text.substring(SMSMessage.MSG_RESULTID_START_INDEX));                            
        String tempFileName = bufFileName.toString();
        String fileName = "p_r_" + tempFileName + ".xml";
        //String smsFileName = "sms_" + tempFileName + ".txt";
        
        AppMIDlet.getInstance().getFileSystem().movePendingACKToSentResult(surveyDir, fileName);
        //AppMIDlet.getInstance().getFileSystem().moveSMSSentResult(surveyDir, smsFileName); 
    }

    private void handleIncomingACKConnectionTest(String text) {
        Logger.getInstance().log("Handling ack connection test: " + text);
        int iImei = text.indexOf("i");
        int iTimeStamp = text.indexOf("t");
        String strImei = text.substring(iImei + 1, iTimeStamp);
        String strTimeStamp = text.substring(iTimeStamp + 1, text.length());
        TestConnection.getInstance().handleIncomingACKConnectionTest(strImei, strTimeStamp);
    }
    
    private void handleIncomingSurvey(String sms){        
        Logger.getInstance().log("HANDLE Incoming Survey");
        try {            
            surveyId = sms.substring(SMSMessage.MSG_SURVEYID_START_INDEX, SMSMessage.MSG_SURVEYID_START_INDEX + SMSMessage.MSG_SURVEYID_LENGTH);
            surveyDir = "survey" + surveyId + "/";
            FileConnection conn = (FileConnection) Connector.open(Resources.ROOT_DIR + surveyDir);
            if(!conn.exists()){ 
                conn.mkdir();
            }
            conn.close();
            
            String smsNumberStr = sms.substring(SMSMessage.MSG_NUMBER_OFFSET, SMSMessage.MSG_NUMBER_OFFSET + 2);//this will give us the part of this SMS, eg, 01 (first part), 02 (second), 03 and so on
            int smsNumberInt = Integer.parseInt(smsNumberStr);
            //check if it is the last one
            int length = sms.length();
            if(sms.charAt(length - 1) == '#'){//last message
                Logger.getInstance().log("LAST MSG");
                String smsContent = sms.substring(0, length - 1);
                //checar se msg esta completa
                //load and assemble AllMsgs (from HashTable and, if necessary, from FS)   
                int lastSmsNumber = smsNumberInt;
                if(getNumberOfParts() == lastSmsNumber - 1){//all parts are already in FS
                    messageParts.insert(0, smsContent.substring(BEGIN_OF_CONTENT_FOR_SURVEY_PROTOCOL));
                    String surveyComplete = assembleSurveyParts(lastSmsNumber);//messageParts.toString();
                    Logger.getInstance().log("Survey complete 1: ", surveyComplete);
                    //int surveyLength = surveyComplete.length();
                    messageParts.delete(0, messageParts.length());
                    parseSurvey(surveyDir, surveyComplete);//.substring(0, surveyLength - 1));
                    deletePartFilesFromFS(surveyDir);
                    ShowNewSurveyReceivedAlert();
                }
                else{
                    saveLastSMSNumberInFile(lastSmsNumber);
                    saveMessage(smsContent);
                }
            }
            else{  //nao eh ultima msg
                Logger.getInstance().log("NOT LAST MSG");
                int lastSMSNumber = getLastSMSNumberFromFile();
                Logger.getInstance().log("lastSMSNumber: " + lastSMSNumber);
                if(getNumberOfParts() == lastSMSNumber - 1){//all parts are already in FS       
                    String surveyComplete = assembleSurveyParts(sms, lastSMSNumber, smsNumberInt);
                    Logger.getInstance().log("Survey complete 2: ", surveyComplete);
                    //int surveyLength = surveyComplete.length();
                    messageParts.delete(0, messageParts.length());
                    parseSurvey(surveyDir, surveyComplete);//.substring(0, surveyLength - 1));   
                    deletePartFilesFromFS(surveyDir);
                    ShowNewSurveyReceivedAlert();
                }
                else{
                    saveMessage(sms);
                }                
            }            
        } 
        catch (IOException ex) {
            Logger.getInstance().log("Exception: " + ex.getMessage());
        }        
    }
    private void parseSurvey(String surveyDir, String survey){
        //int length = survey.length();
        //for (int i = 0; i < length; i++) {
        //     Logger.getInstance().log("Byte: " + (byte) survey.charAt(i));
        //}
        //SMSProtocolSurveyHandler handler = new SMSProtocolSurveyHandler(surveyDir);
        //Parser parser = new Parser(handler);
        //parser.parseSMSProtocolSurvey(survey);
        kParserSMS kparserSMS = new kParserSMS();
        kparserSMS.parserSurveyFile(surveyDir, survey);
    }
    
    private void deletePartFilesFromFS(String surveyDir)
    {
        Enumeration enumeration;      
        try {
            FileConnection conn = (FileConnection) Connector.open(Resources.ROOT_DIR + surveyDir);
            enumeration = conn.list("*.part", true);
        
            while(enumeration.hasMoreElements()) {
                String filename = (String) enumeration.nextElement();
                if (!filename.equals(Resources.SURVEY_NAME)) {
                    try {
                        FileConnection fileConn = (FileConnection) Connector.open(Resources.ROOT_DIR + surveyDir + filename);
                        if (fileConn.exists())
                        {
                            fileConn.delete();
                        }
                        fileConn.close();
                    }
                    catch (IOException ex) {
                        Logger.getInstance().logException("Exception: " + ex.getMessage());
                    }
                }
            }
            conn.close();
        }
        catch (IOException ex) {
            Logger.getInstance().logException("Exception: " + ex.getMessage());
        }
        
//        // Delete last file if exists
//        try {
//            FileConnection fileConn = (FileConnection) Connector.open(Resources.ROOT_DIR + surveyDir + "last");
//            if (fileConn.exists())
//            {
//                fileConn.delete();
//            }
//            fileConn.close();
//        }
//        catch (IOException ex) {
//            Logger.getInstance().logException("Exception: " + ex.getMessage());
//        }
        
//        int numberOfParts = getNumberOfParts(_conn);
//        for (int nIndex = 1; nIndex <= numberOfParts; nIndex++)
//        {
//            try {
//                FileConnection fileConn = (FileConnection) Connector.open(Resources.ROOT_DIR + surveyDir + nIndex + ".part");
//                if (fileConn.exists())
//                {
//                    fileConn.delete();
//                }
//                fileConn.close();
//            }
//            catch (IOException ex) {
//                Logger.getInstance().logException("Exception: " + ex.getMessage());
//            }
//        }
    }
    
    private String assembleSurveyParts(int lastSmsNumber){        
        Logger.getInstance().log("assemble1");
        //int partsCount;                               
        for(int partsCount = lastSmsNumber - 1; partsCount > 0; partsCount--){
            Logger.getInstance().log("Parts Count: " + partsCount);
            String msgPart = (String) messages.get(surveyId + partsCount);
            if (msgPart == null){ // message not in memory. try fs
                msgPart = getMsgPartFromFS(partsCount);            
            }
            messageParts.insert(0, msgPart);
        }
        return messageParts.toString();
    }
    
    private String assembleSurveyParts(String sms, int lastSmsNumber, int currentSMSNumber){        
        Logger.getInstance().log("assemble2");
        String msgPart;
        for(int partsCount = lastSmsNumber; partsCount > 0; partsCount--){
            Logger.getInstance().log("Parts Count: " + partsCount);
            if(currentSMSNumber == partsCount){
                msgPart = sms.substring(BEGIN_OF_CONTENT_FOR_SURVEY_PROTOCOL);
            }
            else{
                msgPart = (String) messages.get(surveyId + partsCount);
                if (msgPart == null){ // message not in memory. try fs
                    msgPart = getMsgPartFromFS(partsCount);                    
                }
            }
            messageParts.insert(0, msgPart);
        }
        return messageParts.toString();
    }
    
    private String getMsgPartFromFS(int partsCount){
        Logger.getInstance().log("getMSgPartFromFS : " + partsCount);
        try {
            FileConnection fileConn = (FileConnection) Connector.open(Resources.ROOT_DIR + surveyDir + partsCount + ".part");
            if (fileConn.exists()) {
                InputStream is = fileConn.openInputStream();
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int data;
                while ((data = isr.read()) != -1) {
                    baos.write(data);
                }
                String msgPart = baos.toString();
                baos.close();
                isr.close();
                is.close();
                fileConn.close();
                return msgPart;
            } else {
                Logger.getInstance().log("Returning null");
                return null;
            }
        } catch (IOException ex) {
            Logger.getInstance().log("Exception: " + ex.getMessage());            
           return null;
        }
    }
    
    private synchronized int getLastSMSNumberFromFile(){
        Logger.getInstance().log("getLastSMSNumber");
        try {
            FileConnection fileConn = (FileConnection) Connector.open(Resources.ROOT_DIR + surveyDir + "last",  Connector.READ);
            if (fileConn.exists()) {
                DataInputStream in = fileConn.openDataInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int data;
                while ((data = in.read()) != -1) {
                    baos.write(data);
                }
                baos.close();
                in.close();
                fileConn.close();
                return (int) Integer.parseInt(baos.toString());
            } else {
                return -99;
            }
        } catch (IOException ex) {
            Logger.getInstance().log("Exc: " + ex);
            return -99;
        }
    }
    
    private synchronized void saveLastSMSNumberInFile(int lastNumber){
        Logger.getInstance().log("saveLastSMSNumberInFile: " + lastNumber);
        try {
            FileConnection fileConn = (FileConnection) Connector.open(Resources.ROOT_DIR + surveyDir + "last");
            if (fileConn.exists()) {
                fileConn.delete();
            }
            fileConn.create();
            DataOutputStream out = fileConn.openDataOutputStream();
            out.write(String.valueOf(lastNumber).getBytes("UTF-8"));
            out.flush();
            out.close();
            fileConn.close();
        } catch (IOException ex) {
            Logger.getInstance().logException("Exception: " + ex.getMessage());
        }
    }
    private synchronized void saveMessage(String sms){        
        Logger.getInstance().log("Saving msg: " + sms);
        try {
            //String surveyId = sms.substring(SMSMessage.MSG_SURVEYID_START_INDEX, SMSMessage.MSG_SURVEYID_START_INDEX + SMSMessage.MSG_SURVEYID_LENGTH);
            //String surveyDir = "SURVEY" + surveyId + "/";
            String smsNumberStr = sms.substring(SMSMessage.MSG_NUMBER_OFFSET, SMSMessage.MSG_NUMBER_OFFSET + 2); //this will give us the part of this SMS, eg, 01 (first part), 02 (second), 03 and so on
            int smsNumberInt = Integer.parseInt(smsNumberStr);
            //save sms to file            
            FileConnection fileConn = (FileConnection) Connector.open(Resources.ROOT_DIR + surveyDir + smsNumberInt + ".part");
            fileConn.create();
            DataOutputStream out = fileConn.openDataOutputStream();
            String content = sms.substring(BEGIN_OF_CONTENT_FOR_SURVEY_PROTOCOL);
            out.write(content.getBytes("UTF-8"));
            out.flush();
            out.close();
            fileConn.close();
            messages.put(surveyId + smsNumberInt, content);            
        } catch (IOException ex) {
            Logger.getInstance().log("Exception: " + ex.getMessage());
        }
    }
    
    private synchronized int getNumberOfParts(){        
        Logger.getInstance().log("getNumberOfParts");
        byte numberOfParts = 0;
        try {
            FileConnection conn = (FileConnection) Connector.open(Resources.ROOT_DIR + surveyDir);
            Enumeration enumeration = conn.list("*.part", false);      
            for(;enumeration.hasMoreElements();enumeration.nextElement()){
                numberOfParts++;
                Logger.getInstance().log("numberOfParts : " + numberOfParts);
            }     
            conn.close();
        } catch (IOException ex) {
            Logger.getInstance().log("Exc:" + ex.getMessage());
        }
        return numberOfParts;
    }
    
    private void ShowNewSurveyReceivedAlert() {
        boolean bIsShown = false; 
        bIsShown = (Display.getDisplay(AppMIDlet.getInstance()).getCurrent() == AppMIDlet.getInstance().getSurveyList());
        if (bIsShown == false) {
            bIsShown = (Display.getDisplay(AppMIDlet.getInstance()).getCurrent() == AppMIDlet.getInstance().getSplashScreen());
        }
        
        if (bIsShown == false) {
            bIsShown = (Display.getDisplay(AppMIDlet.getInstance()).getCurrent() == AppMIDlet.getInstance().getSplashScreen().getNoSurveyScreen());
        }
       
        if (bIsShown == true) {
            AppMIDlet.getInstance().getGeneralAlert().setSurveyListVisible(bIsShown);
        }

        AppMIDlet.getInstance().getGeneralAlert().showAlert(Resources.WARNING, Resources.NEW_SURVEY_RECEIVED + SMSReceiver.getInstance().getSurveyTitle(), "SMSReceiver");
    }
    
    private class MessageHandler extends TimerTask {
        public void run() {
            int size = messagesVector.size();
            for(int i = 0; i < size; i++){
                handleMessage((Message)messagesVector.elementAt(i));                
            }
        }
    
    }
}

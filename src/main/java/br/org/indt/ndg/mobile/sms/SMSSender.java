package br.org.indt.ndg.mobile.sms;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.ResultList;
import br.org.indt.ndg.mobile.logging.Logger;

import javax.microedition.io.*;

import javax.wireless.messaging.*;

import java.io.*;
import java.util.Vector;


/**
 * Prompts for text and sends it via an SMS MessageConnection
 */
public class SMSSender implements Runnable {
    /*
     * These values come from Settings.xml
     */
    //public static String SEND_TO_PHONE;
    //public static String SENDING_PORT;    
    
    private Vector messagesToSend;
    private Vector resultsToSend;
    private byte[] message;
    private boolean cancel = false; 
    private int phoneFormat = 0;
    private int lastIndex = 0;
    
//    public SMSSender(String smsPort, Vector messagesToSend){
//        this.smsPort = smsPort;
//        this.messagesToSend = messagesToSend;
//    }
    
    public SMSSender(){       
        resultsToSend = new Vector();
    } 
    
//    public void enqueue(SMSMessage msg){
//        resultsToSend.addElement(msg);
//    }
    
    public void enqueue(UnsentResult sms){
        resultsToSend.addElement(sms);
    }
    
    public void send(){
        cancel = false;
        phoneFormat = 1;
        lastIndex = 0;
        new Thread(this).start();
    }
    
    public void cancel(){
        cancel = true;
    }
    
    public void run() {
        try {
            phoneFormat = 1;
            sendSMS();
        }
        catch (IOException t) {
            Logger.getInstance().logException("IOException (Level 1)");
            try {
                phoneFormat = 2;
                sendSMS();
            }
            catch (IOException t2) {
               Logger.getInstance().logException("IOException (Level 2)");
                try {
                   phoneFormat = 3;
                   sendSMS();
                }
                catch (IOException t3) {
                    Logger.getInstance().logException("IOException (Level 3)");
                    AppMIDlet.getInstance().getGeneralAlert().showAlert(Resources.NETWORK_FAILURE, Resources.TRY_AGAIN_LATER);               
                    Logger.getInstance().logException("Network Failure: " + t3.getMessage());
                } 
            }
        }
        finally {
            AppMIDlet.getInstance().setResultList(new ResultList());  //updated result list
            AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getResultList());
        }
    }
    
    private void sendSMS() throws IOException {        
        String phoneNumber = "";
        if (phoneFormat == 1) {
            phoneNumber = AppMIDlet.getInstance().getSettings().getStructure().getCountryCode() + 
                             AppMIDlet.getInstance().getSettings().getStructure().getAreaCode() + 
                             AppMIDlet.getInstance().getSettings().getStructure().getPhoneNumber();
        }
        else if (phoneFormat == 2) {
            phoneNumber =  AppMIDlet.getInstance().getSettings().getStructure().getAreaCode() + 
                           AppMIDlet.getInstance().getSettings().getStructure().getPhoneNumber();
        }
        else {
            phoneNumber =  AppMIDlet.getInstance().getSettings().getStructure().getPhoneNumber();
        }
        
        String address = "sms://" + phoneNumber + ":" + AppMIDlet.getInstance().getSettings().getStructure().getSendingPort();

        MessageConnection smsconn = null;
        BinaryMessage binMessage = null;
        String xmlFileName;
        String strDelay = AppMIDlet.getInstance().getAppProperty("sms-delay");
        try {            
            /** Open the message connection. */
            smsconn = (MessageConnection)Connector.open(address);
            int numberOfResults = resultsToSend.size();               
                
            //Binary Message
            int i = lastIndex;
            for(; i < numberOfResults; i++){
                lastIndex = i;
                if(!cancel){
                    UnsentResult unsent = (UnsentResult) resultsToSend.elementAt(i);
                    Logger.getInstance().log("Result to be sent: " + unsent.getXMLFileName());
                    //SMSMessage smsResult = (SMSMessage) resultsToSend.elementAt(i);
                    SMSMessage smsResult = unsent.getMessage();
                    Vector messages = smsResult.getMessages();                    
                    int numberOfMessagesPerResult = messages.size();                         
                    
                    for(int j = 0; j < numberOfMessagesPerResult; j++){
                        binMessage = (BinaryMessage)smsconn.newMessage(MessageConnection.BINARY_MESSAGE);
                        byte[] fragment = (byte[]) messages.elementAt(j);
                        binMessage.setPayloadData(fragment); 
                        Thread.sleep(Integer.parseInt(strDelay));
                        
                        smsconn.send(binMessage);
                        //throw new IOException();
                    }
                    Logger.getInstance().log("Result sent: " + unsent.getXMLFileName());
                    AppMIDlet.getInstance().getFileSystem().moveToPendingACKResult(unsent.getXMLFileName());
                }
            }            
        } catch (InterruptedException ie){
            Logger.getInstance().logException("Exception on sending: " + ie.toString());
        }
        finally{
            Logger.getInstance().log("finally on sendSMS()");
            try {
                binMessage = null;
                if (smsconn != null) {
                    smsconn.close();
                }                
            } catch (IOException ex) {
                Logger.getInstance().logException("Impossible to close connection: " + ex.getMessage());
            }
        }
    }       
}
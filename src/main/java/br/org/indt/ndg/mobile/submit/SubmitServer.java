package br.org.indt.ndg.mobile.submit;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.file.FileConnection;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.ResultList;
import br.org.indt.ndg.mobile.sms.SMSMessage;
import br.org.indt.ndg.mobile.error.ServerCantWriteResultsException;

import br.org.indt.ndg.mobile.logging.Logger;
import br.org.indt.ndg.mobile.sms.SMSSender;
import br.org.indt.ndg.mobile.sms.SMSUtils;
import br.org.indt.ndg.mobile.sms.UnsentResult;
import com.jcraft.jzlib.JZlib;
import com.jcraft.jzlib.ZOutputStream;
import javax.microedition.lcdui.Alert;

public class SubmitServer {
    //These values are set in Settings.xml
    public static boolean gprs_support;
    public static boolean sms_support;
    private final int SERVER_CANNOT_WRITE_RESULT = -1;
    private static final int NO_SURVEY_IN_SERVER = 2;
    private final int SUCCESS = 1;
    
    private static final String ENCODING = "UTF-8";
    
    private HttpConnection httpConn = null;
    //private FileConnection fileConn = null;
    private InputStreamReader fileInput = null;
    private DataOutputStream httpOutput = null;
    private DataInputStream httpInput = null;
    private boolean stop = false;
    private String urlServlet = "";
    
    private StatusScreen statusScreen = null;
    
    private SMSSender sender;
    
    public SubmitServer() {
        gprs_support = AppMIDlet.getInstance().getSettings().getStructure().getGPRSSupport();
        sms_support = AppMIDlet.getInstance().getSettings().getStructure().getSMSSupport();
        if(!gprs_support && !sms_support){
            AppMIDlet.getInstance().getGeneralAlert().showAlert(Resources.NO_TRANSPORT, Resources.NO_TRANSPORT_SELECTED);
        }
        else{
            statusScreen = new StatusScreen(this);
        }                     
    }
    
    public void cancel() {
        stop = true;
        if(sender != null){
            sender.cancel();
        }
    }
    
    public void submitResult(String resultFilename){
        Vector resultsToSend = new Vector();
        resultsToSend.addElement(resultFilename);
        send(resultsToSend);
    }
    
    public void submit(StatusScreen ss) {
        statusScreen = ss;
        Vector resultFilenames = AppMIDlet.getInstance().getSubmitList().getSelectedResultNames();
        send(resultFilenames);
    }
    
    private void send(Vector resultFilenames){
        if(gprs_support){            
            boolean compression_on = AppMIDlet.getInstance().getSettings().getStructure().getServerCompression();
            urlServlet = AppMIDlet.getInstance().getSettings().getStructure().getServerUrl();
            statusScreen.reset(resultFilenames.size());
            statusScreen.setHttpLabel(urlServlet);

            Enumeration e = resultFilenames.elements();
            int currentFileIndex = 0;                     

            while (e.hasMoreElements() && !stop) {
                try {
                    httpConn = (HttpConnection) Connector.open(urlServlet);
                    httpConn.setRequestMethod(HttpConnection.POST);
                    httpOutput = httpConn.openDataOutputStream();                    
                }
                catch(Exception ex){
                    Logger.getInstance().log("Exception sending to Server: " + ex.getMessage());
                    finalizeGPRSTransmission();
                    break;
                }
                
                String file = null;   
                String buffer = "";
                file = (String) e.nextElement();
                statusScreen.setFileLabel(file);
                statusScreen.setCurrentFileIndex(++currentFileIndex);
                
                buffer = loadFile(file);
                try{
                    if (!stop){
                        if (compression_on) submitCompressFile(buffer);
                        else submitFile(buffer);
                    }
                    if (!stop){
                        httpInput = new DataInputStream(httpConn.openDataInputStream());
                        int in = httpInput.readInt();
                        if(in == SERVER_CANNOT_WRITE_RESULT){
                            throw new ServerCantWriteResultsException();
                        }
                        else if (in == NO_SURVEY_IN_SERVER) {
                            AppMIDlet.getInstance().getGeneralAlert().showErrorOk(Resources.SURVEY_NOT_IN_SERVER);
                        } else {
                            //System.out.println("Server received data");
                                        /*
                                         * Just move as sent if user do not previously canceled the process.
                                         * In some cases, some files will reach the server, but for reliability they will be
                                         * kept in the mobile as not sent.
                                         */
                            AppMIDlet.getInstance().getFileSystem().moveSentResult(file);
                        }
                    }
                }
                catch (IOException ioe) {
                    if (!stop){
                        Logger.getInstance().log(Resources.ESEND_RESULT1 + ioe.getMessage());
                        AppMIDlet.getInstance().getGeneralAlert().showAlert(Resources.NETWORK_FAILURE, Resources.TRY_AGAIN_LATER);
                    }
                } catch (ServerCantWriteResultsException snwre) {
                    if (!stop) {
                        Logger.getInstance().log(Resources.ESEND_RESULT2 + snwre.getMessage());
                        AppMIDlet.getInstance().getGeneralAlert().showAlert(Resources.NETWORK_FAILURE, Resources.TRY_AGAIN_LATER);
                    }
                }
                finalizeGPRSTransmission();
            }//while
            //finalizeGPRSTransmission();
            AppMIDlet.getInstance().setResultList(new ResultList());
            AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getResultList());
        }
        else
            throw new IllegalStateException("Transport not set in Settings.xml");
    }
    
    
    
    private void finalizeGPRSTransmission(){
        try {
            if (httpOutput != null) httpOutput.close();
            if (httpInput != null) httpInput.close();
            //if (fileConn != null) fileConn.close();
            if (fileInput != null) fileInput.close();
            if (httpConn != null) httpConn.close();
        } catch (Exception exc) {
            if (!stop) {
                AppMIDlet.getInstance().getGeneralAlert().showErrorExit(Resources.ESEND_RESULT3);
                Logger.getInstance().log(exc.getMessage());
            }
        }
    }
         
    private void submitFile(String buffer) {
        try {
            httpOutput.writeInt(buffer.length());
            
            httpOutput.write(buffer.getBytes(), 0, buffer.length());
            httpOutput.flush();
        } catch (IOException ioe) {
            Logger.getInstance().log(ioe.getMessage());
        }
    }

    private void submitCompressFile(String buffer) {
        try {
            ByteArrayOutputStream out=new ByteArrayOutputStream();
            out.reset();
            
            ZOutputStream zOut=new ZOutputStream(out, JZlib.Z_BEST_COMPRESSION);
            DataOutputStream objOut=new DataOutputStream(zOut);
            
            byte [] bytes = buffer.getBytes(ENCODING);
            objOut.write(bytes);   
            zOut.close();
            
            httpOutput.writeInt(bytes.length);
            httpOutput.writeInt(out.size());                        
            httpOutput.write(out.toByteArray(), 0, out.size());
            httpOutput.flush();            
        } catch (IOException ioe) {
            Logger.getInstance().log(ioe.getMessage());
        }
    }
    
    private String loadFile(String _filename) {
        ByteArrayOutputStream baos=null;
       
        String SURVEY_ROOT = AppMIDlet.getInstance().getFileSystem().getSurveyDirName();
        String strTemp = "";
        try {
            FileConnection fc = (FileConnection) Connector.open(Resources.ROOT_DIR + SURVEY_ROOT + _filename);
            
            DataInputStream dos = fc.openDataInputStream();
            
            baos = new ByteArrayOutputStream();
            
            int data = dos.read();
            
            while (data != -1) {
                baos.write((byte) data);
                data = dos.read();
            }
            
            strTemp = baos.toString();
            baos.close();
            fc.close();
            dos.close();
            
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
       
        return strTemp;
    }          
}

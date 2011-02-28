package br.org.indt.ndg.mobile.download;

import br.org.indt.ndg.lwuit.control.SurveysControl;
import br.org.indt.ndg.lwuit.ui.CheckNewSurveyList;
import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import br.org.indt.ndg.lwuit.ui.StatusScreenDownload;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.FileSystem;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.SurveyList;
import br.org.indt.ndg.mobile.logging.Logger;
import br.org.indt.ndg.mobile.xmlhandle.Parser;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.microedition.io.Connection;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.file.FileConnection;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class DownloadNewSurveys implements Runnable{
    
    private static DownloadNewSurveys dns;
    private String urlList; 
    private String urlDownload;
    private String urlAck;
    //private StatusScreen ss;
    private Boolean operationCanceled = Boolean.FALSE;
    String[] acceptableTypes = {"text/xml", "application/xml"};
    private byte currentStep = '0';
    private final byte STEP1 = '1';
    private final byte STEP2 = '2';
    private Hashtable surveysDirFiles;
    private String strNotDownloadedSurveys = "";
    
    private HttpConnection httpConnection;

    private InputStream httpInputStream;
    private Thread thread = null;

    private String serverStatus = Resources.CONNECTING;
    /** buffer length to download */
    private static final int MAX_DL_SIZE = 1024;
    
    private Parser parser;
    
    private DownloadNewSurveys() {
        String urlBase = AppMIDlet.getInstance().getSettings().getStructure().getReceiveSurveyURL();
        urlList = urlBase + "?do=list&imei=" + AppMIDlet.getInstance().getIMEI();
        urlDownload = urlBase + "?do=download&imei=" + AppMIDlet.getInstance().getIMEI();
        urlAck = urlBase + "?do=ack&imei=" + AppMIDlet.getInstance().getIMEI();
    }
    
    public static DownloadNewSurveys getInstance() {
        if (dns == null) {
            dns = new DownloadNewSurveys();
        }
        return dns;
    }

    public String ServerStatus()
    {
        return serverStatus;
    }

    public void check() {
        setOperationAsNotCanceled();
        serverStatus = Resources.CONNECTING;
        AppMIDlet.getInstance().setDisplayable(StatusScreenDownload.class);
        currentStep = STEP1;
        try { Thread.sleep(500); } catch (InterruptedException ex) {}
        thread = new Thread(this);
        thread.start();
    }
    
    public void download() {
        setOperationAsNotCanceled();
        AppMIDlet.getInstance().setDisplayable(StatusScreenDownload.class);
        currentStep = STEP2;
        try { Thread.sleep(500); } catch (InterruptedException ex) {}
        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        try {
            if (currentStep == STEP1) {
                showListNewSurveys();
            } else if (currentStep == STEP2) {
                downloadSurveys();
            }
        }
        catch (Exception e)
        {
            AppMIDlet.getInstance().setDisplayable( br.org.indt.ndg.lwuit.ui.SurveyList.class );
        }
    }
    
    private void downloadSurveys() {
        Connection conn = null;
        StringBuffer acceptField;
        int responseCode = 0;
        String retryAfterField;
        int retryInterval;
        String mediaType;

        try {
            serverStatus = Resources.CONNECTING;
            AppMIDlet.getInstance().setDisplayable(StatusScreenDownload.class);
            if (isOperationCanceled()) {
                return;
            }
            for (;;) {
                conn = Connector.open(urlDownload, Connector.READ);

                if (isOperationCanceled()) {
                    break;
                }

                httpConnection = (HttpConnection) conn;

                // 256 is given to avoid resizing without adding lengths
                acceptField = new StringBuffer(256);

                // there must be one or more acceptable media types
                acceptField.append(acceptableTypes[0]);
                for (int i = 1; i < acceptableTypes.length; i++) {
                    acceptField.append(", ");
                    acceptField.append(acceptableTypes[i]);
                }

                httpConnection.setRequestProperty("Accept",
                                                  acceptField.toString());

                httpConnection.setRequestMethod(HttpConnection.GET);

                try {
                    responseCode = httpConnection.getResponseCode();
                } catch (IOException ioe) {
                    // -3 user clicks cancel in winsock open
                    if (ioe.getMessage().trim().equals("-3")) {
                        // user canceled
                        cancelOperation();
                    } else {
                        cancelOperation();
                        GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
                        GeneralAlert.getInstance().showCodedAlert(Resources.NETWORK_FAILURE, ioe.getMessage(), GeneralAlert.ERROR);
                        AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.SurveyList.class);
                    }
                    return;
                } catch (SecurityException e) {
                    cancelOperation();
                    GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
                    GeneralAlert.getInstance().showCodedAlert(Resources.NETWORK_FAILURE, Resources.HTTP_UNAUTHORIZED, GeneralAlert.ERROR);
                    AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.SurveyList.class);
                }

                responseCode = httpConnection.getResponseCode();
                if (responseCode != HttpConnection.HTTP_UNAVAILABLE) {
                    break;
                }

                retryAfterField = httpConnection.getHeaderField("Retry-After");
                if (retryAfterField == null) {
                    break;
                }

                try {
                    /*
                     * see if the retry interval is in seconds, and
                     * not an absolute date
                     */
                    retryInterval = Integer.parseInt(retryAfterField);
                    if (retryInterval > 0) {
                        if (retryInterval > 60) {
                            // only wait 1 min
                            retryInterval = 60;
                        }

                        Thread.sleep(retryInterval * 1000);
                    }
                } catch (InterruptedException ie) {
                    // ignore thread interrupt
                    break;
                } catch (NumberFormatException ne) {
                    // ignore bad format
                    break;
                }

                httpConnection.close();
            } // end for

        	if (isOperationCanceled()) {
                AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.SurveyList.class);
                return;
            }

            if (responseCode != HttpConnection.HTTP_OK) {
                cancelOperation();

                GeneralAlert.getInstance().addCommand( GeneralAlert.DIALOG_OK, true );
                GeneralAlert.getInstance().showCodedAlert(Resources.CHECK_NEW_SURVEYS, String.valueOf(responseCode), GeneralAlert.ERROR );
                AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.SurveyList.class);
        	return;
            }

            String mediaTypeStr = httpConnection.getType();
            mediaType = getMediaType(mediaTypeStr);

            if (mediaType != null) {
                boolean goodType = false;

                for (int i = 0; i < acceptableTypes.length; i++) {
                    if (mediaType.equals(acceptableTypes[i])) {
                        goodType = true;
                        break;
                    }
                }

                if (!goodType) {
                    cancelOperation();
                    GeneralAlert.getInstance().addCommand( GeneralAlert.DIALOG_OK, true );
                    GeneralAlert.getInstance().show(Resources.CHECK_NEW_SURVEYS, Resources.EDOWNLOAD_FAILED_INVALID_MIME_TYPE + mediaType, GeneralAlert.ERROR );
                    AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.SurveyList.class);
                    return;
                }
            }
            httpInputStream = httpConnection.openInputStream();
            transferSurveysData(httpInputStream);
            
            if (!isOperationCanceled()) {
                sendAck();
                if (!isOperationCanceled()) {
                    AppMIDlet.getInstance().setFileSystem(new FileSystem(Resources.ROOT_DIR));
                    AppMIDlet.getInstance().setSurveyList(new SurveyList());
                    AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.SurveyList.class);
                    // Send Alert about now downloaded surveys
                    if (!strNotDownloadedSurveys.equals("")) {
                        GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
                        GeneralAlert.getInstance().show(Resources.CHECK_NEW_SURVEYS, Resources.SURVEY_NOT_DOWNLOADED + strNotDownloadedSurveys, GeneralAlert.ALARM);
                        AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.SurveyList.class);
                    }
                }
            } else {
                cancelOperation();
                removeInvalidSurveys();
                AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.SurveyList.class);
            }
        } catch (IOException ioe) {
            cancelOperation();
            GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
            GeneralAlert.getInstance().showCodedAlert(Resources.NETWORK_FAILURE, ioe.getMessage().trim(), GeneralAlert.ERROR);
            AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.SurveyList.class);
            return;
        } catch (SecurityException e) {
            cancelOperation();
            GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
            GeneralAlert.getInstance().showCodedAlert(Resources.NETWORK_FAILURE, Resources.HTTP_UNAUTHORIZED, GeneralAlert.ERROR);
            AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.SurveyList.class);
        } finally {
            // Close the streams or connections this method opened.
            try {
                httpInputStream.close();
            } catch (Exception e) {
                // ignore
            }

            try {
                conn.close();
            } catch (Exception e) {
                // ignore
            }
        }
    }
    
    private void sendAck() {
        boolean ackOK = true;
        try {
            Connection conn = null;
            conn = Connector.open(urlAck, Connector.READ);
            httpConnection = (HttpConnection) conn;
            if (httpConnection.getResponseCode() != HttpConnection.HTTP_OK) {
                ackOK = false;
            }
        } catch (IOException ex) {
            ackOK = false;
        }
        
        if (!ackOK) {
            removeInvalidSurveys();
            cancelOperation();
            GeneralAlert.getInstance().addCommand( GeneralAlert.DIALOG_OK, true );
            GeneralAlert.getInstance().show(Resources.CHECK_NEW_SURVEYS, Resources.EDOWNLOAD_ACK_ERROR, GeneralAlert.ERROR );
            AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.SurveyList.class);
        }
    }
    
    private void removeInvalidSurveys() {
        Enumeration e = surveysDirFiles.keys();
        while (e.hasMoreElements()) {
            String dir = (String)e.nextElement();
            String xmlFile = (String) surveysDirFiles.get(dir);
            try {
                 FileConnection fconn = (FileConnection) Connector.open(xmlFile);
                 if (fconn.exists()) {
                     fconn.delete();
                 }
                 fconn.close();
                 fconn = (FileConnection) Connector.open(dir);
                 if (fconn.exists()) {
                     fconn.delete();
                 }
                 fconn.close();
            } catch (Exception ex){
                //ignore
            }
        }
    }
    
    private void transferSurveysData(InputStream in){
        byte[] buffer = new byte[MAX_DL_SIZE];
        int bytesRead;
        int totalBytesRead = 0;

        if ((int) httpConnection.getLength() <= 0) {
            cancelOperation();
            GeneralAlert.getInstance().addCommand( GeneralAlert.DIALOG_OK, true );
            GeneralAlert.getInstance().show(Resources.CHECK_NEW_SURVEYS, Resources.EDOWNLOAD_FAILED_INVALID_DATA, GeneralAlert.ERROR );
            AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.SurveyList.class);
            return;
        }
        int totalKbytes = (int) httpConnection.getLength() / MAX_DL_SIZE;
        
        serverStatus = Resources.DOWNLOADING_NEW_SURVEYS;
        AppMIDlet.getInstance().setDisplayable(StatusScreenDownload.class);
        //DFss.reset(totalKbytes);
        //DFss.showDownloadingSurveys();
        StringBuffer sb = new StringBuffer();
        surveysDirFiles = new Hashtable();
        strNotDownloadedSurveys = "";
        try {
            for (;;) {
                if (isOperationCanceled()) {
                    break;
                }
                bytesRead = in.read(buffer);

                if (bytesRead == -1) {
                    if (totalKbytes != (totalBytesRead / MAX_DL_SIZE)) {
                            cancelOperation();
                            GeneralAlert.getInstance().addCommand( GeneralAlert.DIALOG_OK, true );
                            GeneralAlert.getInstance().show(Resources.CHECK_NEW_SURVEYS, Resources.EDOWNLOAD_INCOMPLETED, GeneralAlert.ERROR );
                            AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.SurveyList.class);
                        } else {
                            break;
                        }
                }
                
                totalBytesRead += bytesRead;
                //DFss.setCurrentSurveyIndex(totalBytesRead / MAX_DL_SIZE);
                String sBuffer = new String(buffer, 0, bytesRead);
                sb.append(sBuffer);
                while (true) {
                    String ssb = sb.toString();
                    int i = ssb.indexOf(Resources.XML_TAG_END_SURVEY);
                    if (i == -1) {
                        break;
                    }
                    if  (i > 0) {
                        String surveyCompleted = ssb.substring(0, i + Resources.XML_TAG_END_SURVEY.length());

                        String rest = ssb.substring(i + Resources.XML_TAG_END_SURVEY.length(), ssb.length());
                        
                        sb = new StringBuffer(rest);
                        String filename = Resources.ROOT_DIR + Resources.TMP_DOWNLOAD_SURVEY;

                        byte[] out = surveyCompleted.getBytes("UTF-8");

                        SurveyHandler sh = new SurveyHandler();
                        parser = new Parser(sh);
                        ByteArrayInputStream bais = new ByteArrayInputStream(out);
                        parser.parseInputStream(bais);
                        String surveyID = sh.getSurveyID();

                        FileConnection fconnDir = (FileConnection) Connector.open(Resources.ROOT_DIR + Resources.SURVEY + surveyID + "/");
                        if (!fconnDir.exists())
                        {
                            fconnDir.mkdir();
                            fconnDir.close();
                            FileConnection fconnSurvey = (FileConnection) Connector.open(Resources.ROOT_DIR + Resources.SURVEY + surveyID + "/" + Resources.SURVEY_NAME);
                            if (!fconnSurvey.exists()) fconnSurvey.create();
                            else {
                                fconnSurvey.delete();
                                fconnSurvey.create();
                            }
                            DataOutputStream dos = fconnSurvey.openDataOutputStream();
                            dos.write(out, 0, out.length);
                            dos.flush();
                            dos.close();
                            fconnSurvey.close();
                            surveysDirFiles.put(Resources.ROOT_DIR + Resources.SURVEY + surveyID + "/", Resources.ROOT_DIR + Resources.SURVEY + surveyID + "/" + Resources.SURVEY_NAME);
                        }
                        else
                        {
                            //The following surveys were not downloaded since they already exist in mobile.
                            strNotDownloadedSurveys += " | "  + sh.getSurveyTitle();
                        }
                    }
                }
            }
        } 
            catch (IOException ioe) {
                cancelOperation();
                GeneralAlert.getInstance().addCommand( GeneralAlert.DIALOG_OK, true );
                GeneralAlert.getInstance().showCodedAlert(Resources.NETWORK_FAILURE, ioe.getMessage().trim(), GeneralAlert.ERROR );
                AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.SurveyList.class);
        }  catch (SecurityException e) {
                cancelOperation();
                GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
                GeneralAlert.getInstance().showCodedAlert(Resources.NETWORK_FAILURE, Resources.HTTP_UNAUTHORIZED, GeneralAlert.ERROR);
                AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.SurveyList.class);
        }
    }
    
    private void showListNewSurveys() throws Exception {
        String filename = Resources.ROOT_DIR + Resources.NEW_SURVEYS_LIST;
        FileConnection fconn = null;
        DataOutputStream out = null;
        DataInputStream dis = null;
        try {
            fconn = (FileConnection) Connector.open(filename);
            if(!fconn.exists()) fconn.create();
            else {
                fconn.delete();
                fconn.create();
            }
            out = fconn.openDataOutputStream();
            downloadResource(urlList, acceptableTypes, out);
            out.flush();
            out.close();
            if (!isOperationCanceled()) {
                 // Parse the surveys list
                String[] surveysTitles = null;
                dis = fconn.openDataInputStream();
                SurveysListHandler slh = new SurveysListHandler();
                surveysTitles = slh.parse(dis);

                SurveysControl.getInstance().setAvaiableSurveyToDownload(surveysTitles);
                if( surveysTitles.length > 0 ) {
                    AppMIDlet.getInstance().setDisplayable(CheckNewSurveyList.class);
                } else {
                    GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
                    GeneralAlert.getInstance().show(Resources.DOWNLOAD_SURVEYS, Resources.THERE_ARE_NO_NEW_SURVEYS, GeneralAlert.INFO);
                    AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.SurveyList.class);
                }
            }
        } catch (SAXException ex) {
            Logger.getInstance().log("ex: " + ex.getMessage() + "::"+ex.getClass().getName());
            GeneralAlert.getInstance().addCommand( GeneralAlert.DIALOG_OK, true );
            GeneralAlert.getInstance().show(Resources.CHECK_NEW_SURVEYS, Resources.EPARSE_SAX + ex.getMessage() , GeneralAlert.ERROR );
            throw ex;
        } catch (ParserConfigurationException ex) {
            Logger.getInstance().log("ex2: " + ex.getMessage());
            GeneralAlert.getInstance().addCommand( GeneralAlert.DIALOG_OK, true );
            GeneralAlert.getInstance().show(Resources.CHECK_NEW_SURVEYS, Resources.EPARSE_GENERAL + ex.getMessage() , GeneralAlert.ERROR );
            throw ex;
        } catch (ConnectionNotFoundException ex) {
            Logger.getInstance().log(ex.getMessage());
            GeneralAlert.getInstance().addCommand( GeneralAlert.DIALOG_OK, true );
            GeneralAlert.getInstance().showCodedAlert(Resources.NETWORK_FAILURE, ex.getMessage().trim(), GeneralAlert.ERROR );
            AppMIDlet.getInstance().setDisplayable( br.org.indt.ndg.lwuit.ui.SurveyList.class );
        } catch(IOException ex) {
            Logger.getInstance().log(ex.getMessage());
            GeneralAlert.getInstance().addCommand( GeneralAlert.DIALOG_OK, true );
            GeneralAlert.getInstance().show(Resources.CHECK_NEW_SURVEYS, Resources.ERROR_TITLE + ex.getMessage() , GeneralAlert.ERROR );
            throw ex;
        }  catch (SecurityException ex) {
            cancelOperation();
            GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
            GeneralAlert.getInstance().showCodedAlert(Resources.NETWORK_FAILURE, Resources.HTTP_UNAUTHORIZED, GeneralAlert.ERROR);
            throw ex;
        } finally {
            try {
                boolean canceled = false;
                synchronized (operationCanceled) {
                    if (operationCanceled == Boolean.FALSE)
                        canceled = true;
                }
                if (canceled) {
                    fconn.delete();
                }
                if (out != null) {
                    out.close();
                }
                if (dis != null) {
                    dis.close();
                }
                if (fconn != null) {
                    fconn.close();
                }
            } catch (IOException e) {
                // ignore
            }
        }
    }

    public void cancelOperation() {
        synchronized (operationCanceled) {
            operationCanceled = Boolean.TRUE;
        }
    }
    

   public void cancelAndKillOperation() {
        synchronized (operationCanceled) {
            operationCanceled = Boolean.TRUE;
            if(thread != null && thread.isAlive()) {
                thread.interrupt();
            }
             AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.SurveyList.class);
        }
    }

   /**
     * Download data from URL param and write in Output Stream param.
     *
     * @param urlParam URL to download
     * @param acceptableTypes mime types acceptable list to download.
     * @param output output stream to write downloaded data
     *
     */
    public void downloadResource(String urlParam, String[] acceptableTypes,
            OutputStream output) {
        Connection conn = null;
        StringBuffer acceptField;
        int responseCode = 0;
        String retryAfterField;
        int retryInterval;
        String mediaType;
        try {
            if (isOperationCanceled()) {
                AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.SurveyList.class);
                return;
            }

            for (;;) {
                if (isOperationCanceled())
                    break;
                conn = Connector.open(urlParam, Connector.READ);
                httpConnection = (HttpConnection) conn;

                // 256 is given to avoid resizing without adding lengths
                acceptField = new StringBuffer(256);

                // there must be one or more acceptable media types
                acceptField.append(acceptableTypes[0]);
                for (int i = 1; i < acceptableTypes.length; i++) {
                    acceptField.append(", ");
                    acceptField.append(acceptableTypes[i]);
                }
                httpConnection.setRequestProperty("Accept",
                                                  acceptField.toString());
                httpConnection.setRequestMethod(HttpConnection.GET);

                try {
                    responseCode = httpConnection.getResponseCode();
                } catch (IOException ioe) {
                    Logger.getInstance().log("ioe: " + ioe.getMessage());
                    // -3 user clicks cancel in winsock open
                    if (ioe.getMessage().trim().equals("-3")) {
                        // user canceled
                        cancelOperation();
                    } else {
                        cancelOperation();
                        GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
                        GeneralAlert.getInstance().showCodedAlert(Resources.NETWORK_FAILURE, ioe.getMessage().trim(), GeneralAlert.ERROR);
                        AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.SurveyList.class);
                    }
                    return;
                } catch (SecurityException e) {
                    cancelOperation();
                    GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
                    GeneralAlert.getInstance().showCodedAlert(Resources.NETWORK_FAILURE, Resources.HTTP_UNAUTHORIZED, GeneralAlert.ERROR);
                    AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.SurveyList.class);
                }
                //responseCode = httpConnection.getResponseCode(); // what was thtat for?

                // if the server is currently unable to handle the request due 
                // to a temporary overloading or maintenance of the server then
                // retry after a interval.
                if (responseCode != HttpConnection.HTTP_UNAVAILABLE) {
                    break;
                }
                retryAfterField = httpConnection.getHeaderField("Retry-After");
                if (retryAfterField == null) {
                    break;
                }

                try {
                    /*
                     * see if the retry interval is in seconds, and
                     * not an absolute date
                     */
                    retryInterval = Integer.parseInt(retryAfterField);
                    if (retryInterval > 0) {
                        if (retryInterval > 60) {
                            // only wait 1 min
                            retryInterval = 60;
                        }

                        Thread.sleep(retryInterval * 1000);
                    }
                } catch (InterruptedException ie) {
                    // ignore thread interrupt
                    break;
                } catch (NumberFormatException ne) {
                    // ignore bad format
                    break;
                }

                httpConnection.close();
            } // end for

            if (isOperationCanceled()) {
                AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.SurveyList.class);
                return;
            }
            
            if (responseCode != HttpConnection.HTTP_OK) {
                cancelOperation();
                GeneralAlert.getInstance().addCommand( GeneralAlert.DIALOG_OK, true );
                GeneralAlert.getInstance().showCodedAlert(Resources.CHECK_NEW_SURVEYS, String.valueOf(responseCode), GeneralAlert.ERROR );
                AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.SurveyList.class);
                return;
            }
            mediaType = getMediaType(httpConnection.getType());

            if (mediaType != null) {
                boolean goodType = false;

                for (int i = 0; i < acceptableTypes.length; i++) {
                    if (mediaType.equals(acceptableTypes[i])) {
                        goodType = true;
                        break;
                    }
                }

                if (!goodType) {
                    cancelOperation();
                    GeneralAlert.getInstance().addCommand( GeneralAlert.DIALOG_OK, true );
                    GeneralAlert.getInstance().show(Resources.CHECK_NEW_SURVEYS, Resources.EDOWNLOAD_FAILED_INVALID_MIME_TYPE + mediaType, GeneralAlert.ERROR );
                    AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.SurveyList.class);
                    return;
                }
            }
            httpInputStream = httpConnection.openInputStream();

            transferData(httpInputStream, output);

            if (isOperationCanceled()) {
                // Close the streams or connections this method opened.
                //try { httpInputStream.close(); } catch (Exception e) {}
                //try { conn.close(); } catch (Exception e) {}
                AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.SurveyList.class);
            }
          
        } catch (IOException ioe) {
                cancelOperation();
                GeneralAlert.getInstance().addCommand( GeneralAlert.DIALOG_OK, true );
                GeneralAlert.getInstance().showCodedAlert(Resources.NETWORK_FAILURE, ioe.getMessage().trim(), GeneralAlert.ERROR );
                AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.SurveyList.class);
        	return;
        } catch (SecurityException e) {
                cancelOperation();
                GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
                GeneralAlert.getInstance().showCodedAlert(Resources.NETWORK_FAILURE, Resources.HTTP_UNAUTHORIZED, GeneralAlert.ERROR);
                AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.SurveyList.class);
        } finally {
            // Close the streams or connections this method opened.
            try {
                httpInputStream.close();
            } catch (Exception e) {
                // ignore
            }

            try {
                conn.close();
            } catch (Exception e) {
                // ignore
            }
        }
    }
    
    private boolean isOperationCanceled() {
        boolean result = false;
        synchronized (operationCanceled) {
            if (operationCanceled == Boolean.TRUE) {
                result = true;
            }
        }
        return result;
    }
    
    private void setOperationAsNotCanceled() {
        synchronized(operationCanceled) {
            operationCanceled = Boolean.FALSE;
        }
    }
    
   /**
     * Make the parse of mime-types from content-type field
     * The media-type everything for the ';' that marks the parameters.
     *
     * @param contentType http content-type field value
     *
     * @return mime type
     */
    private static String getMediaType(String contentType) {
        int semiColon;

        if (contentType == null) {
            return null;
        }

        semiColon = contentType.indexOf(';');
        if (semiColon < 0) {
            return contentType.toLowerCase();
        }

        return contentType.substring(0, semiColon).toLowerCase();
    }

    /**
     * Make the download of data.
     *     *
     * @param in a input stream from data.
     * @param out o output stream to write data.
     *
     */
    private void transferData(InputStream in, OutputStream out) {
        byte[] buffer = new byte[MAX_DL_SIZE];
        int bytesRead;
        int totalBytesWritten = 0;
        
        if ((int) httpConnection.getLength() <= 0) {
     
            cancelOperation();
            GeneralAlert.getInstance().addCommand( GeneralAlert.DIALOG_OK, true );
            GeneralAlert.getInstance().show(Resources.CHECK_NEW_SURVEYS, Resources.EDOWNLOAD_FAILED_INVALID_DATA, GeneralAlert.ERROR );
            AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.SurveyList.class);
            return;
        }
        int totalKbytes = (int) httpConnection.getLength() / MAX_DL_SIZE;

        try {
            StringBuffer sb = new StringBuffer();
            for (;;) {
                if (isOperationCanceled()) {
                    break;
                }

                bytesRead = in.read(buffer);
                if (bytesRead == -1) {
                    if (totalKbytes != (totalBytesWritten / MAX_DL_SIZE)) {
                        cancelOperation();
                        GeneralAlert.getInstance().addCommand( GeneralAlert.DIALOG_OK, true );
                        GeneralAlert.getInstance().show(Resources.CHECK_NEW_SURVEYS, Resources.EDOWNLOAD_INCOMPLETED, GeneralAlert.ERROR );
                        AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.SurveyList.class);
                    }
                    break;
                }

                String sBuffer = new String(buffer, 0, bytesRead);
                sb.append(sBuffer);
                totalBytesWritten += bytesRead;
            }
            byte[] outbyte = sb.toString().getBytes("UTF-8");
            out.write(outbyte, 0, outbyte.length);
        } catch (IOException ioe) {
            cancelOperation();
            GeneralAlert.getInstance().addCommand( GeneralAlert.DIALOG_OK, true );
            GeneralAlert.getInstance().showCodedAlert(Resources.NETWORK_FAILURE, ioe.getMessage().trim(), GeneralAlert.ERROR );
            AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.SurveyList.class);
            return;
        }
         catch (SecurityException e) {
            cancelOperation();
            GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
            GeneralAlert.getInstance().showCodedAlert(Resources.NETWORK_FAILURE, Resources.HTTP_UNAUTHORIZED, GeneralAlert.ERROR);
            AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.SurveyList.class);
        }
        return;
    }
}

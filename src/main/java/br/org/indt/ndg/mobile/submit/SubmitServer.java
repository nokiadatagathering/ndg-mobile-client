package br.org.indt.ndg.mobile.submit;

import br.org.indt.ndg.lwuit.ui.GeneralAlert;
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
import br.org.indt.ndg.mobile.SurveyList;
import br.org.indt.ndg.mobile.error.ServerCantWriteResultsException;
import br.org.indt.ndg.mobile.logging.Logger;
import com.jcraft.jzlib.JZlib;
import com.jcraft.jzlib.ZOutputStream;

public class SubmitServer {
    //These values are set in Settings.xml
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

    public SubmitServer() {
    }

    public void cancel() {
        stop = true;
    }

    public void submitResult(String resultFilename){
        Vector resultsToSend = new Vector();
        resultsToSend.addElement(resultFilename);
        send(resultsToSend);
    }

    public void submit( Vector resultFilenames ) {
        send(resultFilenames);
    }

    private void send(Vector resultFilenames){
            boolean compression_on = AppMIDlet.getInstance().getSettings().getStructure().getServerCompression();
            urlServlet = AppMIDlet.getInstance().getSettings().getStructure().getServerUrl();

            Enumeration e = resultFilenames.elements();
            while (e.hasMoreElements() && !stop) {
                try {
                    httpConn = (HttpConnection) Connector.open(urlServlet);
                    httpConn.setRequestMethod(HttpConnection.POST);
                    httpOutput = httpConn.openDataOutputStream();
                }
                catch(Exception ex){
                    finalizeGPRSTransmission();
                    break;
                }

                String file = null;
                String buffer = "";
                file = (String) e.nextElement();
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
                           GeneralAlert.getInstance().addCommand( GeneralAlert.DIALOG_OK, true);
                           GeneralAlert.getInstance().show(Resources.ERROR_TITLE, Resources.SURVEY_NOT_IN_SERVER, GeneralAlert.ERROR);
                           AppMIDlet.getInstance().setSurveyList(new SurveyList());
                           AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.SurveyList.class);
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
                        GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
                        GeneralAlert.getInstance().showCodedAlert( Resources.NETWORK_FAILURE,
                                                                   ioe.getMessage() != null ? ioe.getMessage().trim() : "",
                                                                   GeneralAlert.ALARM );
                    }
                } catch (ServerCantWriteResultsException snwre) {
                    if (!stop) {
                        GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
                        GeneralAlert.getInstance().show( Resources.NETWORK_FAILURE, Resources.TRY_AGAIN_LATER , GeneralAlert.ALARM );
                    }
                }
                finalizeGPRSTransmission();
            }
            AppMIDlet.getInstance().setResultList(new ResultList());
            AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.ResultList.class);
    }

    private void finalizeGPRSTransmission(){
        try {
            if (httpOutput != null) httpOutput.close();
            if (httpInput != null) httpInput.close();
            if (fileInput != null) fileInput.close();
            if (httpConn != null) httpConn.close();
        } catch (Exception exc) {
            if (!stop) {
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
            GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
            GeneralAlert.getInstance().showCodedAlert( Resources.NETWORK_FAILURE, ioe.getMessage().trim() , GeneralAlert.ALARM );
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
            FileConnection fc = (FileConnection) Connector.open(Resources.ROOT_DIR + SURVEY_ROOT + "b_"+ _filename + "/" + "b_"+ _filename );//here is file with image data

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
            Logger.getInstance().log(ioe.getMessage());
        }
        return strTemp;
    }
}

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.model.*;
import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import br.org.indt.ndg.lwuit.ui.ResultList;
import br.org.indt.ndg.lwuit.ui.WaitingScreen;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.FileSystem;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.logging.Logger;
import com.nokia.xfolite.xforms.dom.XFormsDocument;
import com.nokia.xfolite.xforms.submission.XFormsXMLSerializer;
import com.nokia.xfolite.xml.dom.Element;
import com.nokia.xfolite.xml.dom.NamedNode;
import com.nokia.xfolite.xml.dom.Node;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.Random;
import java.util.Vector;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.location.Location;

/**
 *
 * @author kgomes
 */
public class PersistenceManager {

    private static PersistenceManager instance = null;
    private SaveResultsObserver m_saveObserver = null;
    private boolean error = false;
    private String resultId;
    private Vector vQuestions;

    private PersistenceManager(){
    }

    public static PersistenceManager getInstance(){
        if(instance==null){
            instance = new PersistenceManager();
        }
        return instance;
    }

    public String generateUniqueID() {
        Random rnd = new Random();
        long uniqueID = (((System.currentTimeMillis() >>>16)<<16)+rnd.nextLong());
        return Integer.toHexString((int) uniqueID);
    }

    public boolean getError() {
        return error;
    }

    public void save(Vector _vQuestions, SaveResultsObserver saveObserver) {
        m_saveObserver = saveObserver;
        vQuestions = _vQuestions;

        WaitingScreen.show(Resources.SAVING_RESULT);
        SaveResultRunnable srr = new SaveResultRunnable();
        Thread t = new Thread(srr);
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
    }

    public void saveXForm(XFormsDocument document){


        XFormsXMLSerializer serilizer = new XFormsXMLSerializer();

        //Create path
        String surveyDir = AppMIDlet.getInstance().getFileSystem().getSurveyDirName();
        String UID = generateUniqueID();

        String filename;  //check whether to create new file or use existing filename
        String fname;  //filename without root/survey directory part

//        if (isLocalFile) {
//            fname = AppMIDlet.getInstance().getFileSystem().getResultFilename();
//        } else {
            fname = "r_" + "_" + AppMIDlet.getInstance().getIMEI() + "_" + UID + ".xml";
//        }
        filename = Resources.ROOT_DIR + surveyDir + fname;

        try {
            FileConnection fCon = (FileConnection)Connector.open(filename);
            if(!fCon.exists()){
                fCon.create();
            }

            OutputStream stream = fCon.openOutputStream();
            serilizer.serialize(stream, document.getDocumentElement(), null);
            stream.close();
            fCon.close();

            AppMIDlet.getInstance().setDisplayable(ResultList.class);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    private void save2() {
        String surveyId = String.valueOf(SurveysControl.getInstance().getSurveyIdNumber());
        try {
            String surveyDir = AppMIDlet.getInstance().getFileSystem().getSurveyDirName();
            boolean isLocalFile = AppMIDlet.getInstance().getFileSystem().isLocalFile();

            String UID = generateUniqueID();

            String filename;  //check whether to create new file or use existing filename
            String fname;  //filename without root/survey directory part
            
            if (isLocalFile) {
                fname = AppMIDlet.getInstance().getFileSystem().getResultFilename();
            } else {
                fname = "r_" + surveyId/*survey.getIdNumber()*/ + "_" + AppMIDlet.getInstance().getIMEI() + "_" + UID + ".xml";
            }

            AppMIDlet.getInstance().getFileSystem().deleteDir( "b_" + fname );//delete old binaries

            filename = Resources.ROOT_DIR + surveyDir + fname;

            resultId = extractResultId(fname);
            if (!isLocalFile) {
                resultId = UID;
            }

            FileConnection connection = (FileConnection) Connector.open(filename);
            if(!connection.exists()) connection.create();

            if (isLocalFile) {
                connection.delete();
                connection.create();
            }

            String displayName = getResultDisplayName(vQuestions);

            FileSystem fs = AppMIDlet.getInstance().getFileSystem();
            fs.storeFilename(displayName, fname);
            AppMIDlet.getInstance().setFileSystem(fs);

            OutputStream out = connection.openOutputStream();
            writeToStream(out, false);

            out.close();
            connection.close();
            save3( fname );//this file is used to send reuslt to server
        } catch( ConnectionNotFoundException e ) {
            error = true;
            Logger.getInstance().log(e.getMessage());
            GeneralAlert.getInstance().addCommand( ExitCommand.getInstance());
            GeneralAlert.getInstance().show(Resources.ERROR_TITLE, Resources.ECREATE_RESULT, GeneralAlert.ERROR );
        } catch( IOException e ) {
            error = true;
            Logger.getInstance().log(e.getMessage());
            GeneralAlert.getInstance().addCommand( ExitCommand.getInstance());
            GeneralAlert.getInstance().show(Resources.ERROR_TITLE, Resources.EWRITE_RESULT, GeneralAlert.ERROR );
        }
        catch(Exception e){
            Logger.getInstance().log(e.getMessage());
            e.printStackTrace();
        }
        finally {
            System.gc();
        }
    }

    private void save3( String name ) throws IOException {
        String surveyDir = AppMIDlet.getInstance().getFileSystem().getSurveyDirName();

        String fname = "b_"+ name;
        String dirname = Resources.ROOT_DIR + surveyDir + fname;

        FileConnection directory = (FileConnection) Connector.open(dirname);
        if( !directory.exists() ) {
            directory.mkdir();
        }
        directory.close();

        String filename = Resources.ROOT_DIR + surveyDir + fname + "/" + fname;
        FileConnection connection = (FileConnection) Connector.open(filename);
        if( connection.exists() ) {
            connection.delete();
        }
        connection.create();

        OutputStream out = connection.openOutputStream();
        writeToStream(out, true);

        out.close();
        connection.close();
    }

    private void writeToStream( OutputStream out, boolean appendBinaryData ) {
        PrintStream output = new PrintStream(out);
        output.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
        output.print("<result ");
        output.print("r_id=\"" + resultId + "\" ");
        // *****************************   PROTOCOL DEFINITION *************************
        //
        //****************************************************************************************************
        //Type of Msg |      SurveyID			  |      Number of  Msgs  |    	ResultID  	   | UserID
        //      9           9999999999                          99             a9b9c9d9       999999999999999
        //****************************************************************************************************
        //end of header
        output.print("s_id=\"" + String.valueOf(SurveysControl.getInstance().getSurveyIdNumber()) + "\" ");
        output.print("u_id=\"" + AppMIDlet.getInstance().getIMEI() + "\" ");

        long timeTaken = (new Date()).getTime();
        output.print("time=\"" + String.valueOf(timeTaken)+ "\">");  //convert to seconds
        output.println();
        if (AppMIDlet.getInstance().getSettings().getStructure().getGpsConfigured()) {
            // we do not set new location if it was already in survey and survey is modified
            // it is kept by resultHandler
            String longitude = null;
            String latitude = null;
            if (AppMIDlet.getInstance().getFileStores().getResultStructure() != null) {
                latitude = AppMIDlet.getInstance().getFileStores().getResultStructure().getLatitude();
                longitude = AppMIDlet.getInstance().getFileStores().getResultStructure().getLongitude();
            }
            if (longitude != null && latitude != null) {
                output.println("<latitude>" + latitude + "</latitude>");
                output.println("<longitude>" + longitude + "</longitude>");
            } else {
                Location loc = AppMIDlet.getInstance().getLocation();
                if (loc != null) {
                    if (loc.getQualifiedCoordinates() != null) {
                        output.println("<latitude>" + loc.getQualifiedCoordinates().getLatitude() + "</latitude>");
                        output.println("<longitude>" + loc.getQualifiedCoordinates().getLongitude() + "</longitude>");
                    }
                }
            }
        }
        output.println("<title>" + AppMIDlet.getInstance().u2x(getResultDisplayName(vQuestions)) + "</title>");
        int catId = 0;
        int qIndex = 0;
        NDGQuestion question = (NDGQuestion) vQuestions.elementAt(qIndex);
        while (qIndex < vQuestions.size()) {
            catId = Integer.parseInt(question.getCategoryId());
            /** Category **/
            output.print("<category " + "name=\"" + AppMIDlet.getInstance().u2x(question.getCategoryName()) + "\" ");
            output.println("id=\"" + question.getCategoryId() + "\">");
            while (catId == Integer.parseInt(question.getCategoryId())) {
                /** Question **/
                String type = getQuestionType(question);
                output.print("<answer " + "type=\"" + type + "\" ");
                output.print("id=\"" + question.getQuestionId() + "\" ");
                output.print("visited=\"" + question.getVisited() + "\"");
                if (question.getType().equals("_time")) {
                    if (((TimeQuestion) question).getConvention() == 24 || ((TimeQuestion) question).getConvention() == 0) {
                        output.print(" convention=\"" + "24" + "\"");
                    } else {
                        if (((TimeQuestion) question).getAm_pm() == TimeQuestion.AM) {
                            output.print(" convention=\"" + "am" + "\"");
                        } else {
                            output.print(" convention=\"" + "pm" + "\"");
                        }
                    }
                    output.print(">");
                    output.println();
                    question.save(output);
                } else if ( question.getType().equals("_img") ) {
                    output.print(">");
                    output.println();
                    ((ImageQuestion)question).save( output, appendBinaryData );
                } else {
                    output.print(">");
                    output.println();
                    question.save(output);
                }
                output.println("</answer>");
                qIndex++;
                if (qIndex >= vQuestions.size()) {
                    break;
                }
                question = (NDGQuestion) vQuestions.elementAt(qIndex);
            }
            output.println("</category>");
        }
        output.println("</result>");
    }

    private String extractResultId(String filename){
        String result = "";
        int i = filename.lastIndexOf('_');
        int z = filename.indexOf('.');
        if((i>0) && (z>0)){
            result = filename.substring(i+1,z);
        }
        return result;
    }

    private String getQuestionType(NDGQuestion question) {
        String result = "";
        if (question instanceof DescriptiveQuestion) {
            result = "_str";
        }
        else if (question instanceof DecimalQuestion) {
            result = "_decimal";
        }
        else if (question instanceof IntegerQuestion) {
            result = "_int";
        }
        else if (question instanceof DateQuestion) {
            result = "_date";
        }
        else if (question instanceof TimeQuestion) {
            result = "_time";
        }
        else if (question instanceof ChoiceQuestion) {
            result = "_choice";
        }
        else if(question instanceof ImageQuestion){
            result = "_img";
        }
        return result;
    }

    private String getResultDisplayName(Vector vQuestions) {
        String result = "";
        String [] displayIds;
        displayIds = SurveysControl.getInstance().getSurveyDisplayIds();
        NDGQuestion q;
        for (int i = 0; i < vQuestions.size(); i++) {
            q = (NDGQuestion) vQuestions.elementAt(i);
            if ( (q.getCategoryId().equals(displayIds[0])) && (q.getQuestionId().equals(displayIds[1])) ) {
                if ((q instanceof ChoiceQuestion) || (q instanceof ImageQuestion)) {
                    result = resultId;
                }
                else if ( (q instanceof DescriptiveQuestion) || (q instanceof NumericQuestion) ) {
                    result = ((String) q.getAnswer().getValue());
                    if ( result.equals("") ) {
                        result = resultId;
                    }
                }
                else if (q instanceof DateQuestion) {
                    long datelong = Long.parseLong((String)q.getAnswer().getValue());
                    Date date = new Date(datelong);
                    result = date.toString();
                } else if (q instanceof TimeQuestion) {
                    long timelong = Long.parseLong((String)q.getAnswer().getValue());
                    Date time = new Date(timelong);
                    result = time.toString();
                }
            }
        }

        return result;
    }

    public boolean isEditing() {
        return AppMIDlet.getInstance().getFileSystem().isLocalFile();
    }

    private void resultsSaved() {
        if ( m_saveObserver != null) {
            m_saveObserver.onResultsSaved();
            m_saveObserver = null;
        }
    }

    class SaveResultRunnable implements Runnable {
        public void run() {
            PersistenceManager.getInstance().save2();
            AppMIDlet.getInstance().getFileStores().resetQuestions();
            AppMIDlet.getInstance().getFileSystem().loadResultFiles();
            AppMIDlet.getInstance().setResultList(new br.org.indt.ndg.mobile.ResultList());
            PersistenceManager.getInstance().resultsSaved();
        }
    }
}

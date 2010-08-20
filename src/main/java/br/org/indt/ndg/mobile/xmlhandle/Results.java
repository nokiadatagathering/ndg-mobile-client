package br.org.indt.ndg.mobile.xmlhandle;

import br.org.indt.ndg.lwuit.model.NDGQuestion;
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

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.FileSystem;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.error.NullWidgetException;
import br.org.indt.ndg.mobile.logging.Logger;
import br.org.indt.ndg.mobile.sms.SMSMessage;
import br.org.indt.ndg.mobile.structures.ResultStructure;
import br.org.indt.ndg.mobile.structures.SurveyStructure;
import br.org.indt.ndg.mobile.structures.question.Question;


import br.org.indt.ndg.mobile.structures.question.TypeChoice;
import br.org.indt.ndg.mobile.structures.question.TypeDate;
import br.org.indt.ndg.mobile.structures.question.TypeTextFieldInteger;
import br.org.indt.ndg.mobile.structures.question.TypeTextFieldString;
import br.org.indt.ndg.mobile.structures.question.TypeTime;

public class Results {
    
    private SurveyStructure survey;
    private ResultStructure result;
    private boolean error = false;
    
    public Results() {
       this.survey = AppMIDlet.getInstance().getFileStores().getSurveyStructure();
       this.result = AppMIDlet.getInstance().getFileStores().getResultStructure();
    }
    
    public boolean getError() {
        return error;
    }
    
    public String generateUniqueID() {
        Random rnd = new Random();
        long uniqueID = (((System.currentTimeMillis() >>>16)<<16)+rnd.nextLong());
        return Integer.toHexString((int) uniqueID);
    }    
    
    
    public void writeToXmlFile() {
        String surveyId = String.valueOf(survey.getIdNumber());
        try {
            String surveyDir = AppMIDlet.getInstance().getFileSystem().getSurveyDirName();
            boolean isLocalFile = AppMIDlet.getInstance().getFileSystem().isLocalFile();
            
         
            long currentTime = (new Date()).getTime();
            long timeTaken = currentTime - AppMIDlet.getInstance().getTimeTracker();
            
            String UID = generateUniqueID();
            
            int length = UID.length();
            if(length != SMSMessage.MSG_RESULTID_LENGTH){
                if(length < 8){
                    int temp = 8 - length;
                    for(int i = 0; i < temp; i++){
                        UID = "0" + UID;
                    }
                }
                else{
                    int temp = length - 8;
                    UID = UID.substring(temp);
                }
            }            
            
            String filename;  //check whether to create new file or use existing filename
            String fname;  //filename without root/survey directory part
            String commonName;
            StringBuffer smsfilePath = new StringBuffer();
            if (isLocalFile) {
                fname = AppMIDlet.getInstance().getFileSystem().getResultFilename();
                filename = Resources.ROOT_DIR + surveyDir + fname;                             
            }
            else {                
                fname = "r_" + surveyId/*survey.getIdNumber()*/ + "_" + survey.getUserId() + "_" + UID + ".xml";
                filename = Resources.ROOT_DIR + surveyDir + fname;
            }
            
            int fileLength = fname.length();
            commonName = fname.substring(2,  fileLength - 4);
            smsfilePath.append(Resources.ROOT_DIR).append(surveyDir).append("sms_").append(commonName).append(".txt");
            
            FileConnection connection = (FileConnection) Connector.open(filename);
            
            SMSMessage sms = new SMSMessage();
            if(!connection.exists()) connection.create();
            
            if (isLocalFile) {
                connection.delete();
                connection.create();
            }
            
            String displayName = "";
            boolean nullWidget = false;
            try{
                displayName = survey.getDisplayValue();
            }
            catch(NullWidgetException e){
                nullWidget = true;
            }
            if (displayName.equals("") || displayName == null || nullWidget) {
                if (isLocalFile) displayName = result.getResultId();
                else displayName = UID;  //if value is empty then use UID as name
                //survey.setDisplayValue(displayName);  //need to set displayvalue so that next time it saves correctly
            }

            FileSystem fs = AppMIDlet.getInstance().getFileSystem();            
            fs.storeFilename(displayName, fname);
            //if (!isLocalFile) fs.addResultFileName(fname);
            AppMIDlet.getInstance().setFileSystem(fs);
            
            OutputStream out = connection.openOutputStream();
            PrintStream output = new PrintStream(out);
            
            output.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
            output.print("<result ");
            
            if (isLocalFile) {                     
                output.print("r_id=\"" + result.getResultId() + "\" ");
                output.print("s_id=\"" + result.getSurveyId() + "\" ");
                output.print("u_id=\"" + result.getUserId() + "\" ");
                output.print("time=\"" + String.valueOf(result.getTimeTaken() + (timeTaken/1000))+ "\">");  //convert to seconds
                
                sms.append(SMSMessage.MSG_TYPE_RESULT);
                sms.append(String.valueOf(result.getSurveyId()));
                sms.append("00");
                sms.append(result.getResultId());
                sms.append(result.getUserId());
            } else {                
                output.print("r_id=\"" + UID + "\" ");
                output.print("s_id=\"" + survey.getIdNumber() + "\" ");
                output.print("u_id=\"" + survey.getUserId() + "\" ");
                output.print("time=\"" + String.valueOf(timeTaken/1000) + "\">");  //convert to seconds                
                
               // *****************************   PROTOCOL DEFINITION *************************
                //
                //****************************************************************************************************
                //Type of Msg |      SurveyID			  |      Number of  Msgs  |    	ResultID  	   | UserID
                //      9           9999999999                          99             a9b9c9d9       999999999999999
                //****************************************************************************************************
                
                sms.append(SMSMessage.MSG_TYPE_RESULT);
                sms.append(String.valueOf(survey.getIdNumber()));
                sms.append("00");          
                sms.append(UID);
                sms.append(survey.getUserId());
                //end of header
            }
            output.println();
            
            Location loc = AppMIDlet.getInstance().getLocation();
            
            if (loc != null) {
                if(loc.getQualifiedCoordinates() != null){                    
                    output.println("<latitude>" + loc.getQualifiedCoordinates().getLatitude() + "</latitude>");
                    output.println("<longitude>" + loc.getQualifiedCoordinates().getLongitude() + "</longitude>");
                    sms.append("{");
                    sms.append(String.valueOf(loc.getQualifiedCoordinates().getLatitude()));
                    sms.append(";");
                    sms.append(String.valueOf(loc.getQualifiedCoordinates().getLongitude()));//.substring(0, 8));
                    sms.append("}");                    
                }
            }
            
            output.println("<title>" + AppMIDlet.getInstance().u2x(displayName) + "</title>");
            
            //answers
            Vector questions;
            
            int numCats = survey.getNumCategories();
            int displayQuestionId = survey.getDisplayQuestionId();
            int displayCategoryId = survey.getDisplayCategoryId();
            
            for (int i=0; i < numCats; i++) {                
                output.print("<category " + "name=\"" + AppMIDlet.getInstance().u2x(survey.getCatName(i)) + "\" ");
                output.println("id=\"" + survey.getCatID(i) + "\">");
                
                //Raw data = <[categoryid]S[answerid]"."[answer]>*
                sms.append('<');
                //sms.append('|');
                sms.append(survey.getCatID(i));
                sms.append('|');
                
                questions = survey.getQuestions(i);

                int questionsSize = questions.size();                
                for (int j=0; j < questionsSize; j++) {
                    NDGQuestion question = (NDGQuestion) questions.elementAt(j);
                    
                    String type = question.getType();
                    output.print("<answer " + "type=\"" + type + "\" ");
                    output.print("id=\"" + question.getIdNumber() + "\" ");
                    output.print("visited=\"" + question.getVisited() + "\">");

                    if (question.getType().equals("_time")) {
                        output.print("convention=\"" + ((TypeTime)question).getConvention() + "\">");
                    }

                    output.println(); 
                    
                    sms.append(String.valueOf(question.getIdNumber()));
                    sms.append('.');
                    
                    //need to check here to use displayName and not and empty string
                    //TODO refactor, please
//                    if ((i==displayCategoryId) && (j==displayQuestionId)) {
////                        output.print("<str>");
////                        output.print(AppMIDlet.getInstance().u2x(displayName));
////                        output.println("</str>");
//                        
//                        sms.append(displayName);
//                    }
//                    else {
                    question.save(output, sms.getContent());
//                    }
                    if(j == questionsSize - 1){
                        //end of questions
                        sms.append('>');
                    }
                    else{
                        sms.append('|');
                    }
                    
                    output.println("</answer>");
                }                
                output.println("</category>");  
            }
            
            output.println("</result>");
            sms.append("#");
                  
            out.close();
            connection.close(); 
            sms.save(smsfilePath.toString());            
            
            //sms.send();
        } catch( ConnectionNotFoundException e ) {            
            error = true;
            Logger.getInstance().log(e.getMessage());
            AppMIDlet.getInstance().getGeneralAlert().showErrorExit(Resources.ECREATE_RESULT);
        } catch( IOException e ) {            
            error = true;
            Logger.getInstance().log(e.getMessage());
            AppMIDlet.getInstance().getGeneralAlert().showErrorExit(Resources.EWRITE_RESULT);
        }
        catch(Exception e){
            Logger.getInstance().log(e.getMessage());
        }
    }
}

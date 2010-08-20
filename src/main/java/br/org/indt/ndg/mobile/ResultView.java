package br.org.indt.ndg.mobile;

import br.org.indt.ndg.lwuit.model.NDGQuestion;
import br.org.indt.ndg.mobile.sms.SMSUtils;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Spacer;
import javax.microedition.lcdui.StringItem;

import br.org.indt.ndg.mobile.structures.SurveyStructure;
import br.org.indt.ndg.mobile.structures.question.TypeChoice;
import br.org.indt.ndg.mobile.structures.question.TypeDate;
import br.org.indt.ndg.mobile.structures.question.TypeTextFieldDecimal;
import br.org.indt.ndg.mobile.structures.question.TypeTextFieldInteger;
import br.org.indt.ndg.mobile.structures.question.TypeTextFieldString;
import br.org.indt.ndg.mobile.structures.question.TypeTime;
import br.org.indt.ndg.mobile.submit.StatusScreen;
import br.org.indt.ndg.mobile.submit.SubmitResultRunnable;
import br.org.indt.ndg.mobile.submit.SubmitServer;
 
public class ResultView extends Form implements CommandListener  {
 
    private SurveyStructure survey;
    private Vector catQuestions;
    
    private ResultList resultList;
 
    public ResultView(ResultList list) {
        super("");
        
        resultList = list;
 
        addCommand(Resources.CMD_OPEN_RESULT);
        addCommand(Resources.CMD_DELETE);
        addCommand(Resources.CMD_SEND);
        addCommand(Resources.CMD_EXIT_OPTIONS);
        
        addCommand(Resources.CMD_BACK);
        setCommandListener(this);
 
        survey = AppMIDlet.getInstance().getFileStores().getSurveyStructure();
 
        display();
    }

    private void display() {
        int numOfCats = survey.getNumCategories();

        int numOfQuests;
        Enumeration e;
        NDGQuestion question;
        StringItem catname;
 
        for (int i=0; i< numOfCats; i++) {
            catQuestions = survey.getQuestions(i);
            
            catname = new StringItem("", survey.getCatName(i));
            catname.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_UNDERLINED, Font.SIZE_MEDIUM));
 
            this.append(catname);
            this.append(new Spacer(this.getWidth(), 1));
 
            e = catQuestions.elements();
            
            while (e.hasMoreElements()) {
                question = (NDGQuestion) e.nextElement();

                this.append(getQuestsFormat(question));              
                if (question.getVisited()) this.append("  " + getAnswerFormat(question).getText());
                else this.append("  " + Resources.NOT_VISITED);
                this.append(new Spacer(this.getWidth(), 1));
            }
        }
    }
    
    private void confirmDeleteFiles() {
        AppMIDlet.getInstance().getGeneralAlert().showConfirmAlert(Resources.DELETE_CONFIRMATION, Resources.DELETE_RESULT_CONFIRMATION, "ResultView");
    }
    
    public void handleConfirmResult(Command _cmd) {
        if (_cmd == Resources.CMD_YES) {
            deleteResult();
        }
        else if (_cmd == Resources.CMD_NO) {
            AppMIDlet.getInstance().setDisplayable(this);
        }
    }
    
    private void deleteResult() {
        FileSystem fs = AppMIDlet.getInstance().getFileSystem();           
        String resultFilename = resultList.getResultFilename();
        fs.deleteFile(resultFilename);            
        fs.deleteSMSFile(SMSUtils.getSMSFileNameFromXMLFileName(resultFilename));
        AppMIDlet.getInstance().setResultList(new ResultList());
        AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getResultList());
    }

    public void commandAction(Command c, Displayable d) {
        if (c == Resources.CMD_BACK) {
            AppMIDlet.getInstance().getFileStores().resetQuestions();
            AppMIDlet.getInstance().setResultList(new ResultList());
            AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getResultList());
        }
        else if(c == Resources.CMD_DELETE){
            confirmDeleteFiles();
        }
        else if(c == Resources.CMD_OPEN_RESULT){
            resultList.openResult();
        }
        else if(c == Resources.CMD_SEND){
            SubmitResultRunnable srr = new SubmitResultRunnable(resultList.getResultFilename());
            SubmitServer submitServer = new SubmitServer();
            StatusScreen statusScreen = new StatusScreen(submitServer);
            srr.submitServer = submitServer;
            srr.statusScreen = statusScreen;
            AppMIDlet.getInstance().setDisplayable(statusScreen);
            Thread t = new Thread(srr);
            t.start();
        }
    }
 
    public StringItem getQuestsFormat(NDGQuestion _question) {
        StringItem si = new StringItem("", _question.getDescription());
        si.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL));
        return si;
    }
 
    public StringItem getAnswerFormat(NDGQuestion _question) {
        StringItem si = new StringItem("", "");
 
        if (_question.getType().equals("_str")) {
            si = new StringItem("", ((TypeTextFieldString) _question).getWidget().getString());
        } 
        else if (_question.getType().equals("_int")) {
            si = new StringItem("", ((TypeTextFieldInteger) _question).getWidget().getString());
        } 
        else if (_question.getType().equals("_decimal")) {
            si = new StringItem("", ((TypeTextFieldDecimal) _question).getWidget().getString());
        } 
        else if (_question.getType().equals("_date")) {
            si = new StringItem("", ((TypeDate) _question).getWidget().getDate().toString());
        }
        else if (_question.getType().equals("_time")) {
            TypeTime typeTime = ((TypeTime) _question);      
            
            si = new StringItem("", typeTime.getWidget().getTime().toString());            
          
        }
        else if (_question.getType().equals("_choice")) {
            si = new StringItem("", ((TypeChoice) _question).getWidget().getSelectedValue());
        }
 
        si.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD | Font.STYLE_ITALIC, Font.SIZE_SMALL));
       
        return si;
    }
}

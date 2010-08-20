package br.org.indt.ndg.mobile;

import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.MIDletStateChangeException;

import br.org.indt.ndg.mobile.error.WaitingForm;
import br.org.indt.ndg.mobile.settings.GpsForm;
import br.org.indt.ndg.mobile.download.CheckNewSurveyList;
import br.org.indt.ndg.mobile.download.DownloadNewSurveys;
import br.org.indt.ndg.mobile.submit.TestConnection;
import br.org.indt.ndg.mobile.xmlhandle.QuestionnaireValidator;

public class SurveyList extends List implements CommandListener {
    
    FileSystem fs;
    
    public SurveyList() {
        super(Resources.SURVEY_LIST_TITLE, Choice.IMPLICIT);

        this.setFitPolicy(this.TEXT_WRAP_ON);
        
        fs = AppMIDlet.getInstance().getFileSystem();
        
        Vector surveyNames = fs.SurveyNames();
        
        Enumeration e = surveyNames.elements();
        
        while (e.hasMoreElements()) {
            this.append((String) e.nextElement(), null);
        }
        
        if (this.size() > 0)
            addCommand(Resources.CMD_OPEN_SURVEY);        
        addCommand(Resources.CMD_GPS);
        if (this.size() > 0)
            addCommand(Resources.CMD_DELETE_SURVEY);
        //else
          //  this.append(Resources.THERE_ARE_NO_SURVEYS + "!", null);
        addCommand(Resources.CMD_CHECK_NEW_SURVEYS);
        addCommand(Resources.CMD_CHECK_UPDATE);
        addCommand(Resources.CMD_TEST_CONN);
        addCommand(Resources.CMD_EXIT_OPTIONS);
        addCommand(Resources.CMD_EXIT);
        
        setCommandListener(this);
    }
    
    class OpenSurveyRunnable implements Runnable {

        public void run() {

            try { Thread.sleep(200); } catch (InterruptedException ex) { ex.printStackTrace(); }
            
            System.out.println("Name: "+Resources.ROOT_DIR + AppMIDlet.getInstance().getFileSystem().getSurveyDirName() + Resources.SURVEY_NAME);
            //QuestionnaireValidator qv = new QuestionnaireValidator();
            //boolean isValidSurvey = qv.isValid(Resources.ROOT_DIR + AppMIDlet.getInstance().getFileSystem().getSurveyDirName() + Resources.SURVEY_NAME);

            boolean isValidSurvey = true;
            try {
                if (isValidSurvey) {
                    AppMIDlet.getInstance().getFileStores().parseSurveyFile();
                    if (AppMIDlet.getInstance().getFileStores().getErrorkParser()){
                        AppMIDlet.getInstance().getGeneralAlert().showErrorExit(Resources.EPARSE_SURVEY);
                    }
                    else {
                        fs.loadResultFiles();
                        if (!fs.getError()) {
                            fs.setResultListIndex(0);
                            AppMIDlet.getInstance().setResultList(new ResultList());
                            AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getResultList());
                        } else {
                            fs.setError(false);
                            AppMIDlet.getInstance().getGeneralAlert().showErrorExit(Resources.EPARSE_RESULT);
                        }
                    }
                }
                else {
                    AppMIDlet.getInstance().getGeneralAlert().showErrorOk(Resources.EINVALID_SURVEY);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void handleConfirmResult(Command _cmd) {
        if (_cmd == Resources.CMD_YES) {
            deleteSurvey();
        }

        AppMIDlet.getInstance().setDisplayable(this);
    }
    
    private void confirmDeleteSurvey() {
        AppMIDlet.getInstance().getGeneralAlert().showConfirmAlert(Resources.DELETE_CONFIRMATION, Resources.DELETE_SURVEY_CONFIRMATION, "SurveyList");
    }
    
    private void deleteSurvey() {
        String dirName = AppMIDlet.getInstance().getFileSystem().getSurveyDirName();
        fs.deleteSurveyDir(dirName);
        this.delete(this.getSelectedIndex());
        
        if (this.size() <= 0)
            removeCommand(Resources.CMD_OPEN_SURVEY);
        addCommand(Resources.CMD_GPS);
        if (this.size() <= 0)
            removeCommand(Resources.CMD_DELETE_SURVEY);
    }
    
    private void checkForUpdates() {
        UpdateClientApp.getInstance().CheckForUpdate();
    }
    
    public void commandAction(Command c, Displayable d) {
        fs.setSurveyCurrentIndex(getSelectedIndex());
        
        if (c == Resources.CMD_EXIT || c == Resources.CMD_EXIT_OPTIONS) {
            exitApp();
        } else if (c == SELECT_COMMAND || c == Resources.CMD_OPEN_SURVEY) {
            AppMIDlet.getInstance().setDisplayable(new WaitingForm(Resources.LOADING_SURVEYS));
            OpenSurveyRunnable osr = new OpenSurveyRunnable();
            Thread t = new Thread(osr);  //create new thread to compensate for waitingform
            t.setPriority(Thread.MIN_PRIORITY);
            t.start();
        } else if (c == Resources.CMD_GPS) {
            AppMIDlet.getInstance().setDisplayable(new GpsForm());
        } else if (c == Resources.CMD_CHECK_NEW_SURVEYS) {
            DownloadNewSurveys.getInstance().check();
        }
        else if (c == Resources.CMD_DELETE_SURVEY) {
            confirmDeleteSurvey();
        }
        else if (c == Resources.CMD_CHECK_UPDATE) {
            checkForUpdates();
        }
        else if (c == Resources.CMD_TEST_CONN) {
            TestConnection.getInstance().doTest();
        }
    }

    private void exitApp() {
        try {
            AppMIDlet.getInstance().destroyApp(true);
        } catch (MIDletStateChangeException e) {
            AppMIDlet.getInstance().getGeneralAlert().showErrorExit(Resources.ELEAVE_MIDLET);
        }
    }
    
}

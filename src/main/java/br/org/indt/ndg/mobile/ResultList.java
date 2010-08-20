package br.org.indt.ndg.mobile;

import java.util.Date;
import java.util.Vector;
import java.util.Enumeration;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import br.org.indt.ndg.mobile.error.WaitingForm;
import javax.microedition.midlet.MIDletStateChangeException;

public class ResultList extends List implements CommandListener {
    
    private FileSystem fs;
    private Vector xmlResultFile;
    final ResultList list;
    
    public ResultList() {
        super(Resources.RESULTS_LIST_TITLE, List.IMPLICIT);
        list = this;
        fs = AppMIDlet.getInstance().getFileSystem();
        xmlResultFile = fs.getXmlResultFile();
        
        Enumeration e = xmlResultFile.elements();
        while (e.hasMoreElements()) {
            this.append(((XmlResultFile) e.nextElement()).getDisplayName(), null);
        }
        
        addCommand(Resources.CMD_BACK);
        addCommand(Resources.CMD_NEW_RESULT);
        
        //only display when result list is not empty
        if (xmlResultFile.size() > 0) {
            addCommand(Resources.CMD_OPEN_RESULT);
            addCommand(Resources.CMD_VIEW);
            addCommand(Resources.CMD_SEND);
            addCommand(Resources.CMD_DELETE);
        }

        addCommand(Resources.CMD_VIEWSENT);
        addCommand(Resources.CMD_EXIT_OPTIONS);
        
        setCommandListener(this);
    }
    
    void openResult(){
        AppMIDlet.getInstance().setDisplayable(new WaitingForm(Resources.CMD_OPEN_RESULT.getLabel()));
        OpenResultRunnable orr = new OpenResultRunnable();
        Thread t = new Thread(orr);  //create new thread to compensate for waitingform
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
    }

    void viewResult(){
        AppMIDlet.getInstance().setDisplayable(new WaitingForm(Resources.CMD_VIEW.getLabel()));
        ViewResultRunnable vrr = new ViewResultRunnable();
        Thread t = new Thread(vrr);  //create new thread to compensate for waitingform
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
    }

    void newResult(){
        AppMIDlet.getInstance().setDisplayable(new WaitingForm(Resources.CMD_NEW_RESULT.getLabel()));
        NewResultRunnable orr = new NewResultRunnable();
        Thread t = new Thread(orr);  //create new thread to compensate for waitingform
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
    }
    
    String getResultFilename(){
        return fs.getResultFilename();
    }
    
    class OpenResultRunnable implements Runnable {
        public void run() {
            try { Thread.sleep(200); } catch(Exception e){}
            if (fs.getResultFilename() != null) {
                fs.setLocalFile(true);
                AppMIDlet.getInstance().getFileStores().parseResultFile();                
                if (AppMIDlet.getInstance().getFileStores().getError()) {                    
                    AppMIDlet.getInstance().getGeneralAlert().showErrorExit(Resources.EPARSE_RESULT);
                } else {
                    AppMIDlet.getInstance().setTimeTracker((new Date()).getTime());  //to keep track of time increment used to create new survey
                    AppMIDlet.getInstance().getFileStores().loadAnswers();
                    AppMIDlet.getInstance().setCategoryList(new CategoryList());
                    AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getCategoryList());
                }
            } 
        }
    }
    
    class ViewResultRunnable implements Runnable {
        public void run() {
            try { Thread.sleep(200); } catch(Exception e){}
            if (fs.getResultFilename() != null) {
                AppMIDlet.getInstance().getFileStores().parseResultFile();
                if (AppMIDlet.getInstance().getFileStores().getError()) {
                    AppMIDlet.getInstance().getGeneralAlert().showErrorExit(Resources.EPARSE_RESULT);
                } else {
                    AppMIDlet.getInstance().getFileStores().loadAnswers();
                    AppMIDlet.getInstance().setResultView(new ResultView(list));
                    AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getResultView());
                }
            }
        }
    }

    class NewResultRunnable implements Runnable {
        public void run() {
            try { Thread.sleep(200); } catch(Exception e){}
            AppMIDlet.getInstance().getFileStores().resetQuestions();
            AppMIDlet.getInstance().getFileStores().clearAnswers();
            AppMIDlet.getInstance().setTimeTracker((new Date()).getTime());  //to keep track of time used to create new survey
            fs.setLocalFile(false);
            AppMIDlet.getInstance().setCategoryList(new CategoryList());
            AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getCategoryList());
        }
    }
    
    public void commandAction(Command c, Displayable d) {
        fs.setResultCurrentIndex(getSelectedIndex());
        
        if (c == Resources.CMD_BACK) {
            AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getSurveyList());
        } else if (c == Resources.CMD_SEND) {
            AppMIDlet.getInstance().setDisplayable(new SubmitList());            
        } else if (c == SELECT_COMMAND || c == Resources.CMD_OPEN_RESULT) {
            openResult();
        } else if (c == Resources.CMD_NEW_RESULT) {
            newResult();
        } else if (c == Resources.CMD_VIEW) {
            viewResult();
        } else if (c == Resources.CMD_VIEWSENT) {
            fs.loadSentFiles();
            AppMIDlet.getInstance().setDisplayable(new SentList());
        } else if (c == Resources.CMD_DELETE) {
            AppMIDlet.getInstance().setDisplayable(new DeleteList());
        } else if (c == Resources.CMD_SELECT) {
            AppMIDlet.getInstance().setDisplayable(new SelectList());
        }
        else if(c == Resources.CMD_EXIT_OPTIONS){
            try {
                AppMIDlet.getInstance().destroyApp(true);
            } catch (MIDletStateChangeException ex) {
                AppMIDlet.getInstance().getGeneralAlert().showErrorExit(Resources.ELEAVE_MIDLET);
            }
        }
        
    }
    
}

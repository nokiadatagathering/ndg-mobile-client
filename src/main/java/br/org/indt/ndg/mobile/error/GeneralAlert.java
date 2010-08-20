
package br.org.indt.ndg.mobile.error;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import br.org.indt.ndg.lwuit.control.Display;
//import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDletStateChangeException;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.SurveyList;
import br.org.indt.ndg.mobile.DeleteList;
import br.org.indt.ndg.mobile.FileSystem;
import br.org.indt.ndg.mobile.ResultView;
import br.org.indt.ndg.mobile.UpdateClientApp;
import java.util.Vector;

public class GeneralAlert extends Alert implements CommandListener {
    
    private Display display;
    private Displayable current;    
    private String className = "";
    private Vector cmds = new Vector();
    private boolean isSurveyListVisible = false;
    
    public GeneralAlert(Display _display) {
        super("Alert!");
        
        display = _display;
        
        this.setType(AlertType.ERROR);
        this.setTimeout(Alert.FOREVER);
        
        removeCommands();
        
        setCommandListener(this);
    }
    
    private void removeCommands(){
        removeCommand(Resources.CMD_EXIT);
        removeCommand(Resources.CMD_OK);
        removeCommand(Resources.CMD_YES);
        removeCommand(Resources.CMD_NO);
        cmds.removeAllElements();
    }
    
    public void showErrorExit(String _error) {
        removeCommands();
        this.addCommand(Resources.CMD_EXIT);
        this.setString(_error);
        this.setTitle(Resources.ERROR_TITLE);
        this.setType(AlertType.ERROR);
        display.setCurrent(this);
    }
    
    public void showErrorOk(String _error) {
        removeCommands();
        this.addCommand(Resources.CMD_OK);
        this.setString(_error);
        this.setTitle(Resources.ERROR_TITLE);
        this.setType(AlertType.ERROR);
        display.setCurrent(this);
    }
    
    public void showError(Exception e) {
        removeCommands();
        this.addCommand(Resources.CMD_EXIT);
        this.setTitle(Resources.ERROR_TITLE);
        this.setType(AlertType.ERROR);
        String message = (e.getMessage() == null) ? e.toString() : e.getMessage();
        this.setString(message);
        current = display.getCurrent();
        display.setCurrent(this);
    }
    
    public void showAlert(String alertTitle, String alertMessage) {
        removeCommands();
        this.setTitle(alertTitle);
        this.setString(alertMessage);
        this.setTimeout(Alert.FOREVER);
        this.setType(AlertType.ALARM);
        current = display.getCurrent();
        display.setCurrent(this);
    }

    public void showAlert(String alertTitle, String alertMessage, String _className) {
        if (this.isShown()) {
            className = "";
            this.commandAction(DISMISS_COMMAND, null);
        }

        removeCommands();
        this.setTitle(alertTitle);
        this.setString(alertMessage);
        this.setTimeout(Alert.FOREVER);
        this.setType(AlertType.ALARM);
        className = _className;
        current = display.getCurrent();
        display.setCurrent(this);
    }

    public void showAlert(String alertTitle, String alertMessage, String _className, AlertType iconType) {
        if (this.isShown()) {
            className = "";
            this.commandAction(DISMISS_COMMAND, null);
        }

        removeCommands();
        this.setTitle(alertTitle);
        this.setString(alertMessage);
        this.setTimeout(Alert.FOREVER);
        this.setType(iconType);
        className = _className;
        current = display.getCurrent();
        display.setCurrent(this);
    }
    
    public void showError(String alertTitle, String alertMessage, Displayable nextDisplayable) {
        removeCommands();
        this.setTitle(alertTitle);
        this.setString(alertMessage);
        this.setTimeout(Alert.FOREVER);
        this.setType(AlertType.ERROR);
        current = nextDisplayable;
        display.setCurrent(this);        
    }

    public void showConfirmAlert(String alertTitle, String alertMessage, String _className) {
        removeCommands();
        this.setTitle(alertTitle);
        this.setString(alertMessage);
        this.setTimeout(Alert.FOREVER);
        this.setType(AlertType.CONFIRMATION);
        this.addCommand(Resources.CMD_YES);
        this.addCommand(Resources.CMD_NO);
        className = _className;
        current = display.getCurrent();
        display.setCurrent(this);
    }

    public void addCommand(Command cmd) {
        super.addCommand(cmd);
        cmds.addElement(cmd);
    }

    public Command[] getCommands() {
        Command[] cmdArray = null;
        int cmdsTotal = cmds.size();
        if (!cmds.isEmpty()) {
            cmdArray = new Command[cmdsTotal];
            for (int i=0; i<cmdsTotal; i++) {
                cmdArray[i] = (Command)cmds.elementAt(i);
            }
        }
        return cmdArray;
    }
    
    public void commandAction(Command c, Displayable d) {
        if (c == Resources.CMD_EXIT) {
            try {
                AppMIDlet.getInstance().destroyApp(true);
            } catch (MIDletStateChangeException e) {
                e.printStackTrace();
            }
        } else if (c == Resources.CMD_OK) {
            AppMIDlet.getInstance().setSurveyList(new SurveyList());
            display.setCurrent(AppMIDlet.getInstance().getSurveyList());
        } 
        else if ( (c == Resources.CMD_YES) || (c == Resources.CMD_NO) ) {
            if (className.equals("DeleteList")) {
                ((DeleteList) current).handleConfirmResult(c);
            }
            else if (className.equals("SurveyList")) {
                ((SurveyList) current).handleConfirmResult(c);
            }
            else if (className.equals("ResultView")) {
                ((ResultView) current).handleConfirmResult(c);
            }
            else if (className.equals("UpdateClientApp")) {
                ((UpdateClientApp) current).handleConfirmResult(c);
            }
        }
        else if (c == null) {
            if (className.equals("SurveyList")) {
                display.setCurrent(AppMIDlet.getInstance().getSurveyList());
                className = "";
            }
        }
        else if (c == DISMISS_COMMAND) {
            if (className.indexOf("SMSReceiver") > -1) {
                if (isSurveyListVisible) {
                    AppMIDlet.getInstance().setFileSystem(new FileSystem(Resources.ROOT_DIR));
                    AppMIDlet.getInstance().setSurveyList(new SurveyList());
                    current = AppMIDlet.getInstance().getSurveyList();
                    isSurveyListVisible = false;
                }
            }
            if (className.indexOf("DownloadNewSurvey") > -1) {
                current = AppMIDlet.getInstance().getSurveyList();
            }
            display.setCurrent(current);
        }
//        else if (c == Resources.CMD_BACK) {
//            display.setCurrent(current);
//        }
    }

    public void setSurveyListVisible(boolean _val) {
        isSurveyListVisible = _val;
    }

}

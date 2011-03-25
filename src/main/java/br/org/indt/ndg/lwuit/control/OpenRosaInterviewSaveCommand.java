package br.org.indt.ndg.lwuit.control;

import com.nokia.mid.appl.cmd.Local;
import com.nokia.xfolite.xforms.dom.XFormsDocument;
import com.sun.lwuit.Command;

/**
 *
 * @author damian.janicki
 */
public class OpenRosaInterviewSaveCommand extends CommandControl {

    private static OpenRosaInterviewSaveCommand instance = null;
    private SaveResultsObserver saveObserver = null;

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CMD_SAVE));
    }

    public void setObserver(SaveResultsObserver observer){
        saveObserver = observer;
    }

    protected void doAction(Object parameter) {
        XFormsDocument doc = (XFormsDocument)parameter;
        PersistenceManager.getInstance().saveXForm(doc, saveObserver);
    }

    public static OpenRosaInterviewSaveCommand getInstance(){
        if(instance == null){
            instance = new OpenRosaInterviewSaveCommand();
        }
        return instance;
    }
}

package br.org.indt.ndg.lwuit.control;

import com.nokia.mid.appl.cmd.Local;
import com.nokia.xfolite.xforms.dom.XFormsDocument;
import com.nokia.xfolite.xforms.submission.XFormsXMLSerializer;
import com.sun.lwuit.Command;

/**
 *
 * @author damian.janicki
 */
public class XfoliteInterviewSaveCommand extends CommandControl {

    private static XfoliteInterviewSaveCommand instance = null;

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CMD_SAVE));
    }

    protected void doAction(Object parameter) {
//        XFormsDocument doc = (XFormsDocument)parameter;
//        PersistenceManager.getInstance().saveXForm(doc);
    }

    public static XfoliteInterviewSaveCommand getInstance(){
        if(instance == null){
            instance = new XfoliteInterviewSaveCommand();
        }
        return instance;
    }
}

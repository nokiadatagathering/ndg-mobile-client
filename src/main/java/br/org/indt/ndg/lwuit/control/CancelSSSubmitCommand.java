/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import com.sun.lwuit.Command;
import br.org.indt.ndg.mobile.Resources;
import com.nokia.mid.appl.cmd.Local;

public class CancelSSSubmitCommand extends BackCommand {

    private static CancelSSSubmitCommand instance;

    public static CancelSSSubmitCommand getInstance() {
        if (instance == null)
            instance = new CancelSSSubmitCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CMD_CANCEL));
    }

    protected void doAction(Object parameter) {
        SurveysControl.getInstance().getCurrentOldStatusScreenSubmit().commandAction(Resources.CMD_CANCEL, null);
    }
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.nokia.mid.appl.cmd.Local;
import com.sun.lwuit.Command;

/**
 *
 * @author mluz
 */
public class UpdateCommand extends CommandControl {

    private static UpdateCommand instance;

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CHECK_FOR_UPDATE));
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().getSurveyList().commandAction(Resources.CMD_CHECK_UPDATE, null);
    }

    public static UpdateCommand getInstance() {
        if (instance == null)
            instance = new UpdateCommand();
        return instance;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.SurveyList;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.UpdateClientApp;
import com.sun.lwuit.Command;
import com.nokia.mid.appl.cmd.Local;

public class CancelUpdateClientAppCommand extends BackCommand {

    private static CancelUpdateClientAppCommand instance;

    public static CancelUpdateClientAppCommand getInstance() {
        if (instance == null)
            instance = new CancelUpdateClientAppCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CMD_CANCEL));
    }

    protected void doAction(Object parameter) {
        UpdateClientApp.getInstance().Cancel();
        AppMIDlet.getInstance().setDisplayable(SurveyList.class);
    }
}

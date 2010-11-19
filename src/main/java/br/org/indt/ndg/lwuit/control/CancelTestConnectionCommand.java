/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.SurveyList;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.submit.TestConnection;
import com.sun.lwuit.Command;
import com.nokia.mid.appl.cmd.Local;

public class CancelTestConnectionCommand extends BackCommand {

    private static CancelTestConnectionCommand instance;

    public static CancelTestConnectionCommand getInstance() {
        if (instance == null)
            instance = new CancelTestConnectionCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CMD_CANCEL));
    }

    protected void doAction(Object parameter) {
        TestConnection.getInstance().cancel();
        AppMIDlet.getInstance().setDisplayable(SurveyList.class);
    }

}

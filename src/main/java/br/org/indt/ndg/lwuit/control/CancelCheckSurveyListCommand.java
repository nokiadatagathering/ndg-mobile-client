/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.SurveyList;
import br.org.indt.ndg.mobile.AppMIDlet;
import com.sun.lwuit.Command;
import com.nokia.mid.appl.cmd.Local;

public class CancelCheckSurveyListCommand extends BackCommand {

    private static CancelCheckSurveyListCommand instance;

    public static CancelCheckSurveyListCommand getInstance() {
        if (instance == null)
            instance = new CancelCheckSurveyListCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CMD_CANCEL));
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().setDisplayable(SurveyList.class);
    }

}
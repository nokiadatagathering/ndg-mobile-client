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
public class OpenSurveyCommand extends CommandControl {

    //private boolean inProcess = false;

    private static OpenSurveyCommand instance;

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CMD_OPEN));
    }

    public static OpenSurveyCommand getInstance() {
        if (instance == null)
            instance = new OpenSurveyCommand();
        return instance;
    }

    protected void doAction(Object parameter) {
        int selectedIndex = ((Integer)parameter).intValue();
        AppMIDlet.getInstance().getSurveyList().setSelectedIndex(selectedIndex, true);
        AppMIDlet.getInstance().getSurveyList().commandAction(Resources.CMD_OPEN_SURVEY, null);
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

import br.org.indt.ndg.mobile.SurveyList;

/**
 *
 * @author mluz
 */
public class DeleteSurveyCommand extends CommandControl {

    private static DeleteSurveyCommand instance;

    protected Command createCommand() {
        return new Command(Resources.DELETE_SURVEY);
    }

    protected void doAction(Object parameter) {
        int selectedIndex = ((Integer)parameter).intValue();
        AppMIDlet.getInstance().getSurveyList().setSelectedIndex(selectedIndex, true);
        AppMIDlet.getInstance().getSurveyList().commandAction(Resources.CMD_DELETE_SURVEY, null);
    }

    public static DeleteSurveyCommand getInstance() {
        if (instance == null)
            instance = new DeleteSurveyCommand();
        return instance;
    }
}


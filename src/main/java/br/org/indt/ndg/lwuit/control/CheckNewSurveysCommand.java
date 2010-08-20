/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.download.DownloadNewSurveys;
import com.sun.lwuit.Command;

/**
 *
 * @author mluz
 */
public class CheckNewSurveysCommand extends CommandControl {

    private static CheckNewSurveysCommand instance;

    protected Command createCommand() {
        return new Command(Resources.CHECK_NEW_SURVEYS);
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().getSurveyList().commandAction(Resources.CMD_CHECK_NEW_SURVEYS, null);
    }

    public static CheckNewSurveysCommand getInstance() {
        if (instance == null)
            instance = new CheckNewSurveysCommand();
        return instance;
    }
}

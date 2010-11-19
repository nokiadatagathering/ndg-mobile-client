/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import com.sun.lwuit.Command;
import br.org.indt.ndg.mobile.download.DownloadNewSurveys;
import com.nokia.mid.appl.cmd.Local;

public class DownloadCheckSurveyListCommand extends CommandControl {

    private static DownloadCheckSurveyListCommand instance;

    public static DownloadCheckSurveyListCommand getInstance() {
        if (instance == null)
            instance = new DownloadCheckSurveyListCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CMD_DOWNLOAD));
    }

    protected void doAction(Object parameter) {
        DownloadNewSurveys.getInstance().download();
    }
}

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;
import br.org.indt.ndg.mobile.download.DownloadNewSurveys;

public class DownloadCheckSurveyListCommand extends CommandControl {

    private static DownloadCheckSurveyListCommand instance;

    public static DownloadCheckSurveyListCommand getInstance() {
        if (instance == null)
            instance = new DownloadCheckSurveyListCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command( Resources.DOWNLOAD );
    }

    protected void doAction(Object parameter) {
        DownloadNewSurveys.getInstance().download();
    }
}

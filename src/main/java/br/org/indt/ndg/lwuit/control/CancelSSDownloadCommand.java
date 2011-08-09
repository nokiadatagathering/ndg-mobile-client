package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;
import br.org.indt.ndg.mobile.download.DownloadNewSurveys;

public class CancelSSDownloadCommand extends BackCommand {

    private static CancelSSDownloadCommand instance;

    public static CancelSSDownloadCommand getInstance() {
        if (instance == null)
            instance = new CancelSSDownloadCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command(Resources.NEWUI_CANCEL);
    }

    protected void doAction(Object parameter) {
        DownloadNewSurveys.getInstance().cancelAndKillOperation();
    }

}

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.SurveyList;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.UpdateClientApp;
import com.sun.lwuit.Command;

public class CancelUpdateClientAppCommand extends BackCommand {

    private static CancelUpdateClientAppCommand instance;

    public static CancelUpdateClientAppCommand getInstance() {
        if (instance == null)
            instance = new CancelUpdateClientAppCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command( Resources.NEWUI_CANCEL );
    }

    protected void doAction(Object parameter) {
        UpdateClientApp.getInstance().Cancel();
        AppMIDlet.getInstance().setDisplayable(SurveyList.class);
    }
}

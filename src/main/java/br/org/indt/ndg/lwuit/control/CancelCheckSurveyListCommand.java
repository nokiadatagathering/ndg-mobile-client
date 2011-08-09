package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.SurveyList;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

public class CancelCheckSurveyListCommand extends BackCommand {

    private static CancelCheckSurveyListCommand instance;

    public static CancelCheckSurveyListCommand getInstance() {
        if (instance == null)
            instance = new CancelCheckSurveyListCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command(Resources.NEWUI_CANCEL);
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().setDisplayable(SurveyList.class);
    }

}
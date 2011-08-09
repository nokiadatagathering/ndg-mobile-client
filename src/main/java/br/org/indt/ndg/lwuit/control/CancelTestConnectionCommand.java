package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.SurveyList;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.submit.TestConnection;
import com.sun.lwuit.Command;

public class CancelTestConnectionCommand extends BackCommand {

    private static CancelTestConnectionCommand instance;

    public static CancelTestConnectionCommand getInstance() {
        if (instance == null)
            instance = new CancelTestConnectionCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command( Resources.NEWUI_CANCEL );
    }

    protected void doAction(Object parameter) {
        TestConnection.getInstance().cancel();
        AppMIDlet.getInstance().setDisplayable(SurveyList.class);
    }

}

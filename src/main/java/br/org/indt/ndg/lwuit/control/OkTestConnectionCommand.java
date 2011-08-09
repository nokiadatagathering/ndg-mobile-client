package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.SurveyList;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.submit.TestConnection;
import com.sun.lwuit.Command;

public class OkTestConnectionCommand extends BackCommand {

    private static OkTestConnectionCommand instance;

    public static OkTestConnectionCommand getInstance() {
        if (instance == null)
            instance = new OkTestConnectionCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command( Resources.NEW_IU_OK );
    }

    protected void doAction(Object parameter) {
        TestConnection.getInstance().UserConfirmation();
        AppMIDlet.getInstance().setDisplayable(SurveyList.class);
    }

}

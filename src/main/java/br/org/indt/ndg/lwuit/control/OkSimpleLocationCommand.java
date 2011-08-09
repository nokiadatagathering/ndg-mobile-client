package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.SettingsForm;
import com.sun.lwuit.Command;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;

public class OkSimpleLocationCommand extends BackCommand {

    private static OkSimpleLocationCommand instance;

    public static OkSimpleLocationCommand getInstance() {
        if (instance == null)
            instance = new OkSimpleLocationCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command( Resources.NEW_IU_OK );
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().setDisplayable(SettingsForm.class);
    }

}

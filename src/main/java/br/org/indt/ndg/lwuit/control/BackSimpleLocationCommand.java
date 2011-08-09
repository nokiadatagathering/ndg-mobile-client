package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.GpsForm;
import com.sun.lwuit.Command;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;

public class BackSimpleLocationCommand extends BackCommand{

    private static BackSimpleLocationCommand instance;

    public static BackSimpleLocationCommand getInstance() {
        if (instance == null)
            instance = new BackSimpleLocationCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command( Resources.NEWUI_BACK );
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().setDisplayable(GpsForm.class);
    }
}

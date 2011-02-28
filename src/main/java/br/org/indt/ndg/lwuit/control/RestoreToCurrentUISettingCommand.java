package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.UISettings;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;


public class RestoreToCurrentUISettingCommand extends CommandControl {

    private static RestoreToCurrentUISettingCommand instance;

    public static RestoreToCurrentUISettingCommand getInstance() {
        if (instance == null)
            instance = new RestoreToCurrentUISettingCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command( Resources.RELOAD );
    }

    protected void doAction(Object parameter) {
        NDGStyleToolbox.getInstance().loadSettings();
        AppMIDlet.getInstance().setDisplayable(UISettings.class);
    }
}

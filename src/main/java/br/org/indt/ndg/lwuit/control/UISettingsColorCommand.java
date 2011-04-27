package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.UISettings;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;



public class UISettingsColorCommand extends CommandControl {

    private static  UISettingsColorCommand instance;

    protected Command createCommand() {
        return new Command( Resources.CMD_UI_COLORS );
    }

    public static UISettingsColorCommand getInstance() {
        if (instance == null)
            instance = new  UISettingsColorCommand();
        return instance;
    }


    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().setDisplayable(UISettings.class);
    }
}

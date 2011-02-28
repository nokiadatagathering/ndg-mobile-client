package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.UISettings;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;



public class AdvancedUISettingsCommand extends CommandControl {

    private static  AdvancedUISettingsCommand instance;

    protected Command createCommand() {
        return new Command( "Customize" );//Resources.UI_SETTINGS
    }

    public static AdvancedUISettingsCommand getInstance() {
        if (instance == null)
            instance = new  AdvancedUISettingsCommand();
        return instance;
    }


    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().setDisplayable(UISettings.class);
    }
}

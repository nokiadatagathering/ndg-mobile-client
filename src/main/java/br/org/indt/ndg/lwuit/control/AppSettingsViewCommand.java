package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.ApplicationSettings;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

public class AppSettingsViewCommand extends CommandControl {

    private static AppSettingsViewCommand instance;

    protected Command createCommand() {
        return new Command( Resources.APPLICATION_SETTINGS );
    }

    public static AppSettingsViewCommand getInstance() {
        if (instance == null)
            instance = new AppSettingsViewCommand();
        return instance;
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().setDisplayable(ApplicationSettings.class);
    }
}

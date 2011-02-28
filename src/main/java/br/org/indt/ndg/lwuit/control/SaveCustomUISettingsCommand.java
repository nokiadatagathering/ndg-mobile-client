package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.SelectStyleView;
import br.org.indt.ndg.lwuit.ui.style.PreviewStyleContainer;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;


public class SaveCustomUISettingsCommand extends CommandControl {


    private static SaveCustomUISettingsCommand instance;

    public static SaveCustomUISettingsCommand getInstance() {
        if (instance == null)
            instance = new SaveCustomUISettingsCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command( Resources.JUST_SAVE );
    }

    protected void doAction(Object param) {
        ((PreviewStyleContainer)param).applySettings();
        NDGStyleToolbox.getInstance().saveSettings();
        AppMIDlet.getInstance().initLWUIT();
        AppMIDlet.getInstance().setDisplayable( SelectStyleView.class);
    }
}

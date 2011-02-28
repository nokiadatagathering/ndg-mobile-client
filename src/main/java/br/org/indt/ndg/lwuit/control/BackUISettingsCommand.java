package br.org.indt.ndg.lwuit.control;


import br.org.indt.ndg.lwuit.ui.SelectStyleView;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

public class BackUISettingsCommand extends BackCommand{
    private static BackUISettingsCommand instance;

    public static BackUISettingsCommand getInstance() {
        if (instance == null)
            instance = new BackUISettingsCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command( Resources.NEWUI_BACK );
    }

    protected void doAction(Object parameter) {
         AppMIDlet.getInstance().setDisplayable( SelectStyleView.class );
    }
}

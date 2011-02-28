package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.SelectStyleView;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

public class SelectStyleViewCommand extends CommandControl {

    private static SelectStyleViewCommand instance;

    protected Command createCommand() {
        return new Command( Resources.UI_PREFERENCES );
    }

    public static SelectStyleViewCommand getInstance() {
        if (instance == null)
            instance = new SelectStyleViewCommand();
        return instance;
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().setDisplayable(SelectStyleView.class);
    }
}

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.ResolutionSelectForm;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

public class ResolutionSelectionViewCommand extends CommandControl {

    private static ResolutionSelectionViewCommand instance;

    protected Command createCommand() {
        return new Command(Resources.PHOTO_RESOLUTION);
    }

    public static ResolutionSelectionViewCommand getInstance() {
        if (instance == null)
            instance = new ResolutionSelectionViewCommand();
        return instance;
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().setDisplayable(ResolutionSelectForm.class);
    }
}

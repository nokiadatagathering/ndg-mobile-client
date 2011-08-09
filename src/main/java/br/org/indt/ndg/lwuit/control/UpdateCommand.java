package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.UpdateClientApp;
import com.sun.lwuit.Command;


/**
 *
 * @author mluz
 */
public class UpdateCommand extends CommandControl {

    private static UpdateCommand instance;

    protected Command createCommand() {
        return new Command( Resources.CHECK_FOR_UPDATE );
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().setDisplayable( br.org.indt.ndg.lwuit.ui.UpdateClientApp.class );
        UpdateClientApp.getInstance().CheckForUpdate();
    }

    public static UpdateCommand getInstance() {
        if (instance == null)
            instance = new UpdateCommand();
        return instance;
    }
}

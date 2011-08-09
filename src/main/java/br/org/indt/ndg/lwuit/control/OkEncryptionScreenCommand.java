package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

/**
 *
 * @author roda
 */
public class OkEncryptionScreenCommand extends BackCommand {

    private static OkEncryptionScreenCommand instance;

    public static OkEncryptionScreenCommand getInstance() {
        if (instance == null)
            instance = new OkEncryptionScreenCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command(Resources.NEW_IU_OK);
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().continueAppLoading();
    }
}
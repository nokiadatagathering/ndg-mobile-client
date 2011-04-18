package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.WaitingScreen;
import br.org.indt.ndg.mobile.AppMIDlet;
import com.sun.lwuit.Command;
import com.nokia.mid.appl.cmd.Local;

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
        return new Command(Local.getText(Local.QTJ_CMD_OK));
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().continueAppLoading();
    }
}
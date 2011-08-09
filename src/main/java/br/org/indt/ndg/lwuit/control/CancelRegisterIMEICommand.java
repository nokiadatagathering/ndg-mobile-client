package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.AppMIDlet;
import com.sun.lwuit.Command;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 *
 * @author kgomes
 */
public class CancelRegisterIMEICommand extends BackCommand{
    private static CancelRegisterIMEICommand instance;

    public static CancelRegisterIMEICommand getInstance() {
        if (instance == null)
            instance = new CancelRegisterIMEICommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command(Resources.NEWUI_CANCEL);
    }

    protected void doAction(Object parameter) {
         try {
            AppMIDlet.getInstance().destroyApp(true);
        } catch (MIDletStateChangeException e) {
            GeneralAlert.getInstance().addCommand( ExitCommand.getInstance());
            GeneralAlert.getInstance().show(Resources.ERROR_TITLE, Resources.ELEAVE_MIDLET, GeneralAlert.ERROR );
        }
    }

}

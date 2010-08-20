package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.AppMIDlet;
import com.nokia.mid.appl.cmd.Local;
import com.sun.lwuit.Command;
import javax.microedition.midlet.MIDletStateChangeException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
        return new Command(Local.getText(Local.QTJ_CMD_CANCEL));
    }

    protected void doAction(Object parameter) {
         try {
            AppMIDlet.getInstance().destroyApp(true);
        } catch (MIDletStateChangeException e) {
            AppMIDlet.getInstance().getGeneralAlert().showErrorExit(Resources.ELEAVE_MIDLET);
        }
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.nokia.mid.appl.cmd.Local;
import com.sun.lwuit.Command;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 *
 * @author mluz
 */
public class ExitCommand extends CommandControl {

    private static ExitCommand instance;

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CMD_EXIT));
    }

    public static ExitCommand getInstance() {
        if (instance == null)
            instance = new ExitCommand();
        return instance;
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

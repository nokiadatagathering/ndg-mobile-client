/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.nokia.mid.appl.cmd.Local;
import com.sun.lwuit.Command;

/**
 *
 * @author mluz
 */
public class SaveResultCommand extends CommandControl {

    private static SaveResultCommand instance;

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CMD_SAVE));
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().getCategoryList().commandAction(Resources.CMD_SAVE, null);
    }

    public static SaveResultCommand getInstance() {
        if (instance == null)
            instance = new SaveResultCommand();
        return instance;
    }

}


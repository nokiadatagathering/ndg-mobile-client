/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.Resources;
import com.nokia.mid.appl.cmd.Local;
import com.sun.lwuit.Command;
import br.org.indt.ndg.mobile.AppMIDlet;

public class BackSimpleLocationCommand extends BackCommand{

    private static BackSimpleLocationCommand instance;

    public static BackSimpleLocationCommand getInstance() {
        if (instance == null)
            instance = new BackSimpleLocationCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CMD_BACK));
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().getSimpleLocation().commandAction(Resources.CMD_BACK, null);
    }
}

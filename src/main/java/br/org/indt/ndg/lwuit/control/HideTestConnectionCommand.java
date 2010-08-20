/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.submit.TestConnection;
import com.sun.lwuit.Command;
import br.org.indt.ndg.mobile.Resources;
import com.nokia.mid.appl.cmd.Local;

public class HideTestConnectionCommand extends BackCommand {

    private static HideTestConnectionCommand instance;

    public static HideTestConnectionCommand getInstance() {
        if (instance == null)
            instance = new HideTestConnectionCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CMD_HIDE));
    }

    protected void doAction(Object parameter) {
        TestConnection.getInstance().commandAction(Resources.CMD_HIDE, null);
    }

}

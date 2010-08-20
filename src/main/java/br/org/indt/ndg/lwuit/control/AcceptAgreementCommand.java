/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.nokia.mid.appl.cmd.Local;
import com.sun.lwuit.Command;

public class AcceptAgreementCommand extends CommandControl {

    private static AcceptAgreementCommand instance;

    public static AcceptAgreementCommand getInstance() {
        if (instance == null)
            instance = new AcceptAgreementCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CMD_ACCEPT));
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().getAgreementScreen().commandAction(Resources.CMD_ACCEPT, null);
    }

}

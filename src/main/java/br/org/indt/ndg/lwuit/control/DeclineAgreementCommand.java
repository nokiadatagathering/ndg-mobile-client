/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.nokia.mid.appl.cmd.Local;
import com.sun.lwuit.Command;

public class DeclineAgreementCommand extends CommandControl {

    private static DeclineAgreementCommand instance;

    public static DeclineAgreementCommand getInstance() {
        if (instance == null)
            instance = new DeclineAgreementCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CMD_DECLINE));
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().getAgreementScreen().commandAction(Resources.CMD_DECLINE, null);
    }

}

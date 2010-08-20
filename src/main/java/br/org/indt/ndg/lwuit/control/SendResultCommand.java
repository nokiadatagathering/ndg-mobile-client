/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

/**
 *
 * @author mluz
 */
public class SendResultCommand extends CommandControl {

    private static SendResultCommand instance;

    protected Command createCommand() {
        return new Command(Resources.NEWUI_SEND_RESULTS);
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().getResultView().commandAction(Resources.CMD_SEND, null);
    }

    public static SendResultCommand getInstance() {
        if (instance == null)
            instance = new SendResultCommand();
        return instance;
    }

}
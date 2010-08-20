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
public class BackResultViewCommand extends BackCommand {

    private static BackResultViewCommand instance;

    protected Command createCommand() {
        return new Command(Resources.NEWUI_BACK);
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().getResultView().commandAction(Resources.CMD_BACK, null);
    }

    public static BackResultViewCommand getInstance() {
        if (instance == null)
            instance = new BackResultViewCommand();
        return instance;
    }


}

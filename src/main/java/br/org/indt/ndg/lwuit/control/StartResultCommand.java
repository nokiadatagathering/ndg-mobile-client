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
public class StartResultCommand extends CommandControl {

    private static StartResultCommand instance;

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CMD_START));
    }

    protected void doAction(Object parameter) {
        int selectedIndex = ((Integer)parameter).intValue();
        AppMIDlet.getInstance().getCategoryList().setSelectedIndex(selectedIndex, true);
        AppMIDlet.getInstance().getCategoryList().commandAction(Resources.CMD_START, null);
    }

    public static StartResultCommand getInstance() {
        if (instance == null)
            instance = new StartResultCommand();
        return instance;
    }

}

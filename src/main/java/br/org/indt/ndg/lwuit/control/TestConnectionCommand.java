/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AppMIDlet;
import com.sun.lwuit.Command;
import br.org.indt.ndg.mobile.Resources;
import com.nokia.mid.appl.cmd.Local;

public class TestConnectionCommand extends CommandControl {

    private static TestConnectionCommand instance;

    public static TestConnectionCommand getInstance() {
        if (instance == null)
            instance = new TestConnectionCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CMD_TEST_CONNECTION));
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().getSurveyList().commandAction(Resources.CMD_TEST_CONN, null);
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

import br.org.indt.ndg.mobile.FileSystem;
import br.org.indt.ndg.mobile.ResultList;
import br.org.indt.ndg.mobile.sms.SMSUtils;

/**
 *
 * @author mluz
 */
public class DeleteCurrentResultCommand extends CommandControl {

    private static DeleteCurrentResultCommand instance;

    protected Command createCommand() {
        return new Command(Resources.NEWUI_DELETE_CURRENT_RESULT);
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().getResultView().commandAction(Resources.CMD_DELETE, null);
    }

    public static DeleteCurrentResultCommand getInstance() {
        if (instance == null)
            instance = new DeleteCurrentResultCommand();
        return instance;
    }
}

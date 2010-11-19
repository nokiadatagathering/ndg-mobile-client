/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AppMIDlet;
import com.sun.lwuit.Command;
import br.org.indt.ndg.mobile.ResultList;
import com.nokia.mid.appl.cmd.Local;

public class CancelSSSubmitCommand extends BackCommand {

    private static CancelSSSubmitCommand instance;

    public static CancelSSSubmitCommand getInstance() {
        if (instance == null)
            instance = new CancelSSSubmitCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CMD_CANCEL));
    }

    protected void doAction(Object parameter) {
         AppMIDlet.getInstance().getSubmitServer().cancel();
         AppMIDlet.getInstance().setResultList(new ResultList());  //updated result list
         AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.ResultList.class);
    }
}
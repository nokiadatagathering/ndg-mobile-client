/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.ResultList;
import com.nokia.mid.appl.cmd.Local;
import com.sun.lwuit.Command;

/**
 *
 * @author Alexandre Martini
 */
public class BackSendResultListCommand extends BackCommand{

    private static BackSendResultListCommand instance = new BackSendResultListCommand();

    private BackSendResultListCommand(){}

    public static BackSendResultListCommand getInstance(){
        return instance;
    }

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CMD_BACK));
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().setResultList(new ResultList());
        AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getResultList());
    }

}

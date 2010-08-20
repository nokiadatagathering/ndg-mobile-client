/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.DeleteList;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

/**
 *
 * @author Alexandre Martini
 */
public class BackDeleteResultListCommand extends BackCommand{
    private static BackDeleteResultListCommand instance = new BackDeleteResultListCommand();

    private BackDeleteResultListCommand(){}

    public static BackDeleteResultListCommand getInstance(){
        return instance;
    }

    protected Command createCommand() {
        return new Command(Resources.NEWUI_BACK);
    }

    protected void doAction(Object parameter) {
        DeleteList list = (DeleteList) parameter;
        list.commandAction(Resources.CMD_BACK, null);
    }

}

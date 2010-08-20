/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.DeleteList;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

/**
 *
 * @author mluz
 */
public class DeleteResultCommand extends CommandControl {

    private static DeleteResultCommand instance;

    private DeleteResultCommand(){}

    protected Command createCommand() {
        return new Command(Resources.NEWUI_DELETE_RESULTS);
    }

    protected void doAction(Object parameter) {
        //AppMIDlet.getInstance().getResultList().commandAction(Resources.CMD_DELETE, null);
        AppMIDlet.getInstance().setDisplayable(new DeleteList());
    }

    public static DeleteResultCommand getInstance() {
        if (instance == null)
            instance = new DeleteResultCommand();
        return instance;
    }

}
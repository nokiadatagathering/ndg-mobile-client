/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.lwuit.ui.ResultList;
import com.sun.lwuit.Command;

/**
 *
 * @author mturiel
 */
public class BackInterview2FormCommand extends BackCommand {

    private static BackInterview2FormCommand instance;

    protected Command createCommand() {
        return new Command(Resources.NEWUI_BACK);
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().setDisplayable(ResultList.class);
    }

    public static BackInterview2FormCommand getInstance() {
        if (instance == null)
            instance = new BackInterview2FormCommand();
        return instance;
    }
}

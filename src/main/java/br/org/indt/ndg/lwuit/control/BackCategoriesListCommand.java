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
public class BackCategoriesListCommand extends BackCommand {

    private static BackCategoriesListCommand instance;

    protected Command createCommand() {
        return new Command(Resources.NEWUI_BACK);
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().getCategoryList().commandAction(Resources.CMD_BACK, null);
    }

    public static BackCategoriesListCommand getInstance() {
        if (instance == null)
            instance = new BackCategoriesListCommand();
        return instance;
    }


}

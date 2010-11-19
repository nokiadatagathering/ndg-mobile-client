/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.DisplayCategoryForm;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

/**
 *
 * @author mluz
 */
public class DisplayCategoryViewCommand extends CommandControl {

    private static DisplayCategoryViewCommand instance;

    protected Command createCommand() {
        return new Command(Resources.CATEGORYVIEW);
    }

    public static DisplayCategoryViewCommand getInstance() {
        if (instance == null)
            instance = new DisplayCategoryViewCommand();
        return instance;
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().setDisplayable(DisplayCategoryForm.class);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.InterviewForm;
import br.org.indt.ndg.mobile.AppMIDlet;
import com.nokia.mid.appl.cmd.Local;
import com.sun.lwuit.Command;

/**
 *
 * @author mluz
 */
public class EnterCategoryCommand extends CommandControl {

    private static EnterCategoryCommand instance;

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CMD_RESULTS));
    }

    protected void doAction(Object parameter) {
        int selectedIndex = ((Integer)parameter).intValue();
        //AppMIDlet.getInstance().getCategoryList().setSelectedIndex(selectedIndex);
        SurveysControl.getInstance().setSelectedCategory(selectedIndex);
        AppMIDlet.getInstance().setDisplayable(InterviewForm.class);
        //AppMIDlet.getInstance().getCategoryList().commandAction(Resources.CMD_RESULTS, null);
    }

    public static EnterCategoryCommand getInstance() {
        if (instance == null)
            instance = new EnterCategoryCommand();
        return instance;
    }

}


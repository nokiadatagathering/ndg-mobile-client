/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.CategoryList;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;


public class AcceptQuestionListFormCommand extends BackCommand {

    private static AcceptQuestionListFormCommand instance;

    protected Command createCommand() {
        return new Command(Resources.NEWUI_NEXT);//change resource name
    }

    protected void doAction(Object parameter) {
        SurveysControl.getInstance().setSelectedCategory(SurveysControl.getInstance().getSelectedCategoryIndex()+1);
        AppMIDlet.getInstance().setDisplayable(CategoryList.class);
    }

    public static AcceptQuestionListFormCommand getInstance() {
        if (instance == null)
            instance = new AcceptQuestionListFormCommand();
        return instance;
    }
}
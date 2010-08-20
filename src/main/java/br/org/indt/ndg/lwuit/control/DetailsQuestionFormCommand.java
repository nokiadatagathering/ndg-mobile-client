/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.structures.question.custom.CustomChoiceGroup;
import com.nokia.mid.appl.cmd.Local;
import com.sun.lwuit.Command;

public class DetailsQuestionFormCommand extends CommandControl {

    private static DetailsQuestionFormCommand instance;

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_MORE_DETAILS));
    }

    protected void doAction(Object parameter) {
        CustomChoiceGroup cg = ((CustomChoiceGroup) SurveysControl.getInstance().getCurrentQuestionFormOldUI());
        cg.commandAction(cg.details, null);
    }

    public static DetailsQuestionFormCommand getInstance() {
        if (instance == null)
            instance = new DetailsQuestionFormCommand();
        return instance;
    }
}

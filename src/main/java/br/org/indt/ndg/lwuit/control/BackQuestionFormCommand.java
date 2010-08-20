/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.structures.question.custom.CustomChoiceGroup;
import br.org.indt.ndg.lwuit.model.ChoiceQuestion;
import com.sun.lwuit.Command;

/**
 *
 * @author mluz
 */
public class BackQuestionFormCommand extends BackCommand {

    private static BackQuestionFormCommand instance;

    protected Command createCommand() {
        return new Command(Resources.NEWUI_BACK);
    }

    protected void doAction(Object parameter) {
        if (parameter instanceof ChoiceQuestion)
            ((CustomChoiceGroup)SurveysControl.getInstance().getCurrentQuestionFormOldUI()).commandAction(Resources.CMD_BACK, null);
        else
            AppMIDlet.getInstance().getQuestionList().questionForm.commandAction(Resources.CMD_BACK, null);
    }

    public static BackQuestionFormCommand getInstance() {
        if (instance == null)
            instance = new BackQuestionFormCommand();
        return instance;
    }

}

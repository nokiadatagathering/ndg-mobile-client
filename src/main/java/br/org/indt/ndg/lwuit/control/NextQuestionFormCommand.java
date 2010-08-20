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
import com.sun.lwuit.animations.CommonTransitions;
import com.sun.lwuit.plaf.UIManager;

/**
 *
 * @author mluz
 */
public class NextQuestionFormCommand extends CommandControl {

    private static NextQuestionFormCommand instance;

    protected Command createCommand() {
        return new Command(Resources.NEWUI_NEXT);
    }

    protected void doAction(Object parameter) {
        if (parameter instanceof ChoiceQuestion)
            ((CustomChoiceGroup)SurveysControl.getInstance().getCurrentQuestionFormOldUI()).commandAction(Resources.CMD_NEXT, null);
        else
            AppMIDlet.getInstance().getQuestionList().questionForm.commandAction(Resources.CMD_NEXT, null);
    }

    public static NextQuestionFormCommand getInstance() {
        if (instance == null)
            instance = new NextQuestionFormCommand();
        return instance;
    }

    public void doBefore() {
        UIManager.getInstance().getLookAndFeel().setDefaultFormTransitionIn(CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, false, 500));
    }

    public void doAfter() {
        UIManager.getInstance().getLookAndFeel().setDefaultFormTransitionIn(CommonTransitions.createSlide(CommonTransitions.SLIDE_HORIZONTAL, true, 500));
    }


}


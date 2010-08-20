/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.structures.question.custom.CustomTextField;
import com.sun.lwuit.Command;

/**
 *
 * @author mluz
 */
public class OkQuestionFormCommand extends CommandControl {

    private static OkQuestionFormCommand instance;

    protected Command createCommand() {
        return new Command(Resources.NEW_IU_OK);
    }

    protected void doAction(Object parameter) {
        ((CustomTextField)(SurveysControl.getInstance().getCurrentQuestionFormOldUI())).commandAction(Resources.CMD_OK, null);
    }

    public static OkQuestionFormCommand getInstance() {
        if (instance == null)
            instance = new OkQuestionFormCommand();
        return instance;
    }

}

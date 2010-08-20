/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.nokia.mid.appl.cmd.Local;
import com.sun.lwuit.Command;
import com.sun.lwuit.animations.CommonTransitions;
import com.sun.lwuit.plaf.UIManager;

/**
 *
 * @author Alexandre Martini
 */
public class EnterQuestionListCommand extends CommandControl{
    private static EnterQuestionListCommand instance = new EnterQuestionListCommand();

    private EnterQuestionListCommand(){}

    public static EnterQuestionListCommand getInstance(){
        return instance;
    }

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CMD_RESULTS));
    }

    protected void doAction(Object parameter) {
        Integer index = (Integer) parameter;
        AppMIDlet.getInstance().getQuestionList().setSelectedIndex(index.intValue(), true);
        AppMIDlet.getInstance().getQuestionList().commandAction(Resources.CMD_RESULTS, null);
    }

    public void doAfter() {
        UIManager.getInstance().getLookAndFeel().setDefaultFormTransitionIn(CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, false, 500));
    }

}

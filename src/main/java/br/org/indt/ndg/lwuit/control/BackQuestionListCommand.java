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
 * @author Alexandre Martini
 */
public class BackQuestionListCommand extends BackCommand{
    private static BackQuestionListCommand instance = new BackQuestionListCommand();

    private BackQuestionListCommand(){}

    public static BackQuestionListCommand getInstance(){
        return instance;
    }
    protected Command createCommand() {
        return new Command(Resources.NEWUI_BACK);
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().getQuestionList().commandAction(Resources.CMD_BACK, null);
    }

}

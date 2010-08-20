/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.error.WaitingForm;
import br.org.indt.ndg.lwuit.ui.SentResultList;
import com.sun.lwuit.Command;

/**
 *
 * @author Alexandre Martini
 */
public class BackSentResultListCommand extends BackCommand{
    private static BackSentResultListCommand instance = new BackSentResultListCommand();

    private BackSentResultListCommand(){}

    public static BackSentResultListCommand getInstance() {
        return instance;
    }

    protected Command createCommand() {
        return new Command(Resources.NEWUI_BACK);
    }

    protected void doAction(Object parameter) {
        SentResultList list = (SentResultList) parameter;
        list.getSentList().commandAction(Resources.CMD_BACK, null);
    }

}

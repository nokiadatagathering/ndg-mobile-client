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
 * @author mturiel
 */
public class BackInterviewFormCommand extends BackCommand {

    private static BackInterviewFormCommand instance;

    protected Command createCommand() {
        return new Command(Resources.NEWUI_BACK);
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getResultList());
    }

    public static BackInterviewFormCommand getInstance() {
        if (instance == null)
            instance = new BackInterviewFormCommand();
        return instance;
    }
}

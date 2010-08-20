/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.SubmitList;
import br.org.indt.ndg.mobile.error.WaitingForm;
import com.sun.lwuit.Command;

/**
 *
 * @author mluz
 */
public class SendResultsCommand extends CommandControl {

    private static SendResultsCommand instance;

    protected Command createCommand() {
        return new Command(Resources.NEWUI_SEND_RESULTS);
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().setDisplayable(new WaitingForm(Resources.CMD_VIEW.getLabel()));
        SendResultsRunnable vrr = new SendResultsRunnable();
        Thread t = new Thread(vrr);  //create new thread to compensate for waitingform
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
    }

    public static SendResultsCommand getInstance() {
        if (instance == null)
            instance = new SendResultsCommand();
        return instance;
    }

    class SendResultsRunnable implements Runnable {
        public void run() {
            try { Thread.sleep(200); } catch(Exception e){}
            AppMIDlet.getInstance().setDisplayable(new SubmitList());
        }
    }

}
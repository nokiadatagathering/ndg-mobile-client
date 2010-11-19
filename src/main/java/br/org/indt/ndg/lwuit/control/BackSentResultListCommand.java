/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.lwuit.ui.WaitingScreen;
import br.org.indt.ndg.mobile.ResultList;
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
        WaitingScreen.show(Resources.LOADING_RESULTS);
        BackResultRunnable brr = new BackResultRunnable();
        Thread t = new Thread(brr);  //create new thread to compensate for waitingform
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
    }

    class BackResultRunnable implements Runnable {
        public void run() {
            AppMIDlet.getInstance().getFileSystem().loadResultFiles();
            AppMIDlet.getInstance().setResultList(new ResultList() );
            AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.ResultList.class);
        }
    }
}

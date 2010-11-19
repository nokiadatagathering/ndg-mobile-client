/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.SentResultList;
import br.org.indt.ndg.lwuit.ui.WaitingScreen;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

/**
 *
 * @author mluz
 */
public class ViewSentResultsCommand extends CommandControl {

    private static ViewSentResultsCommand instance;

    protected Command createCommand() {
        return new Command(Resources.NEWUI_VIEW_SENT_RESULTS);
    }

    protected void doAction(Object parameter) {
        WaitingScreen.show(Resources.CMD_VIEW);
        ViewSentResultsRunnable vrr = new ViewSentResultsRunnable();
        Thread t = new Thread(vrr);  //create new thread to compensate for waitingform
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
    }

    public static ViewSentResultsCommand getInstance() {
        if (instance == null)
            instance = new ViewSentResultsCommand();
        return instance;
    }

    class ViewSentResultsRunnable implements Runnable {
        public void run() {
            try { Thread.sleep(200); } catch(Exception e){}
            AppMIDlet.getInstance().getFileSystem().loadSentFiles();
            AppMIDlet.getInstance().setDisplayable(SentResultList.class);
        }
    }

}
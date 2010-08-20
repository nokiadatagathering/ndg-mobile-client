/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.SentList;
import br.org.indt.ndg.mobile.error.WaitingForm;
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
        //AppMIDlet.getInstance().getResultList().commandAction(Resources.CMD_VIEWSENT, null);
        AppMIDlet.getInstance().setDisplayable(new WaitingForm(Resources.CMD_VIEW.getLabel()));
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
            AppMIDlet.getInstance().setDisplayable(new SentList());
        }
    }

}
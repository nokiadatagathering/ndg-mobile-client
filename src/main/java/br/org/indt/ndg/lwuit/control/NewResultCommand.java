/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.CategoryList;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.error.WaitingForm;
import com.sun.lwuit.Command;
import java.util.Date;

/**
 *
 * @author mluz
 */
public class NewResultCommand extends CommandControl {

    private static NewResultCommand instance;

    protected Command createCommand() {
        return new Command(Resources.NEWUI_NEW_RESULT);
    }

    protected void doAction(Object parameter) {
        //AppMIDlet.getInstance().getResultList().commandAction(Resources.CMD_NEW_RESULT, null);
        //AppMIDlet.getInstance().setDisplayable(new WaitingForm(Resources.CMD_NEW_RESULT.getLabel()));
        br.org.indt.ndg.lwuit.ui.WaitingForm.show(br.org.indt.ndg.lwuit.ui.WaitingForm.class, true);
        NewResultRunnable orr = new NewResultRunnable();
        Thread t = new Thread(orr);  //create new thread to compensate for waitingform
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
    }

    public static NewResultCommand getInstance() {
        if (instance == null)
            instance = new NewResultCommand();
        return instance;
    }

    class NewResultRunnable implements Runnable {
        public void run() {
            try {
                try { Thread.sleep(200); } catch(Exception e){}
                AppMIDlet.getInstance().getFileStores().resetQuestions();
                AppMIDlet.getInstance().getFileStores().clearAnswers();
                AppMIDlet.getInstance().setTimeTracker((new Date()).getTime());  //to keep track of time used to create new survey
                AppMIDlet.getInstance().getFileSystem().setLocalFile(false);
                AppMIDlet.getInstance().setCategoryList(new CategoryList());
                AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getCategoryList());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
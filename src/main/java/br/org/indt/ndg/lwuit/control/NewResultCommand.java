/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.WaitingScreen;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
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
        WaitingScreen.show(Resources.PROCESSING);
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
                AppMIDlet.getInstance().setTimeTracker((new Date()).getTime());  //to keep track of time used to create new survey
                AppMIDlet.getInstance().getFileSystem().setLocalFile(false);

                AppMIDlet.getInstance().getFileStores().resetResultStructure();
                SurveysControl.getInstance().reset();
                SurveysControl.getInstance().resetQuestion();//this is from refactoring IV


                AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getInterviewForm());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
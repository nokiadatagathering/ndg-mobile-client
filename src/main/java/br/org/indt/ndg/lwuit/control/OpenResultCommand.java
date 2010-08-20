/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.CategoryList;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.ResultList;
import com.sun.lwuit.Command;
import java.util.Date;

/**
 *
 * @author mluz
 */
public class OpenResultCommand extends CommandControl {

    private static OpenResultCommand instance;

    protected Command createCommand() {
        return new Command(Resources.NEWUI_OPEN_RESULT);
    }

    protected void doAction(Object parameter) {
        if (parameter != null) {
            int selectedIndex = ((Integer)parameter).intValue();
            AppMIDlet.getInstance().getResultList().setSelectedIndex(selectedIndex, true);
            AppMIDlet.getInstance().getFileSystem().setResultCurrentIndex(selectedIndex);
        }
        //AppMIDlet.getInstance().getResultList().commandAction(Resources.CMD_OPEN_RESULT, null);
        //AppMIDlet.getInstance().setDisplayable(new WaitingForm(Resources.CMD_OPEN_RESULT.getLabel()));
        br.org.indt.ndg.lwuit.ui.WaitingForm.show(br.org.indt.ndg.lwuit.ui.WaitingForm.class, true);
        OpenResultRunnable orr = new OpenResultRunnable();
        Thread t = new Thread(orr);  //create new thread to compensate for waitingform
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
    }

    public static OpenResultCommand getInstance() {
        if (instance == null)
            instance = new OpenResultCommand();
        return instance;
    }

    class OpenResultRunnable implements Runnable {
        public void run() {
            try { Thread.sleep(200); } catch(Exception e){}

            if (AppMIDlet.getInstance().getFileSystem().getResultFilename() != null) {
                AppMIDlet.getInstance().getFileSystem().setLocalFile(true);
                AppMIDlet.getInstance().getFileStores().parseResultFile();
                if (AppMIDlet.getInstance().getFileStores().getError()) {
                    AppMIDlet.getInstance().getGeneralAlert().showErrorExit(Resources.EPARSE_RESULT);
                } else {
                    AppMIDlet.getInstance().setTimeTracker((new Date()).getTime());  //to keep track of time increment used to create new survey
                    AppMIDlet.getInstance().getFileStores().loadAnswers();
                    AppMIDlet.getInstance().setCategoryList(new CategoryList());
                    AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getCategoryList());
                }
            }
        }
    }


}

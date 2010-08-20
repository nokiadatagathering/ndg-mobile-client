/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;


import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.ResultView;
import br.org.indt.ndg.mobile.error.WaitingForm;
import com.sun.lwuit.Command;

/**
 *
 * @author mluz
 */
public class ViewResultCommand extends CommandControl {

    private static ViewResultCommand instance;

    protected Command createCommand() {
        return new Command(Resources.NEWUI_VIEW_RESULT);
    }

    protected void doAction(Object parameter) {
        if (parameter != null) {
            int selectedIndex = ((Integer)parameter).intValue();
            AppMIDlet.getInstance().getResultList().setSelectedIndex(selectedIndex, true);
            AppMIDlet.getInstance().getFileSystem().setResultCurrentIndex(selectedIndex);
        }
        
        //AppMIDlet.getInstance().getResultList().commandAction(Resources.CMD_VIEW, null);*/
        AppMIDlet.getInstance().setDisplayable(new WaitingForm(Resources.CMD_VIEW.getLabel()));
        ViewResultRunnable vrr = new ViewResultRunnable();
        Thread t = new Thread(vrr);  //create new thread to compensate for waitingform
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
    }

    public static ViewResultCommand getInstance() {
        if (instance == null)
            instance = new ViewResultCommand();
        return instance;
    }

    class ViewResultRunnable implements Runnable {
        public void run() {
            try { Thread.sleep(200); } catch(Exception e){}
            if (AppMIDlet.getInstance().getFileSystem().getResultFilename() != null) {
                AppMIDlet.getInstance().getFileStores().parseResultFile();
                if (AppMIDlet.getInstance().getFileStores().getError()) {
                    AppMIDlet.getInstance().getGeneralAlert().showErrorExit(Resources.EPARSE_RESULT);
                } else {
                    AppMIDlet.getInstance().getFileStores().loadAnswers();
                    AppMIDlet.getInstance().setResultView(new ResultView(AppMIDlet.getInstance().getResultList()));
                    AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getResultView());
                }
            }
        }
    }

}
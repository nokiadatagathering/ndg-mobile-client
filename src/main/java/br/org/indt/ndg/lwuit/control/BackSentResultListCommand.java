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
import com.sun.lwuit.animations.CommonTransitions;
import com.sun.lwuit.plaf.UIManager;

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

    /**
     * Because action is executed in different thread doAfter would execute before
     * action is finished thus most probably would execute in unexpected manner
     */
    public void doAfter(){}

    class BackResultRunnable implements Runnable {
        public void run() {
            AppMIDlet.getInstance().getFileSystem().loadResultFiles();
            AppMIDlet.getInstance().setResultList(new ResultList() );
            AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.ResultList.class);
            BackSentResultListCommand.super.doAfter(); // see doAfter() description
        }
    }
}

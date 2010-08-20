/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.submit.SubmitServer;
import br.org.indt.ndg.mobile.submit.StatusScreen;
import br.org.indt.ndg.mobile.submit.SubmitResultRunnable;
import com.nokia.mid.appl.cmd.Local;
import com.sun.lwuit.Command;
import java.util.Vector;

/**
 *
 * @author Alexandre Martini
 */
public class SendResultNowCommand extends CommandControl{

    private static SendResultNowCommand instance = new SendResultNowCommand();
    private Vector selectedFiles = new Vector();

    private SendResultNowCommand(){}

    public static SendResultNowCommand getInstance(){
        return instance;
    }

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CMD_SEND));
    }

    public Vector getSelectedFiles(){
        return selectedFiles;
    }

    protected void doAction(Object parameter) {
        boolean[] listFlags = (boolean[]) parameter;
        
        int size = listFlags.length;
        selectedFiles.removeAllElements();
        for (int i=0; i < size; i++){
            if (listFlags[i]) selectedFiles.addElement(AppMIDlet.getInstance().getFileSystem().getResultFilename(i));
        }
        if (selectedFiles.size() > 0) {  //only send if items are selected
            AppMIDlet.getInstance().setSubmitList(new br.org.indt.ndg.mobile.SubmitList());
            SubmitResultRunnable srr = new SubmitResultRunnable(null);
            SubmitServer submitServer = new SubmitServer();
            StatusScreen statusScreen = new StatusScreen(submitServer);
            srr.submitServer = submitServer;
            srr.statusScreen = statusScreen;
            AppMIDlet.getInstance().setDisplayable(statusScreen);
            try { Thread.sleep(500); } catch (InterruptedException ex) {}
            Thread t = new Thread(srr);
            t.start();
        }
    }

}

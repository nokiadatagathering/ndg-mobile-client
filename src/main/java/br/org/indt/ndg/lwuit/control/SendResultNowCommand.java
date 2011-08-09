package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.StatusScreen;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.FileSystem;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.submit.SubmitServer;
import br.org.indt.ndg.mobile.submit.SubmitResultRunnable;
import com.sun.lwuit.Command;
import java.util.Vector;

/**
 *
 * @author Alexandre Martini
 */
public class SendResultNowCommand extends CommandControl{

    private static SendResultNowCommand instance;
    private Vector selectedFiles;

    private SendResultNowCommand()
    {
        selectedFiles = new Vector();
    }

    public static SendResultNowCommand getInstance(){
        if( instance == null )
        {
            instance = new SendResultNowCommand();
        }
        return instance;
    }

    protected Command createCommand() {
        return new Command(Resources.NEWUI_SEND_RESULTS);
    }

    protected void doAction(Object parameter) {
        boolean[] listFlags = (boolean[]) parameter;
        AppMIDlet.getInstance().getFileSystem().useResults(FileSystem.USE_NOT_SENT_RESULTS);
        
        int size = listFlags.length;
        selectedFiles.removeAllElements();
        for (int i=0; i < size; i++)
        {
            if (listFlags[i]) selectedFiles.addElement(AppMIDlet.getInstance().getFileSystem().getResultFilename(i));
        }
        if (selectedFiles.size() > 0) {  //only send if items are selected
            SubmitResultRunnable srr = new SubmitResultRunnable( selectedFiles );
            AppMIDlet.getInstance().setSubmitServer( new SubmitServer() );
            srr.setSubmitServer( AppMIDlet.getInstance().getSubmitServer() );
            AppMIDlet.getInstance().setDisplayable(StatusScreen.class);
            try { Thread.sleep(500); } catch (InterruptedException ex) {}
            Thread t = new Thread(srr);
            t.start();
        }
    }
}

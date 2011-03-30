package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.model.CheckableListModel;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.FileSystem;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.XmlResultFile;
import br.org.indt.ndg.lwuit.ui.WaitingScreen;
import br.org.indt.ndg.mobile.ResultList;
import com.nokia.mid.appl.cmd.Local;
import com.sun.lwuit.Command;
import java.util.Vector;

/**
 *
 * @author Alexandre Martini
 */
public class MoveToUnsentCommand extends CommandControl{
    private static MoveToUnsentCommand instance = new MoveToUnsentCommand();

    private MoveToUnsentCommand(){}

    public static MoveToUnsentCommand getInstance(){
        return instance;
    }

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CMD_MOVETOUNSENT));
    }

    protected void doAction(Object parameter) {
        CheckableListModel model = (CheckableListModel) parameter;
        boolean[] listFlags = model.getSelectedFlags();
        FileSystem fs = AppMIDlet.getInstance().getFileSystem();
        Vector xmlResultFile = fs.getXmlSentFile();
        int size = listFlags.length;
        for (int i=0; i < size; i++){
            if (listFlags[i]) {
                String xmlFileName = ((XmlResultFile) xmlResultFile.elementAt(i)).getFileName();
                if(xmlFileName.startsWith("s_")) {
                    fs.moveUnsentResult(xmlFileName);
                }
            }
        }

        WaitingScreen.show(Resources.LOADING_RESULTS);
        UnsentResultRunnable urr = new UnsentResultRunnable();
        Thread t = new Thread(urr);  //create new thread to compensate for waitingform
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();

    }

    class UnsentResultRunnable implements Runnable {
        public void run() {
            //markAsUnsent();
            AppMIDlet.getInstance().getFileSystem().loadResultFiles();
            AppMIDlet.getInstance().setResultList( new ResultList() );
            AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.ResultList.class);
        }
    }
}

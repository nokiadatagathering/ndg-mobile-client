package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.model.CheckableListModel;
import br.org.indt.ndg.lwuit.ui.SentResultList;
import br.org.indt.ndg.mobile.AppMIDlet;
import com.nokia.mid.appl.cmd.Local;
import com.sun.lwuit.Command;
import br.org.indt.ndg.mobile.FileSystem;
import br.org.indt.ndg.mobile.XmlResultFile;
import java.util.Enumeration;
import java.util.Vector;

/**
 *
 * @author Alexandre Martini
 */
public class DeleteSentResultCommand extends CommandControl{

    private static DeleteSentResultCommand instance = new DeleteSentResultCommand();

    private DeleteSentResultCommand(){}

    public static DeleteSentResultCommand getInstance(){
        return instance;
    }

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CMD_DELETE));
    }

    protected void doAction(Object parameter) {
        // mark the old screen with selected checkboxes
        CheckableListModel model = (CheckableListModel) parameter;
        boolean[] listFlags = model.getSelectedFlags();

        FileSystem fs = AppMIDlet.getInstance().getFileSystem();
        fs.useResults(FileSystem.USE_SENT_RESULTS);
        Vector xmlResultFile = fs.getXmlSentFile();
        Vector selectedFiles = new Vector();
        
        int size = listFlags.length;
        for (int i=0; i < size; i++){
            if (listFlags[i]) {
                selectedFiles.addElement(((XmlResultFile) xmlResultFile.elementAt(i)).getFileName());
            }
        }
        DeleteResultRunnable drr = new DeleteResultRunnable( selectedFiles );
        Thread t = new Thread(drr);
        t.start();
    }


    class DeleteResultRunnable implements Runnable {
        Vector selectedFiles;

        public DeleteResultRunnable( Vector _selectedFiles )
        {
            this.selectedFiles = _selectedFiles;
        }

        public void run(){
            Enumeration e = selectedFiles.elements();
            String fName;
            while (e.hasMoreElements()) {
                fName = (String) e.nextElement();
                AppMIDlet.getInstance().getFileSystem().removeDisplayName(fName);
                AppMIDlet.getInstance().getFileSystem().deleteFile(fName);

                String name = fName.substring( "s_".length() );
                AppMIDlet.getInstance().getFileSystem().deleteDir( "b_" + name );//will delete binary files releated with selected result
            }
            AppMIDlet.getInstance().getFileSystem().loadSentFiles();
            AppMIDlet.getInstance().setDisplayable(SentResultList.class);
        }
    }
}

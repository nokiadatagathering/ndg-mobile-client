
package br.org.indt.ndg.mobile;

import br.org.indt.ndg.mobile.submit.SubmitResultRunnable;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import br.org.indt.ndg.mobile.submit.SubmitServer;
import br.org.indt.ndg.lwuit.control.SendResultNowCommand;
import br.org.indt.ndg.mobile.submit.StatusScreen;

public class SubmitList extends List implements CommandListener {
    
    private Vector selectedFiles;
    private Vector xmlResultFile;
    private FileSystem fs;
    
    public SubmitList() {
        super(Resources.SUBMIT_LIST_TITLE, List.MULTIPLE);

        fs = AppMIDlet.getInstance().getFileSystem();
        xmlResultFile = fs.getXmlResultFile();
             
        Enumeration e = xmlResultFile.elements();
        while (e.hasMoreElements()) {
            this.append(((XmlResultFile) e.nextElement()).getDisplayName(), null);
        }
        
        selectedFiles = new Vector();
        
        addCommand(Resources.CMD_SEND);
        addCommand(Resources.CMD_MARKALL);
        addCommand(Resources.CMD_UNMARKALL);
        addCommand(Resources.CMD_BACK);
        
        setCommandListener(this);
    }
    
    public Vector getSelectedResultNames() {
        return SendResultNowCommand.getInstance().getSelectedFiles();
    }
    
    public void markAll() {
        for (int i=0; i < this.size(); i++) {
            this.setSelectedIndex(i, true);
        }
    }
    
    public void unmarkAll() {        
        for (int i=0; i < this.size(); i++) {
            this.setSelectedIndex(i, false);
        }
    }
    
    private void loadSelectedFiles() {
        selectedFiles.removeAllElements();
        boolean [] selected = new boolean[this.size()];
        this.getSelectedFlags(selected);
        
        for (int i=0; i < selected.length; i++)
            if (selected[i]) selectedFiles.addElement(fs.getResultFilename(i));
    }    
    
    public void commandAction(Command c, Displayable d) {
        if (c == Resources.CMD_BACK) {
            AppMIDlet.getInstance().setResultList(new ResultList());
            AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getResultList());
        } else if (c == Resources.CMD_SEND) {
            this.loadSelectedFiles();
            if (selectedFiles.size() > 0) {  //only send if items are selected
                AppMIDlet.getInstance().setSubmitList(this);
                SubmitResultRunnable srr = new SubmitResultRunnable(null);
                SubmitServer submitServer = new SubmitServer();
                StatusScreen statusScreen = new StatusScreen(submitServer);
                srr.submitServer = submitServer;
                srr.statusScreen = statusScreen;
                AppMIDlet.getInstance().setDisplayable(statusScreen);
                Thread t = new Thread(srr);
                t.start();
            }
        } else if (c == Resources.CMD_MARKALL) {
            this.markAll();
        } else if (c == Resources.CMD_UNMARKALL) {
            this.unmarkAll();
        }
    }
    
}

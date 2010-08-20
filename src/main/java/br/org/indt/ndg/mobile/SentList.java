
package br.org.indt.ndg.mobile;

import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import br.org.indt.ndg.mobile.error.WaitingForm;
import br.org.indt.ndg.mobile.sms.SMSUtils;

public class SentList extends List implements CommandListener {
    
    Vector xmlResultFile;
    FileSystem fs;
    
    public SentList() {
        super(Resources.SENT_LIST_TITLE, List.MULTIPLE);
        
        fs = AppMIDlet.getInstance().getFileSystem();
        xmlResultFile = fs.getXmlSentFile();
        
        XmlResultFile xmlResultFileObj = null;
        Enumeration e = xmlResultFile.elements();
        while (e.hasMoreElements()){
            xmlResultFileObj = (XmlResultFile) e.nextElement();
            String fileName = xmlResultFileObj.getFileName();
            if(fileName.startsWith("p_"))
                this.append(xmlResultFileObj.getDisplayName(), Resources.pendingACK);
            else if(fileName.startsWith("s_"))
                this.append(xmlResultFileObj.getDisplayName(), null);
        }
        
        if(this.size() > 0){
            addCommand(Resources.CMD_MOVETOUNSENT);
            addCommand(Resources.CMD_MARKALL);
            addCommand(Resources.CMD_UNMARKALL);
            addCommand(Resources.CMD_DELETE);
        }
        addCommand(Resources.CMD_BACK);
        
        setCommandListener(this);
    }
    
    public void unmarkAll() {
        for (int i=0; i < this.size(); i++) this.setSelectedIndex(i, false);
    }
    
    public void markAll() {
        for (int i=0; i < this.size(); i++) this.setSelectedIndex(i, true);
    }
    
    private void markAsUnsent() {
        boolean [] selected = new boolean[this.size()];
        this.getSelectedFlags(selected);
        
        String xmlFileName;
        //String smsFileName;
        int length = selected.length;
        for (int i=0; i < length; i++){            
            if (selected[i]) {
                xmlFileName = ((XmlResultFile) xmlResultFile.elementAt(i)).getFileName();
                if(xmlFileName.startsWith("s_") || xmlFileName.startsWith("p_")) {                    
                    fs.moveUnsentResult(xmlFileName);
                }
            }            
        }
    }
    private Vector getSelectedFiles(){
        Vector selectedFiles = new Vector();
        boolean [] selected = new boolean[this.size()];
        this.getSelectedFlags(selected);
     
        int length = selected.length;
        for (int i=0; i < length; i++){            
            if (selected[i]){
                selectedFiles.addElement(((XmlResultFile) xmlResultFile.elementAt(i)).getFileName());
            }
        }
        return selectedFiles;
    }
    
    class BackResultRunnable implements Runnable {
        public void run() {
            AppMIDlet.getInstance().getFileSystem().loadResultFiles();
            AppMIDlet.getInstance().setResultList(new ResultList());
            AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getResultList());
        }
    }
    
    class UnsentResultRunnable implements Runnable {
        public void run() {
            markAsUnsent();
            AppMIDlet.getInstance().getFileSystem().loadResultFiles();
            AppMIDlet.getInstance().setResultList(new ResultList());
            AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getResultList());
        }
    }
    
    class DeleteResultRunnable implements Runnable {
        public void run(){
            Vector filesToDelete = getSelectedFiles();
            Enumeration e = filesToDelete.elements();
            String fName;
            while (e.hasMoreElements()) {
                fName = (String) e.nextElement();
                fs.removeDisplayName(fName);
                fs.deleteFile(fName);            
                fs.deleteSMSFile(SMSUtils.getSMSFileNameFromXMLFileName(fName));
            }
            fs.loadSentFiles();            
            AppMIDlet.getInstance().setDisplayable(new SentList());
        }
    }
    
    public void commandAction(Command c, Displayable d) {
        if (c == Resources.CMD_BACK) {
            AppMIDlet.getInstance().setDisplayable(new WaitingForm(Resources.LOADING_RESULTS));
            BackResultRunnable brr = new BackResultRunnable();
            Thread t = new Thread(brr);  //create new thread to compensate for waitingform
            t.setPriority(Thread.MIN_PRIORITY);
            t.start();
        } else if (c == Resources.CMD_MARKALL) {
            markAll();
        } else if (c == Resources.CMD_UNMARKALL) {
            unmarkAll();
        } else if (c == Resources.CMD_MOVETOUNSENT) {
            AppMIDlet.getInstance().setDisplayable(new WaitingForm(Resources.LOADING_RESULTS));
            UnsentResultRunnable urr = new UnsentResultRunnable();
            Thread t = new Thread(urr);  //create new thread to compensate for waitingform
            t.setPriority(Thread.MIN_PRIORITY);
            t.start();
        }
        else if (c == Resources.CMD_DELETE){
            DeleteResultRunnable drr = new DeleteResultRunnable();
            Thread t = new Thread(drr);
            t.start();
        }
    }
}

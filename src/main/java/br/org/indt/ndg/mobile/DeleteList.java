
package br.org.indt.ndg.mobile;

import br.org.indt.ndg.mobile.sms.SMSUtils;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

public class DeleteList extends List implements CommandListener {
    
    private Vector filenamesToDelete;
    private Vector xmlResultFile;
    private FileSystem fs;
    
    public DeleteList() {
        super(Resources.CMD_DELETE.getLabel(), List.MULTIPLE);
        
        fs = AppMIDlet.getInstance().getFileSystem();
        xmlResultFile = fs.getXmlResultFile();
             
        Enumeration e = xmlResultFile.elements();
        while (e.hasMoreElements()) {
            this.append(((XmlResultFile) e.nextElement()).getDisplayName(), null);
        }
        
        filenamesToDelete=new Vector();
        
        addCommand(Resources.CMD_DELETE);
        addCommand(Resources.CMD_MARKALL);
        addCommand(Resources.CMD_UNMARKALL);
        addCommand(Resources.CMD_BACK);
        
        setCommandListener(this);
    }
    
    public void handleConfirmResult(Command _cmd) {
        if (_cmd == Resources.CMD_YES) {
            deleteSelectedFiles();
        }
        else if (_cmd == Resources.CMD_NO) {
            AppMIDlet.getInstance().setDisplayable(this);
        }
    }
    
    private void confirmDeleteFiles() {
        checkSelectedFiles();
        if(filenamesToDelete.size() > 0) {
            AppMIDlet.getInstance().getGeneralAlert().showConfirmAlert(Resources.DELETE_CONFIRMATION, Resources.DELETE_RESULTS_CONFIRMATION, "DeleteList");
        }
        else {
            //AppMIDlet.getInstance().getGeneralAlert().showAlert(Resources.NO_RESULTS_SELECTED, Resources.SELECT_RESULTS_DELETE);
            AppMIDlet.getInstance().getGeneralAlert().showAlert(Resources.CMD_DELETE.getLabel(), Resources.SELECT_RESULTS_DELETE);
        }
    }
    
    private void checkSelectedFiles() {
        filenamesToDelete.removeAllElements();
        
        boolean [] selected = new boolean[this.size()];
        this.getSelectedFlags(selected);
        
        int length = selected.length;
        for (int i=0; i < length; i++)
            if (selected[i]) filenamesToDelete.addElement(fs.getResultFilename(i));
    }
    
    private void deleteSelectedFiles() {
        String fName;
        
        Enumeration e = filenamesToDelete.elements();        
        while (e.hasMoreElements()) {
            fName = (String) e.nextElement();
            fs.deleteFile(fName);            
            fs.deleteSMSFile(SMSUtils.getSMSFileNameFromXMLFileName(fName));
        }
        AppMIDlet.getInstance().setResultList(new ResultList());
        AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getResultList());
    }
    
    public void commandAction(Command c, Displayable d) {
        if (c == Resources.CMD_BACK) {
            AppMIDlet.getInstance().setResultList(new ResultList());
            AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getResultList());
        } else if (c == Resources.CMD_DELETE) {
            confirmDeleteFiles();
        } else if (c == Resources.CMD_MARKALL) {
            this.markAll();
        } else if (c == Resources.CMD_UNMARKALL) {
            this.unmarkAll();
        }
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
}

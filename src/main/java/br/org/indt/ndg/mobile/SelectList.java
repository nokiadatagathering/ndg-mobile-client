
package br.org.indt.ndg.mobile;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import java.util.Vector;

public class SelectList extends List implements CommandListener {
    
    private FileSystem fs;
    private Vector xmlResultFile;
    
    public SelectList() {
        super(Resources.RANGE_TITLE, List.IMPLICIT);
        
        fs = AppMIDlet.getInstance().getFileSystem();
        xmlResultFile = fs.getXmlResultFile();
        
        int size = xmlResultFile.size();
        int limit = AppMIDlet.getInstance().getSettings().getStructure().getResultListRange();
        
        int items = size/limit;
        int leftover = size%limit;
        if (leftover != 0) items++;
        else leftover = limit;
        
        int mult = 0;
        String value;
        
        for (int i=1; i <= items; i++) {
            mult = limit*(i-1);
            if (i==items)
                value = (1 + mult) + "..." + (leftover + mult);
            else 
                value = (1 + mult) + "..." + (limit+mult); 
            this.append(value, null);
        }
        
        addCommand(Resources.CMD_BACK);
        
        setCommandListener(this);
    }
    
    public void commandAction(Command c, Displayable d) {
        if (c == SELECT_COMMAND) {
            fs.setResultListIndex(this.getSelectedIndex());
            AppMIDlet.getInstance().setResultList(new ResultList());
            AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getResultList());
        } 
        else if (c == Resources.CMD_BACK) {
            AppMIDlet.getInstance().setResultList(new ResultList());
            AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getResultList());
        }
    }
    
}

package br.org.indt.ndg.mobile.settings;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ItemStateListener;
import javax.microedition.lcdui.Item;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;

public class ServerForm extends Form implements CommandListener, ItemStateListener {
    
    private ChoiceGroup cg = null;
    
    public ServerForm() {
        super("");
        
        this.addCommand(Resources.CMD_BACK);
        
        cg = new ChoiceGroup(Resources.COMPRESSION, Choice.EXCLUSIVE);
        cg.append(Resources.ON, null);
        cg.append(Resources.OFF, null);
        
        boolean compression = AppMIDlet.getInstance().getSettings().getStructure().getServerCompression();
        if (compression) cg.setSelectedIndex(0, true);
        else cg.setSelectedIndex(1, true);
        
        this.append(cg);
        
        this.setCommandListener(this);
        this.setItemStateListener(this);
        
    }
    
    public void commandAction(Command c, Displayable d) {
        if (c == Resources.CMD_BACK) {
            AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getSettings());
        }
    }
    
    public void itemStateChanged(Item item) {
        if (item.equals(cg)) {
            if (cg.getSelectedIndex() == 0) 
                AppMIDlet.getInstance().getSettings().getStructure().setServerCompression(true);
            else 
                AppMIDlet.getInstance().getSettings().getStructure().setServerCompression(false);
            
            AppMIDlet.getInstance().getSettings().writeSettings();
        }
    }
}
    
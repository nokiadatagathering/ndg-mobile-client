/*
 * RangeForm.java
 *
 * Created on November 28, 2007, 2:46 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

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

public class RangeForm extends Form implements CommandListener, ItemStateListener {
    
    private ChoiceGroup cg = null;
    
    public RangeForm() {
        super("");
        
        this.addCommand(Resources.CMD_BACK);
        
        cg = new ChoiceGroup(Resources.RANGE, Choice.EXCLUSIVE);
        cg.append("10", null);
        cg.append("25", null);
        cg.append("50", null);
        cg.append("100", null);
        
        String currentSelected = String.valueOf(AppMIDlet.getInstance().getSettings().getStructure().getResultListRange());

        for (int i=0; i < cg.size(); i++) {
            if (cg.getString(i).equals(currentSelected)) cg.setSelectedIndex(i, true);
        }
        
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
                AppMIDlet.getInstance().getSettings().getStructure().setResultListRange(10);
            else if (cg.getSelectedIndex() == 1)
                AppMIDlet.getInstance().getSettings().getStructure().setResultListRange(25);
            else if (cg.getSelectedIndex() == 2)
               AppMIDlet.getInstance().getSettings().getStructure().setResultListRange(50);
            else if (cg.getSelectedIndex() == 3)
                AppMIDlet.getInstance().getSettings().getStructure().setResultListRange(100);
            
            AppMIDlet.getInstance().getSettings().writeSettings();
        }
    }
    
}



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

public class GpsForm extends Form implements CommandListener, ItemStateListener {
    
    private ChoiceGroup cg = null;
    
    public GpsForm() {
        super("");
      
        boolean gps_state = AppMIDlet.getInstance().getSettings().getStructure().getGpsConfigured();
        if (gps_state) {
            this.addCommand(Resources.CMD_VIEW_GPS_DETAILS);
        }
        this.addCommand(Resources.CMD_BACK);
        
        cg = new ChoiceGroup(Resources.GPSCONFIG, Choice.EXCLUSIVE);
        cg.append(Resources.ON, null);
        cg.append(Resources.OFF, null);
        
        if (gps_state) cg.setSelectedIndex(0, true);
        else cg.setSelectedIndex(1, true);
        
        this.append(cg);
        
        this.setCommandListener(this);
        this.setItemStateListener(this);
    }
    
    public void commandAction(Command c, Displayable d) {
        if (c == Resources.CMD_BACK) {
            AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getSurveyList());
        }
        else if (c == Resources.CMD_VIEW_GPS_DETAILS) {
            if (AppMIDlet.getInstance().getSimpleLocation() == null) {
                try {
                        SimpleLocation loc = new SimpleLocation(true);
                        AppMIDlet.getInstance().setSimpleLocation(loc);
                        AppMIDlet.getInstance().setDisplayable(loc);
                    } catch (Exception e) {
                        AppMIDlet.getInstance().getGeneralAlert().showError(e);
                    }
                } 
            else {                    
                SimpleLocation loc = AppMIDlet.getInstance().getSimpleLocation();
                loc.setDisplay(true);
                AppMIDlet.getInstance().setDisplayable(loc);
            }
        }
    }

    public void itemStateChanged(Item item) {
        if (item.equals(cg)) {           
            if (cg.getSelectedIndex() == 0) {//ON
                this.addCommand(Resources.CMD_VIEW_GPS_DETAILS);
                AppMIDlet.getInstance().getSettings().getStructure().setGpsConfigured(true);
                if (AppMIDlet.getInstance().getSimpleLocation() == null) {
                    try {
                        SimpleLocation loc = new SimpleLocation(true);
                        AppMIDlet.getInstance().setSimpleLocation(loc);
                        AppMIDlet.getInstance().setDisplayable(loc);
                    } catch (Exception e) {
                        AppMIDlet.getInstance().getGeneralAlert().showError(e);
                    }
                } else {                    
                    SimpleLocation loc = AppMIDlet.getInstance().getSimpleLocation();
                    loc.setDisplay(true);
                    AppMIDlet.getInstance().setDisplayable(loc);
                }
            } else if (cg.getSelectedIndex() == 1) {//OFF
                this.removeCommand(Resources.CMD_VIEW_GPS_DETAILS);
                AppMIDlet.getInstance().getSettings().getStructure().setGpsConfigured(false);
                
                if (AppMIDlet.getInstance().getSimpleLocation() != null) {
                    AppMIDlet.getInstance().getSimpleLocation().close();
                    AppMIDlet.getInstance().setSimpleLocation(null);
                }                
            }
            AppMIDlet.getInstance().getSettings().writeSettings();
        }
    }
}

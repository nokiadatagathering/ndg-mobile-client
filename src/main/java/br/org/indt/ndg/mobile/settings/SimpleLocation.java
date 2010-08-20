package br.org.indt.ndg.mobile.settings;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;
import javax.microedition.location.Criteria;
import javax.microedition.location.Location;
import javax.microedition.location.LocationListener;
import javax.microedition.location.LocationProvider;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;

public class SimpleLocation extends TextBox implements CommandListener, LocationListener {
    
    private Criteria crit = null;
    private LocationProvider provider = null;
    private LocationHolder lastLocation = new LocationHolder();
    private boolean display = false;
    private Location curLoc = null;
    private String str = "";
    private boolean connected = false;
    
    public SimpleLocation(boolean _display) throws Exception {
        super(Resources.GPS_LOCAL, Resources.CONNECTING + " \n\n\n\n\n\n", 500, 0);
        
        this.display = _display;
        
        this.addCommand(Resources.CMD_BACK);
        this.addCommand(Resources.CMD_OK);
        this.setCommandListener(this);
        
        crit = new Criteria();
        provider = LocationProvider.getInstance(crit);
        provider.setLocationListener(this, 1, 1, 1);
        
        //if (display) {
            updateDisplay();
            //AppMIDlet.getInstance().setDisplayable(this);
        //}
    }
    
    public void locationUpdated(LocationProvider provider, Location loc) {
        lastLocation.set(loc);
        //if (display)
            updateDisplay();
    }
    
    public void providerStateChanged(LocationProvider provider, int status) {
        
    }
    
    public void setDisplay(boolean _bool) {
        this.display = _bool;
        //updateDisplay();
    }
    
    public void commandAction(Command cmd, Displayable d) {
        AppMIDlet instance = AppMIDlet.getInstance();
        if (cmd == Resources.CMD_BACK) {
            instance.setDisplayable(new GpsForm());
        }
        else if(cmd == Resources.CMD_OK){
            instance.setDisplayable(instance.getSurveyList());
        }
    }
    
    public Location getLocation() {
        return lastLocation.get();
    }
    
    public void close() {
        provider.setLocationListener(null, 0, 0, 0);
        provider.reset();
    }
    
    public void updateDisplay() {
        
        curLoc = lastLocation.get();
        
        str = "";
        
        if (curLoc == null) {
            str += Resources.CONNECTING +"\n\n\n\n\n\n";
            //connected = false;
        }
        else {
            str += Resources.CONNECTED +" \n";
            //connected = true;
        }

        
        if(curLoc != null) {
            str += Resources.LATITUDE + curLoc.getQualifiedCoordinates().getLatitude() + "\n";
            str += Resources.LONGITUDE + curLoc.getQualifiedCoordinates().getLongitude() + "\n";
            str += Resources.ALTITUDE + curLoc.getQualifiedCoordinates().getAltitude() + "\n";
            str += Resources.HORIZONTAL_ACCU + curLoc.getQualifiedCoordinates().getHorizontalAccuracy() + "\n";
            str += Resources.VERTICAL_ACCU + curLoc.getQualifiedCoordinates().getVerticalAccuracy() + "\n";
            connected = true;
        } else
            connected = false;
        
        this.setString(str);
    }
    
    public static class LocationHolder {
        private Location loc;
        public synchronized Location get() { return loc; }
        public synchronized void set(Location l) { loc = l; }
    }

    public boolean isConnected() {
        return connected;
    }
}

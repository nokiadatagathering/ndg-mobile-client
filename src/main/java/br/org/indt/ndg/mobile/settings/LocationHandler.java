/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.mobile.settings;


import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import javax.microedition.location.Criteria;
import javax.microedition.location.Location;
import javax.microedition.location.LocationListener;
import javax.microedition.location.LocationProvider;
import javax.microedition.location.LocationException;

public class LocationHandler implements LocationListener {

    private Criteria criteria = null;
    private LocationHolder lastLocation = new LocationHolder();
    private LocationProvider provider = null;
    volatile private boolean locationPresent = false;
    volatile private boolean updateProvider = false;;
    private int currentState = 0;
    private Thread tUpdate = null;

    public LocationHandler() {
    }

    public int connect() {
        lastLocation.set(null);
        locationPresent = false;
        currentState = 0;
        try {
            provider = LocationProvider.getInstance(criteria);
            if (provider != null) {
                provider.setLocationListener(this, -1, -1, 0);
                currentState = provider.getState();
            }
        } catch (LocationException e) {
        } catch (SecurityException e) {
        }
        return currentState;
    }

    public void disconnect() {
        if (provider != null) {
            try {
                provider.setLocationListener(null, 0, 0, 0);
                provider.reset();
            } catch (SecurityException e) {
            }
        }
    }
    
    public void locationUpdated(LocationProvider lp, Location lctn) {
        if(currentState == LocationProvider.AVAILABLE &&  // result may be mistaken otherwise
                lctn != null &&
                lctn.getQualifiedCoordinates() != null) {
            lastLocation.set(lctn);
            locationPresent = true;
        } else {
            locationPresent = false;
        }
    }

    public void providerStateChanged(LocationProvider lp, int state) {
        currentState = state;
        locationPresent = false;
    }

    public boolean getLocationObtained() {
        return locationPresent;
    }
    
    public void updateServiceOn() {
        if (tUpdate == null || !tUpdate.isAlive()) {
            updateProvider = true;
            tUpdate = new Thread(new UpdateProvider());
            tUpdate.setPriority(Thread.MIN_PRIORITY);
            locationPresent = false;
            tUpdate.start();
        }
    }

    public void updateServiceOff() {
        updateProvider = false;
//        if (tUpdate != null && tUpdate.isAlive()) {
//            tUpdate.interrupt(); //unevitable in order to take  proper care of bluetooth gps
//        }
    }
    
    public String getLocationString() {
        Location curLoc = getLocation();
        String locationString = "";

        if ( !locationPresent ) {
            locationString += Resources.CONNECTING + "\n\n\n\n\n\n";
        } else {
            locationString += Resources.CONNECTED + " \n";
                locationString += Resources.LATITUDE + curLoc.getQualifiedCoordinates().getLatitude() + "\n";
                locationString += Resources.LONGITUDE + curLoc.getQualifiedCoordinates().getLongitude() + "\n";
                locationString += Resources.ALTITUDE + curLoc.getQualifiedCoordinates().getAltitude() + "\n";
                locationString += Resources.HORIZONTAL_ACCU + curLoc.getQualifiedCoordinates().getHorizontalAccuracy() + "\n";
                locationString += Resources.VERTICAL_ACCU + curLoc.getQualifiedCoordinates().getVerticalAccuracy() + "\n";
        }
        return locationString;
    }

    public Location getLocation() {
        return lastLocation.get();
    }

    public static class LocationHolder {
        private Location loc;
        public synchronized Location get() { return loc; }
        public synchronized void set(Location l) { loc = l; }
    }

// in case that of connecting bluetooth gps
        class UpdateProvider implements Runnable {

            public void run() {
                try {
// if location not obtained in
                    Thread.sleep(15000);
                    while (updateProvider && !locationPresent ) {

                        disconnect();
                        connect();
                        Thread.sleep(60000);
                    }
                } catch (InterruptedException ex) {
                    // we do nothing
                } finally {
                    tUpdate = null;
                }

            }

    }
}


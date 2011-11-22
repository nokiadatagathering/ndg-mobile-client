/*
*  Nokia Data Gathering
*
*  Copyright (C) 2011 Nokia Corporation
*
*  This program is free software; you can redistribute it and/or
*  modify it under the terms of the GNU Lesser General Public
*  License as published by the Free Software Foundation; either
*  version 2.1 of the License, or (at your option) any later version.
*
*  This program is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
*  Lesser General Public License for more details.
*
*  You should have received a copy of the GNU Lesser General Public License
*  along with this program.  If not, see <http://www.gnu.org/licenses/
*/

package br.org.indt.ndg.mobile;

import javax.microedition.location.Coordinates;
import javax.microedition.location.Location;
import javax.microedition.location.LocationProvider;

public class LocationHelper {

    private static LocationHelper instance = null;

    private Coordinates currentCoordinates;
    private Coordinates myLocation;

    private LocationHelper() {
    }

    public static LocationHelper getInstance() {
        if(instance == null){
            instance = new LocationHelper();
        }
        return instance;
    }
    
    public Location getLocation() {
        if ( AppMIDlet.getInstance().getLocationHandler() == null )
            return null;
        else
            return  AppMIDlet.getInstance().getLocationHandler().getLocation();
    }

    public Coordinates getCoordinates() {
        if ( getLocation() == null )
            return null;
        else
            return getLocation().getQualifiedCoordinates();
    }

    public void checkAvailable() {
        if(AppMIDlet.getInstance().getSettings().getStructure().getGpsConfigured()) {
            int status = AppMIDlet.getInstance().getLocationHandler().connect();
            if (status == LocationProvider.AVAILABLE ||
                status == LocationProvider.TEMPORARILY_UNAVAILABLE) {
            } else {
                AppMIDlet.getInstance().getSettings().getStructure().setGpsConfigured(false);
            }
        }
    }

    public void setCurrentCoordinatesNull() {
        currentCoordinates = null;
    }

    public void setCurrentCoordinates(double latitude, double longitude, float altitude) {
        currentCoordinates = new Coordinates(latitude, longitude, altitude);
    }

    public Coordinates getCurrentCoordinates() {
        return currentCoordinates;
    }

    public void setGeoTag(Coordinates location) {
        myLocation = location;
    }

    public Coordinates getGeoTag() {
        return myLocation;
    }

}

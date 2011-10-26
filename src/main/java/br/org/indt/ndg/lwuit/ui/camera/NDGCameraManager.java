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

package br.org.indt.ndg.lwuit.ui.camera;

import br.org.indt.ndg.lwuit.model.ImageAnswer;
import br.org.indt.ndg.lwuit.model.ImageData;
import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.error.OutOfMemoryErrorExtended;
import com.sun.lwuit.Button;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import javax.microedition.location.Coordinates;


/**
 *
 * @author amartini
 */
public class NDGCameraManager implements ICameraManager {
    private static NDGCameraManager instance = new NDGCameraManager();

    private ImageAnswer currentImageAnswer;
    private Container imageContainer;
    private Component thumbnailButton;
    private int imageIndex;
    private CameraManagerListener listener;
    private boolean isFromFile = false;


    private NDGCameraManager(){
    }

    public static NDGCameraManager getInstance(){
        return instance;
    }

    public void sendPostProcessData( CameraManagerListener ndgListener,
                                     Component button,
                                     ImageAnswer aAnswer,
                                     Container container ) {
        currentImageAnswer = aAnswer;
        thumbnailButton = button;
        imageContainer = container;
        imageIndex = imageContainer.getComponentIndex(thumbnailButton);
        listener = ndgListener;
    }

    public byte[] getCurrentImageData() throws OutOfMemoryErrorExtended {
        byte[] data = null;
        if ( imageIndex < currentImageAnswer.getImages().size() ){
            ImageData img = (ImageData) currentImageAnswer.getImages().elementAt(imageIndex);
            data = img.getData();
        }
        return data;
    }

    public void updateInterviewForm() {
        ImageData imageData = (ImageData)currentImageAnswer.getImages().elementAt(imageIndex);
        try {
            Button button = (Button) thumbnailButton;
            button.setIcon(imageData.getThumbnail());
        } catch (IllegalArgumentException ex) {
        }

        if (!isFromFile &&
             AppMIDlet.getInstance().getSettings().getStructure().getGeoTaggingConfigured() &&
             AppMIDlet.getInstance().getSettings().getStructure().getGpsConfigured()) {

            Coordinates location = AppMIDlet.getInstance().getCoordinates();

            GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
            if(location == null){
                GeneralAlert.getInstance().show(Resources.WARNING, Resources.ADD_LOCATION_FAILURE, GeneralAlert.WARNING);
            }else if(location != null && !AppMIDlet.getInstance().locationObtained()){
                GeneralAlert.getInstance().show(Resources.WARNING, Resources.LOCATION_OUT_OF_DATE_WARN, GeneralAlert.WARNING);
            }
            imageData.setGeoTag( location );
        }
        listener.update();
    }

    public boolean showLastInterviewForm() {
        listener.update();
        return true;
    }

    public void updatePhotoForm(byte[] picture) {
        ImageData imageData = new ImageData(picture);
        if ( imageIndex >= currentImageAnswer.getImages().size() ) {
            currentImageAnswer.getImages().addElement(imageData);
        } else {
            ((ImageData)currentImageAnswer.getImages().elementAt(imageIndex)).delete();
            currentImageAnswer.getImages().setElementAt(imageData, imageIndex);
        }
    }

    public void deletePhoto() {
        ((ImageData)currentImageAnswer.getImages().elementAt(imageIndex)).delete();
        currentImageAnswer.getImages().removeElementAt(imageIndex);
        imageContainer.removeComponent(thumbnailButton);
    }

    public void setIsFromFile(boolean bVal) {
        isFromFile = bVal;
    }
}

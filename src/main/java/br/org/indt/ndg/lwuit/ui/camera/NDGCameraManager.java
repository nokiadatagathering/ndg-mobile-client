package br.org.indt.ndg.lwuit.ui.camera;

import br.org.indt.ndg.lwuit.model.ImageAnswer;
import br.org.indt.ndg.lwuit.model.ImageData;
import br.org.indt.ndg.mobile.AppMIDlet;
import com.sun.lwuit.Button;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;


/**
 *
 * @author amartini
 */
public class NDGCameraManager {
    private static NDGCameraManager instance = new NDGCameraManager();

    private ImageAnswer currentImageAnswer;
    private Container imageContainer;
    private Component thumbnailButton;
    private int imageIndex;
    private NDGCameraManagerListener listener;

    private NDGCameraManager(){
    }

    public static NDGCameraManager getInstance(){
        return instance;
    }

    public void sendPostProcessData( NDGCameraManagerListener ndgListener,
                                     Component button,
                                     ImageAnswer aAnswer,
                                     Container container ) {
        currentImageAnswer = aAnswer;
        thumbnailButton = button;
        imageContainer = container;
        imageIndex = imageContainer.getComponentIndex(thumbnailButton);
        listener = ndgListener;
    }

    public ImageData getCurrentImageData(){
        if(imageIndex < currentImageAnswer.getImages().size()){
            return (ImageData)currentImageAnswer.getImages().elementAt(imageIndex);
        }
        else
            return null;
    }

    public void updateInterviewForm() {
        try {
            Button button = (Button) thumbnailButton;
            button.setIcon(((ImageData)currentImageAnswer.
                    getImages().elementAt(imageIndex)).getThumbnail());
        } catch (IllegalArgumentException ex) {
        }

        listener.update();
    }

    public boolean showLastInterviewForm() {
        listener.update();
        return true;
    }

    public void updatePhotoForm(byte[] picture) {
        ImageData imageData = new ImageData(picture);
        if ( AppMIDlet.getInstance().getSettings().getStructure().getGeoTaggingConfigured() ) {
            imageData.setGeoTag( AppMIDlet.getInstance().getCoordinates() );
        }
        if ( imageIndex >= currentImageAnswer.getImages().size() ) {
            currentImageAnswer.getImages().addElement(imageData);
        } else {
            ((ImageData)currentImageAnswer.getImages().elementAt(imageIndex)).delete();
            currentImageAnswer.getImages().setElementAt(imageData, imageIndex);
        }
    }

    public void deletePhoto( ) {
        ((ImageData)currentImageAnswer.getImages().elementAt(imageIndex)).delete();
        currentImageAnswer.getImages().removeElementAt(imageIndex);
        imageContainer.removeComponent(thumbnailButton);
    }
}

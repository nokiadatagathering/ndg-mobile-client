package br.org.indt.ndg.lwuit.ui.camera;

import br.org.indt.ndg.lwuit.model.ImageAnswer;
import br.org.indt.ndg.lwuit.model.ImageData;
import br.org.indt.ndg.lwuit.model.ImageQuestion;
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

    private ImageQuestion imageQuestion;
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
                                     ImageQuestion question,
                                     Container container ) {
        if ( question == null) {
            throw new IllegalArgumentException("Neither parameter can be null");
        }
        imageQuestion = question;
        currentImageAnswer = (ImageAnswer) question.getAnswer();
        thumbnailButton = button;
        imageContainer = container;
        imageIndex = imageContainer.getComponentIndex(thumbnailButton);
        listener = ndgListener;
    }

    public ImageQuestion getCurrentImageQuestion(){
        return imageQuestion;
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
        if(imageIndex >= ((ImageAnswer)imageQuestion.getAnswer()).getImages().size()){
            ((ImageAnswer)imageQuestion.getAnswer()).getImages().addElement(imageData);
        } else {
            ((ImageAnswer)imageQuestion.getAnswer()).getImages().setElementAt(imageData, imageIndex);
        }
    }

    public void deletePhoto( ) {
        ((ImageData)((ImageAnswer)imageQuestion.getAnswer()).getImages().elementAt(imageIndex)).delete();
        ((ImageAnswer)imageQuestion.getAnswer()).getImages().removeElementAt(imageIndex);
        imageContainer.removeComponent(thumbnailButton);
    }
}

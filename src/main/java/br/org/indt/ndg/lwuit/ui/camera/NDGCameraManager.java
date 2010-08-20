/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui.camera;

import br.org.indt.ndg.lwuit.model.ImageQuestion;
import br.org.indt.ndg.lwuit.ui.InterviewForm;
import br.org.indt.ndg.lwuit.ui.InterviewForm2;
import br.org.indt.ndg.lwuit.ui.Screen;
import br.org.indt.ndg.mobile.multimedia.Camera;
import br.org.indt.ndg.mobile.multimedia.Picture;
import com.sun.lwuit.Button;
import com.sun.lwuit.Component;
import com.sun.lwuit.Image;

/**
 *
 * @author amartini
 */
public class NDGCameraManager {
    private static NDGCameraManager instance = new NDGCameraManager();

    private ImageQuestion imageQuestion;
    private InterviewForm2 interviewForm;

    private NDGCameraManager(){
    }

    public static NDGCameraManager getInstance(){
        return instance;
    }

    public void displayCamera(InterviewForm2 form, ImageQuestion question){
        if(form == null || question == null)
            throw new IllegalArgumentException("Neither parameter can be null");
        imageQuestion = question;
        interviewForm = form;
        if(question.getAnswer().getValue() == null){
            ViewFinderFormLCDUICanvas.getInstance().show();
        }
        else            
            Screen.show(PhotoForm.class, true);
    }

    public ImageQuestion getCurrentImageQuestion(){
        return imageQuestion;
    }

    public void updateInterviewForm() {
        Component components[] = (Component[]) interviewForm.getGroups().elementAt(interviewForm.getFocusIndex());
        Object interviewFormElement = components[0];
        if(interviewFormElement instanceof Button){
            Picture picture = Picture.createPicture((byte[]) imageQuestion.getAnswer().getValue());
            if(picture != null){
                Button button = (Button) interviewFormElement;
                button.setIcon(picture.getThumbnail());
                //button.setIcon(Screen.getRes().getImage("camera-icon-imagetaken"));
            }
            interviewForm.show();
        }
        else
            throw new IllegalStateException("A button should exist here");
    }

    void updatePhotoForm(byte[] picture) {
        imageQuestion.getAnswer().setValue(picture);
        Screen.show(PhotoForm.class, true);
    }
}

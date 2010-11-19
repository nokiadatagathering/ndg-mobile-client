/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui.camera;

import br.org.indt.ndg.lwuit.extended.Form;
import br.org.indt.ndg.lwuit.model.ImageQuestion;
import br.org.indt.ndg.lwuit.ui.InterviewForm;
import br.org.indt.ndg.lwuit.ui.InterviewForm2;
import br.org.indt.ndg.lwuit.ui.Screen;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.multimedia.Picture;
import com.sun.lwuit.Button;
import com.sun.lwuit.Component;

/**
 *
 * @author amartini
 */
public class NDGCameraManager {
    private static NDGCameraManager instance = new NDGCameraManager();

    private ImageQuestion imageQuestion;
    private InterviewForm2 interviewForm2;
    private Form interviewForm;
    private Component component;

    private NDGCameraManager(){
    }

    public static NDGCameraManager getInstance(){
        return instance;
    }

    public void displayCamera(InterviewForm2 form, ImageQuestion question){
        if(form == null || question == null)
            throw new IllegalArgumentException("Neither parameter can be null");
        imageQuestion = question;
        interviewForm2 = form;
        if(question.getAnswer().getValue() == null){
            AppMIDlet.getInstance().setDisplayable( ViewFinderForm.class );
        }
        else            
            Screen.show(PhotoForm.class, true);
    }

   public void displayCamera(Form form, Component button, ImageQuestion question){
        if( form == null || question == null)
            throw new IllegalArgumentException("Neither parameter can be null");
        imageQuestion = question;
        interviewForm = form;
        this.component = button;
        if(question.getAnswer().getValue() == null){
            AppMIDlet.getInstance().setDisplayable( ViewFinderForm.class );
        }
        else
            Screen.show(PhotoForm.class, true);
    }

    public ImageQuestion getCurrentImageQuestion(){
        return imageQuestion;
    }

    public void updateInterviewForm() {
        if( interviewForm2 != null ) {

            Component components[] = (Component[]) interviewForm2.getGroups().elementAt(interviewForm2.getFocusIndex());
            Object interviewFormElement = components[0];
            if(interviewFormElement instanceof Button)
            {
                Picture picture = Picture.createPicture((byte[]) imageQuestion.getAnswer().getValue());
                if(picture != null){
                    Button button = (Button) interviewFormElement;
                    button.setIcon(picture.getThumbnail());
                }
                interviewForm2.show();
            }
        }
        else if ( interviewForm != null )
        {
                Picture picture = Picture.createPicture((byte[]) imageQuestion.getAnswer().getValue());
                if(picture != null){
                    Button button = (Button) component;
                    button.setIcon(picture.getThumbnail());
                }
                interviewForm.show();
        }
        else
            throw new IllegalStateException("A button should exist here");

        interviewForm = null;
        component = null;
        interviewForm2 = null;
    }

    void updatePhotoForm(byte[] picture) {
        imageQuestion.getAnswer().setValue(picture);
    }
}

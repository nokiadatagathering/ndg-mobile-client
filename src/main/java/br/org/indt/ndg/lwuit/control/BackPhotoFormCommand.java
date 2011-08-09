package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.InterviewForm;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.multimedia.Camera;
import com.sun.lwuit.Command;

/**
 *
 * @author amartini
 */
public class BackPhotoFormCommand extends CommandControl{

    private static BackPhotoFormCommand instance;

    private BackPhotoFormCommand(){}

    public static BackPhotoFormCommand getInstance(){
        if(instance == null)
            instance = new BackPhotoFormCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command( Resources.NEWUI_BACK );
    }

    protected void doAction(Object parameter) {
        Camera.getInstance().stopCamera();
        if (!AppMIDlet.getInstance().getCurrentCameraManager().showLastInterviewForm()) {
            AppMIDlet.getInstance().setDisplayable(InterviewForm.class);
        }
    }

}

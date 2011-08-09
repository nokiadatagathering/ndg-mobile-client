package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.multimedia.Camera;
import com.sun.lwuit.Command;

/**
 *
 * @author amartini
 */
public class OKPhotoFormCommand extends CommandControl{

    private static OKPhotoFormCommand instance;

    private OKPhotoFormCommand(){}

    public static OKPhotoFormCommand getInstance(){
        if(instance == null)
            instance = new OKPhotoFormCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command( Resources.NEW_IU_OK );
    }

    protected void doAction(Object parameter) {
        Camera.getInstance().stopCamera();
        AppMIDlet.getInstance().getCurrentCameraManager().updateInterviewForm();
    }

}

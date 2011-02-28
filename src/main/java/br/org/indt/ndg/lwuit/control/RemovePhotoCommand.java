package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.camera.NDGCameraManager;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

public class RemovePhotoCommand extends CommandControl{
    private static RemovePhotoCommand instance;

    private RemovePhotoCommand(){}

    protected Command createCommand() {
        return new Command(Resources.DELETE_PHOTO);
    }

    protected void doAction(Object parameter) {
        NDGCameraManager.getInstance().deletePhoto();
        NDGCameraManager.getInstance().showLastInterviewForm();
    }

    public static RemovePhotoCommand getInstance(){
        if(instance == null)
            instance = new RemovePhotoCommand();
        return instance;
    }

}

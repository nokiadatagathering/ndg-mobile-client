package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

public class RemovePhotoCommand extends CommandControl{
    private static RemovePhotoCommand instance;

    private RemovePhotoCommand(){}

    protected Command createCommand() {
        return new Command(Resources.DELETE_PHOTO);
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().getCurrentCameraManager().deletePhoto();
        AppMIDlet.getInstance().getCurrentCameraManager().showLastInterviewForm();
    }

    public static RemovePhotoCommand getInstance(){
        if(instance == null)
            instance = new RemovePhotoCommand();
        return instance;
    }

}

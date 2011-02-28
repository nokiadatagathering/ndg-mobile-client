package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.camera.ViewFinderForm;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

public class CapturePhotoCommand extends CommandControl {

    private static CapturePhotoCommand instance;

    private CapturePhotoCommand(){}

    public static CapturePhotoCommand getInstance(){
        if(instance == null)
            instance = new CapturePhotoCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command(Resources.CAPTURE_PHOTO);
    }

    protected void doAction(Object parameter) {
        ViewFinderForm view = (ViewFinderForm) parameter;
        view.capturePictureAndShowPhoto();
    }
}

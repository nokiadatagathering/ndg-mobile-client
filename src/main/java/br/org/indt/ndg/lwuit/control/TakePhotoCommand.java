package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.camera.ViewFinderForm;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

/**
 *
 * @author amartini
 */
public class TakePhotoCommand extends CommandControl{
    private static TakePhotoCommand instance;

    private TakePhotoCommand(){}

    protected Command createCommand() {
        return new Command(Resources.TAKE_PHOTO);
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().setDisplayable( ViewFinderForm.class );
    }

    public static TakePhotoCommand getInstance(){
        if(instance == null)
            instance = new TakePhotoCommand();
        return instance;
    }

}

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import br.org.indt.ndg.lwuit.ui.Screen;
import br.org.indt.ndg.lwuit.ui.camera.PhotoForm;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

public class ShowPhotoCommand extends CommandControl{
    private static ShowPhotoCommand instance;

    private ShowPhotoCommand(){}

    protected Command createCommand() {
        return new Command( Resources.SHOW_PHOTO );
    }

    protected void doAction(Object parameter) {
            try {
                Screen.show(PhotoForm.class, true);
            } catch(OutOfMemoryError err) {
                GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
                GeneralAlert.getInstance().show(Resources.WARNING, Resources.MEMORY_OUT , GeneralAlert.WARNING);
            }
    }

    public static ShowPhotoCommand getInstance(){
        if(instance == null)
            instance = new ShowPhotoCommand();
        return instance;
    }
}

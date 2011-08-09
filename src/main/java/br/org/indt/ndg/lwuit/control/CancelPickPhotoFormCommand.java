package br.org.indt.ndg.lwuit.control;
import br.org.indt.ndg.lwuit.ui.InterviewForm;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

public class CancelPickPhotoFormCommand extends BackCommand {

    private static CancelPickPhotoFormCommand instance;

    public static CancelPickPhotoFormCommand getInstance() {
        if (instance == null)
            instance = new CancelPickPhotoFormCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command(Resources.NEWUI_BACK);
    }

    protected void doAction(Object parameter) {
        if (!AppMIDlet.getInstance().getCurrentCameraManager().showLastInterviewForm()) {
            AppMIDlet.getInstance().setDisplayable(InterviewForm.class);
        }
    }

}

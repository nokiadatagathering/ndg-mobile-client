/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;
import br.org.indt.ndg.lwuit.ui.InterviewForm;
import br.org.indt.ndg.lwuit.ui.camera.NDGCameraManager;
import br.org.indt.ndg.mobile.AppMIDlet;
import com.sun.lwuit.Command;
import com.nokia.mid.appl.cmd.Local;

public class CancelPickPhotoFormCommand extends BackCommand {

    private static CancelPickPhotoFormCommand instance;

    public static CancelPickPhotoFormCommand getInstance() {
        if (instance == null)
            instance = new CancelPickPhotoFormCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CMD_BACK));
    }

    protected void doAction(Object parameter) {
        if (!NDGCameraManager.getInstance().showLastInterviewForm()) {
            AppMIDlet.getInstance().setDisplayable(InterviewForm.class);
        }
    }

}

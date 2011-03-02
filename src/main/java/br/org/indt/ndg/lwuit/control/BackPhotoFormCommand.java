/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.InterviewForm;
import br.org.indt.ndg.lwuit.ui.camera.NDGCameraManager;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.multimedia.Camera;
import com.nokia.mid.appl.cmd.Local;
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
        return new Command(Local.getText(Local.QTJ_CMD_BACK));
    }

    protected void doAction(Object parameter) {
        Camera.getInstance().stopCamera();
        if (!NDGCameraManager.getInstance().showLastInterviewForm()) {
            AppMIDlet.getInstance().setDisplayable(InterviewForm.class);
        }
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.camera.NDGCameraManager;
import br.org.indt.ndg.mobile.multimedia.Camera;
import com.nokia.mid.appl.cmd.Local;
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
        return new Command(Local.getText(Local.QTJ_CMD_OK));
    }

    protected void doAction(Object parameter) {
        Camera.getInstance().shutDown();
        NDGCameraManager.getInstance().updateInterviewForm();
    }

}

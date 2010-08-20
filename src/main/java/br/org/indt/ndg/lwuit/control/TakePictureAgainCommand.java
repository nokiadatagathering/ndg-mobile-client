/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.camera.ViewFinderFormLCDUICanvas;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

/**
 *
 * @author amartini
 */
public class TakePictureAgainCommand extends CommandControl{
    private static TakePictureAgainCommand instance;

    private TakePictureAgainCommand(){}

    protected Command createCommand() {
        return new Command(Resources.TRY_AGAIN);
    }

    protected void doAction(Object parameter) {
        ViewFinderFormLCDUICanvas.getInstance().show();
    }

    public static TakePictureAgainCommand getInstance(){
        if(instance == null)
            instance = new TakePictureAgainCommand();
        return instance;
    }

}

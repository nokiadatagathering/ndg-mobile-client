/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.lwuit.ui.SurveyList;
import com.sun.lwuit.Command;


public class RestoreToDefaultUISettingCommand extends CommandControl {

    private static RestoreToDefaultUISettingCommand instance;

    public static RestoreToDefaultUISettingCommand getInstance() {
        if (instance == null)
            instance = new RestoreToDefaultUISettingCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command( Resources.RESTORE_DEFAULT );
    }

    protected void doAction(Object parameter) {
        NDGStyleToolbox.getInstance().reset();
        NDGStyleToolbox.getInstance().saveSettings();
        AppMIDlet.getInstance().setDisplayable( SurveyList.class );
    }
}

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.SettingsForm;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

/**
 *
 * @author damian.janicki
 */
public class OpenSettingsForm extends CommandControl{

    private static OpenSettingsForm instance;

    public static OpenSettingsForm getInstance(){
        if(instance == null){
            instance = new OpenSettingsForm();
        }
        return instance;
    }

    protected Command createCommand() {
        return new Command(Resources.SETTINGS);
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().setDisplayable( SettingsForm.class );
    }
}

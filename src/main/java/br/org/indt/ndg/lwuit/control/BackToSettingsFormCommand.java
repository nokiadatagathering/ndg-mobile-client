package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.SettingsForm;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

/**
 *
 * @author damian.janicki
 */
public class BackToSettingsFormCommand extends BackCommand{

    private static BackToSettingsFormCommand instance = null;

    public static BackToSettingsFormCommand getInstance(){
        if(instance == null){
            instance = new BackToSettingsFormCommand();
        }
        return instance;
    }
    protected Command createCommand() {
        return new Command( Resources.NEWUI_BACK );
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().setDisplayable(SettingsForm.class);
    }

}

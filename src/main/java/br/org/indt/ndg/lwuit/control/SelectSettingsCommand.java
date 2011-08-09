package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

/**
 *
 * @author damian.janicki
 */
public class SelectSettingsCommand extends CommandControl{

    private static SelectSettingsCommand instance = null;

    public static SelectSettingsCommand getInstance(){
        if(instance == null){
            instance = new SelectSettingsCommand();
        }
        return instance;
    }

    protected Command createCommand() {
        return new Command( Resources.NEWUI_SELECT );
    }

    protected void doAction(Object parameter) {
    }

}

package br.org.indt.ndg.lwuit.control;

import com.nokia.mid.appl.cmd.Local;
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
        return new Command(Local.getText(Local.QTJ_CMD_SELECT));
    }

    protected void doAction(Object parameter) {
    }

}

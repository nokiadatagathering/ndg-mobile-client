package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.SettingsForm;
import com.nokia.mid.appl.cmd.Local;
import com.sun.lwuit.Command;
import br.org.indt.ndg.mobile.AppMIDlet;

public class OkSimpleLocationCommand extends BackCommand {

    private static OkSimpleLocationCommand instance;

    public static OkSimpleLocationCommand getInstance() {
        if (instance == null)
            instance = new OkSimpleLocationCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CMD_OK));
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().setDisplayable(SettingsForm.class);
    }

}

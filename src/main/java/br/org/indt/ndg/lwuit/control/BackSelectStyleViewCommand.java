package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.SurveyList;
import br.org.indt.ndg.mobile.AppMIDlet;
import com.sun.lwuit.Command;
import com.nokia.mid.appl.cmd.Local;

public class BackSelectStyleViewCommand extends BackCommand {

    private static BackSelectStyleViewCommand instance;

    public static BackSelectStyleViewCommand getInstance() {
        if (instance == null)
            instance = new BackSelectStyleViewCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CMD_BACK));
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().setDisplayable(SurveyList.class);
    }
}

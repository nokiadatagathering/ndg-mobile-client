package br.org.indt.ndg.lwuit.control;
import br.org.indt.ndg.lwuit.ui.SurveyList;
import br.org.indt.ndg.mobile.AppMIDlet;
import com.sun.lwuit.Command;
import com.nokia.mid.appl.cmd.Local;

public class BackResolutionSelectFormCommand extends BackCommand {

    private static BackResolutionSelectFormCommand instance;

    public static BackResolutionSelectFormCommand getInstance() {
        if (instance == null)
            instance = new BackResolutionSelectFormCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CMD_BACK));
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().setDisplayable(SurveyList.class);
    }

}

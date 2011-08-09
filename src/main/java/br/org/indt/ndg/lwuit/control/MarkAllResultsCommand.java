package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.model.CheckableListModel;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;


/**
 *
 * @author Alexandre Martini
 */
public class MarkAllResultsCommand extends CommandControl{
    private static MarkAllResultsCommand instance = new MarkAllResultsCommand();

    private MarkAllResultsCommand(){}

    public static MarkAllResultsCommand getInstance(){
        return instance;
    }

    protected Command createCommand() {
        return new Command(Resources.CMD_MARKALL);
    }

    protected void doAction(Object parameter) {
        CheckableListModel model = (CheckableListModel) parameter;
        model.markAll();
    }

}

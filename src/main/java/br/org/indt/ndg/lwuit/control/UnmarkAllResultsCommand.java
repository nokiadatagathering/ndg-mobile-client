package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.model.CheckableListModel;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

/**
 *
 * @author Alexandre Martini
 */
public class UnmarkAllResultsCommand extends CommandControl{
    private static UnmarkAllResultsCommand instance = new UnmarkAllResultsCommand();

    private UnmarkAllResultsCommand(){}

    public static UnmarkAllResultsCommand getInstance(){
        return instance;
    }

    protected Command createCommand() {
        return new Command( Resources.CMD_UNMARKALL );
    }

    protected void doAction(Object parameter) {
        CheckableListModel model = (CheckableListModel) parameter;
        model.unmarkAll();
    }

}

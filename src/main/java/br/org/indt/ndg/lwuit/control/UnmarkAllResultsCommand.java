/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.model.CheckableListModel;
import com.nokia.mid.appl.cmd.Local;
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
        return new Command(Local.getText(Local.QTJ_CMD_UNMARKALL));
    }

    protected void doAction(Object parameter) {
        CheckableListModel model = (CheckableListModel) parameter;
        model.unmarkAll();
    }

}

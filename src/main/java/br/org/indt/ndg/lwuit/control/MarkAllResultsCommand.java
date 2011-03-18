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
public class MarkAllResultsCommand extends CommandControl{
    private static MarkAllResultsCommand instance = new MarkAllResultsCommand();

    private MarkAllResultsCommand(){}

    public static MarkAllResultsCommand getInstance(){
        return instance;
    }

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CMD_MARKALL));
    }

    protected void doAction(Object parameter) {
        CheckableListModel model = (CheckableListModel) parameter;
        model.markAll();
    }

}

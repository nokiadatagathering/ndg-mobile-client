/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.lwuit.ui.SentResultListCellRenderer;
import com.nokia.mid.appl.cmd.Local;
import com.sun.lwuit.Command;

/**
 *
 * @author Alexandre Martini
 */
public class DeleteSentResultCommand extends CommandControl{

    private static DeleteSentResultCommand instance = new DeleteSentResultCommand();

    private DeleteSentResultCommand(){}

    public static DeleteSentResultCommand getInstance(){
        return instance;
    }

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CMD_DELETE));
    }

    protected void doAction(Object parameter) {
        // mark the old screen with selected checkboxes
        SentResultListCellRenderer renderer = (SentResultListCellRenderer) parameter;
        boolean[] listFlags = renderer.getSelectedFlags();
        SurveysControl.getInstance().getCurrentOldSentList().setSelectedFlags(listFlags);

        // call command action in old screen
        SurveysControl.getInstance().getCurrentOldSentList().commandAction(Resources.CMD_DELETE, null);
    }

}

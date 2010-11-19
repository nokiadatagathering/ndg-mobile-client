/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.lwuit.ui.GeneralAlert;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.ResultList;
import com.sun.lwuit.Command;

/**
 *
 * @author Alexandre Martini
 */
public class DeleteResultNowCommand extends CommandControl {
    private static DeleteResultNowCommand instance = new DeleteResultNowCommand();

    public static DeleteResultNowCommand getInstance(){
        return instance;
    }

    private DeleteResultNowCommand(){}

    protected Command createCommand() {
        return new Command(Resources.NEWUI_DELETE_CURRENT_RESULT);
    }

    protected void doAction(Object parameter) {

        boolean[] listFlags = (boolean[]) parameter;

        GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_YES_NO, true);
        int resultCmdIndex = GeneralAlert.getInstance().show(Resources.CMD_DELETE, Resources.DELETE_RESULTS_CONFIRMATION, GeneralAlert.CONFIRMATION);
        if (resultCmdIndex == GeneralAlert.RESULT_YES) {
            SurveysControl.getInstance().deleteResults(listFlags);
            AppMIDlet.getInstance().setResultList(new ResultList());
            AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.ResultList.class);
        }
    }
    
}

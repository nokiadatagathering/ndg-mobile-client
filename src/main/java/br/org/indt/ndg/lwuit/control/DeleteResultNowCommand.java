/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.lwuit.ui.GeneralAlert;

import br.org.indt.ndg.mobile.AppMIDlet;
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

        /*SurveysControl.getInstance().getCurrentOldDeleteList().setSelectedFlags(listFlags);
        // call command action in old screen
        SurveysControl.getInstance().getCurrentOldDeleteList().commandAction(Resources.CMD_DELETE, null);
        */
        GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_YES_NO, true);
        int resultCmdIndex = GeneralAlert.getInstance().show(Resources.CMD_DELETE.getLabel(), Resources.DELETE_RESULTS_CONFIRMATION, GeneralAlert.CONFIRMATION);
        if (resultCmdIndex == GeneralAlert.RESULT_YES) {
            SurveysControl.getInstance().deleteResults(listFlags);
            AppMIDlet.getInstance().setResultList(new br.org.indt.ndg.mobile.ResultList());
            AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getResultList());
        }
    }
    
}

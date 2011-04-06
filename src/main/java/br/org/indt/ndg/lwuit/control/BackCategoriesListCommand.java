package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

/**
 *
 * @author mluz
 */
public class BackCategoriesListCommand extends BackCommand implements SaveResultsObserver {

    private static BackCategoriesListCommand instance;

    protected Command createCommand() {
        return new Command(Resources.NEWUI_BACK);
    }

    protected void doAction(Object parameter) {
        // ask whether result shall be discarded or saved to file
        GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_YES_NO, true);
        if ( GeneralAlert.RESULT_YES ==  GeneralAlert.getInstance().show( Resources.CMD_SAVE,
                                            "Do You want to save the survey?", // TODO localization
                                             GeneralAlert.CONFIRMATION) ) {
            SaveResultCommand.getInstance().setObserver(this);
            SaveResultCommand.getInstance().execute(null);
        } else {
            moveToPreviousView();
        }
    }

    public void onResultsSaved() {
        moveToPreviousView();
    }

    protected void moveToPreviousView() {
        AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.ResultList.class);
    }

    public static BackCategoriesListCommand getInstance() {
        if (instance == null)
            instance = new BackCategoriesListCommand();
        return instance;
    }

}

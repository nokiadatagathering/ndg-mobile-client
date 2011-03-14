package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import br.org.indt.ndg.lwuit.ui.InterviewForm;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;
import com.sun.lwuit.events.ActionEvent;

/**
 *
 * @author mturiel
 */
public class BackInterviewFormCommand extends BackCommand {

    private static BackInterviewFormCommand instance;

    protected Command createCommand() {
        return new Command(Resources.NEWUI_BACK);
    }

    protected void doAction(Object parameter) {
        InterviewForm view = (InterviewForm)parameter;
        // ask whether chanes shall be discarded or saved (in memory, NOT in file)
        GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_YES_NO, true);
        if ( view.isModifiedInterview() &&
                GeneralAlert.getInstance().show( Resources.CMD_SAVE,
                Resources.DISCARD_CHANGES, GeneralAlert.CONFIRMATION)
                    == GeneralAlert.RESULT_NO ) {
            // if accepted proceed as if 'Next' was clicked
            view.actionPerformed(new ActionEvent(AcceptQuestionListFormCommand.getInstance().getCommand()));
        } else {
            if ( SurveysControl.getInstance().hasMoreThenOneCategory() ) {
                AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.CategoryList.class);
            } else {
                AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.ResultList.class);
            }
        }
    }

    public static BackInterviewFormCommand getInstance() {
        if (instance == null)
            instance = new BackInterviewFormCommand();
        return instance;
    }
}

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import br.org.indt.ndg.lwuit.ui.InterviewForm;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;


public class AcceptQuestionListFormCommand extends BackCommand implements SaveResultsObserver {

    private static AcceptQuestionListFormCommand instance;

    protected Command createCommand() {
        return new Command(Resources.NEWUI_NEXT);//change resource name
    }

    protected void doAction(Object parameter) {
        InterviewForm view = (InterviewForm)parameter;
        // commit results and decide where to go based on category count
        if ( view.validateAllAnswersAndResetModifiedFlag() ) {
            GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_YES_NO, true);
            if ( SurveysControl.getInstance().hasMoreThenOneCategory() ) {
                leaveInterviewForm();
            } else if ( GeneralAlert.getInstance().show( Resources.CMD_SAVE,
                        Resources.SAVE_MODIFICATIONS, GeneralAlert.CONFIRMATION ) // TODO change to something more meaningful like "Save (& Quit) Interview?"
                            == GeneralAlert.RESULT_YES ) {
                // ^ask to save results if this is the only category
                SaveResultCommand.getInstance().setObserver(this);
                SaveResultCommand.getInstance().execute(SurveysControl.getInstance().getQuestionsFlat());
            } else {
                leaveInterviewForm();
            }
        } else {
            // do nothing, user has to correct input
        }
    }

    private void leaveInterviewForm() {
        if ( SurveysControl.getInstance().hasMoreThenOneCategory() ) {
            AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.CategoryList.class);
        } else {
            AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.ResultList.class);
        }
    }

    public void onResultsSaved() {
        leaveInterviewForm();
    }

    public static AcceptQuestionListFormCommand getInstance() {
        if (instance == null)
            instance = new AcceptQuestionListFormCommand();
        return instance;
    }
}

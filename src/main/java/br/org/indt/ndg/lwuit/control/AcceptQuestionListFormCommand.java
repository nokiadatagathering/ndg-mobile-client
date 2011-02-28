package br.org.indt.ndg.lwuit.control;

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
            if ( SurveysControl.getInstance().hasMoreThenOneCategory() ) {
                AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.CategoryList.class);
            } else {
                // save results if this is the only category
                SaveResultCommand.getInstance().setObserver(this);
                SaveResultCommand.getInstance().execute(SurveysControl.getInstance().getQuestionsFlat());
            }
        } else {
            // do nothing, user has to correct input
        }
    }

    public void onResultsSaved() {
        AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.ResultList.class);
    }

    public static AcceptQuestionListFormCommand getInstance() {
        if (instance == null)
            instance = new AcceptQuestionListFormCommand();
        return instance;
    }
}

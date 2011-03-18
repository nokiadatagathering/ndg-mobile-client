package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.model.CategoryConditional;
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
            if ( SurveysControl.getInstance().getSelectedCategory() instanceof CategoryConditional ) {
                AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.CategoryConditionalList.class);
            } else {
                AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.CategoryList.class);
            }
        } else {
            // do nothing, user has to correct input
        }
    }

    private void leaveInterviewForm() {
          if ( SurveysControl.getInstance().getSelectedCategory() instanceof CategoryConditional ) {
                AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.CategoryConditionalList.class);
          } else {
                AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.CategoryList.class);
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

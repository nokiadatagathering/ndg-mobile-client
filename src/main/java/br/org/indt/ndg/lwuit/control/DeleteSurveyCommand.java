package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import br.org.indt.ndg.lwuit.ui.SurveyList;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

/**
 *
 * @author mluz
 */
public class DeleteSurveyCommand extends CommandControl {

    private static DeleteSurveyCommand instance;

    protected Command createCommand() {
        return new Command(Resources.DELETE_SURVEY);
    }

    protected void doAction(Object parameter) {
        int selectedIndex = ((Integer)parameter).intValue();

        GeneralAlert.getInstance().addCommand( GeneralAlert.DIALOG_YES_NO , true );
        if ( GeneralAlert.RESULT_YES == GeneralAlert.getInstance().show(Resources.DELETE_CONFIRMATION,Resources.DELETE_SURVEY_CONFIRMATION, GeneralAlert.CONFIRMATION ) )
        {
            AppMIDlet.getInstance().getSurveyList().setSurveyCurrentIndex(selectedIndex);
            AppMIDlet.getInstance().getSurveyList().getList().removeElementAt(selectedIndex+1);
            AppMIDlet.getInstance().getSurveyList().deleteSurvey();
            AppMIDlet.getInstance().setDisplayable(SurveyList.class);
        }
    }

    public static DeleteSurveyCommand getInstance() {
        if (instance == null)
            instance = new DeleteSurveyCommand();
        return instance;
    }
}


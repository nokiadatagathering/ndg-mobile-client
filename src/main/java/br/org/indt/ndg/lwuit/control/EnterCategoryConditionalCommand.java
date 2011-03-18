package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.InterviewForm;
import br.org.indt.ndg.mobile.AppMIDlet;
import com.nokia.mid.appl.cmd.Local;
import com.sun.lwuit.Command;

/**
 *
 * @author mluz
 */
public class EnterCategoryConditionalCommand extends CommandControl {

    private static EnterCategoryConditionalCommand instance;

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CMD_RESULTS));
    }

    protected void doAction(Object parameter) {
        int selectedIndex = ((Integer)parameter).intValue();
        SurveysControl.getInstance().setSelectedSubCategoryIndex(selectedIndex);
        AppMIDlet.getInstance().setDisplayable( InterviewForm.class );
    }

    public static EnterCategoryConditionalCommand getInstance() {
        if (instance == null)
            instance = new EnterCategoryConditionalCommand();
        return instance;
    }
}

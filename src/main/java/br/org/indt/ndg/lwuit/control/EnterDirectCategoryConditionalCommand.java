package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.model.CategoryConditional;
import br.org.indt.ndg.lwuit.ui.CategoryConditionalList;
import br.org.indt.ndg.lwuit.ui.InterviewForm;
import br.org.indt.ndg.lwuit.ui.QuantityForm;
import br.org.indt.ndg.mobile.AppMIDlet;
import com.nokia.mid.appl.cmd.Local;
import com.sun.lwuit.Command;

public class EnterDirectCategoryConditionalCommand extends CommandControl {

    private static EnterDirectCategoryConditionalCommand instance;

    protected Command createCommand() {
        return new Command("Open");//TODO localize
    }

    protected void doAction(Object parameter) {
        int selectedIndex = ((Integer)parameter).intValue();
        SurveysControl.getInstance().setSelectedCategory(selectedIndex);
        if ( SurveysControl.getInstance().getSelectedCategory() instanceof CategoryConditional ) {
            AppMIDlet.getInstance().setDisplayable(CategoryConditionalList.class);
        } else {
            AppMIDlet.getInstance().setDisplayable(InterviewForm.class);
        }
    }

    public static EnterDirectCategoryConditionalCommand getInstance() {
        if (instance == null)
            instance = new EnterDirectCategoryConditionalCommand();
        return instance;
    }
}

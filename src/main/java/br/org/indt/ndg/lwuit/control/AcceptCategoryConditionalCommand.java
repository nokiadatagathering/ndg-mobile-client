package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.CategoryList;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;


public class AcceptCategoryConditionalCommand extends BackCommand {

    private static AcceptCategoryConditionalCommand instance;

    protected Command createCommand() {
        return new Command(Resources.NEWUI_NEXT);//change resource name
    }

    protected void doAction(Object parameter) {
       SurveysControl.getInstance().setSelectedSubCategoryIndex(0);
       AppMIDlet.getInstance().setDisplayable( CategoryList.class );
    }

    public static AcceptCategoryConditionalCommand getInstance() {
        if (instance == null)
            instance = new AcceptCategoryConditionalCommand();
        return instance;
    }
}

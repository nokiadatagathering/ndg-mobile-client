package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;


public class BackCategoryConditionalFormCommand extends BackCommand {

    private static BackCategoryConditionalFormCommand instance;

    protected Command createCommand() {
        return new Command(Resources.NEWUI_BACK);
    }

    protected void doAction(Object parameter) {
        SurveysControl.getInstance().setSelectedSubCategoryIndex(0);
        AppMIDlet.getInstance().setDisplayable( br.org.indt.ndg.lwuit.ui.CategoryList.class );
    }

    public static BackCategoryConditionalFormCommand getInstance() {
        if (instance == null)
            instance = new BackCategoryConditionalFormCommand();
        return instance;
    }
}

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.model.CategoryConditional;
import br.org.indt.ndg.lwuit.ui.CategoryConditionalList;
import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import br.org.indt.ndg.lwuit.ui.InterviewForm;
import br.org.indt.ndg.lwuit.ui.QuantityForm;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.nokia.mid.appl.cmd.Local;
import com.sun.lwuit.Command;

/**
 *
 * @author mluz
 */
public class EnterCategoryCommand extends CommandControl {

    private static EnterCategoryCommand instance;

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CMD_RESULTS));
    }

    protected void doAction(Object parameter) {
        int selectedIndex = ((Integer)parameter).intValue();
        SurveysControl.getInstance().setSelectedCategory(selectedIndex);

        if ( SurveysControl.getInstance().getSelectedCategory() instanceof CategoryConditional ) {
            CategoryConditional cat = (CategoryConditional) SurveysControl.getInstance().getSelectedCategory();
            QuantityForm.show( cat.getConditionQuestion(), String.valueOf( cat.getQuantity() ) );
            String quanityStr = SurveysControl.getInstance().getItemOtherText();
            int quantity = 0;
            try {
                quantity = Integer.parseInt( quanityStr );
                if( quantity < 0) {
                    quantity = 0;
                }
            } catch (NumberFormatException ex) {
                quantity = 0;
            }

            GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_YES_NO, true);
            if( quantity < cat.getQuantity() ) {//confirm removing
                if ( GeneralAlert.getInstance().show( Resources.REMOVE_CATEGORIES,
                                    Resources.CATEGORIES_LIMIT, GeneralAlert.CONFIRMATION ) == GeneralAlert.RESULT_YES ) {
                    cat.setQuantity( quantity );
                    SurveysControl.getInstance().removeResultsFromConditionaCategory( cat );
                }
            } else if ( quantity > cat.getQuantity() ) {//confirm adding
                if ( GeneralAlert.getInstance().show( Resources.ADD_CATEGORIES, Resources.ADD_ADDITIONAL_COPIES, GeneralAlert.CONFIRMATION ) == GeneralAlert.RESULT_YES ) {
                    SurveysControl.getInstance().prepareResultsForConditionaCategory( cat, quantity );
                    cat.setQuantity( quantity );
                }
            }

            if ( cat.getQuantity() > 0 ) {
                AppMIDlet.getInstance().setDisplayable(CategoryConditionalList.class);
            }
        } else {
            AppMIDlet.getInstance().setDisplayable(InterviewForm.class);
        }
    }

    public static EnterCategoryCommand getInstance() {
        if (instance == null)
            instance = new EnterCategoryCommand();
        return instance;
    }
}

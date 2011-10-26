/*
*  Nokia Data Gathering
*
*  Copyright (C) 2011 Nokia Corporation
*
*  This program is free software; you can redistribute it and/or
*  modify it under the terms of the GNU Lesser General Public
*  License as published by the Free Software Foundation; either
*  version 2.1 of the License, or (at your option) any later version.
*
*  This program is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
*  Lesser General Public License for more details.
*
*  You should have received a copy of the GNU Lesser General Public License
*  along with this program.  If not, see <http://www.gnu.org/licenses/
*/

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.model.CategoryConditional;
import br.org.indt.ndg.lwuit.ui.CategoryConditionalList;
import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import br.org.indt.ndg.lwuit.ui.InterviewForm;
import br.org.indt.ndg.lwuit.ui.QuantityForm;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

/**
 *
 * @author mluz
 */
public class EnterCategoryCommand extends CommandControl {

    private static EnterCategoryCommand instance;

    protected Command createCommand() {
        return new Command(Resources.CMD_RESULTS );
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

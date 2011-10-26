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
import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import br.org.indt.ndg.lwuit.ui.InterviewForm;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

/**
 *
 * @author mturiel
 */
public class BackInterviewFormCommand extends BackCommand {

    private static BackInterviewFormCommand instance;

    protected Command createCommand() {
        return new Command(Resources.NEWUI_BACK);
    }

    protected void doAction(Object parameter) {
        InterviewForm view = (InterviewForm)parameter;
        // ask whether changes shall be discarded or saved (in memory, NOT in file)
        GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_YES_NO, true);
        if ( view.isModifiedInterview() &&
             GeneralAlert.RESULT_NO ==  GeneralAlert.getInstance().show( Resources.CMD_SAVE,
                                                                         Resources.DISCARD_CHANGES,
                                                                         GeneralAlert.CONFIRMATION) ) {
            // if accepted proceed as if 'Next' was clicked
            AcceptQuestionListFormCommand.getInstance().execute( view );
        } else {
            if ( SurveysControl.getInstance().getSelectedCategory() instanceof CategoryConditional ) {
                AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.CategoryConditionalList.class);
            } else {
                AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.CategoryList.class);
            }
        }
    }

    public static BackInterviewFormCommand getInstance() {
        if (instance == null)
            instance = new BackInterviewFormCommand();
        return instance;
    }
}

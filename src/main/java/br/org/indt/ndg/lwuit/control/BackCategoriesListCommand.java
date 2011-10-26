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

import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

/**
 *
 * @author mluz
 */
public class BackCategoriesListCommand extends BackCommand implements SaveResultsObserver {

    private static BackCategoriesListCommand instance;

    protected Command createCommand() {
        return new Command(Resources.NEWUI_BACK);
    }

    protected void doAction(Object parameter) {
        // ask whether result shall be discarded or saved to file
        GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_YES_NO, true);

        if (SurveysControl.getInstance().isSurveyChanged() &&
                GeneralAlert.RESULT_YES ==  GeneralAlert.getInstance().show( Resources.CMD_SAVE,
                                             Resources.SAVE_SURVEY_QUESTION,
                                             GeneralAlert.CONFIRMATION) ) {
            SaveResultCommand.getInstance().setObserver(this);
            SaveResultCommand.getInstance().execute(null);
        } else {
            moveToPreviousView();
        }
    }

    public void onResultsSaved() {
        moveToPreviousView();
    }

    protected void moveToPreviousView() {
        AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.ResultList.class);
    }

    public static BackCategoriesListCommand getInstance() {
        if (instance == null)
            instance = new BackCategoriesListCommand();
        return instance;
    }

}

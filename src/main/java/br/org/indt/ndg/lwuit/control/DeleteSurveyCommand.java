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


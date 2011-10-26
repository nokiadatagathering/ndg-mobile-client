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

import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.lwuit.ui.GeneralAlert;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.ResultList;
import com.sun.lwuit.Command;

/**
 *
 * @author Alexandre Martini
 */
public class DeleteResultNowCommand extends CommandControl {
    private static DeleteResultNowCommand instance = new DeleteResultNowCommand();

    public static DeleteResultNowCommand getInstance(){
        return instance;
    }

    private DeleteResultNowCommand(){}

    protected Command createCommand() {
        return new Command(Resources.NEWUI_DELETE_CURRENT_RESULT);
    }

    protected void doAction(Object parameter) {

        boolean[] listFlags = (boolean[]) parameter;

        GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_YES_NO, true);
        int resultCmdIndex = GeneralAlert.getInstance().show(Resources.CMD_DELETE, Resources.DELETE_RESULTS_CONFIRMATION, GeneralAlert.CONFIRMATION);
        if (resultCmdIndex == GeneralAlert.RESULT_YES) {
            SurveysControl.getInstance().deleteResults(listFlags);
            AppMIDlet.getInstance().setResultList(new ResultList());
            AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.ResultList.class);
        }
    }
}

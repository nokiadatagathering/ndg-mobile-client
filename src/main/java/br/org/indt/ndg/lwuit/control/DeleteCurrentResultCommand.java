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
import br.org.indt.ndg.mobile.FileSystem;
import br.org.indt.ndg.mobile.ResultList;

/**
 *
 * @author mluz
 */
public class DeleteCurrentResultCommand extends CommandControl {

    private static DeleteCurrentResultCommand instance;

    protected Command createCommand() {
        return new Command(Resources.NEWUI_DELETE_CURRENT_RESULT);
    }

    protected void doAction(Object parameter) {
        GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_YES_NO, true);
        int resultCmdIndex = GeneralAlert.getInstance().show(Resources.DELETE_CONFIRMATION, Resources.DELETE_RESULT_CONFIRMATION, GeneralAlert.CONFIRMATION);
        if( resultCmdIndex == GeneralAlert.RESULT_YES )
        {
            FileSystem fs = AppMIDlet.getInstance().getFileSystem();
            fs.useResults(FileSystem.USE_NOT_SENT_RESULTS);
            String resultFilename = fs.getResultFilename();
            fs.deleteFile(resultFilename);
            fs.deleteDir( "b_" + resultFilename );
            AppMIDlet.getInstance().setResultList(new ResultList());
            AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.ResultList.class);
        }
    }

    public static DeleteCurrentResultCommand getInstance() {
        if (instance == null)
            instance = new DeleteCurrentResultCommand();
        return instance;
    }
}

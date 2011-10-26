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
import com.nokia.xfolite.xforms.dom.XFormsDocument;
import com.sun.lwuit.Command;

/**
 *
 * @author damian.janicki
 */
public class OpenRosaInterviewSaveCommand extends CommandControl {

    private static OpenRosaInterviewSaveCommand instance = null;
    private SaveResultsObserver saveObserver = null;

    protected Command createCommand() {
        return new Command(Resources.CMD_SAVE);
    }

    public void setObserver(SaveResultsObserver observer){
        saveObserver = observer;
    }

    protected void doAction(Object parameter) {
        XFormsDocument doc = (XFormsDocument)parameter;
        PersistenceManager.getInstance().saveOpenRosaResult(doc, saveObserver);
    }

    public static OpenRosaInterviewSaveCommand getInstance(){
        if(instance == null){
            instance = new OpenRosaInterviewSaveCommand();
        }
        return instance;
    }
}

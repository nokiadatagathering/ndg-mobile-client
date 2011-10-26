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

import br.org.indt.ndg.lwuit.ui.SelectDateFormatForm;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

/**
 *
 * @author damian.janicki
 */
public class SelectDateFormatCommand extends CommandControl{

    private static SelectDateFormatCommand instance;

    public static SelectDateFormatCommand getInstance(){
        if(instance == null){
            instance = new SelectDateFormatCommand();
        }
        return instance;
    }

    protected Command createCommand() {
        return new Command( Resources.DATE_FORMAT );
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().setDisplayable(SelectDateFormatForm.class);
    }
}

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

package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.DeleteSurveyCommand;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

public class SurveyListContextMenu extends ContextMenu {

    public SurveyListContextMenu(int index, int size){
        super(index, size);
    }

    protected void buildOptions(){
        Command[] options  = new Command[] { DeleteSurveyCommand.getInstance().getCommand() };
        buildOptions(options);
    }

    protected void buildCommands(){
        String[] commands = new String[] {Resources.NEWUI_CANCEL, Resources.NEWUI_SELECT};
        buildCommands(commands);
    }

    protected void action(Command cmd){
        if (cmd == DeleteSurveyCommand.getInstance().getCommand()) {
            DeleteSurveyCommand.getInstance().execute(new Integer(indexList));
        }
    }

    public void show( int leftMargin, int topMargin ) {
        super.show(getHorizontalMargin(), topMargin - getSingleOptionHeight()/2 );
    }

}

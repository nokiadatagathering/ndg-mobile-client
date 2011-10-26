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

import br.org.indt.ndg.lwuit.control.DeleteResultNowCommand;
import br.org.indt.ndg.lwuit.control.OpenResultCommand;
import br.org.indt.ndg.lwuit.control.SendResultNowCommand;
import br.org.indt.ndg.lwuit.control.ViewResultCommand;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;


/**
 *
 * @author kgomes
 */
public class ResultListContextMenu extends ContextMenu{

    public ResultListContextMenu(int index, int size){
        super(index, size);
    }

    protected void buildOptions(){
        Command[] options  = new Command[] {SendResultNowCommand.getInstance().getCommand(),
                                            OpenResultCommand.getInstance().getCommand(),
                                            ViewResultCommand.getInstance().getCommand(),
                                            DeleteResultNowCommand.getInstance().getCommand()};
        buildOptions(options);
    }

    protected void buildCommands(){
        String[] commands = new String[] {Resources.NEWUI_CANCEL, Resources.NEWUI_SELECT};
        buildCommands(commands);
    }

    protected void action(Command cmd){
        if (cmd == OpenResultCommand.getInstance().getCommand()) {
            OpenResultCommand.getInstance().execute(new Integer(indexList));
        } else if (cmd == ViewResultCommand.getInstance().getCommand()) {
            ViewResultCommand.getInstance().execute(new Integer(indexList));
        } else if (cmd == SendResultNowCommand.getInstance().getCommand()) {
            boolean[] listFlags = new boolean[sizeList];
            listFlags[indexList] = true;
            SendResultNowCommand.getInstance().execute(listFlags);
        } else if (cmd == DeleteResultNowCommand.getInstance().getCommand()) {
            boolean[] listFlags = new boolean[sizeList];
            listFlags[indexList] = true;
            DeleteResultNowCommand.getInstance().execute(listFlags);
        }
    }

    public void show() {
        super.show(getHorizontalMargin(), getVerticalMargin()/2);
    }
}

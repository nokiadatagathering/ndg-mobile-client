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

import br.org.indt.ndg.lwuit.control.OpenFileBrowserCommand;
import br.org.indt.ndg.lwuit.control.RemovePhotoCommand;
import br.org.indt.ndg.lwuit.control.ShowPhotoCommand;
import br.org.indt.ndg.lwuit.control.TakePhotoCommand;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

public class ImageQuestionContextMenu extends ContextMenu{
    public static final int FOUR_ACTIONS_CONTEXT_MENU = 4;//TakePhotoCommand,OpenFileBrowserCommand,ShowPhotoCommand,RemovePhotoCommand
    public static final int TWO_ACTIONS_CONTEXT_MENU = 2;//TakePhotoCommand,OpenFileBrowserCommand


    public ImageQuestionContextMenu(int index, int size){
        super(index, size);
    }

    protected void buildOptions(){
        Command[] options = null;
        if ( sizeList == 2) {
            options  = new Command[] { TakePhotoCommand.getInstance().getCommand(),
                                       OpenFileBrowserCommand.getInstance().getCommand()
                                       };
        } else {
            options  = new Command[] { TakePhotoCommand.getInstance().getCommand(),
                                       OpenFileBrowserCommand.getInstance().getCommand(),
                                       ShowPhotoCommand.getInstance().getCommand(),
                                       RemovePhotoCommand.getInstance().getCommand()
                                     };
        }
        buildOptions(options);
    }

    protected void buildCommands(){
        String[] commands = new String[] {Resources.NEWUI_CANCEL, Resources.NEWUI_SELECT};
        buildCommands(commands);
    }

    protected void action(Command cmd){
        if (cmd == TakePhotoCommand.getInstance().getCommand()) {
            TakePhotoCommand.getInstance().execute(null);
        } else if (cmd ==  OpenFileBrowserCommand.getInstance().getCommand()) {
            OpenFileBrowserCommand.getInstance().execute(null);
        } else if ( cmd == ShowPhotoCommand.getInstance().getCommand() ) {
            ShowPhotoCommand.getInstance().execute(null);
        } else if ( cmd == RemovePhotoCommand.getInstance().getCommand() ) {
            RemovePhotoCommand.getInstance().execute(null);
        }
    }

    public void show() {
        super.show(getHorizontalMargin()/2, getVerticalMargin()/2);
    }
}

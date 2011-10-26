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

package br.org.indt.ndg.lwuit.extended;

import com.sun.lwuit.Command;
import com.sun.lwuit.Form;
import com.sun.lwuit.TextField;
import com.sun.lwuit.events.DataChangedListener;

/**
 *
 * @author mluz
 */
public class DescriptiveField extends TextField implements DataChangedListener {

    private int length;

    public DescriptiveField(int length) {
        super();
        this.length = length;
        setInputMode("abc");
        setGrowByContent(true);
        setSingleLineTextArea(false);
        addDataChangeListener(this);
    }

    private void checkLength() {
        String text = "" + getText();
        if (text.length()> length) {
            text = text.substring(0, length);
            setText(text);
        }
    }

    public void dataChanged(int type, int index) {
        checkLength();
    }

    protected Command installCommands(Command clear, Command t9) {
        Form f = getComponentForm();
        Command[] originalCommands = new Command[f.getCommandCount()];
        for(int iter = 0 ; iter < originalCommands.length ; iter++) {
            originalCommands[iter] = f.getCommand(iter);
        }
        f.removeAllCommands();
        Command retVal = super.installCommands(clear, t9);
        for(int iter = originalCommands.length - 1 ; iter >= 0 ; iter--) {
            f.addCommand(originalCommands[iter]);
        }
        return retVal;
    }

    protected void removeCommands(Command clear, Command t9, Command originalClear) {
        //when context menu is shown, text field is hidden and deinitialize() is called on it, which calls removeCommands
        //this is a workaround
        if(isInitialized()) {
            super.removeCommands(clear, t9, originalClear);
        }
    }
}

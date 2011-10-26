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
import com.sun.lwuit.Display;
import com.sun.lwuit.TextField;
import com.sun.lwuit.Form;
import com.sun.lwuit.events.DataChangedListener;
import com.sun.lwuit.impl.midp.VirtualKeyboard;

/**
 *
 * @author mluz
 */
public class NumericField extends TextField implements DataChangedListener {

    private int m_length = 3;
    private boolean m_decimal = false;

    public NumericField( int length, boolean decimal ) {
        super();
        setConstraint(DECIMAL);
        m_length = length;
        m_decimal = decimal;
        addDataChangeListener(this);
        setOverwriteMode(false);

        setInputMode("123");
        setInputModeOrder(new String[]{"123"});

        if( Display.getInstance().isTouchScreenDevice() ) {
            VirtualKeyboard onScreenKeyboard = new VirtualKeyboard();
            if (m_decimal) {
                onScreenKeyboard.setInputModeOrder(new String[] {VirtualKeyboard.NUMBERS_SYMBOLS_MODE} );
            } else {
                onScreenKeyboard.setInputModeOrder(new String[] {VirtualKeyboard.NUMBERS_MODE} );
            }
            VirtualKeyboard.bindVirtualKeyboard(this, onScreenKeyboard);
        }
    }

    private void checkLength() {
        String text = "" + getText();
        if (text.length()> m_length) { // maximum length important for decimals
            text = text.substring(0, m_length);
            setText(text);
        }
    }
    private void checkDots() {
        String text = "" + getText();
        int firstDot = text.indexOf('.');
        if (m_decimal) {
            int lastDot = text.lastIndexOf('.');
            if ( firstDot != lastDot ) { // more then 1 '.' not allowed
                int secondDot = text.indexOf('.', firstDot+1); // could be more then 2 '.'
                text = text.substring(0, secondDot);
                setText(text);
                setCursorPosition(text.length()-1);
            }
            if (firstDot == 0 ) { // '.' at begining not allowed so adding '0'
                text = "0" + text;
                setText(text);
                setCursorPosition(text.length()-1);
            }
        } else {
            if ( firstDot != -1 ) { // dots not allowed in integer
                text = text.substring(0, firstDot);
                setText(text);
                setCursorPosition(text.length()-1);
            }
        }
    }

    private void checkPlusMinus() {
        String text = "" + getText();
        boolean textChanged = false;
        while ( text.lastIndexOf('-') > 0 ) { // '-' not allowed on index different then 0
            if ( text.lastIndexOf('-') < text.length()-1 )
                text = text.substring(0, text.lastIndexOf('-')) + text.substring(text.lastIndexOf('-')+1);
            else
                text = text.substring(0, text.lastIndexOf('-'));
            textChanged = true;
        }
        while ( text.lastIndexOf('+') >= 0 ) { // '+' not allowed
            if ( text.lastIndexOf('+') < text.length()-1 )
                text = text.substring(0, text.lastIndexOf('+')) + text.substring(text.lastIndexOf('+')+1);
            else
                text = text.substring(0, text.lastIndexOf('+'));
            textChanged = true;
        }
        if (textChanged) {
            setText(text);
            setCursorPosition(text.length()-1);
        }
    }

    public void dataChanged(int type, int index) {   
        checkDots();
        checkPlusMinus();
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
}

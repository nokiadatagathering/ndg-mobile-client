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

import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import com.sun.lwuit.Display;
import com.sun.lwuit.Font;
import com.sun.lwuit.TextArea;


public class UIUtils {

    private static final int NUMBER_OF_COLUMNS = 40;

    static public TextArea createTextArea( String aText, Font aFont ) {
        TextArea item = new TextArea();
        item.getStyle().setFont(aFont);
        item.getUnselectedStyle().setFont(aFont);
        item.setEditable(false);
        item.setFocusable(false);
        item.setColumns(NUMBER_OF_COLUMNS);
        item.setRows(1);
        item.setGrowByContent(true);
        item.setText(aText);

// Also not necessary
//        int pw = Display.getInstance().getDisplayWidth() - 10;// magic number, it should rather be set to width of margins+borders+padding
//        int w = aFont.stringWidth(aText);
//        if (w >= pw) {
//            item.setGrowByContent(true);
//            //item.setRows(2);
//        } else {
//            item.setGrowByContent(false);
//            item.setRows(1);
//        }
        return item;
    }

    static public TextArea createTextArea( String aText, Font aFont, int aColor ) {
        TextArea item = createTextArea(aText, aFont);
        item.getStyle().setFgColor(aColor);
        item.getSelectedStyle().setFgColor(aColor);
        return item;
    }

    static public TextArea createQuestionName( String aText ) {
        return createTextArea( aText, NDGStyleToolbox.fontSmall );
    }
}

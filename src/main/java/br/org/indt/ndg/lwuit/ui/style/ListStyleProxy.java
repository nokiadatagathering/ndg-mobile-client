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

package br.org.indt.ndg.lwuit.ui.style;

import com.sun.lwuit.Font;
import com.sun.lwuit.plaf.Style;
import com.sun.lwuit.plaf.UIManager;


public class ListStyleProxy extends StyleProxy {
    private Style baseStyle;
    private Style selectedStyle;
    public Font secondarySelectedFont;
    public Font secondaryUnselectedFont;

    public ListStyleProxy() {
        baseStyle = UIManager.getInstance().getComponentStyle( "Container" );
        selectedStyle = UIManager.getInstance().getComponentSelectedStyle( "Container" );

        bgUnselectedColor = baseStyle.getBgColor();

        bgSelectedEndColor = UIManager.getInstance().getComponentStyle("").getFgColor();
        bgSelectedStartColor = selectedStyle.getBgColor();

        selectedFontColor = selectedStyle.getFgColor();
        unselectedFontColor = baseStyle.getFgColor();

        selectedFont = selectedStyle.getFont();
        unselectedFont = baseStyle.getFont();

        secondaryUnselectedFont = UIManager.getInstance().getComponentStyle( "Container2" ).getFont();
        secondarySelectedFont = UIManager.getInstance().getComponentSelectedStyle( "Container2" ).getFont();
    }

    public Style getBaseStyle() {
        return baseStyle;
    }

    public Style getSelectedStyle() {
        return selectedStyle;
    }

    public void updateFonts() {
        super.updateFonts();
        if( secondarySelectedFont.getStyle() == Font.STYLE_PLAIN ) {
            secondarySelectedFont = NDGStyleToolbox.getFont( NDGStyleToolbox.FONTSANS, Font.SIZE_SMALL );
        } else if ( secondarySelectedFont.getStyle() == Font.STYLE_BOLD ) {
            secondarySelectedFont = NDGStyleToolbox.getFont( NDGStyleToolbox.FONTSANSBOLD, Font.SIZE_SMALL );
        } else if ( secondarySelectedFont.getStyle() == Font.STYLE_ITALIC ) {
            secondarySelectedFont = NDGStyleToolbox.getFont( NDGStyleToolbox.FONTSANSBOLD, Font.SIZE_SMALL );
        }

        if( secondaryUnselectedFont.getStyle() == Font.STYLE_PLAIN ) {
            secondaryUnselectedFont = NDGStyleToolbox.getFont( NDGStyleToolbox.FONTSANS, Font.SIZE_SMALL );
        } else if ( secondaryUnselectedFont.getStyle() == Font.STYLE_BOLD ) {
            secondaryUnselectedFont = NDGStyleToolbox.getFont( NDGStyleToolbox.FONTSANSBOLD, Font.SIZE_SMALL );
        } else if ( secondaryUnselectedFont.getStyle() == Font.STYLE_ITALIC ) {
            secondaryUnselectedFont = NDGStyleToolbox.getFont( NDGStyleToolbox.FONTSANSBOLD, Font.SIZE_SMALL );
        }
    }
}

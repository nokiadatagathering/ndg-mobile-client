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
import java.io.PrintStream;


abstract public class StyleProxy {

    public int bgUnselectedColor;
    public int bgSelectedStartColor;
    public int bgSelectedEndColor;
    public int selectedFontColor;
    public int unselectedFontColor;
    public Font selectedFont;
    public Font unselectedFont;

    public void writeSettings( PrintStream _out ) {
        _out.print("<bgUnselectedColor>");
        _out.print( String.valueOf( bgUnselectedColor ) );
        _out.println("</bgUnselectedColor>");
        _out.print("<bgSelectedStartColor>");
        _out.print( String.valueOf( bgSelectedStartColor ) );
        _out.println("</bgSelectedStartColor>");
        _out.print("<bgSelectedEndColor>");
        _out.print( String.valueOf( bgSelectedEndColor ) );
        _out.println("</bgSelectedEndColor>");
        _out.print("<selectedFontColor>");
        _out.print( String.valueOf( selectedFontColor ) );
        _out.println("</selectedFontColor>");
        _out.print("<unselectedFontColor>");
        _out.print( String.valueOf( unselectedFontColor ) );
        _out.println("</unselectedFontColor>");
    }

    public void updateFonts() {
        if( selectedFont.getStyle() == Font.STYLE_PLAIN ) {
            selectedFont = NDGStyleToolbox.getFont( NDGStyleToolbox.FONTSANS, Font.SIZE_SMALL );
        } else if ( selectedFont.getStyle() == Font.STYLE_BOLD ) {
            selectedFont = NDGStyleToolbox.getFont( NDGStyleToolbox.FONTSANSBOLD, Font.SIZE_SMALL );
        } else if ( selectedFont.getStyle() == Font.STYLE_ITALIC) {
            selectedFont = NDGStyleToolbox.getFont( NDGStyleToolbox.FONTSANSBOLD, Font.SIZE_SMALL );
        }

        if( unselectedFont.getStyle() == Font.STYLE_PLAIN ) {
            unselectedFont = NDGStyleToolbox.getFont( NDGStyleToolbox.FONTSANS, Font.SIZE_SMALL );
        } else if ( unselectedFont.getStyle() == Font.STYLE_BOLD ) {
            unselectedFont = NDGStyleToolbox.getFont( NDGStyleToolbox.FONTSANSBOLD, Font.SIZE_SMALL );
        } else if ( unselectedFont.getStyle() == Font.STYLE_ITALIC) {
            unselectedFont = NDGStyleToolbox.getFont( NDGStyleToolbox.FONTSANSBOLD, Font.SIZE_SMALL );
        }
    }
}

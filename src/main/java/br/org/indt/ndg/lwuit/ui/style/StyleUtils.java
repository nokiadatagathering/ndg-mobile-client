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


public class StyleUtils {

    public static void getRGB( int color, int[] rgb ) {
        final int maskRed = 0xff0000;
        final int maskGreen = 0xff00;
        final int maskBlue = 0xff;
        rgb[0] = (color & maskRed)>>16;
        rgb[1] = (color & maskGreen)>>8;
        rgb[2] = (color & maskBlue);
    }

    public static int mixRGB(  int red, int green, int blue ) {
        int color = 0;
        color = red << 16;
        color = color | ( green << 8 );
        color = color | blue;
        return color;
    }
}

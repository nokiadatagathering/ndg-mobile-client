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

import com.sun.lwuit.Component;
import com.sun.lwuit.Form;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.layouts.BorderLayout;
import java.io.IOException;

public class SplashScreen extends Form{
    private final int WHITE = 0xffffff;

    public SplashScreen() {
        super();
        setFocusable(false);
        getStyle().setBgColor( WHITE );
        setScrollable(false);
        setLayout( new BorderLayout() );

        Label label = new Label( ScaleToCurrentDisplay() );
        label.setVerticalAlignment( Component.BOTTOM );
        label.getStyle().setMargin(0,0,0,0);
        label.getStyle().setPadding(0,0,0,0);

        addComponent(BorderLayout.SOUTH, label );
    }

    private Image ScaleToCurrentDisplay() {
        Image image = null;
        try {
            int dispWidth = getWidth();
            int dispHeigh = getHeight();
            image = dispHeigh < dispWidth ? Image.createImage("/resources/images/splash_screen_landscape.jpg")
                                          : Image.createImage("/resources/images/splash_screen_portrait.jpg");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return image.scaledWidth(getWidth());
    }
}

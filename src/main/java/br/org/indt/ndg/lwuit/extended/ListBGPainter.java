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

import br.org.indt.ndg.lwuit.ui.Screen;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import com.sun.lwuit.Component;
import com.sun.lwuit.Display;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.painter.BackgroundPainter;

public class ListBGPainter extends BackgroundPainter {

    protected final Image m_bottomLine = Screen.getRes().getImage("bottom");

    public ListBGPainter(Component parent) {
        super(parent);
    }

    public void paint(Graphics g, Rectangle rect) {
        int height = rect.getSize().getHeight();
        g.setColor(NDGStyleToolbox.getInstance().listStyle.bgUnselectedColor);
        g.fillRect(rect.getX(), rect.getY(), rect.getSize().getWidth(), height);

        g.drawImage(m_bottomLine.scaled(Display.getInstance().getDisplayWidth(),
                Screen.getRes().getImage("bottom").getHeight()),
                rect.getX(), rect.getY() + height - 2);
    }
}

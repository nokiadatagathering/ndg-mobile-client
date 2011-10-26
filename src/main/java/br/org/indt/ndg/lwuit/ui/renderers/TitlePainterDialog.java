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

package br.org.indt.ndg.lwuit.ui.renderers;

import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Painter;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.plaf.UIManager;


public class TitlePainterDialog implements Painter {

    static final public int TITLE_LEFT_MARGIN = 5;
    static final public int TITLE_TOP_MARGIN = 5;
    private String mTitle = "";

    public void setTitle( String aTitle ) {
        mTitle = aTitle;
    }

    public void paint(Graphics g, Rectangle rect) {
        int width = rect.getSize().getWidth();
        int height = rect.getSize().getHeight();

        int bgColor = NDGStyleToolbox.getInstance().dialogTitleStyle.bgUnselectedColor;
        g.setColor(bgColor);
        g.fillRect(rect.getX(), rect.getY(), width, height);

        int endColor = NDGStyleToolbox.getInstance().dialogTitleStyle.bgSelectedEndColor;
        int startColor = NDGStyleToolbox.getInstance().dialogTitleStyle.bgSelectedStartColor;
        g.fillLinearGradient(startColor, endColor, rect.getX()+1, rect.getY()+1, width-2, height, false);

        // curve left side
        g.fillRect(rect.getX()+1, rect.getY()+1, 1, 1);

        // curve right side
        g.fillRect(rect.getX()+width-2, rect.getY()+1, 1, 1);

        int tintColor = UIManager.getInstance().getLookAndFeel().getDefaultFormTintColor();
        g.setColor(tintColor);
        g.fillRect(rect.getX(), rect.getY(), 1, 1, (byte) ((tintColor >> 24) & 0xff));
        g.fillRect(rect.getX()+width-1, rect.getY(), 1, 1, (byte) ((tintColor >> 24) & 0xff));

        g.setFont( NDGStyleToolbox.getInstance().dialogTitleStyle.unselectedFont );
        g.setColor( NDGStyleToolbox.getInstance().dialogTitleStyle.unselectedFontColor );
        g.drawString( mTitle, rect.getX() + TITLE_LEFT_MARGIN, rect.getY() + TITLE_TOP_MARGIN );
    }
}

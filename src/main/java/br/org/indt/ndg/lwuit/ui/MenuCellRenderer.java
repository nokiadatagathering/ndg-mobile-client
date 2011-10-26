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
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.Painter;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.list.ListCellRenderer;

/**
 *
 * @author mluz
 */
public class MenuCellRenderer extends Label implements ListCellRenderer, Painter{

    private BGPainter bgPainter;

    public MenuCellRenderer() {
        bgPainter = new BGPainter();
        if(com.sun.lwuit.Display.getInstance().isTouchScreenDevice()) {
            getStyle().setPadding( 10, 10,
                                   getStyle().getPadding( Component.LEFT),
                                   getStyle().getPadding( Component.RIGHT) );
            getSelectedStyle().setPadding( 10, 10,
                                           getSelectedStyle().getPadding(Component.LEFT),
                                           getSelectedStyle().getPadding(Component.RIGHT) );
        }
    }

    public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
        if (value instanceof Command) {
            Command cmd = (Command)value;
            setText(cmd.getCommandName());
            setIcon(cmd.getIcon());
        }
        if (isSelected) {
            setFocus(true);
            getStyle().setFgColor( NDGStyleToolbox.getInstance().menuStyle.selectedFontColor );
            getStyle().setFont( NDGStyleToolbox.getInstance().menuStyle.selectedFont );
            getStyle().setBgPainter(this);
        } else {
            getStyle().setFgColor( NDGStyleToolbox.getInstance().menuStyle.unselectedFontColor );
            getStyle().setFont( NDGStyleToolbox.getInstance().menuStyle.unselectedFont );
            setFocus(false);
            getStyle().setBgPainter(bgPainter);
        }
        return this;
    }

    public Component getListFocusComponent(List list) {
        setText("");
        getStyle().setBgPainter(this);
        setIcon(null);
        setFocus(true);
        return this;
    }

    public void paint(Graphics g, Rectangle rect) {
        int width = rect.getSize().getWidth();
        int height = rect.getSize().getHeight();

        int startColor = NDGStyleToolbox.getInstance().menuStyle.bgSelectedStartColor;
        int endColor = NDGStyleToolbox.getInstance().menuStyle.bgSelectedEndColor;
        g.fillLinearGradient(startColor, endColor, rect.getX(), rect.getY(), width, height, false);

        int borderColor = NDGStyleToolbox.getInstance().menuStyle.bgUnselectedColor;
        g.setColor(borderColor);
        g.fillRect(rect.getX(), rect.getY(), 1, 1);
        g.fillRect(rect.getX()+width-1, rect.getY(), 1, 1);
        g.fillRect(rect.getX(), rect.getY()+height-1, 1, 1);
        g.fillRect(rect.getX()+width-1, rect.getY()+height-1, 1, 1);
    }

    class BGPainter implements Painter {

        public void paint(Graphics g, Rectangle rect) {
            g.setColor(NDGStyleToolbox.getInstance().menuStyle.bgUnselectedColor);
            g.fillRect(rect.getX(), rect.getY(), rect.getSize().getWidth(),rect.getSize().getHeight());
        }
    }
}

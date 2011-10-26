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

import br.org.indt.ndg.lwuit.extended.ListBGPainter;
import br.org.indt.ndg.lwuit.extended.ListFocusBGPainter;
import br.org.indt.ndg.lwuit.ui.Screen;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.Painter;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.list.ListCellRenderer;
import com.sun.lwuit.painter.BackgroundPainter;


/**
 *
 * @author mluz
 */
public abstract class DefaultNDGListCellRenderer extends Container implements ListCellRenderer {

    protected final BackgroundPainter m_bgPainter;
    protected final Painter m_focusBGPainter;
    protected final Image m_bottomLine;
    protected final Label m_transitionLabel = new Label();

    protected static final int TOUCH_SCREEN_VERTICAL_PADDING = 10;

    public DefaultNDGListCellRenderer() {
        super(new BorderLayout());

        m_bgPainter = new ListBGPainter(this);
        m_focusBGPainter = new ListFocusBGPainter(this);
        m_bottomLine = Screen.getRes().getImage("bottom");
        m_transitionLabel.setCellRenderer(true);
        setCellRenderer(true);
    }

    public abstract Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected);

    public Component getListFocusComponent(List list) {
        m_transitionLabel.setFocus(true);
        m_transitionLabel.getStyle().setMargin(0,0,0,0);
        m_transitionLabel.getStyle().setBgPainter(m_focusBGPainter);
        return m_transitionLabel;
    }

    public void addComponent(Object constraints, Component cmp) {
        super.addComponent(constraints, cmp);
        if(com.sun.lwuit.Display.getInstance().isTouchScreenDevice()) {
            cmp.getStyle().setPadding(TOUCH_SCREEN_VERTICAL_PADDING, TOUCH_SCREEN_VERTICAL_PADDING, 0, 0);
        }
    }

}

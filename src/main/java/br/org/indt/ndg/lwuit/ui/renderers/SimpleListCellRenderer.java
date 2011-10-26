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
import com.sun.lwuit.Component;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.layouts.BorderLayout;

public class SimpleListCellRenderer extends DefaultNDGListCellRenderer {

    protected final Label m_label = new Label();

    protected String getText(Object value) {
        String result = null;
        if ( value instanceof String ) {
           result = (String)value;
        } else {
           result =  String.valueOf(value);
        }
        return result;
    }

    public SimpleListCellRenderer() {
        super();
        m_label.setCellRenderer(true);
        addComponent(BorderLayout.CENTER, m_label);
    }

    public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
        m_label.setText(getText(value));

        if (isSelected) {
            setFocus(true);
            m_label.getStyle().setFont(NDGStyleToolbox.getInstance().listStyle.selectedFont);
            m_label.getStyle().setFgColor( NDGStyleToolbox.getInstance().listStyle.selectedFontColor );
            getStyle().setBgPainter(m_focusBGPainter);
        } else {
            setFocus(false);
            m_label.getStyle().setFont(NDGStyleToolbox.getInstance().listStyle.unselectedFont);
            m_label.getStyle().setFgColor( NDGStyleToolbox.getInstance().listStyle.unselectedFontColor );
            getStyle().setBgPainter(m_bgPainter);
        }
        return this;
    }
}

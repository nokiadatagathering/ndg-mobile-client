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

import br.org.indt.ndg.lwuit.model.CheckableItem;
import br.org.indt.ndg.lwuit.model.DisplayableItem;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import com.sun.lwuit.CheckBox;
import com.sun.lwuit.Component;
import com.sun.lwuit.List;
import com.sun.lwuit.layouts.BorderLayout;
import java.util.Vector;

/**
 *
 * @author Alexandre Martini
 */
public class CheckableListCellRenderer extends DefaultNDGListCellRenderer{

    protected final Vector checkboxes = new Vector(); // of Boolean
    protected final CheckBox m_rendererCheckbox = new CheckBox();

    public CheckableListCellRenderer() {
        super();
    }

    public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
        if ( list.size() == 0 )
            return this;
        removeAll();

        prepareCheckBox((CheckableItem) value);

        if (isSelected) {
            m_rendererCheckbox.setFocus(true);
            m_rendererCheckbox.getStyle().setFont(NDGStyleToolbox.getInstance().listStyle.selectedFont);
            m_rendererCheckbox.getStyle().setFgColor( NDGStyleToolbox.getInstance().listStyle.selectedFontColor );
            getStyle().setBgPainter(m_focusBGPainter);
        } else {
            m_rendererCheckbox.setFocus(false);
            m_rendererCheckbox.getStyle().setFont(NDGStyleToolbox.getInstance().listStyle.unselectedFont);
            m_rendererCheckbox.getStyle().setFgColor( NDGStyleToolbox.getInstance().listStyle.unselectedFontColor );
            getStyle().setBgPainter(m_bgPainter);
        }

        addComponent(BorderLayout.CENTER, m_rendererCheckbox);

        return this;
    }

    protected void prepareCheckBox( Object disp ) {
        m_rendererCheckbox.setText(((DisplayableItem)disp).getDisplayableName());
        m_rendererCheckbox.setSelected(((CheckableItem)disp).isChecked());
    }
}

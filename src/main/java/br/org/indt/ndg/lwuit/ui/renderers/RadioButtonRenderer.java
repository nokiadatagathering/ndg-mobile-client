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

import br.org.indt.ndg.lwuit.extended.RadioButton;
import br.org.indt.ndg.lwuit.ui.InterviewForm.RadioChoiceItem;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import com.sun.lwuit.Component;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.list.ListCellRenderer;

public class RadioButtonRenderer extends RadioButton implements ListCellRenderer {

    protected final Label m_transitionLabel = new Label();
    protected static final int TOUCH_SCREEN_VERTICAL_PADDING = 10;

    public RadioButtonRenderer() {
        super("");
        m_transitionLabel.setCellRenderer(true);
        setCellRenderer(true);
    }

    public Component getListCellRendererComponent( List list, Object item, int index, boolean isSelected ) {
        if( item == null ) {
            return this;
        }
        RadioChoiceItem radioItem = (RadioChoiceItem)item;
        boolean isChecked =  radioItem.isChecked();
        boolean hasDetails = radioItem.hasMoreDetails();
        setSelected(isChecked);
        useMoreDetails(hasDetails);
        setText(radioItem.getValue());

        setEnabled(list.isEnabled());

        if ( isSelected && list.hasFocus() ) {
            setFocus(true);
            getStyle().setFont(NDGStyleToolbox.getInstance().listStyle.selectedFont);
            getStyle().setFgColor( NDGStyleToolbox.getInstance().listStyle.selectedFontColor );
            getStyle().setBgPainter(focusBGPainter);
        } else {
            setFocus(false);
            getStyle().setFont(NDGStyleToolbox.getInstance().listStyle.unselectedFont);
            getStyle().setFgColor( NDGStyleToolbox.getInstance().listStyle.unselectedFontColor );
            getStyle().setBgPainter(bgPainter);
        }
        return this;
    }

    public Component getListFocusComponent(List list) {
        if ( list.hasFocus() ) {
            m_transitionLabel.getStyle().setFont(NDGStyleToolbox.getInstance().listStyle.selectedFont);
            m_transitionLabel.getStyle().setFgColor( NDGStyleToolbox.getInstance().listStyle.selectedFontColor );
            m_transitionLabel.getStyle().setBgPainter(focusBGPainter);
        } else {
            m_transitionLabel.getStyle().setFont(NDGStyleToolbox.getInstance().listStyle.unselectedFont);
            m_transitionLabel.getStyle().setFgColor( NDGStyleToolbox.getInstance().listStyle.unselectedFontColor );
            m_transitionLabel.getStyle().setBgPainter(bgPainter);
        }
        return m_transitionLabel;
    }
}
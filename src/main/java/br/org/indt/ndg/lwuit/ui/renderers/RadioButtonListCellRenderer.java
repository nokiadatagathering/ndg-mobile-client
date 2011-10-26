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

import br.org.indt.ndg.lwuit.model.RadioButtonGroupItem;
import br.org.indt.ndg.lwuit.ui.Screen;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import br.org.indt.ndg.mobile.Utils;
import com.sun.lwuit.Component;
import com.sun.lwuit.Font;
import com.sun.lwuit.List;
import com.sun.lwuit.RadioButton;
import com.sun.lwuit.layouts.BorderLayout;

/**
 *
 * @author damian.janicki
 */
public class RadioButtonListCellRenderer extends DefaultNDGListCellRenderer{


    private RadioButton rendererRadioButton = new RadioButton();

    public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
        if ( list.size() == 0 || value == null)
            return this;
        removeAll();
        RadioButtonGroupItem item = (RadioButtonGroupItem)value;

        Font font = null;
        if(!Utils.isS40()){
            font = Screen.getFontRes().getFont( item.getValue() );
        }

        if (isSelected) {
            rendererRadioButton.setFocus(true);
            if(font == null){
                font = NDGStyleToolbox.getInstance().listStyle.selectedFont;
            }
            rendererRadioButton.getStyle().setFont(font);
            rendererRadioButton.getStyle().setFgColor( NDGStyleToolbox.getInstance().listStyle.selectedFontColor );
            getStyle().setBgPainter(m_focusBGPainter);
        } else {
            rendererRadioButton.setFocus(false);
            if(font == null){
                font = NDGStyleToolbox.getInstance().listStyle.unselectedFont;
            }
            rendererRadioButton.getStyle().setFont(font);
            rendererRadioButton.getStyle().setFgColor( NDGStyleToolbox.getInstance().listStyle.unselectedFontColor );
            getStyle().setBgPainter(m_bgPainter);
        }

        rendererRadioButton.setText(item.getDisplayName());
        rendererRadioButton.setSelected(item.isChecked());
        addComponent(BorderLayout.CENTER, rendererRadioButton);

        return this;
    }

}

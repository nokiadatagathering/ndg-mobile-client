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
import br.org.indt.ndg.lwuit.ui.NDGLookAndFeel;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import com.sun.lwuit.Component;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.layouts.BorderLayout;


/**
 *
 * @author mluz, kgomes
 */

public class ResultListCellRenderer extends CheckableListCellRenderer {

    public ResultListCellRenderer(){
        super();
    }

    public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
        CheckableItem disp = (CheckableItem)value;
        removeAll();

        Component comp = null;
        if(index>0){
            prepareCheckBox(disp);
            comp = m_rendererCheckbox;
        } else {
            Label label = new Label(((DisplayableItem)disp).getDisplayableName());
            label.setAlignment(CENTER);
            comp = label;
        }

        if (isSelected) {
            setFocus(true);
            comp.getStyle().setFont(NDGStyleToolbox.getInstance().listStyle.selectedFont);
            comp.getStyle().setFgColor( NDGStyleToolbox.getInstance().listStyle.selectedFontColor );
            if( index > 0 ){
                Image arrow = NDGLookAndFeel.getRightContextMenuImage( getHeight() );
                Label larrow = new Label(arrow);
                larrow.getStyle().setMargin(0,0,0,10);
                addComponent(BorderLayout.EAST,larrow);
            }
            getStyle().setBgPainter(m_focusBGPainter);
        } else {
            setFocus(false);
            comp.getStyle().setFont(NDGStyleToolbox.getInstance().listStyle.unselectedFont);
            comp.getStyle().setFgColor( NDGStyleToolbox.getInstance().listStyle.unselectedFontColor );
            getStyle().setBgPainter(m_bgPainter);
        }

        addComponent(BorderLayout.CENTER, comp);

        return this;
    }
}

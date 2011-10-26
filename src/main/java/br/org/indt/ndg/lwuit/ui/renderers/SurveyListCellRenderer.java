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

import com.sun.lwuit.Component;
import com.sun.lwuit.List;

public class SurveyListCellRenderer extends SimpleListWithAnimatedTextCellRenderer {

    public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
        if (index == 0) {
            m_label.setAlignment(Component.CENTER);
        } else {
            m_label.setAlignment(Component.LEFT);
        }
        return super.getListCellRendererComponent(list,value,index,isSelected);
    }

    protected String getText(Object value) {
        String surveyName = (String) value;
        return (String)surveyName;
    }

    protected String getTextToAnimate() {
        return m_label.getText();
    }
}
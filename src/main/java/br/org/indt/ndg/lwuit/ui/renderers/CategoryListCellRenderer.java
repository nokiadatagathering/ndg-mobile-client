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

import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.lwuit.model.Category;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import com.sun.lwuit.Component;
import com.sun.lwuit.Display;
import com.sun.lwuit.Font;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.List;

/**
 *
 * @author mluz
 */
public class CategoryListCellRenderer extends DefaultNDGListCellRenderer {

    private Category category;

    private static final int PADDING = 5;

    Font catFont = null;
    Font questFont = null;
    int fontColor = 0;
    int touchPadding = 0;

    public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
        category = (Category)value;

        if(Display.getInstance().isTouchScreenDevice()) {
           touchPadding = TOUCH_SCREEN_VERTICAL_PADDING;
        }

        if (isSelected) {
            setFocus(true);
            getStyle().setBgPainter(m_focusBGPainter);

            catFont = NDGStyleToolbox.getInstance().listStyle.selectedFont;
            questFont = NDGStyleToolbox.getInstance().listStyle.secondarySelectedFont;
            fontColor = NDGStyleToolbox.getInstance().listStyle.selectedFontColor;
        } else {
            setFocus(false);
            getStyle().setBgPainter(m_bgPainter);

            catFont = NDGStyleToolbox.getInstance().listStyle.unselectedFont;
            questFont = NDGStyleToolbox.getInstance().listStyle.secondaryUnselectedFont;
            fontColor = NDGStyleToolbox.getInstance().listStyle.unselectedFontColor;
        }

        setPreferredH(catFont.getHeight() + questFont.getHeight() + 2 * touchPadding);

        return this;
    }

    public void paint(Graphics g) {
        super.paint(g);

        int height = catFont.getHeight() + questFont.getHeight();
        setPreferredH(height);

        Image img = category.isFullFilled() ? Resources.check : Resources.question;
        g.drawImage( img,
                getX() + PADDING,
                touchPadding + getY() + (height - img.getHeight())/2);

        g.setColor(fontColor);
        g.setFont(catFont);
        g.drawString(category.getName(),
                getX() + (PADDING * 2) + img.getWidth(),
                touchPadding + getY() );

        int diff = (int)(catFont.getHeight() * 0.25);

        String questionCount = category.getQuestions().size() + (category.getQuestions().size() > 1 ? " " + Resources.QUESTIONS : " "+ Resources.QUESTION);
        g.setFont(questFont);
        g.drawString(questionCount,
                getX() + (PADDING * 2) + img.getWidth(),
                touchPadding + getY() + catFont.getHeight() - diff);
    }
}
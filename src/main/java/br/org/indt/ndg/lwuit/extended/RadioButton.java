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

package br.org.indt.ndg.lwuit.extended;

import br.org.indt.ndg.lwuit.ui.NDGLookAndFeel;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import br.org.indt.ndg.mobile.logging.Logger;
import com.sun.lwuit.Component;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.Painter;
import com.sun.lwuit.events.FocusListener;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.painter.BackgroundPainter;

/**
 *
 * @author mluz
 */
public class RadioButton extends com.sun.lwuit.RadioButton implements FocusListener {

    protected final Painter focusBGPainter = new FocusBGPainter();
    protected final BackgroundPainter bgPainter = new BackgroundPainter(this);
    private boolean mUseMoreDetails = false;
    private Image image = null;

    public RadioButton(String text) {
        super(text);
        addFocusListener(this);
        getSelectedStyle().setFont(NDGStyleToolbox.fontMediumBold, false);
        getUnselectedStyle().setFont(NDGStyleToolbox.fontMedium, false);
    }

    public RadioButton(String text, Image image){
        this(text);
        this.image = image;
    }

    public void focusGained(Component cmp) {
        getStyle().setBgPainter(focusBGPainter);
        getStyle().setFont( NDGStyleToolbox.fontMediumBold );
    }

    public void focusLost(Component cmp) {
        getStyle().setBgPainter(bgPainter);
        getStyle().setFont( NDGStyleToolbox.fontMedium );
    }

    public void useMoreDetails(boolean _val) {
        mUseMoreDetails = _val;
    }

    public boolean hasMoreDetails() {
        return mUseMoreDetails;
    }

    public void paint(Graphics g) {
        super.paint(g);
        if ( isSelected() && hasMoreDetails() || image != null) {
            Image img = null;
            if(image == null){
                img = NDGLookAndFeel.getRightContextMenuImage(getWidth());
            }else{
                img = image;
            }
            int x = getX() + getWidth() - (int)(img.getWidth()*1.5);
            int y = getY() + (getHeight() - img.getHeight())/2;
            g.drawImage(img, x, y);
        } 
    }

    class FocusBGPainter implements Painter {

        public void paint(Graphics g, Rectangle rect) {
            int width = rect.getSize().getWidth();
            int height = rect.getSize().getHeight();
            
            int endColor = NDGStyleToolbox.getInstance().listStyle.bgSelectedEndColor;
            int startColor = NDGStyleToolbox.getInstance().listStyle.bgSelectedStartColor;
            g.fillLinearGradient(startColor, endColor, rect.getX(), rect.getY(), width, height, false);

            int borderColor = NDGStyleToolbox.getInstance().listStyle.bgUnselectedColor;
            g.setColor(borderColor);
            g.fillRect(rect.getX(), rect.getY(), 1, 1);
            g.fillRect(rect.getX()+width-1, rect.getY(), 1, 1);
            g.fillRect(rect.getX(), rect.getY()+height-1, 1, 1);
            g.fillRect(rect.getX()+width-1, rect.getY()+height-1, 1, 1);
        }
    }

}

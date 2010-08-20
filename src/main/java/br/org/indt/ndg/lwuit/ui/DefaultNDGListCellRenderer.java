/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui;

import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.List;
import com.sun.lwuit.Painter;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.list.ListCellRenderer;
import com.sun.lwuit.painter.BackgroundPainter;
import com.sun.lwuit.plaf.Style;
import com.sun.lwuit.plaf.UIManager;


/**
 *
 * @author mluz
 */
abstract class DefaultNDGListCellRenderer extends Container implements ListCellRenderer {

    protected Style styleContainer, styleLabel;

    protected BackgroundPainter bgPainter;

    protected Painter focusBGPainter;

    protected Image hr;

    public DefaultNDGListCellRenderer() {
        super(new BorderLayout());
        styleContainer = UIManager.getInstance().getComponentStyle("List");
        styleLabel = UIManager.getInstance().getComponentStyle("List");
        setStyle(styleContainer);
        bgPainter = new BGPainter(this);
        focusBGPainter = new FocusBGPainter();
        hr = Screen.getRes().getImage("bottom");
    }

    public abstract Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected);

    public abstract Component getListFocusComponent(List list);

    class FocusBGPainter implements Painter {

        public void paint(Graphics g, Rectangle rect) {
            int width = rect.getSize().getWidth();
            int height = rect.getSize().getHeight();

            int endColor = UIManager.getInstance().getComponentStyle("").getFgColor();
            int startColor = styleContainer.getBgSelectionColor();
            g.fillLinearGradient(startColor, endColor, rect.getX(), rect.getY(), width, height-3, false);


            int borderColor = UIManager.getInstance().getComponentStyle("List").getBgColor();
            g.setColor(borderColor);
            g.fillRect(rect.getX(), rect.getY(), 1, 1);
            g.fillRect(rect.getX()+width-1, rect.getY(), 1, 1);
            g.fillRect(rect.getX(), rect.getY()+height-4, 1, 1);
            g.fillRect(rect.getX()+width-1, rect.getY()+height-4, 1, 1);

            g.drawImage(hr, rect.getX(), rect.getY() + height -2);
        }

    }

    class BGPainter extends BackgroundPainter {

        BGPainter(Component parent) {
            super(parent);
        }

        public void paint(Graphics g, Rectangle rect) {
            super.paint(g, rect);
            int height = rect.getSize().getHeight();
            g.drawImage(hr, rect.getX(), rect.getY() + height - 2);
        }

    }


}

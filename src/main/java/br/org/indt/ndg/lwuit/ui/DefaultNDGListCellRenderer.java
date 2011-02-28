package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.List;
import com.sun.lwuit.Painter;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.list.ListCellRenderer;
import com.sun.lwuit.painter.BackgroundPainter;


/**
 *
 * @author mluz
 */
abstract class DefaultNDGListCellRenderer extends Container implements ListCellRenderer {

    protected BackgroundPainter bgPainter;
    protected Painter focusBGPainter;
    protected Image hr;

    protected final int TOUCH_SCREEN_VERTICAL_PADDING = 10;

    public DefaultNDGListCellRenderer() {
        super(new BorderLayout());

        bgPainter = new BGPainter(this);
        focusBGPainter = new FocusBGPainter(this);
        hr = Screen.getRes().getImage("bottom");
    }

    public abstract Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected);

    public abstract Component getListFocusComponent(List list);

    public void addComponent(Object constraints, Component cmp) {
        super.addComponent(constraints, cmp);
        if(com.sun.lwuit.Display.getInstance().isTouchScreenDevice()) {
            cmp.getStyle().setPadding(TOUCH_SCREEN_VERTICAL_PADDING, TOUCH_SCREEN_VERTICAL_PADDING, 0, 0);
        }
    }

    class FocusBGPainter extends BackgroundPainter {
        
        FocusBGPainter(Component parent) {
            super(parent);
        }

        public void paint(Graphics g, Rectangle rect) {
            int width = rect.getSize().getWidth();
            int height = rect.getSize().getHeight();

            int endColor = NDGStyleToolbox.getInstance().listStyle.bgSelectedEndColor;
            int startColor = NDGStyleToolbox.getInstance().listStyle.bgSelectedStartColor;

            g.fillLinearGradient(startColor, endColor, rect.getX(), rect.getY(), width, height-3, false);

            int borderColor = NDGStyleToolbox.getInstance().listStyle.bgUnselectedColor;
            g.setColor(borderColor);
            g.fillRect(rect.getX(), rect.getY(), 1, 1);
            g.fillRect(rect.getX()+width-1, rect.getY(), 1, 1);
            g.fillRect(rect.getX(), rect.getY()+height-4, 1, 1);
            g.fillRect(rect.getX()+width-1, rect.getY()+height-4, 1, 1);

            g.drawImage( hr.scaled( Display.getInstance().getDisplayWidth(),
                                    Screen.getRes().getImage("bottom").getHeight() ),
                        rect.getX(), rect.getY() + height -2);
        }
    }

    class BGPainter extends BackgroundPainter {

        BGPainter(Component parent) {
            super(parent);
        }

        public void paint(Graphics g, Rectangle rect) {
            int height = rect.getSize().getHeight();
            g.setColor(NDGStyleToolbox.getInstance().listStyle.bgUnselectedColor);
            g.fillRect(rect.getX(), rect.getY(), rect.getSize().getWidth(), height );

            g.drawImage(hr.scaled( Display.getInstance().getDisplayWidth(),
                                   Screen.getRes().getImage("bottom").getHeight() ),
                                   rect.getX(), rect.getY() + height - 2);
        }
    }
}

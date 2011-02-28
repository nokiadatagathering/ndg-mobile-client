package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.Painter;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.list.ListCellRenderer;

/**
 *
 * @author mluz
 */
public class MenuCellRenderer extends Label implements ListCellRenderer, Painter{

    private BGPainter bgPainter;

    public MenuCellRenderer() {
        bgPainter = new BGPainter();
        if(com.sun.lwuit.Display.getInstance().isTouchScreenDevice()) {
            getStyle().setPadding(10, 10, 0, 0);
            getSelectedStyle().setPadding(10, 10, 0, 0);
        }
    }

    public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
        if (value instanceof Command) {
            Command cmd = (Command)value;
            setText(" " + cmd.getCommandName());
            setIcon(cmd.getIcon());
        }
        if (isSelected) {
            setFocus(true);
            getStyle().setFgColor( NDGStyleToolbox.getInstance().menuStyle.selectedFontColor );
            getStyle().setFont( NDGStyleToolbox.getInstance().menuStyle.selectedFont );
            getStyle().setBgPainter(this);
        } else {
            getStyle().setFgColor( NDGStyleToolbox.getInstance().menuStyle.unselectedFontColor );
            getStyle().setFont( NDGStyleToolbox.getInstance().menuStyle.unselectedFont );
            setFocus(false);
            getStyle().setBgPainter(bgPainter);
        }
        return this;
    }

    public Component getListFocusComponent(List list) {
        setText("");
        getStyle().setBgPainter(this);
        setIcon(null);
        setFocus(true);
        return this;
    }

    public void paint(Graphics g, Rectangle rect) {
        int width = rect.getSize().getWidth();
        int height = rect.getSize().getHeight();

        int startColor = NDGStyleToolbox.getInstance().menuStyle.bgSelectedStartColor;
        int endColor = NDGStyleToolbox.getInstance().menuStyle.bgSelectedEndColor;
        g.fillLinearGradient(startColor, endColor, rect.getX(), rect.getY(), width, height, false);

        int borderColor = NDGStyleToolbox.getInstance().menuStyle.bgUnselectedColor;
        g.setColor(borderColor);
        g.fillRect(rect.getX(), rect.getY(), 1, 1);
        g.fillRect(rect.getX()+width-1, rect.getY(), 1, 1);
        g.fillRect(rect.getX(), rect.getY()+height-1, 1, 1);
        g.fillRect(rect.getX()+width-1, rect.getY()+height-1, 1, 1);
    }

    class BGPainter implements Painter {

        public void paint(Graphics g, Rectangle rect) {
            g.setColor(NDGStyleToolbox.getInstance().menuStyle.bgUnselectedColor);
            g.fillRect(rect.getX(), rect.getY(), rect.getSize().getWidth(),rect.getSize().getHeight());
        }
    }
}

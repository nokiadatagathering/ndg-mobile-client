package br.org.indt.ndg.lwuit.extended;

import br.org.indt.ndg.lwuit.ui.Screen;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import com.sun.lwuit.Component;
import com.sun.lwuit.Display;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.painter.BackgroundPainter;

public class ListFocusBGPainter extends BackgroundPainter {

    protected final Image m_bottomLine = Screen.getRes().getImage("bottom");

    public ListFocusBGPainter(Component parent) {
        super(parent);
    }

    public void paint(Graphics g, Rectangle rect) {
        int width = rect.getSize().getWidth();
        int height = rect.getSize().getHeight();

        int endColor = NDGStyleToolbox.getInstance().listStyle.bgSelectedEndColor;
        int startColor = NDGStyleToolbox.getInstance().listStyle.bgSelectedStartColor;

        g.fillLinearGradient(startColor, endColor, rect.getX(), rect.getY(), width, height - 3, false);

        int borderColor = NDGStyleToolbox.getInstance().listStyle.bgUnselectedColor;
        g.setColor(borderColor);
        g.fillRect(rect.getX(), rect.getY(), 1, 1);
        g.fillRect(rect.getX() + width - 1, rect.getY(), 1, 1);
        g.fillRect(rect.getX(), rect.getY() + height - 4, 1, 1);
        g.fillRect(rect.getX() + width - 1, rect.getY() + height - 4, 1, 1);

        g.drawImage(m_bottomLine.scaled(Display.getInstance().getDisplayWidth(),
                Screen.getRes().getImage("bottom").getHeight()),
                rect.getX(), rect.getY() + height - 2);
    }
}

package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Display;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.Painter;
import com.sun.lwuit.geom.Rectangle;

/**
 *
 * @author mluz
 */
public class TitleBar implements Painter {

    private String title1;
    private String title2;
    private Image hr = Screen.getRes().getImage("bottom");

    TitleBar(String title1, String title2) {
        this.title1 = title1;
        this.title2 = title2;
    }

    public void paint(Graphics g, Rectangle rect) {
        int width = rect.getSize().getWidth();
        int height = rect.getSize().getHeight();

        g.fillLinearGradient(0xffffff, 0xe1e1e1, rect.getX(), rect.getY(), width, height-3, false);

        Image logo = Resources.logo.scaledHeight( height - 4 );
        g.drawImage( hr.scaled( Display.getInstance().getDisplayWidth(),
                                Screen.getRes().getImage("bottom").getHeight()),
                                rect.getX(), rect.getY() + height - 2 );
        g.drawImage( logo, 10, 1 );
        g.setFont(NDGStyleToolbox.fontSmall);
        g.setColor(0x007b7b7b);
        g.drawString(title1, logo.getWidth() + 10 + 1, 3);
        g.setFont(NDGStyleToolbox.fontLarge);
        g.drawString(title2, logo.getWidth() + 10 + 1, 3 + NDGStyleToolbox.fontSmall.getHeight() );
        if (AppMIDlet.getInstance().getLocationHandler().locationObtained()) {
            Image imageGPS = Screen.getRes().getImage("gps");
            g.drawImage(imageGPS, 2, 22);
        }
    }

    public void setTitle1(String title1) {
        this.title1 = title1;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }
}

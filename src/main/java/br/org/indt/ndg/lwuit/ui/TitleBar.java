package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Display;
import com.sun.lwuit.Font;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.Painter;
import com.sun.lwuit.geom.Rectangle;

/**
 *
 * @author mluz
 */
public class TitleBar implements Painter {

    private static Font small = Screen.getRes().getFont("NokiaSansWide11");
    private static Font large = Screen.getRes().getFont("NokiaSansWide18");
    private static Image hr = Screen.getRes().getImage("bottom");
    private static final Image imageGPS = Screen.getRes().getImage("gps");
    private static int logoWidth;
    private static int logoHeight;
    private static final int gpsWidth = imageGPS.getWidth();
    private static final int textPadding = 2;

    private String title1;
    private String title2;

    TitleBar(String title1, String title2) {
        this.title1 = title1;
        this.title2 = title2;
        logoWidth = Resources.logo.getWidth();
        logoHeight = Resources.logo.getHeight();
    }

    public void paint(Graphics g, Rectangle rect) {
        int width = rect.getSize().getWidth();
        int height = rect.getSize().getHeight();

        g.fillLinearGradient(0xffffff, 0xe1e1e1, rect.getX(), rect.getY(), width, height-3, false);

        g.drawImage( hr.scaled( Display.getInstance().getDisplayWidth(),
                                hr.getHeight()),
                                rect.getX(), rect.getY() + height - 2 );

        g.drawImage( Resources.logo, textPadding + gpsWidth + textPadding, (height - logoHeight)>>1 );
        g.setFont( small );
        g.setColor( 0x007b7b7b );
        int textOffset = textPadding + gpsWidth + textPadding + logoWidth + textPadding;
        g.drawString( title1, textOffset, textPadding );
        g.setFont( large );
        g.drawString(title2, textOffset, textPadding + small.getHeight() );
        if (AppMIDlet.getInstance().getLocationHandler().locationObtained()) {
            g.drawImage(imageGPS, textPadding, 22);
        }
    }

    public void setTitle1(String title1) {
        this.title1 = title1;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }
}

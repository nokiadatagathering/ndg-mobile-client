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

    private static final Image hr = Screen.getRes().getImage("bottom");
    private static final Image imageGPS = Screen.getRes().getImage("gps");
    private static final int gpsWidth = imageGPS.getWidth();
    private static final int textPadding = 2;
    private static int logoWidth;
    private static int logoHeight;

    private String title1;
    private String title2;

    TitleBar(String title1, String title2) {
        this.title1 = title1;
        this.title2 = title2;
        logoWidth = Resources.logo.getWidth();
        logoHeight = Resources.logo.getHeight();
    }

    public int getPrefferedH() {
        if( title1.length() == 0 && title2.length() == 0 )
            return 0;
        int fontsHight = 3*textPadding + NDGStyleToolbox.fontSmall.getHeight() + NDGStyleToolbox.fontMedium.getHeight();
        int logoHight = 2*textPadding + logoHeight;
        return fontsHight < logoHight ? logoHight : fontsHight;
    }

    public void paint(Graphics g, Rectangle rect) {
        int width = rect.getSize().getWidth();
        int height = rect.getSize().getHeight();

        g.fillLinearGradient(0xffffff, 0xe1e1e1, rect.getX(), rect.getY(), width, height-3, false);

        g.drawImage( hr.scaled( Display.getInstance().getDisplayWidth(),
                                hr.getHeight()),
                                rect.getX(), rect.getY() + height - 2 );

        g.drawImage( Resources.logo, textPadding + gpsWidth + textPadding, ( height - logoHeight )>>1 );
        g.setFont( NDGStyleToolbox.fontSmall );
        g.setColor( 0x007b7b7b );
        int textOffsetHorizontal = textPadding + gpsWidth + textPadding + logoWidth + textPadding;
        int spacingVertical =  ( height - NDGStyleToolbox.fontSmall.getHeight() - NDGStyleToolbox.fontMedium.getHeight() )/3;
        g.drawString( title1, textOffsetHorizontal, spacingVertical );
        g.setFont( NDGStyleToolbox.fontMedium );
        g.drawString( title2, textOffsetHorizontal, spacingVertical +  NDGStyleToolbox.fontSmall.getHeight() + spacingVertical );
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

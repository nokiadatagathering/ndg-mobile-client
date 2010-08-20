/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.SurveysControl;
import br.org.indt.ndg.mobile.AppMIDlet;
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

    private String title1;
    private String title2;

    TitleBar(String title1, String title2) {
        this.title1 = title1;
        this.title2 = title2;
    }

    public void paint(Graphics g, Rectangle rect) {
        Image image = Screen.getRes().getImage("header");
        Font nokiaLarge = Screen.getRes().getFont("NokiaLarge18");
        Font nokiaSansWide = Screen.getRes().getFont("NokiaSansWide11");
        g.drawImage(image, 0, 0);
        g.setFont(nokiaSansWide);
        g.setColor(0x007b7b7b);
        g.drawString(title1, 65, 3);
        g.setFont(nokiaLarge);
        g.drawString(title2, 65, 17);
        if (SurveysControl.getInstance().isGPSConnected()) {
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

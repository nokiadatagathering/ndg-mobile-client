/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui.camera;

import br.org.indt.ndg.mobile.*;
import br.org.indt.ndg.mobile.multimedia.Camera;
import br.org.indt.ndg.mobile.multimedia.Picture;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author alexandre martini
 */
public class ViewFinderFormLCDUICanvas extends Canvas{
    private static ViewFinderFormLCDUICanvas instance = new ViewFinderFormLCDUICanvas();

    public static ViewFinderFormLCDUICanvas getInstance(){
        return instance;
    }

    private ViewFinderFormLCDUICanvas(){
    }
    
    public void show(){
        setFullScreenMode(true);
        Camera.getInstance().setFullScreen();
        Display.getDisplay(AppMIDlet.getInstance()).setCurrent(this);
    }

    protected void paint(Graphics g) {
        g.setClip(0, 0, 240, 320);
    }

    public void keyPressed(int code){
        int action = getGameAction(code);
        if(action == Canvas.FIRE){
            byte[] picture = Camera.getInstance().takePicture(320, 240);
            NDGCameraManager.getInstance().updatePhotoForm(picture);
        }
    }
}

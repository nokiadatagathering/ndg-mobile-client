/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.mobile.multimedia;

import br.org.indt.ndg.lwuit.ui.camera.ViewFinderFormLCDUICanvas;
import br.org.indt.ndg.mobile.logging.Logger;
import com.sun.lwuit.Image;
import com.sun.lwuit.MediaComponent;
import java.io.IOException;
import java.util.Date;
import javax.microedition.lcdui.Item;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;

/**
 *
 * @author alexandre martini
 */
public class Camera {
    private static Camera instance = new Camera();

    public static int THUMBNAIL_WIDTH = 62;
    public static int THUMBNAIL_HEIGHT = 54;

    private VideoControl vc;
    private Item viewFinder;
    private Player player;
    private boolean init;
    private MediaComponent mediaComp;
    byte[] imageData = null;

    private Camera(){
        try {
            player = Manager.createPlayer("capture://video");
            initCamera();
            vc = (VideoControl) player.getControl("VideoControl");
            if (vc != null) {
                viewFinder = (Item) vc.initDisplayMode(vc.USE_DIRECT_VIDEO, ViewFinderFormLCDUICanvas.getInstance());
                vc.setVisible(true);
            } else {
                throw new IllegalStateException("There is no VideoControl mechanism");
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (MediaException ex) {
            ex.printStackTrace();
        }
//        initCamera();
    }

    public void shutDown() {
        try {
            player.stop();
            init = false;
            imageData = null;
        } catch (MediaException ex) {
            Logger.getInstance().log("Player Stopped");
        }
    }

    public static Camera getInstance(){
        return instance;
    }

    public void initCamera(){
        if(!init){
            try {
                player.realize();
                player.prefetch();
                player.start();
                init = true;
            }
            catch (MediaException me) {
                Logger.getInstance().logException(me.getMessage());
            }
            catch(Exception e){
                Logger.getInstance().logException("General exception: " + e.getMessage());
            }
        }
    }

    private Item getViewFinder(){
        return viewFinder;
    }

    public Item setFullScreen(){
        initCamera();
        try {
            vc.setDisplayFullScreen(true);
        } catch (MediaException ex) {
            ex.printStackTrace();
        }
        return getViewFinder();
    }
  

    /**
     *
     * @return the last picture or null, if there is none.
     */
    public byte[] getLastPictureTaken(){
        return imageData;
    }

    public byte[] takePicture(int width, int height){
        initCamera();
        
        if(width <= 0 || height <= 0)
            throw new IllegalArgumentException("Invalid width or height");        
        try {
            if(vc == null){
                vc = (VideoControl)player.getControl("VideoControl");
            }
            imageData = vc.getSnapshot("encoding=jpeg" + "&width=" + width + "&height=" + height);            
        } catch (MediaException ex) {
            Logger.getInstance().logException(ex.getMessage());
        }
        return imageData;
    }

    public static Image createThumbnail(Image image) {
        int sourceWidth = image.getWidth();
        int sourceHeight = image.getHeight();

        int thumbWidth = 64;
        int thumbHeight = -1;

        if (thumbHeight == -1)
            thumbHeight = thumbWidth * sourceHeight / sourceWidth;

        Image thumb = Image.createImage(thumbWidth, thumbHeight);
        com.sun.lwuit.Graphics g = thumb.getGraphics();

        for (int y = 0; y < thumbHeight; y++) {
            for (int x = 0; x < thumbWidth; x++) {
                g.setClip(x, y, 1, 1);
                int dx = x * sourceWidth / thumbWidth;
                int dy = y * sourceHeight / thumbHeight;
                g.drawImage(image, x - dx, y - dy);
            }
        }
        return thumb;
    }


        /**
     * @deprecated only for use with LWUIT
     * @return
     */
    public MediaComponent getViewFinderLWUIT(){
        if(!init){
            //init();
        }
        return mediaComp;
    }


}

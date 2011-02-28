package br.org.indt.ndg.mobile.multimedia;

import br.org.indt.ndg.mobile.logging.Logger;
import com.sun.lwuit.Image;
import com.sun.lwuit.MediaComponent;
import java.io.IOException;
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
    private final String JPEG = "encoding=image/jpeg";
    private final String JPG = "encoding=image/jpg";

    private VideoControl vc;
    private Player player;
    private boolean init;
    private MediaComponent mediaComp;
    byte[] imageData = null;

    private Camera(){
        try {
            try
            {//for S40 devices
                player = Manager.createPlayer("capture://image");
            }
            catch(Exception ex)
            {//for S60 devices
                player = Manager.createPlayer("capture://video");
            }
            player.prefetch();
            player.realize();
            vc = (VideoControl) player.getControl("VideoControl");
            mediaComp = new MediaComponent(player);
            mediaComp.setFullScreen(false);
            player.start();
            init = true;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (MediaException ex) {
            ex.printStackTrace();
        }
    }

    public void shutDown() {
            mediaComp.stop();
            mediaComp.setVisible(false);
            player.deallocate();
            init = false;
            imageData = null;
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

    public byte[] takePicture(int width, int height){
        if(width <= 0 || height <= 0)
            throw new IllegalArgumentException("Invalid width or height");
        try {
            if(vc == null){
                vc = (VideoControl)player.getControl("VideoControl");
            }
            imageData = vc.getSnapshot( GetEncoding() + "&width=" + width + "&height=" + height);
        } catch (MediaException ex) {
            Logger.getInstance().logException(ex.getMessage());
        }
        return imageData;
    }

    private String GetEncoding() {
        String encodings = System.getProperty("video.snapshot.encodings");
        if( encodings.indexOf(JPEG) >=0 )
        {
            return JPEG;
        }
        else if ( encodings.indexOf(JPG) >= 0)
        {
            return JPG;
        }
        else
        {
            if( encodings.length() == 0)
            {
                return null;
            }
            else
            {
                int encodingEnd = encodings.indexOf(" ");
                if( encodingEnd == 0 || encodingEnd == encodings.length() || encodingEnd == -1 )
                {
                    return encodings;
                }
                else
                {
                    return encodings.substring(0, encodingEnd-1);
                }
            }
        }
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
            initCamera();
        }
        return mediaComp;
    }
}

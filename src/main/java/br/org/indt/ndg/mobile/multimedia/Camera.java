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

    private VideoControl vc = null;
    private Player player = null;
    private boolean init = false;
    private MediaComponent mediaComp = null;
    byte[] imageData = null;

    public static Camera getInstance(){
        return instance;
    }

    public void startCamera(){
        try {
            initializeCamera();
            player.start();
        } catch (MediaException ex) {
            Logger.getInstance().logException("Could not initialize Camera: " + ex.getMessage());
        } catch (IOException ex) {
            Logger.getInstance().logException("Could not initialize Camera: " + ex.getMessage());
        }
    }

    public void stopCamera() {
        deinitializeCamera();
    }

    public byte[] takePicture(int width, int height){
        if(width <= 0 || height <= 0)
            throw new IllegalArgumentException("Invalid width or height");
        if (!init)
            throw new IllegalStateException("Camera not initialized");

        try {
            imageData = vc.getSnapshot( GetEncoding() + "&width=" + width + "&height=" + height);
        } catch (MediaException ex) {
            Logger.getInstance().logException(ex.getMessage());
        }
        return imageData;
    }

    private Camera(){
    }

    private void initializeCamera() throws MediaException, IOException {
        if(!init){
            try { //for S40 devices
                player = Manager.createPlayer("capture://image");
            } catch(Exception ex) {//for S60 devices
                player = Manager.createPlayer("capture://video");
            }
            player.realize();
            player.prefetch();
            vc = (VideoControl) player.getControl("VideoControl");
            mediaComp = new MediaComponent(player);
            mediaComp.setFullScreen(false);
            init = true;
        }
    }

    private void deinitializeCamera() {
        if (init) {
            init = false;
            imageData = null;
            mediaComp.stop();
            player.deallocate(); // calls stop on player explicitly
            // FIX for N8 issue with landscapoe blocking, makes player unusable
            player.close();
            vc = null;
            player = null;
            mediaComp = null;
        }
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
            startCamera();
        }
        return mediaComp;
    }
}

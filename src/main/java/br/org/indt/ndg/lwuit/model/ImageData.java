package br.org.indt.ndg.lwuit.model;

import br.org.indt.ndg.lwuit.ui.Screen;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Image;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.location.Coordinates;
import javax.wireless.messaging.BinaryMessage;

/**
 *
 * @author damian.janicki
 */
public class ImageData {

    final static private String imageExtenstion = ".ndgImg";
    final static private String imgDir = Resources.ROOT_DIR + "imgTmp/";
    final private String uniqueId = String.valueOf(System.currentTimeMillis());
    final private String privPath = Resources.ROOT_DIR + "imgTmp/" + uniqueId + imageExtenstion;

    final public static int THUMBNAIL_SIZE = 50;

    Coordinates myLocation;
    Image thumbnail;

    // create directory
    static {
        try {
            FileConnection file = (FileConnection) Connector.open(
                    imgDir, Connector.READ_WRITE);
            if (!file.exists())
                    file.mkdir();
            file.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    static public void cleanTemporaryPhotoFiles() {
        try {
            FileConnection file = (FileConnection) Connector.open(
                    imgDir, Connector.READ_WRITE);
            String fileName = null;
            Enumeration e = file.list();
            file.close();
            while (e.hasMoreElements()) {
                try {
                    fileName = e.nextElement().toString();
                    file = (FileConnection) Connector.open(imgDir + fileName, Connector.READ_WRITE);
                    if (file.exists() && (fileName.endsWith(imageExtenstion))) {
                        file.delete();
                    }
                    file.close();
                } catch (SecurityException ex) {
                    // not accessible file or dir is not displayed
                } catch (Exception ex) {
                    // bad formatted file or dir is not displayed
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public ImageData(){
        myLocation = null;
    }

    public ImageData(byte[] data){
        saveData(data);
        myLocation = null;
    }

    public ImageData(byte[] data, Coordinates location){
        saveData(data);
        this.myLocation = location;
    }

    public byte[] getData() {
        return readData();
    }

    public void setGeoTag(Coordinates location) {
        myLocation = location;
    }

    public Coordinates getGeoTag() {
        return myLocation;
    }

    public Image getThumbnail() {
        if(thumbnail==null) {
            createThumbnail(readData());
        }
        return thumbnail;
    }

    private void saveData(byte[] data) {
        createThumbnail(data);

        try {
            FileConnection file = (FileConnection) Connector.open(privPath, Connector.READ_WRITE);
            if(!file.exists())
            {
                file.create();
            } else {
                file.delete();
                file.create();
            }
            OutputStream output = file.openOutputStream();
            output.write(data);
            output.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private byte[] readData() {
        byte[] result = null;
        FileConnection file = null;
        InputStream input = null;
        try {
            file = (FileConnection) Connector.open(privPath, Connector.READ);
            if (file.exists()) {
                input = file.openInputStream();
                if(file.fileSize() > Integer.MAX_VALUE) {
                    // hope this wont happen in 21 century
                    throw new IOException("file is to large?!?");
                }
                result = new byte[(int) file.fileSize()];
                input.read(result);
                input.close();

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if ( input != null ) {
                try {
                    input.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if ( file != null ) {
                try {
                    file.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return result;
    }

   private void createThumbnail(byte[] rawData){
        Image picture = null;
        try {
            picture = Image.createImage(rawData, 0, rawData.length);
        } catch ( Exception e){ // in case of corrupt data
            picture = Screen.getRes().getImage("camera-icon-imagetaken");
        }
        thumbnail = picture.scaled(THUMBNAIL_SIZE, THUMBNAIL_SIZE);
    }

    public void delete() {
        FileConnection file = null;
        try {
            file = (FileConnection) Connector.open(privPath, Connector.READ_WRITE);
            if (file.exists()) {
                file.delete();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if ( file != null ) {
                try {
                    file.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}

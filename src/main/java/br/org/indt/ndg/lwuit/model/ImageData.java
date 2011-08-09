package br.org.indt.ndg.lwuit.model;

import br.org.indt.ndg.lwuit.ui.Screen;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.NdgConsts;
import br.org.indt.ndg.mobile.error.OutOfMemoryErrorExtended;
import br.org.indt.ndg.mobile.logging.Logger;
import com.sun.lwuit.Image;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.location.Coordinates;

/**
 *
 * @author damian.janicki
 */
public class ImageData {
    final static private String imageExtenstion = ".ndgImg";
    final public static int THUMBNAIL_SIZE = 50;

    // This directory is used to store original images in case user decides to revert the changes
    private String imgDir = AppMIDlet.getInstance().getRootDir()
                          + AppMIDlet.getInstance().getFileSystem().getSurveyDirName()
                          + "imgTmp/";

    private String uniqueId = String.valueOf(System.currentTimeMillis()) + imageExtenstion;
    final private String privPath = imgDir + uniqueId ;


    private Coordinates myLocation;
    private Image thumbnail;

    // create directory
    private void createDir() {
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

    public ImageData( String aPath ){
        this(aPath, null);
    }

    public ImageData( String aPath, Coordinates location ){
        myLocation = location;
        loadResult( aPath );
    }

    public ImageData( byte[] data ) {
        this(data, null);
    }

    public ImageData( byte[] data, Coordinates location ){
        saveData(data);
        myLocation = location;
    }

    public String saveResult() throws OutOfMemoryErrorExtended {
        String outPath = null;
        FileConnection fileout = null;
        try {
            outPath = AppMIDlet.getInstance().getRootDir()
                           + AppMIDlet.getInstance().getFileSystem().getSurveyDirName()
                           + "b_"
                           + AppMIDlet.getInstance().getFileSystem().getResultFilename()
                           + "/";
            fileout = (FileConnection) Connector.open(outPath, Connector.READ_WRITE);

            if( !fileout.exists() ) {
                fileout.mkdir();
                fileout.close();
                fileout = null;
            }

            fileout = (FileConnection) Connector.open( outPath + uniqueId, Connector.READ_WRITE );
            if ( fileout.exists()) {
                fileout.delete();
            }
            fileout.create();

            fileout.openOutputStream().write( getData() );
        } catch (IOException ex ) {
            ex.printStackTrace();
        } finally {
            if( fileout != null )
                try {
                fileout.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return outPath + uniqueId;
    }

    public byte[] getData() throws OutOfMemoryErrorExtended {
        return readData();
    }

    public void setGeoTag(Coordinates location) {
        myLocation = location;
    }

    public Coordinates getGeoTag() {
        return myLocation;
    }

    public Image getThumbnail() {
        if ( thumbnail == null ) {
            try {
                createThumbnail( readData() );
            } catch (OutOfMemoryErrorExtended ex) {
                Logger.getInstance().logException(ex.getMessage());
            }
        }
        return thumbnail;
    }

    private void loadResult( String aPath ) {
        FileConnection file = null;
        InputStream input = null;
        try {
            file = (FileConnection) Connector.open( aPath, Connector.READ);
            if(!file.exists()) {
               throw new IllegalStateException("Missing binary file");
            }
            input = file.openInputStream();
            long fileSize = file.fileSize();
            byte[] data = new byte[(int)fileSize];
            input.read( data);
            uniqueId = aPath.substring( aPath.lastIndexOf('/'), aPath.length() );
            saveData(data);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if ( input != null ) {
                try {
                    input.close();
                } catch (IOException ex) {}
            }
            if ( file != null ) {
                try {
                    file.close();
                } catch (IOException ex) {}
            }
        }
    }

    private void saveData(byte[] data) {
        createDir();
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

    private byte[] readData() throws OutOfMemoryErrorExtended {
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
        } catch (OutOfMemoryError ex) {
            throw new OutOfMemoryErrorExtended("Failed to read image from file");
        } finally {
            if ( input != null ) {
                try {
                    input.close();
                } catch (IOException ex) {}
            }
            if ( file != null ) {
                try {
                    file.close();
                } catch (IOException ex) {}
            }
        }
        return result;
    }

   private void createThumbnail(byte[] rawData){
        Image picture = null;
        thumbnail = null;
        try {
            try {
                picture = Image.createImage(rawData, 0, rawData.length);
            } catch ( Exception e){ // in case of corrupt data
                // TODO icon resource should be already scaled in file so we could avoid scaling here
                picture = Screen.getRes().getImage("camera-icon-imagetaken");
            }
            thumbnail = picture.scaled(THUMBNAIL_SIZE, THUMBNAIL_SIZE);
        } catch ( OutOfMemoryError ex ) {
           Logger.getInstance().logException("OutOfMemoryError: Failed to create thumbnail: " + ex.getMessage());
           thumbnail = Image.createImage(THUMBNAIL_SIZE, THUMBNAIL_SIZE);
        }
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

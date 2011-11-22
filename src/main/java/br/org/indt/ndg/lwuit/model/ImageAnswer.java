/*
*  Nokia Data Gathering
*
*  Copyright (C) 2011 Nokia Corporation
*
*  This program is free software; you can redistribute it and/or
*  modify it under the terms of the GNU Lesser General Public
*  License as published by the Free Software Foundation; either
*  version 2.1 of the License, or (at your option) any later version.
*
*  This program is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
*  Lesser General Public License for more details.
*
*  You should have received a copy of the GNU Lesser General Public License
*  along with this program.  If not, see <http://www.gnu.org/licenses/
*/

package br.org.indt.ndg.lwuit.model;

import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import br.org.indt.ndg.mobile.LocationHelper;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.error.OutOfMemoryErrorExtended;
import br.org.indt.ndg.mobile.logging.Logger;
import br.org.indt.ndg.mobile.multimedia.Base64Coder;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

/**
 *
 * @author alexandre martini
 */

// TODO removing of tmp files during interview when one picture is repeated many times

public class ImageAnswer extends NDGAnswer{

    private final static int MAX_CHUNK_SIZE = 3 * 1024;//3kB
    private Vector images = new Vector();

    public Vector getImages (){
        return images;
    }

    public Object getValue(){
        // TODO notify programmer not to use this method for ImageAnswer
        // use getImages insted
        return null;
    }

    public void save( PrintStream _output ) {
        ImageData imgData = null;
        for (int idx = 0; idx < getImages().size(); idx++) {
            try {
                imgData = (ImageData) getImages().elementAt(idx);
                byte[] imageData = imgData.getData();
                if (imageData != null) {
                    if(System.getProperty("microedition.location.version") != null) {
                        if (LocationHelper.getInstance().getGeoTag() != null) {
                            _output.print("<img_data " + "latitude=\"" + LocationHelper.getInstance().getGeoTag().getLatitude() + "\"" + " " + "longitude=\"" + LocationHelper.getInstance().getGeoTag().getLongitude() + "\"" + " " + "type=\"binary\">");
                        } else {
                         _output.print("<img_data type=\"binary\">");
                        }
                    }
                    else
                        _output.print("<img_data type=\"binary\">");
                    try {
                        _output.print(imgData.saveResult());
                    } finally {
                        _output.println("</img_data>");
                    }
                } else {
                    // ignoring picture without actual image
                }
            } catch (OutOfMemoryErrorExtended ex) {
                Logger.getInstance().logException("Failed to save image data to answer" );
            }
        }
    }

    public void save(PrintStream _output, boolean appendBinaryData ) {
        if( appendBinaryData ) {
            ImageData imgData = null;
            for(int idx = 0; idx < getImages().size(); idx++){
                boolean outOfMemoryNotified = false;
                try {
                    imgData = (ImageData) getImages().elementAt(idx);
                    byte[] imageData = imgData.getData();
                    if (imageData != null) {
                        if(System.getProperty("microedition.location.version") != null) {
                            if (LocationHelper.getInstance().getGeoTag() != null) {
                                _output.print("<img_data " + "latitude=\"" + LocationHelper.getInstance().getGeoTag().getLatitude() + "\"" + " " + "longitude=\"" + LocationHelper.getInstance().getGeoTag().getLongitude() + "\">");
                            } else {
                                _output.print("<img_data>");
                            }
                        }
                        else
                            _output.print("<img_data>");
                        try {
                            if( imageData.length > MAX_CHUNK_SIZE ) {
                                chunkWrite( _output, imageData );
                            }else {
                                _output.write(new String(Base64Coder.encode(imageData)).getBytes("UTF-8"));
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        } catch (OutOfMemoryError er) {
                            throw new OutOfMemoryErrorExtended(Resources.FAIL_IMAGE_SAVE);
                        } finally {
                            _output.println("</img_data>");
                        }
                    } else {
                        // ignoring picture without actual image
                    }
                } catch (OutOfMemoryErrorExtended ex) {
                    if( !outOfMemoryNotified ) {
                        GeneralAlert.getInstance().addCommand( GeneralAlert.DIALOG_OK, true );
                        GeneralAlert.getInstance().show( Resources.OUT_OF_MEMORY, Resources.NOT_ENOUGH_MEMORY, GeneralAlert.ERROR );
                        outOfMemoryNotified = true;
                    }
                }
            }
        } else {
            save( _output );
        }
    }

    private void chunkWrite( PrintStream _output, byte[] imageData ) throws IOException {
        int startIndex = 0;
        byte[] buffer = new byte[MAX_CHUNK_SIZE];
        int chunkSize = MAX_CHUNK_SIZE;

        while ( startIndex + chunkSize < imageData.length ) {
            System.arraycopy( imageData, startIndex, buffer, 0, chunkSize );
            _output.write( new String( Base64Coder.encode( buffer, chunkSize ) ).getBytes("UTF-8") );
            startIndex+= chunkSize;
            chunkSize = startIndex + MAX_CHUNK_SIZE < imageData.length ? MAX_CHUNK_SIZE :  imageData.length - startIndex;
        }
        //last chunk
        if ( chunkSize > 0 ) {
             System.arraycopy( imageData, startIndex, buffer, 0, chunkSize );
            _output.write( new String( Base64Coder.encode( buffer, chunkSize ) ).getBytes("UTF-8") );
        }
    }
}

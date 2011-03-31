package br.org.indt.ndg.lwuit.model;

import br.org.indt.ndg.mobile.multimedia.Base64Coder;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;
import javax.microedition.location.Coordinates;

/**
 *
 * @author alexandre martini
 */

// TODO removing of tmp files during interview when one picture is repeated many times

public class ImageAnswer extends NDGAnswer{

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
        for(int idx = 0; idx < getImages().size(); idx++){
            imgData = (ImageData) getImages().elementAt(idx);
            byte[] imageData = imgData.getData();
            if (imageData != null) {
                Coordinates loc = imgData.getGeoTag();
                if ( loc != null) {
                    _output.print(
                            "<img_data " +
                            "latitude=\"" + loc.getLatitude() + "\"" + " " +
                            "longitude=\"" + loc.getLongitude() + "\"" + " " +
                            "type=\"binary\">");
                } else {
                    _output.print("<img_data type=\"binary\">");
                }
                _output.print( imgData.saveResult() );
                _output.println("</img_data>");
            } else {
                // ignoring picture without actual image
            }
        }
    }

    public void save(PrintStream _output, boolean appendBinaryData ) {
        if( appendBinaryData ) {
            ImageData imgData = null;
            for(int idx = 0; idx < getImages().size(); idx++){
                imgData = (ImageData)getImages().elementAt(idx);
                byte[] imageData = imgData.getData();
                if (imageData != null) {
                    Coordinates loc = imgData.getGeoTag();
                    if ( loc != null) {
                        _output.print(
                                "<img_data " +
                                "latitude=\"" + loc.getLatitude() + "\"" + " " +
                                "longitude=\"" + loc.getLongitude() + "\">" );
                    } else {
                        _output.print("<img_data>");
                    }
                    try{
                        _output.write(new String(Base64Coder.encode(imageData)).getBytes("UTF-8"));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    _output.println("</img_data>");
                } else {
                    // ignoring picture without actual image
                }
            }
        } else {
            save( _output );
        }
    }
}

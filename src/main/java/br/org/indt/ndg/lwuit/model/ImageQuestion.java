package br.org.indt.ndg.lwuit.model;

import br.org.indt.ndg.mobile.multimedia.Base64Coder;
import java.io.IOException;
import java.io.PrintStream;
import javax.microedition.location.Coordinates;

/**
 *
 * @author alexandre martini
 */
public class ImageQuestion extends Question{

    private int maxCount = 1;
    
    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxImages) {
        this.maxCount = maxImages;
    }

    //IMPLEMENT
    public String getDisplayValue() throws Exception{
        return "getDisplayvalue";
    }

// XML Structure:
//    <img_data latitude="xxx" longitude="yyy">...</img_data>
//    <img_data latitude="xxx" longitude="yyy">...</img_data>
//    ...
    public void save(PrintStream _output) {
        ImageAnswer imgAnswer = ((ImageAnswer) getAnswer());
        ImageData imgData = null;
        for(int idx = 0; idx < imgAnswer.getImages().size(); idx++){
            imgData = (ImageData) imgAnswer.getImages().elementAt(idx);
            byte[] imageData = imgData.getData();
            if (imageData != null) {
                Coordinates loc = imgData.getGeoTag();
                if ( loc != null) {
                    _output.print(
                            "<img_data " +
                            "latitude=\"" + loc.getLatitude() + "\"" + " " +
                            "longitude=\"" + loc.getLongitude() + "\"" +
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
            ImageAnswer imgAnswer = ((ImageAnswer) getAnswer());
            ImageData imgData = null;
            for(int idx = 0; idx < imgAnswer.getImages().size(); idx++){
                imgData = (ImageData) imgAnswer.getImages().elementAt(idx);
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

    public boolean passConstraints() {
        return true;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.mobile.multimedia;




import com.sun.lwuit.Image;
import java.util.Date;
//import javax.microedition.lcdui.Graphics;

/**
 *
 * @author 
 */
public class Picture {    
    private byte[] rawData;
    //private int width;
    //private int height;

    public static Picture createPicture(byte[] data) {
        return new Picture(data);
    }


    //private Picture(){}
    private Picture(byte[] data){
        //if(width <= 0 || height <= 0)
        //    throw new IllegalArgumentException("Invalid width or height");
        if(data == null)
            throw new IllegalArgumentException("Picture data cannot be null");
        //this.width = width;
        //this.height = height;
        this.rawData = data;
    }

    public Image getThumbnail(){
        Image picture = Image.createImage(rawData, 0, rawData.length);
        return createThumbnail(picture);
    }

    
    public byte[] getData(){
        return rawData;
    }  

    public Image getImage(){
        return Image.createImage(rawData, 0, rawData.length);
    }

    private Image createThumbnail(Image image) {
        return image.scaled(50, 50);
    }
}

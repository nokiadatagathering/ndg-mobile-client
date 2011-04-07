/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui.camera;

import br.org.indt.ndg.lwuit.model.ImageData;
import br.org.indt.ndg.mobile.multimedia.Base64Coder;
import com.nokia.xfolite.xforms.dom.BoundElement;
import com.sun.lwuit.Button;
import com.sun.lwuit.Component;
import com.sun.lwuit.Image;

/**
 *
 * @author damian.janicki
 */
public class OpenRosaCameraManager implements ICameraManager{

    private CameraManagerListener listener = null;
    private byte[] imageArray = null;

    private static OpenRosaCameraManager instance = new OpenRosaCameraManager();
    public static OpenRosaCameraManager getInstance(){
        return instance;
    }

    public void reset(){
        imageArray = null;
    }

    public byte[] getCurrentImageData() {
        return imageArray;
    }

    public void updateInterviewForm() {
        listener.update();
    }

    public void updatePhotoForm(byte[] picture) {
        imageArray = picture;
    }

    public void deletePhoto() {
        imageArray = null;
        listener.update();
    }

    public void sendPostProcessData(CameraManagerListener managerListener){
        listener = managerListener;
    }

    public boolean showLastInterviewForm() {
        listener.update();
        return true;
    }

    public byte[] getImageArray(){
        return imageArray;
    }

    public String getImageStringValue(){
        if(imageArray != null){
            return new String(Base64Coder.encode(imageArray));
        }else{
            return "";
        }
    }

    public void setImageArray(byte[] image){
        imageArray = image;
    }
}

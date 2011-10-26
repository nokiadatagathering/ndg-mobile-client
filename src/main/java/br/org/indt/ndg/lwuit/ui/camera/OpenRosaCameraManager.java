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

package br.org.indt.ndg.lwuit.ui.camera;

import br.org.indt.ndg.mobile.multimedia.Base64Coder;

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

    public void setIsFromFile(boolean bVal) {
        //not needed
    }
}

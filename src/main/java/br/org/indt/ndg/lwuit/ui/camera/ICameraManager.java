/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui.camera;

import br.org.indt.ndg.lwuit.model.ImageData;

/**
 *
 * @author damian.janicki
 */
public interface ICameraManager {
    byte[] getCurrentImageData();
    void updateInterviewForm();
    void updatePhotoForm(byte[] picture);
    void deletePhoto();
    boolean showLastInterviewForm();
}

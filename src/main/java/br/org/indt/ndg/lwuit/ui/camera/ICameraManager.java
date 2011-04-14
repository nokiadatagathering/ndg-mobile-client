package br.org.indt.ndg.lwuit.ui.camera;

import br.org.indt.ndg.mobile.error.OutOfMemoryErrorExtended;

/**
 *
 * @author damian.janicki
 */
public interface ICameraManager {
    byte[] getCurrentImageData() throws OutOfMemoryErrorExtended;
    void updateInterviewForm();
    void updatePhotoForm(byte[] picture);
    void deletePhoto();
    boolean showLastInterviewForm();
    void setIsFromFile(boolean bVal);
}

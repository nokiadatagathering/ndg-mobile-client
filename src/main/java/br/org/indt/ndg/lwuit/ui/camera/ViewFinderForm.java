package br.org.indt.ndg.lwuit.ui.camera;
import br.org.indt.ndg.lwuit.control.BackPhotoFormCommand;
import br.org.indt.ndg.lwuit.control.CapturePhotoCommand;
import br.org.indt.ndg.lwuit.ui.Screen;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.error.OutOfMemoryErrorExtended;
import br.org.indt.ndg.mobile.logging.Logger;
import br.org.indt.ndg.mobile.multimedia.Camera;
import br.org.indt.ndg.mobile.settings.PhotoSettings.PhotoResolution;
import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.MediaComponent;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;


/**
 *
 * @author alexandre martini
 */
public class ViewFinderForm extends Screen implements ActionListener {
    private final Container container = new Container(new BorderLayout());
    private boolean enableTakingPicture;

    protected void loadData() {
        enableTakingPicture = true;
    }

    protected void customize() {
        setTitle( "", "");
        form.setLayout(new BorderLayout());

        MediaComponent comp = Camera.getInstance().getViewFinderLWUIT();
        comp.getStyle().setMargin(0, 0, 0, 0);

        container.removeAll();
        form.removeComponent(container);

        container.addComponent(BorderLayout.CENTER, comp);
        form.removeAll(); // does not always work?
        form.removeAllCommands();
        form.addCommand(BackPhotoFormCommand.getInstance().getCommand());
        form.addCommand(CapturePhotoCommand.getInstance().getCommand());
        form.addGameKeyListener(Display.GAME_FIRE, this);
        form.addCommandListener(this);
        form.setScrollable(false);
        form.addComponent(BorderLayout.CENTER, container);
    }

    private void capturePicture() throws OutOfMemoryErrorExtended {
        PhotoResolution resolution = AppMIDlet.getInstance().getSettings().getStructure().getPhotoResolution();
        byte[] picture = null;
        try {
            picture = Camera.getInstance().takePicture(resolution.getWidth(), resolution.getHeight());
            AppMIDlet.getInstance().getCurrentCameraManager().setIsFromFile(false);
            AppMIDlet.getInstance().getCurrentCameraManager().updatePhotoForm(picture);
        } catch (OutOfMemoryError ex) {
            throw new OutOfMemoryErrorExtended("Failed to create raw image data");
        } finally {
            stopCamera();
        }
    }

    public void capturePictureAndShowPhoto() {
        if ( enableTakingPicture ) {
            enableTakingPicture = false;
            try {
                capturePicture();
            } catch ( OutOfMemoryErrorExtended ex) {
                // this is extremly memory consuming operation so it will often fail on S40
                Logger.getInstance().log(ex.getMessage());
            } finally {
                Screen.show(PhotoForm.class, true);
            }
        }
    }

    public void stopCamera() {
        Camera.getInstance().stopCamera();
    }

    public void actionPerformed(ActionEvent evt) {
        if ( evt.getKeyEvent() == Display.GAME_FIRE ||
                evt.getSource() == CapturePhotoCommand.getInstance().getCommand() ) {
            CapturePhotoCommand.getInstance().execute(this);
        } else if(evt.getSource() == BackPhotoFormCommand.getInstance().getCommand() ) {
            BackPhotoFormCommand.getInstance().execute(null);
        }
    }
}

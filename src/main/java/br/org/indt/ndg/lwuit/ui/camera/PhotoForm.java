package br.org.indt.ndg.lwuit.ui.camera;

import br.org.indt.ndg.lwuit.control.OKPhotoFormCommand;
import br.org.indt.ndg.lwuit.control.TakePictureAgainCommand;
import br.org.indt.ndg.lwuit.ui.*;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.error.OutOfMemoryErrorExtended;
import br.org.indt.ndg.mobile.logging.Logger;
import com.sun.lwuit.Button;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;

/**
 *
 * @author alexandre martini
 */
public class PhotoForm extends Screen implements ActionListener {

    protected void loadData() {
    }

    protected void customize() {
        setTitle( "", "" );
        form.removeAll();
        form.removeAllCommands();

        form.addCommand(TakePictureAgainCommand.getInstance().getCommand());
        form.addCommand(OKPhotoFormCommand.getInstance().getCommand());

        Component preview = null;
        try {
            byte[] imageData = AppMIDlet.getInstance().getCurrentCameraManager().getCurrentImageData();
            Image image = null;
            try {
                image = Image.createImage(imageData, 0, imageData.length);
            } catch ( Exception e){ // in case of corrupt data
                image = Screen.getRes().getImage("camera-icon-imagetaken");
            }
            imageData = null;
            image = scaleImage(image);
            preview = new Label(image);
        } catch ( OutOfMemoryErrorExtended ex ) {
            Logger.getInstance().logException(ex.getMessage());
        } catch ( OutOfMemoryError ex) {
            Logger.getInstance().logException(ex.getMessage());
        } finally {
            if (preview == null) {
                preview = new TextArea(Resources.EFAILED_LOAD_IMAGE_LIMITED_DEVICE_RESOURCES);
                ((TextArea)preview).setAlignment(Label.CENTER);
            }
        }
        preview.getStyle().setMargin( 0, 0, 0, 0 );
        preview.getStyle().setPadding( 0, 0, 0, 0 );
        preview.setIsScrollVisible(false);

        form.setScrollable(false);
        form.addComponent(preview);
        try{
            form.removeCommandListener(this);
        } catch (NullPointerException npe ) {
            //during first initialisation thwors exception.
            //this ensure that we have registered listener once
        }
        form.addCommandListener(this);
    }

    // this is extremly memory consuming operation so it will often fail on S40
    private Image scaleImage( Image image ) throws OutOfMemoryErrorExtended {
        try {
            image = image.scaled( form.getPreferredSize().getWidth(),
                              form.getPreferredSize().getHeight()
                                - Display.getInstance().getCurrent().getSoftButton(0).getPreferredH());
        }  catch ( OutOfMemoryError ex ) {
            throw new OutOfMemoryErrorExtended("Failed to scale the image: " + ex.getMessage());
        }
        return image;
    }

    public void actionPerformed(ActionEvent arg0) {
        Command cmd = arg0.getCommand();
        if(cmd == TakePictureAgainCommand.getInstance().getCommand()){
            TakePictureAgainCommand.getInstance().execute(null);
        }
        else if(cmd == OKPhotoFormCommand.getInstance().getCommand()){
            OKPhotoFormCommand.getInstance().execute(null);
        }
        else {
            throw new IllegalStateException("Invalid Command on actionPerformed");
        }
    }
}

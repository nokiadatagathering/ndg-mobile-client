package br.org.indt.ndg.lwuit.ui.camera;

import br.org.indt.ndg.lwuit.control.OKPhotoFormCommand;
import br.org.indt.ndg.lwuit.control.TakePictureAgainCommand;
import br.org.indt.ndg.lwuit.ui.*;
import com.sun.lwuit.Button;
import com.sun.lwuit.Command;
import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
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
        createScreen();

        form.addCommand(TakePictureAgainCommand.getInstance().getCommand());
        form.addCommand(OKPhotoFormCommand.getInstance().getCommand());

        byte[] imageData = NDGCameraManager.getInstance().getCurrentImageData().getData();
        Image image = null;
        try {
            image = Image.createImage(imageData, 0, imageData.length);
        } catch ( Exception e){ // in case of corrupt data
            image = Screen.getRes().getImage("camera-icon-imagetaken");
        }
        Label img = new Label(image.scaled( form.getPreferredSize().getWidth(),
                                            form.getPreferredSize().getHeight()
                                          - Display.getInstance().getCurrent().getSoftButton(0).getPreferredH()));
        img.getStyle().setMargin( 0, 0, 0, 0 );
        img.getStyle().setPadding( 0, 0, 0, 0 );
        img.setIsScrollVisible(false);

        form.setScrollable(false);
        form.addComponent(img);
        try{
            form.removeCommandListener(this);
        } catch (NullPointerException npe ) {
            //during first initialisation thwors exception.
            //this ensure that we have registered listener once
        }
        form.addCommandListener(this);
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

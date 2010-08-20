/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui.camera;

import br.org.indt.ndg.lwuit.control.OKPhotoFormCommand;
import br.org.indt.ndg.lwuit.control.TakePictureAgainCommand;
import br.org.indt.ndg.lwuit.model.Answer;
import br.org.indt.ndg.lwuit.ui.*;
import br.org.indt.ndg.mobile.multimedia.Picture;
import com.sun.lwuit.Button;
import com.sun.lwuit.Command;
import com.sun.lwuit.Container;
import com.sun.lwuit.Image;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.geom.Dimension;



/**
 *
 * @author alexandre martini
 */
public class PhotoForm extends Screen implements ActionListener{
    private final static int PHOTO_WIDTH_DISPLAY = 256;
    private final static int PHOTO_HEIGHT_DISPLAY = 192;
    private Container container;

    protected void loadData() {        
    }

    protected void customize() {
        createScreen();        

        Dimension d = new Dimension();
        d.setWidth(ViewFinderForm.VIEWFINDER_WIDTH);
        d.setHeight(ViewFinderForm.VIEWFINDER_HEIGHT);

        byte[] imageData = (byte[]) NDGCameraManager.getInstance().getCurrentImageQuestion().getAnswer().getValue();
        Image image = Image.createImage(imageData, 0, imageData.length);
        Button imgButton = new Button(image.scaled(PHOTO_WIDTH_DISPLAY, PHOTO_HEIGHT_DISPLAY));
        //Button imgButton = new Button();
        
        container = new Container();
        container.setPreferredSize(d);
        container.addComponent(imgButton);
        form.addComponent(container);
        form.addCommand(OKPhotoFormCommand.getInstance().getCommand());
        form.addCommand(TakePictureAgainCommand.getInstance().getCommand());
        form.setCommandListener(this);
    }

    public void actionPerformed(ActionEvent arg0) {
        Command cmd = arg0.getCommand();
        if(cmd == TakePictureAgainCommand.getInstance().getCommand()){
            TakePictureAgainCommand.getInstance().execute(null);
        }
        else if(cmd == OKPhotoFormCommand.getInstance().getCommand()){
            OKPhotoFormCommand.getInstance().execute(null);
                       
        }
        else
            throw new IllegalStateException("Invalid Command on actionPerformed");
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui.camera;

import br.org.indt.ndg.lwuit.control.OKPhotoFormCommand;
import br.org.indt.ndg.lwuit.control.TakePictureAgainCommand;
import br.org.indt.ndg.lwuit.ui.*;
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
public class PhotoForm extends Screen implements ActionListener {

    private Container container;

    protected void loadData() {        
    }

    protected void customize() {
        createScreen();        

        form.addCommand(OKPhotoFormCommand.getInstance().getCommand());
        form.addCommand(TakePictureAgainCommand.getInstance().getCommand());

        byte[] imageData = (byte[]) NDGCameraManager.getInstance().getCurrentImageQuestion().getAnswer().getValue();
        Image image = Image.createImage(imageData, 0, imageData.length);
        Button imgButton = new Button(image.scaled(form.getPreferredSize().getWidth() - 15,
                form.getPreferredSize().getHeight()-form.getSoftButton(0).getParent().getPreferredH() - 15));
        // getting menu height does not work as expected, so additionally -15
        imgButton.setIsScrollVisible(false);
        container = new Container();
        container.addComponent(imgButton);
        container.setScrollable(false);
        form.addComponent(container);
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

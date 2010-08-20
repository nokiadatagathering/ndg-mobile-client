/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui.camera;
import br.org.indt.ndg.lwuit.ui.*;
import br.org.indt.ndg.mobile.logging.Logger;
import br.org.indt.ndg.mobile.multimedia.Camera;
import br.org.indt.ndg.mobile.multimedia.Picture;
import com.sun.lwuit.Button;
import com.sun.lwuit.Command;
import com.sun.lwuit.Container;
import com.sun.lwuit.MediaComponent;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.geom.Dimension;
import java.io.IOException;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;

/**
 *
 * @author alexandre martini
 */
public class ViewFinderForm extends Screen implements ActionListener{
    private Command takePictureCmd;
    private Container container;
    private MediaComponent comp;
    

    public static int VIEWFINDER_WIDTH = 256;
    public static int VIEWFINDER_HEIGHT = 192;
        

    protected void loadData() {
        
    }

    protected void customize() {
        createScreen();
        
        Dimension d = new Dimension();
        d.setWidth(VIEWFINDER_WIDTH);
        d.setHeight(VIEWFINDER_HEIGHT);

        comp = Camera.getInstance().getViewFinderLWUIT();
        comp.setPreferredSize(d);

        Logger.getInstance().log("MediaComp: " + comp);
        Logger.getInstance().log("Media Comp info: " + comp.isEnabled());
        Logger.getInstance().log("Media Comp info 2: " + comp.isFocusPainted());
        Logger.getInstance().log("Media Comp info 3: " + comp.isFocusable());
        Logger.getInstance().log("Media Comp info 4: " + comp.isScrollVisible());
        Logger.getInstance().log("Media Comp info 5: " + comp.isVisible());

        container = new Container();
        container.setPreferredSize(d);
        if(!container.contains(comp))
                container.addComponent(comp);

        Logger.getInstance().log("Container: " + container);
        Logger.getInstance().log("Container info: " + container.isEnabled());
        Logger.getInstance().log("Container info 2: " + container.isFocusPainted());
        Logger.getInstance().log("Container info 3: " + container.isFocusable());
        Logger.getInstance().log("Container info 4: " + container.isVisible());
        
        takePictureCmd = new Command("Take Picture");

        form.addCommand(takePictureCmd);
        form.setCommandListener(this);
        form.addComponent(container);

        Logger.getInstance().log("Form: " + form);
        Logger.getInstance().log("Form info: " + form.isEnabled());
        Logger.getInstance().log("Form info 2: " + form.isFocusable());
        Logger.getInstance().log("Form info:3 " + form.isFocusPainted());
        Logger.getInstance().log("Form info 4: " + form.isEnabled());

        Logger.getInstance().log("end customize");
    }

    private void showPicture(){
        Camera.getInstance().takePicture(VIEWFINDER_WIDTH, VIEWFINDER_HEIGHT);
        Screen.show(PhotoForm.class, true);
    }

    public void actionPerformed(ActionEvent evt) {
        Object obj = evt.getCommand();

        Logger.getInstance().log("actionPerformed: " + obj.toString());

        if(obj == takePictureCmd){            
            showPicture();
        }        
        else
            throw new IllegalStateException("Invalid Command on actionPerformed");
    }

}

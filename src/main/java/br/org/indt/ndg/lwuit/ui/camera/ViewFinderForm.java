/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui.camera;
import br.org.indt.ndg.lwuit.ui.*;
import br.org.indt.ndg.mobile.multimedia.Camera;
import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.MediaComponent;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;


/**
 *
 * @author alexandre martini
 */
public class ViewFinderForm extends Screen implements ActionListener {
    private Container container;
    private MediaComponent comp;
    private boolean enableTakingPicture;
     

    protected void loadData() {
        enableTakingPicture = true;
    }

    protected void customize() {
        createScreen();
        
        comp = Camera.getInstance().getViewFinderLWUIT();
        container = new Container();
        if(!container.contains(comp))
            container.addComponent(comp);
        
        form.addGameKeyListener(Display.GAME_FIRE, this);
        form.addComponent(container);
    }

    private void showPicture(){
        byte[]  picture = Camera.getInstance().takePicture(comp.getWidth(), comp.getHeight());
        NDGCameraManager.getInstance().updatePhotoForm(picture);
        Camera.getInstance().shutDown();
        container.removeAll();
        form.removeAll();
        Screen.show(PhotoForm.class, true);
    }

    public void actionPerformed(ActionEvent evt) {
        if(evt.getKeyEvent() == Display.GAME_FIRE && enableTakingPicture ) {
            enableTakingPicture = false;
            showPicture();
        }
    }
}

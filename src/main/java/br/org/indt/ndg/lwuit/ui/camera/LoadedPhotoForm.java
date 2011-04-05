package br.org.indt.ndg.lwuit.ui.camera;

import br.org.indt.ndg.lwuit.control.BackPreviewLoadedFile;
import br.org.indt.ndg.lwuit.control.OKPhotoFormCommand;
import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import br.org.indt.ndg.lwuit.ui.Screen;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Button;
import com.sun.lwuit.Command;
import com.sun.lwuit.Container;
import com.sun.lwuit.Image;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;


public class LoadedPhotoForm extends Screen implements ActionListener {

    private Container container;

    protected void loadData() {
    }

    protected void customize() {
        form.removeAll();
        form.removeAllCommands();
        byte[] imageData = NDGCameraManager.getInstance().getCurrentImageData().getData();
        Image image = null;
        try {
            image = Image.createImage(imageData, 0, imageData.length);
        } catch (RuntimeException ex) {
            GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
            GeneralAlert.getInstance().show(Resources.ERROR_TITLE, Resources.ELOAD_IMAGES, GeneralAlert.ERROR);
            image = Screen.getRes().getImage("camera-icon");

        }

        form.addCommand(BackPreviewLoadedFile.getInstance().getCommand());
        form.addCommand(OKPhotoFormCommand.getInstance().getCommand());


        Button imgButton = new Button(image.scaled(form.getPreferredSize().getWidth() - 15,
                form.getPreferredSize().getHeight()-form.getSoftButton(0).getParent().getPreferredH() - 15));
        // getting menu height does not work as expected, so additionally -15
        imgButton.setIsScrollVisible(false);
        container = new Container();
        container.addComponent(imgButton);
        container.setScrollable(false);
        form.addCommand(OKPhotoFormCommand.getInstance().getCommand());
        form.addCommand(BackPreviewLoadedFile.getInstance().getCommand());
        form.addComponent(container);
        try {
            form.removeCommandListener(this);
        } catch ( Exception ex ) {
            //nothing;
        }
        form.addCommandListener(this);
    }

    public void actionPerformed(ActionEvent arg0) {
        Command cmd = arg0.getCommand();
        if(cmd == BackPreviewLoadedFile.getInstance().getCommand()){
            BackPreviewLoadedFile .getInstance().execute(null);
        }
        else if(cmd == OKPhotoFormCommand.getInstance().getCommand()){
            OKPhotoFormCommand.getInstance().execute(null);
        }
        else {
            throw new IllegalStateException("Invalid Command on actionPerformed");
        }
    }
}

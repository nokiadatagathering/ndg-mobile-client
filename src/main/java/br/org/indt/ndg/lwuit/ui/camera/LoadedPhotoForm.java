/*
*  Nokia Data Gathering
*
*  Copyright (C) 2011 Nokia Corporation
*
*  This program is free software; you can redistribute it and/or
*  modify it under the terms of the GNU Lesser General Public
*  License as published by the Free Software Foundation; either
*  version 2.1 of the License, or (at your option) any later version.
*
*  This program is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
*  Lesser General Public License for more details.
*
*  You should have received a copy of the GNU Lesser General Public License
*  along with this program.  If not, see <http://www.gnu.org/licenses/
*/

package br.org.indt.ndg.lwuit.ui.camera;

import br.org.indt.ndg.lwuit.control.BackPreviewLoadedFile;
import br.org.indt.ndg.lwuit.control.OKPhotoFormCommand;
import br.org.indt.ndg.lwuit.ui.Screen;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.error.OutOfMemoryErrorExtended;
import br.org.indt.ndg.mobile.logging.Logger;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;


public class LoadedPhotoForm extends Screen implements ActionListener {

    protected void loadData() {
    }

    protected void customize() {
        setTitle( "", "" );
        form.removeAll();
        form.removeAllCommands();

        form.addCommand(OKPhotoFormCommand.getInstance().getCommand());
        form.addCommand(BackPreviewLoadedFile.getInstance().getCommand());

        Image image = null;
        Component preview = null;
        try {
            try {
                byte[] imageData = AppMIDlet.getInstance().getCurrentCameraManager().getCurrentImageData();
                image = Image.createImage(imageData, 0, imageData.length);
                preview = new Label( image.scaled(
                        form.getPreferredSize().getWidth(),
                        form.getPreferredSize().getHeight() - form.getSoftButton(0).getParent().getPreferredH()) );
                ((Label)preview).setAlignment(Label.CENTER);
            } catch ( OutOfMemoryError ex ) {
                throw new OutOfMemoryErrorExtended("Failed to load image: " + ex.getMessage());
            }
        } catch ( OutOfMemoryErrorExtended ex ) {
            Logger.getInstance().logException(ex.getMessage());
            preview = new TextArea(Resources.EFAILED_LOAD_IMAGE_LIMITED_DEVICE_RESOURCES);
            ((TextArea)preview).setAlignment(TextArea.CENTER);
        }
        preview.getStyle().setMargin( 0, 0, 0, 0 );
        preview.getStyle().setPadding( 0, 0, 0, 0 );
        preview.setIsScrollVisible(false);

        form.setScrollable(false);
        form.addComponent(preview);
        try {
            form.removeCommandListener(this);
        } catch (Exception ex) {
            //nothing;
        }
        form.addCommandListener(this);
    }

    public void actionPerformed(ActionEvent arg0) {
        Command cmd = arg0.getCommand();
        if(cmd == BackPreviewLoadedFile.getInstance().getCommand()){
            AppMIDlet.getInstance().getCurrentCameraManager().deletePhoto();
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

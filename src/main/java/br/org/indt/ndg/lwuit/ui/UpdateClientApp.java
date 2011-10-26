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

package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.CancelUpdateClientAppCommand;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Component;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.plaf.UIManager;

public class UpdateClientApp extends Screen implements ActionListener {
    private TextArea item;
    private Image image;
    private Label l;

    private String strText1, strText2;

    protected void loadData() {
        strText1 = Resources.CHECK_FOR_UPDATE_TITLE;
        strText2 = Resources.CONNECTING;

        if (item == null) {
            item = new TextArea(3,20);
            item.setEditable(false);
            item.setFocusable(false);

            image = Screen.getRes().getImage("wait2");
            l = new Label(image);
            l.setAlignment(Component.CENTER);
        }
        item.setUnselectedStyle(UIManager.getInstance().getComponentStyle("Label"));
        item.getStyle().setFont(NDGStyleToolbox.fontSmall);
    }

    protected void customize() {
        form.removeAll();
        form.removeAllCommands();

        setTitle(Resources.NEWUI_NOKIA_DATA_GATHERING, strText1);
        item.setText(strText2);
        form.addComponent(item);
        form.addComponent(l);

        form.addCommand(CancelUpdateClientAppCommand.getInstance().getCommand());

        try{
            form.removeCommandListener(this);
        } catch (NullPointerException npe ) {
            //during first initialisation remove throws exception.
            //this ensure that we have registered listener once
        }
        form.addCommandListener(this);
    }

    public void actionPerformed(ActionEvent evt) {
        Object cmd = evt.getSource();
        if (cmd == CancelUpdateClientAppCommand.getInstance().getCommand()) {
            CancelUpdateClientAppCommand.getInstance().execute(null);
        }
    }
}

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

import br.org.indt.ndg.lwuit.control.CancelTestConnectionCommand;
import br.org.indt.ndg.lwuit.control.HideTestConnectionCommand;
import br.org.indt.ndg.lwuit.control.OkTestConnectionCommand;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.submit.TestConnection;
import com.sun.lwuit.Component;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.plaf.UIManager;

public class TestConnectionNewUI extends Screen implements ActionListener {

    private TextArea item;
    private Image image;
    private Label l;

    protected void loadData() {
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

        setTitle(Resources.NEWUI_NOKIA_DATA_GATHERING, TestConnection.getInstance().getFormContentText1());
        item.setText(TestConnection.getInstance().getFormContentText2());
        //item.setRows(item.getLines()-1);
        form.addComponent(item);

        if (TestConnection.getInstance().getLastFormCreated() == 1) {
            form.addComponent(l);

            form.addCommand(CancelTestConnectionCommand.getInstance().getCommand());
            form.addCommand(HideTestConnectionCommand.getInstance().getCommand());
        }
        else if (TestConnection.getInstance().getLastFormCreated() == 2){
            form.addCommand(OkTestConnectionCommand.getInstance().getCommand());
        }

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
        if (cmd == CancelTestConnectionCommand.getInstance().getCommand()) {
            CancelTestConnectionCommand.getInstance().execute(null);
        } else if (cmd == HideTestConnectionCommand.getInstance().getCommand()) {
            HideTestConnectionCommand.getInstance().execute(null);
        } else if (cmd == OkTestConnectionCommand.getInstance().getCommand()) {
            OkTestConnectionCommand.getInstance().execute(null);
        }
    }
}

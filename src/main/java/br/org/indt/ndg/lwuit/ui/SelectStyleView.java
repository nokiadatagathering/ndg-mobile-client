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

import br.org.indt.ndg.lwuit.control.BackToSettingsFormCommand;
import br.org.indt.ndg.lwuit.control.UISettingsColorCommand;
import br.org.indt.ndg.lwuit.control.UISettingsSizeCommand;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import br.org.indt.ndg.lwuit.extended.ChoiceGroup;
import br.org.indt.ndg.lwuit.extended.ChoiceGroupListener;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import br.org.indt.ndg.lwuit.ui.style.StyleConst;
import br.org.indt.ndg.mobile.AppMIDlet;
import com.sun.lwuit.Label;

public class SelectStyleView extends Screen implements ActionListener, ChoiceGroupListener {
    private ChoiceGroup cg = null;

    protected void loadData() {
    }

    protected void customize() {
        setTitle(Resources.NEWUI_NOKIA_DATA_GATHERING, Resources.STYLES);
        form.removeAllCommands();

        try{
            form.removeCommandListener(this);
        } catch (NullPointerException npe ) {
            //during first initialisation remove throws exception.
            //this ensure that we have registered listener once
        }
        form.addCommandListener(this);
        form.removeAll();
        Label label = new Label(Resources.AVAILABLE_STYLES);
        label.getStyle().setFont( NDGStyleToolbox.fontSmall );
        label.setFocusable(false);
        form.addComponent(label);

        String[] choices = StyleConst.STYLENAMES;
        cg = new ChoiceGroup(choices, AppMIDlet.getInstance().getSettings().getStructure().getStyleId() );
        cg.setCgListener(this);

        form.addComponent(cg);

        cg.setItemFocused(AppMIDlet.getInstance().getSettings().getStructure().getStyleId());
        form.addCommand(BackToSettingsFormCommand.getInstance().getCommand());
        if( AppMIDlet.getInstance().getSettings().getStructure().getStyleId() == StyleConst.CUSTOM ) {
            form.addCommand(UISettingsColorCommand.getInstance().getCommand());
            form.addCommand(UISettingsSizeCommand.getInstance().getCommand());
        }
    }

    public void actionPerformed(ActionEvent evt) {
        Object cmd = evt.getSource();
        if (cmd == BackToSettingsFormCommand.getInstance().getCommand()) {
            BackToSettingsFormCommand.getInstance().execute(null);
        } else if ( cmd == UISettingsColorCommand.getInstance().getCommand() ) {
            UISettingsColorCommand.getInstance().execute(null);
        } else if ( cmd == UISettingsSizeCommand.getInstance().getCommand() ) {
            UISettingsSizeCommand.getInstance().execute( null );
        }
    }

    // Listener from ChoiceGroup
    public void itemChoosed(int i) {
        cg.setSelectedIndex(i);
        form.removeAllCommands();
        if( i == StyleConst.CUSTOM ) {
            form.addCommand(BackToSettingsFormCommand.getInstance().getCommand());
            form.addCommand(UISettingsColorCommand.getInstance().getCommand());
            form.addCommand(UISettingsSizeCommand.getInstance().getCommand());
        } else {
            form.addCommand(BackToSettingsFormCommand.getInstance().getCommand());
        }
        AppMIDlet.getInstance().getSettings().getStructure().setStyleId(i);
        AppMIDlet.getInstance().getSettings().writeSettings();
        AppMIDlet.getInstance().initLWUIT();
        form.show();
    }
}


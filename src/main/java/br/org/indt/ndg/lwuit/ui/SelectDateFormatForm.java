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
import br.org.indt.ndg.lwuit.extended.ChoiceGroup;
import br.org.indt.ndg.lwuit.extended.ChoiceGroupListener;
import br.org.indt.ndg.lwuit.extended.DateField;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.NdgConsts;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Label;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;

/**
 *
 * @author damian.janicki
 */
public class SelectDateFormatForm extends Screen implements ActionListener, ChoiceGroupListener{
    private ChoiceGroup cg = null;

    protected void loadData() {
    }

    protected void customize() {
        setTitle(Resources.NEWUI_NOKIA_DATA_GATHERING, Resources.DATE_FORMAT);
        form.removeAllCommands();

        try{
            form.removeCommandListener(this);
        } catch (NullPointerException npe ) {
            //during first initialisation remove throws exception.
            //this ensure that we have registered listener once
        }
        form.addCommandListener(this);
        form.removeAll();

        Label label = new Label(Resources.AVAILABLE_DATE_FORMAT);
        label.getStyle().setFont( NDGStyleToolbox.fontSmall );
        label.setFocusable(false);
        form.addComponent(label);

        form.addCommand(BackToSettingsFormCommand.getInstance().getCommand());

        String[] choices = new String[]{NdgConsts.DDMMYYYY, NdgConsts.MMDDYYYY};
        int id = AppMIDlet.getInstance().getSettings().getStructure().getDateFormatId();

        int selected = 0;
        if(id == DateField.MMDDYYYY){
            selected = 1;
        }

        cg = new ChoiceGroup(choices, selected);
        cg.setCgListener(this);

        form.addComponent(cg);
    }

    public void actionPerformed(ActionEvent ae) {
        Object cmd = ae.getSource();
        if (cmd == BackToSettingsFormCommand.getInstance().getCommand()) {
            BackToSettingsFormCommand.getInstance().execute(null);
        }
    }

    public void itemChoosed(int i) {
        cg.setSelectedIndex(i);
        int dateFormat;

        if(i == 1 ){
            dateFormat = DateField.MMDDYYYY;
        }else{
            dateFormat = DateField.DDMMYYYY;
        }

        AppMIDlet.getInstance().getSettings().getStructure().setDateFormatId(dateFormat);
        AppMIDlet.getInstance().getSettings().writeSettings();
    }
}

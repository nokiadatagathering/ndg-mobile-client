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

import br.org.indt.ndg.lwuit.control.SurveysControl;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Component;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.layouts.BoxLayout;

/**
 *
 * @author mturiel, kgomes
 */
public class WaitingForm extends Screen {
    private String status;
    private String title1;
    private String title2;

    public WaitingForm()
    {
    }

    public WaitingForm( String _title )
    {
        this.title1 = _title;
    }

    protected void loadData() {
        status = Resources.PROCESSING;
        title1 = SurveysControl.getInstance().getSurveyTitle();
        title2 = Resources.NEWUI_NOKIA_DATA_GATHERING;
    }

    protected void customize() {
        form.removeAll();
        setTitle(title1, title2);
        form.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        form.addComponent(new Label(" "));
        form.addComponent(new Label(" "));
        Image image = Screen.getRes().getImage("wait");
        Label l = new Label(image);
        l.setAlignment(Component.CENTER);
        l.setText("  "+status);
        form.addComponent(l);
    }

}

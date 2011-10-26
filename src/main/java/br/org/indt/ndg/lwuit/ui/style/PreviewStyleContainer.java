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

package br.org.indt.ndg.lwuit.ui.style;

import com.sun.lwuit.plaf.Border;
import com.sun.lwuit.plaf.Style;


public class PreviewStyleContainer {
    final private int OPAQUE = 0xff;
    final private int BLACK = 0x00;

    public Style listUnselectedStyle;
    public Style listSelectedStyle;
    public Style menuUnselectedStyle;
    public Style menuSelectedStyle;
    public Style dialogTitleUnselectedStyle;
    public Style dialogTitleSelectedStyle;

    public PreviewStyleContainer() {
        listUnselectedStyle = new Style();
        listUnselectedStyle.setFont(NDGStyleToolbox.fontSmall);
        listSelectedStyle = new Style();
        listSelectedStyle.setFont(NDGStyleToolbox.fontSmall);
        menuUnselectedStyle = new Style();
        menuUnselectedStyle.setFont(NDGStyleToolbox.fontSmall);
        menuSelectedStyle = new Style();
        menuSelectedStyle.setFont(NDGStyleToolbox.fontSmall);
        dialogTitleUnselectedStyle = new Style();
        dialogTitleUnselectedStyle.setFont(NDGStyleToolbox.fontSmall);
        dialogTitleSelectedStyle = new Style();
        dialogTitleUnselectedStyle.setFont(NDGStyleToolbox.fontSmall);
    }

    public void init() {
        listUnselectedStyle = new Style( NDGStyleToolbox.getInstance().listStyle.getBaseStyle() );
        listUnselectedStyle.setBgTransparency(OPAQUE);
        listUnselectedStyle.setBorder( Border.createRoundBorder(1, 1, BLACK) );
        listUnselectedStyle.setFont(NDGStyleToolbox.fontSmall);

        listSelectedStyle = new Style( NDGStyleToolbox.getInstance().listStyle.getSelectedStyle() );
        listSelectedStyle.setBgTransparency(OPAQUE);
        listSelectedStyle.setBorder( Border.createRoundBorder(1, 1, BLACK) );
        listSelectedStyle.setFont(NDGStyleToolbox.fontSmall);

        menuUnselectedStyle = new Style( NDGStyleToolbox.getInstance().menuStyle.getBaseStyle() );
        menuUnselectedStyle.setBgTransparency(OPAQUE);
        menuUnselectedStyle.setBorder( Border.createRoundBorder(1, 1, BLACK) );
        menuUnselectedStyle.setFont(NDGStyleToolbox.fontSmall);

        menuSelectedStyle = new Style( NDGStyleToolbox.getInstance().menuStyle.getSelectedStyle() );
        menuSelectedStyle.setBgTransparency(OPAQUE);
        menuSelectedStyle.setBorder( Border.createRoundBorder(1, 1, BLACK) );
        menuSelectedStyle.setFont(NDGStyleToolbox.fontSmall);

        dialogTitleUnselectedStyle = new Style( NDGStyleToolbox.getInstance().dialogTitleStyle.getBaseStyle() );
        dialogTitleUnselectedStyle.setBgTransparency(OPAQUE);
        dialogTitleUnselectedStyle.setBorder( Border.createRoundBorder(1, 1, BLACK) );
        dialogTitleUnselectedStyle.setFont(NDGStyleToolbox.fontSmall);

        dialogTitleSelectedStyle = new Style( NDGStyleToolbox.getInstance().dialogTitleStyle.getSelectedStyle() );
        dialogTitleSelectedStyle.setBgTransparency(OPAQUE);
        dialogTitleSelectedStyle.setBorder( Border.createRoundBorder(1, 1, BLACK) );
        dialogTitleSelectedStyle.setFont(NDGStyleToolbox.fontSmall);

        loadCurrentChanges();
    }

    private void loadCurrentChanges() {
        loadList();
        loadMenu();
        loadDialogTitle();
    }

    public void applySettings() {
        applyList();
        applyMenu();
        applyDialogTitle();
    }

    private void loadList() {
        listSelectedStyle.setBgColor( NDGStyleToolbox.getInstance().listStyle.bgSelectedStartColor );
        listUnselectedStyle.setBgColor( NDGStyleToolbox.getInstance().listStyle.bgUnselectedColor );
        listSelectedStyle.setFgColor( NDGStyleToolbox.getInstance().listStyle.selectedFontColor );
        listUnselectedStyle.setFgColor( NDGStyleToolbox.getInstance().listStyle.unselectedFontColor );
    }

    private void loadMenu() {
        menuSelectedStyle.setBgColor( NDGStyleToolbox.getInstance().menuStyle.bgSelectedStartColor );
        menuUnselectedStyle.setBgColor( NDGStyleToolbox.getInstance().menuStyle.bgUnselectedColor );
        menuSelectedStyle.setFgColor( NDGStyleToolbox.getInstance().menuStyle.selectedFontColor );
        menuUnselectedStyle.setFgColor( NDGStyleToolbox.getInstance().menuStyle.unselectedFontColor );
    }

    private void loadDialogTitle() {
        dialogTitleSelectedStyle.setBgColor( NDGStyleToolbox.getInstance().dialogTitleStyle.bgSelectedStartColor );
        dialogTitleUnselectedStyle.setBgColor( NDGStyleToolbox.getInstance().dialogTitleStyle.bgUnselectedColor );
        dialogTitleSelectedStyle.setFgColor( NDGStyleToolbox.getInstance().dialogTitleStyle.selectedFontColor );
        dialogTitleUnselectedStyle.setFgColor( NDGStyleToolbox.getInstance().dialogTitleStyle.unselectedFontColor );
    }

    private void applyList() {
        NDGStyleToolbox.getInstance().listStyle.bgSelectedEndColor = listSelectedStyle.getBgColor();
        NDGStyleToolbox.getInstance().listStyle.bgSelectedStartColor = listSelectedStyle.getBgColor();
        NDGStyleToolbox.getInstance().listStyle.bgUnselectedColor = listUnselectedStyle.getBgColor();
        NDGStyleToolbox.getInstance().listStyle.selectedFontColor = listSelectedStyle.getFgColor();
        NDGStyleToolbox.getInstance().listStyle.unselectedFontColor = listUnselectedStyle.getFgColor();
    }

    private void applyMenu() {
        NDGStyleToolbox.getInstance().menuStyle.bgSelectedEndColor = menuSelectedStyle.getBgColor();
        NDGStyleToolbox.getInstance().menuStyle.bgSelectedStartColor = menuSelectedStyle.getBgColor();
        NDGStyleToolbox.getInstance().menuStyle.bgUnselectedColor = menuUnselectedStyle.getBgColor();
        NDGStyleToolbox.getInstance().menuStyle.selectedFontColor = menuSelectedStyle.getFgColor();
        NDGStyleToolbox.getInstance().menuStyle.unselectedFontColor = menuUnselectedStyle.getFgColor();
    }

    private void applyDialogTitle() {
        NDGStyleToolbox.getInstance().dialogTitleStyle.bgSelectedEndColor = dialogTitleSelectedStyle.getBgColor();
        NDGStyleToolbox.getInstance().dialogTitleStyle.bgSelectedStartColor = dialogTitleSelectedStyle.getBgColor();
        NDGStyleToolbox.getInstance().dialogTitleStyle.bgUnselectedColor = dialogTitleUnselectedStyle.getBgColor();
        NDGStyleToolbox.getInstance().dialogTitleStyle.selectedFontColor = dialogTitleSelectedStyle.getFgColor();
        NDGStyleToolbox.getInstance().dialogTitleStyle.unselectedFontColor = dialogTitleUnselectedStyle.getFgColor();
    }
}

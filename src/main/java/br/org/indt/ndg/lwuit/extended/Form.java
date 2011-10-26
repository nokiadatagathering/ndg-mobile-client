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

package br.org.indt.ndg.lwuit.extended;

import br.org.indt.ndg.lwuit.ui.MenuCellRenderer;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.List;
import com.sun.lwuit.events.StyleListener;
import com.sun.lwuit.plaf.Style;
import com.sun.lwuit.plaf.UIManager;

/**
 *
 * @author mluz
 */
public class Form extends com.sun.lwuit.Form implements StyleListener {
    final static int MIN_MARGIN = 30;
    final static int LEFT_MARGIN_OFFSET = 10;

    public Form() {
        super();
        Style style = getSoftButtonStyle();
        style.setFont( NDGStyleToolbox.getInstance().menuStyle.unselectedFont );
        setSoftButtonStyle( style );
    }

    protected Command showMenuDialog(Dialog menu) {
        menu.getDialogStyle().setBgColor(NDGStyleToolbox.getInstance().menuStyle.bgUnselectedColor);

        int marginW = calculateMarginW(menu);
        marginW = marginW < MIN_MARGIN ? MIN_MARGIN : marginW;

        int marginH = calculateMarginH( menu );
        marginH = marginH < 0 ? 0 :marginH;

        return menu.show( marginH,0, LEFT_MARGIN_OFFSET, marginW - LEFT_MARGIN_OFFSET, true );
    }

    public void show() {
        Style style = getSoftButtonStyle();
        style.setFont( NDGStyleToolbox.getInstance().menuStyle.unselectedFont );
        setSoftButtonStyle( style );
        super.show();
    }

    private int calculateMarginW( Dialog menuDialog ) {
         String longestDesc = "";
         for ( int i = 0; i< getCommandCount(); i++ ) {
             String description = getCommand(i).getCommandName();
             longestDesc = description.length() < longestDesc.length() ? longestDesc : description;
         }
         Style style = UIManager.getInstance().getComponentStyle("Command");
         int commandGap = 0;
         if ( style != null ) {
             commandGap = style.getMargin(Component.LEFT)
                        + style.getMargin(Component.RIGHT)
                        + style.getPadding(Component.LEFT)
                        + style.getPadding(Component.RIGHT);
         }
         //there is no wey to get cell renderer from form
         //creating temporary one to calculete margins and offsets
         MenuCellRenderer mcr = new MenuCellRenderer();

         return Display.getInstance().getDisplayWidth() - menuDialog.getStyle().getMargin(Component.LEFT)
                                                        - menuDialog.getStyle().getMargin(Component.RIGHT)
                                                        - menuDialog.getStyle().getPadding(Component.LEFT)
                                                        - menuDialog.getStyle().getPadding(Component.RIGHT)
                                                        - mcr.getStyle().getMargin(Component.LEFT)
                                                        - mcr.getStyle().getMargin(Component.RIGHT)
                                                        - mcr.getStyle().getPadding(Component.LEFT)
                                                        - mcr.getStyle().getPadding(Component.RIGHT)
                                                        - NDGStyleToolbox.getInstance().menuStyle.selectedFont.stringWidth( longestDesc )
                                                        - getSideGap()
                                                        - menuDialog.getSideGap()
                                                        - commandGap
                                                        - 10;
    }

    private int calculateMarginH( Dialog menuDialog ) {
         //there is no way to get cell renderer from form
         //creating temporary one to calculete margins and offsets
        MenuCellRenderer rendererItem = new MenuCellRenderer();
        int itemTopMargin = rendererItem.getStyle().getMargin(Component.TOP);
        int itemBottonMargin = rendererItem.getStyle().getMargin(Component.BOTTOM);
        int itemTopPadding = rendererItem.getStyle().getPadding(Component.TOP);
        int itemBottonPadding = rendererItem.getStyle().getPadding(Component.BOTTOM);
        int fontHigh = NDGStyleToolbox.getInstance().menuStyle.selectedFont.getHeight();//this font is used in cell

        List list = new List();
        Style style = list.getStyle();
        int listGap = 0;
        if ( style != null ) {
            listGap = style.getMargin(Component.LEFT)
                    + style.getMargin(Component.RIGHT)
                    + style.getPadding(Component.LEFT)
                    + style.getPadding(Component.RIGHT)
                    + getCommandCount() * list.getItemGap()
                    + 2 * list.getBorderGap();
        }
        return  Display.getInstance().getDisplayHeight() - menuDialog.getStyle().getMargin( Component.TOP )
                                                         - menuDialog.getStyle().getMargin( Component.BOTTOM )
                                                         - menuDialog.getStyle().getPadding( Component.BOTTOM )
                                                         - menuDialog.getStyle().getPadding( Component.TOP )
                                                         - Display.getInstance().getCurrent().getSoftButton(0).getPreferredH()
                                                         - getCommandCount() * ( fontHigh
                                                                                + itemTopMargin
                                                                                + itemBottonMargin
                                                                                + itemTopPadding
                                                                                + itemBottonPadding)
                                                         - listGap;
    }

    public void styleChanged(String propertyName, Style source) {
        super.styleChanged(propertyName, source);
        getSoftButtonStyle().setFont( NDGStyleToolbox.getInstance().menuStyle.unselectedFont );
        this.invalidate();
    }
}

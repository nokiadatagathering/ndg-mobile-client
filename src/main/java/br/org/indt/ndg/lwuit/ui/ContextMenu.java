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

import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.logging.Logger;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.List;
import com.sun.lwuit.animations.CommonTransitions;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.list.DefaultListModel;
import com.sun.lwuit.list.ListModel;
import com.sun.lwuit.plaf.Border;
import com.sun.lwuit.plaf.Style;

public abstract class ContextMenu {

    protected Dialog menuDialog = null;
    protected ListModel optionsModel = null;
    protected List optionsList = null;
    protected int indexList;
    protected int sizeList;
    private boolean mIgnoreFirstRelease = false; // fix for issue when menu is activated with long press

    protected int displayWidth, displayHeight;

    public ContextMenu(int index, int size){
        displayWidth = Display.getInstance().getDisplayWidth();
        displayHeight = Display.getInstance().getDisplayHeight();
        indexList = index;
        sizeList = size;

        menuDialog = new Dialog();
        menuDialog.setDialogStyle( NDGStyleToolbox.getInstance().menuStyle.getBaseStyle() );
        menuDialog.getDialogStyle().setBgColor( NDGStyleToolbox.getInstance().menuStyle.bgUnselectedColor );
        menuDialog.setAutoDispose(true);
        menuDialog.setScrollable(true);
        menuDialog.setLayout(new BorderLayout());

        optionsList = new List() {

            public void pointerPressed(int x, int y) {
                super.pointerPressed(x, y);
                mIgnoreFirstRelease = false;
            }

            public void pointerReleased(int x, int y) {
                if ( mIgnoreFirstRelease )
                    mIgnoreFirstRelease = false;
                else
                    super.pointerReleased(x, y);
            }
        };
        optionsList.getStyle().setBorder(Border.createEmpty());
        optionsList.setFixedSelection(List.FIXED_NONE_CYCLIC);

        setTransitions();
        buildMenu();

        Style style = menuDialog.getSoftButtonStyle();
        style.setFont( NDGStyleToolbox.getInstance().menuStyle.unselectedFont );
        menuDialog.setSoftButtonStyle(style);
    }

    public void show( int leftMargin, int topMargin ) {
        mIgnoreFirstRelease = true;
        leftMargin += 5; // offset to avoid automatic selection of item that shows under the pointer
        int rightMargin = getHorizontalMargin() - leftMargin;
        int bottomMargin = getVerticalMargin() - topMargin;
        if ( bottomMargin < 0 ) { // check if not out off screen
            bottomMargin = 0;
            topMargin = getVerticalMargin();
        }
        if ( rightMargin < 0 ) { // check if not out off screen
            rightMargin = 0;
            leftMargin = getHorizontalMargin();
        }
        menuDialog.show( topMargin, bottomMargin,leftMargin, rightMargin, true );
    }

    public Dialog getMenuDialog(){
        return this.menuDialog;
    }

    public void setIndexList(int index) {
        this.indexList = index;
    }

    public void setSizeList(int size) {
        this.sizeList = size;
    }

    protected void buildMenu() {
        buildOptions();
        buildCommands();
    }

    protected abstract void buildOptions();

    protected abstract void buildCommands();

    protected abstract void action(Command command);

    protected final void buildCommands(String[] commands) {
        for ( int i=0; i<commands.length; i++ ) {
            Command cmd = new Command(commands[i]);
            menuDialog.addCommand(cmd);
        }

        menuDialog.addCommandListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Command cmd = evt.getCommand();
                menuDialog.dispose();
                if(cmd.getCommandName().equals(Resources.NEWUI_CANCEL)){
                    // do nothing
                } else {
                    action((Command)optionsList.getSelectedItem());
                }
            }
        });

        menuDialog.addGameKeyListener(Display.GAME_LEFT, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                menuDialog.dispose();
            }
        });
    }

    protected final void buildOptions(Command[] options) {
        optionsModel = new DefaultListModel(options);
        optionsList.setModel(optionsModel);
        optionsList.setListCellRenderer(new MenuCellRenderer());
        optionsList.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if ( evt.getSource() instanceof List ) {
                    menuDialog.dispose();
                    List list = (List)evt.getSource();
                    Command cmd = (Command)list.getSelectedItem();
                    action(cmd);
                }
            }

        });
        menuDialog.addComponent(BorderLayout.CENTER, optionsList);
    }

    protected void setTransitions(){
        CommonTransitions ct = CommonTransitions.createSlide(CommonTransitions.SLIDE_HORIZONTAL, true, 600, false);
        CommonTransitions ct2 = CommonTransitions.createSlide(CommonTransitions.SLIDE_HORIZONTAL, false, 600, false);
        menuDialog.setTransitionInAnimator(ct);
        menuDialog.setTransitionOutAnimator(ct2);
    }

    /**
     * Calculates available hotizontal space that should be distributed for top and bottom margins
     * @return  hotizontal margin
     */
    protected int getHorizontalMargin() {
         return displayWidth - getMenuWidth();
    }

    /**
     * Calculates available vertical space that should be distributed for top and bottom margins
     * @return  vertical margin
     */
    protected int getVerticalMargin() {
        return displayHeight - getMenuHeight() - Display.getInstance().getCurrent().getSoftButton(0).getPreferredH();
    }

    protected int getMenuHeight() {
        int height = menuDialog.getStyle().getMargin( Component.BOTTOM )
                + menuDialog.getStyle().getMargin( Component.TOP )
                + menuDialog.getStyle().getPadding( Component.BOTTOM )
                + menuDialog.getStyle().getPadding( Component.TOP )
                + menuDialog.getBottomGap()
                + optionsList.getStyle().getMargin( Component.BOTTOM )
                + optionsList.getStyle().getMargin( Component.TOP )
                + optionsList.getStyle().getPadding( Component.BOTTOM )
                + optionsList.getStyle().getPadding( Component.TOP )
                + optionsList.size() * (getSingleOptionHeight() + optionsList.getItemGap())
                + 2 * optionsList.getBorderGap()
                + optionsList.getBottomGap()

                + 15; // magic number, could not find all height influencing factors
        return height;
    }

    protected int getSingleOptionHeight() {
        int fontHigh = NDGStyleToolbox.getInstance().menuStyle.selectedFont.getHeight(); //this font is used in cell
        MenuCellRenderer rendererItem = (MenuCellRenderer)optionsList.getRenderer();
        int height = fontHigh
                     + rendererItem.getStyle().getPadding(Component.BOTTOM)
                     + rendererItem.getStyle().getPadding(Component.TOP)
                     + rendererItem.getStyle().getMargin(Component.BOTTOM)
                     + rendererItem.getStyle().getMargin(Component.TOP)
                     + 2 * rendererItem.getStyle().getBorder().getThickness();
        return height;
    }

    protected int getMenuWidth() {
        String longestDesc = "";
        for ( int i = 0; i< optionsList.size(); i++ ) {
            String description = ((Command)optionsList.getModel().getItemAt(i)).getCommandName();
            longestDesc = description.length() < longestDesc.length() ? longestDesc : description;
        }
        MenuCellRenderer rendererItem = (MenuCellRenderer)optionsList.getRenderer();
        int selectedWidth = NDGStyleToolbox.getInstance().menuStyle.selectedFont.stringWidth( longestDesc );
        int unselectedWidth = NDGStyleToolbox.getInstance().menuStyle.unselectedFont.stringWidth( longestDesc );
        return optionsList.getStyle().getMargin( Component.LEFT )
                               + optionsList.getStyle().getMargin( Component.RIGHT )
                               + optionsList.getStyle().getPadding( Component.LEFT )
                               + optionsList.getStyle().getPadding( Component.RIGHT )
                               + menuDialog.getStyle().getMargin( Component.LEFT )
                               + menuDialog.getStyle().getMargin( Component.RIGHT )
                               + menuDialog.getStyle().getPadding( Component.LEFT )
                               + menuDialog.getStyle().getPadding( Component.RIGHT )
                               + selectedWidth < unselectedWidth ? unselectedWidth : selectedWidth
                               + rendererItem.getStyle().getMargin( Component.LEFT )
                               + rendererItem.getStyle().getMargin( Component.RIGHT )
                               + rendererItem.getStyle().getPadding( Component.LEFT )
                               + rendererItem.getStyle().getPadding( Component.RIGHT )
                               + 2 * optionsList.getBorderGap()
                               + optionsList.getSideGap()
                               + 5; // magic number, could not find all height influencing factors
    }
}

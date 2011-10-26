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

import br.org.indt.ndg.lwuit.control.BackToFontSizeFormCommand;
import br.org.indt.ndg.lwuit.model.RadioButtonGroupItem;
import br.org.indt.ndg.lwuit.model.RadioButtonGroupListModel;
import br.org.indt.ndg.lwuit.ui.renderers.RadioButtonListCellRenderer;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Container;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.plaf.Border;
import com.sun.lwuit.plaf.UIManager;
import java.util.Vector;

/**
 *
 * @author damian.janicki
 */
public class UISettingsFontSizeCustom extends Screen implements ActionListener{

    private List fontList = null;

    protected void loadData() {
    }

    protected void customize() {
        setTitle(Resources.NEWUI_NOKIA_DATA_GATHERING, Resources.AVAILABLE_FONT_SIZE);
        form.removeAll();
        form.removeAllCommands();

        try{
            form.removeCommandListener(this);
        } catch (NullPointerException npe ) {
            //during first initialisation remove throws exception.
            //this ensure that we have registered listener once
        }
        form.addCommandListener(this);

        form.addCommand(BackToFontSizeFormCommand.getInstance().getCommand());

        addFontChoiceList();
    }

    private void addFontChoiceList(){
        Vector tempList = NDGStyleToolbox.getAvailableFontSizes();
        String currentFont = NDGStyleToolbox.getInstance().getFontSizeNameSetting();
        Vector list = prepereRadioItemList(tempList, currentFont);

        fontList = new List(new RadioButtonGroupListModel(list));
        fontList.setItemGap(0);
        fontList.addActionListener(this);
        fontList.setListCellRenderer(new RadioButtonListCellRenderer());
        fontList.setFixedSelection(List.FIXED_NONE);
        form.addComponent(fontList);
        fontList.repaint();
    }

    public void actionPerformed(ActionEvent a) {
        Object obj = a.getSource();
        if(obj == BackToFontSizeFormCommand.getInstance().getCommand()){
            BackToFontSizeFormCommand.getInstance().execute(null);
        }else if(obj == fontList){
            ((RadioButtonGroupListModel)fontList.getModel()).setCheckedIndex(fontList.getSelectedIndex());
            RadioButtonGroupItem item = ((RadioButtonGroupListModel)fontList.getModel()).getCheckedItem();
            if(item != null){
                NDGStyleToolbox.getInstance().applayFontSetting(item.getValue());
                form.show();
            }
        }
    }

    private Vector prepereRadioItemList(Vector fontSizeStrList, String selectedFont){
        Vector radioItemList = new Vector();

        String fontName = null;
        String dispName = null;
        RadioButtonGroupItem item = null;
        for(int idx = 0; idx < fontSizeStrList.size(); idx++){
            fontName = (String)fontSizeStrList.elementAt(idx);
            dispName = preperDisplayName(fontName);
            if(dispName != null){
                item = new RadioButtonGroupItem(dispName, fontName);
                if(fontName.equals(selectedFont)){
                    item.setChecked(true);
                }
                radioItemList.addElement(item);
            }
        }
        return radioItemList;
    }

    private String preperDisplayName(String value){
        if(value.startsWith(NDGStyleToolbox.FONTSANSBOLD)){
            return value.substring(NDGStyleToolbox.FONTSANSBOLD.length()) + " (Bold)"; //TODO localize
        }else if(value.startsWith(NDGStyleToolbox.FONTSANS)){
            return value.substring(NDGStyleToolbox.FONTSANS.length());
        }else
            return null;
    }
}

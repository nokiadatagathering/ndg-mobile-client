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

package br.org.indt.ndg.lwuit.model;

import com.sun.lwuit.list.DefaultListModel;
import java.util.Vector;

/**
 *
 * @author damian.janicki
 */
public class RadioButtonGroupListModel extends DefaultListModel{


    private int checkedIndex = -1;
    public RadioButtonGroupListModel(Object[] items) {
        super(items);
    }

    public RadioButtonGroupListModel(Vector items) {
        super(items);
    }

    public void setCheckedIndex(int selectedIndex){
        checkedIndex = selectedIndex;
        if ( getSize() > 0 && selectedIndex < getSize() ){
            for(int idx = 0; idx < getSize(); idx++){
                RadioButtonGroupItem item = (RadioButtonGroupItem) getItemAt(idx);
                if(idx == selectedIndex){
                    item.setChecked(true);
                }else{
                    item.setChecked(false);
                }
            }
        }
    }

    public int getCheckedIndex(){
        return checkedIndex;
    }

    public RadioButtonGroupItem getCheckedItem(){
        if(checkedIndex >= 0){
            return (RadioButtonGroupItem) getItemAt(checkedIndex);
        }else{
            return null;
        }
    }
}

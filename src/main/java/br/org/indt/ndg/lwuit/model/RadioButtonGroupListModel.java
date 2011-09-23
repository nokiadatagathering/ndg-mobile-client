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

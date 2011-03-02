package br.org.indt.ndg.lwuit.model;

import br.org.indt.ndg.mobile.logging.Logger;
import com.sun.lwuit.list.DefaultListModel;
import java.util.Vector;

/**
 *
 * @author tomasz.baniak
 */
public class CheckableListModel extends DefaultListModel {

    private boolean m_firstItemUncheckable = false;

    public static final boolean FIRST_ITEM_UNCHECKABLE = true;
    public static final boolean ALL_ITEMS_CHECKABLE = false;

    public CheckableListModel(Object[] items, boolean firstItemUncheckable) {
        super(items);
        m_firstItemUncheckable = firstItemUncheckable;
    }

    /**
     *
     * @return an array with the status of each list entry
     */
    public boolean[] getSelectedFlags() {
        int offset = 0;
        if ( m_firstItemUncheckable )
            offset = 1;
        int arraySize = (getSize()-offset > 0 ? getSize() - offset : 0);
        boolean[] selections = new boolean[arraySize];
        for (int index = 0; index < getSize()-offset; index++) {
            CheckableItem item = (CheckableItem) this.getItemAt(index+offset);
            selections[index] = item.isChecked();
            Logger.getInstance().emul("getSelectedFlags: ", "item " + (index+offset) + " is " + (item.isChecked()?"":"not ") + "checked");
        }
        Logger.getInstance().emul("getSelectedFlags", "");
        return selections;
    }

    public void markAll() {
        for(int i = 0; i < getSize(); i++){
            CheckableItem item = (CheckableItem) this.getItemAt(i);
            item.setChecked(true);
        }
    }

    public void unmarkAll(){
        for(int i = 0; i < getSize(); i++){
            CheckableItem item = (CheckableItem) this.getItemAt(i);
            item.setChecked(false);
        }
    }

    public void updateCheckbox(int index) {
        if ( getSize() > 0 ){
            CheckableItem item = (CheckableItem) this.getItemAt(index);
            item.setChecked(!item.isChecked());
        }
    }

    public int getQtSelecteds() {
        int selectionCount = 0;
        for (int index = 0; index < getSize()-1; index++) {
            CheckableItem item = (CheckableItem) this.getItemAt(index);
            if (item.isChecked())
                selectionCount++;
        }
        return selectionCount;
    }

    public void setChecked(int index){
        if ( index < getSize() ){
            CheckableItem item = (CheckableItem) this.getItemAt(index);
            item.setChecked(true);
        }
    }
}

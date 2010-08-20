/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.model.DisplayableModel;
import com.sun.lwuit.CheckBox;
import com.sun.lwuit.Component;
import com.sun.lwuit.List;
import com.sun.lwuit.layouts.BorderLayout;
import java.util.Vector;

/**
 *
 * @author Alexandre Martini
 */
public class CheckableListCellRenderer extends DefaultNDGListCellRenderer{

    protected CheckBox checkbox;
    protected Vector checkboxes = new Vector();
    protected DisplayableModel disp;
    protected int realSize;

    public CheckableListCellRenderer(int realSize) {
        super();
        this.realSize = realSize;
    }

    public CheckableListCellRenderer() {
        super();
        this.realSize = 0;
    }

    public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
        disp = (DisplayableModel) value;
        checkbox = getCheckBox(disp, index);
        checkbox.setStyle(styleLabel);
        checkbox.setVisible(true);
        
        if (isSelected) {
            setFocus(true);
            checkbox.setFocus(true);
            styleLabel.setFont(Screen.getRes().getFont("NokiaSansWideBold15"));
            styleContainer.setBgPainter(focusBGPainter);
        } else {
            styleLabel.setFont(Screen.getRes().getFont("NokiaSansWide15"));
            setFocus(false);
            checkbox.setFocus(false);
            styleContainer.setBgPainter(bgPainter);
        }

        addComponent(BorderLayout.CENTER, checkbox);

        return this;
    }

    public Component getListFocusComponent(List list) {
        if (checkbox != null) {
            checkbox.setText(" ");
            checkbox.setFocus(true);
            checkbox.setVisible(false);
        }
        styleContainer.setBgPainter(focusBGPainter);
        this.setFocus(true);

        return this;
    }

    /**
     *
     * @return an array with the status of each list entry
     */
    public boolean[] getSelectedFlags() {
        if (checkboxes.size() < realSize) {
            for (int i = 0; i < realSize; i++) {
                getCheckBox(disp, i);
            }
        }
        int size = checkboxes.size();
        boolean[] selected = new boolean[size];
        for(int i = 0; i < size; i++){
            CheckBox c = (CheckBox) checkboxes.elementAt(i);
            if(c.isSelected())
                selected[i] = true;
            else
                selected[i] = false;
        }
        return selected;

    }

    public void markAll() {
        if (checkboxes.size() < realSize) {
            for (int i = 0; i < realSize; i++) {
                getCheckBox(disp, i);
            }
        }
        int size = checkboxes.size();
        for(int i = 0; i < size; i++){
            CheckBox c = (CheckBox) checkboxes.elementAt(i);
            c.setSelected(true);
        }
    }

    public void unmarkAll(){
        int size = checkboxes.size();
        for(int i = 0; i < size; i++){
            CheckBox c = (CheckBox) checkboxes.elementAt(i);
            c.setSelected(false);
        }
    }

    public void updateCheckbox(int index) {
        if(checkboxes.size()>0){
            CheckBox c = (CheckBox) checkboxes.elementAt(index);
            c.setSelected(!c.isSelected());
        }
    }

    CheckBox getCheckBox(DisplayableModel disp, int index){
        CheckBox c, cBox;
        try{
            c = (CheckBox) checkboxes.elementAt(index);
            cBox = new CheckBox(disp.getDisplayableName());
            cBox.setSelected(c.isSelected());
            return cBox;
        }
        catch(ArrayIndexOutOfBoundsException outExc){
            c = new CheckBox(disp.getDisplayableName());
            checkboxes.addElement(c);
            return c;
        }        
    }

}

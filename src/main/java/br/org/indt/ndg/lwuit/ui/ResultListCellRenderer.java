/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui;

import com.sun.lwuit.CheckBox;
import br.org.indt.ndg.lwuit.model.DisplayableModel;
import com.sun.lwuit.Component;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.layouts.BorderLayout;


/**
 *
 * @author mluz, kgomes
 */

class ResultListCellRenderer extends CheckableListCellRenderer {

    private int qtSelecteds = 0;

    public ResultListCellRenderer(int realsize){
        super(realsize);
    }

    public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {

        disp = (DisplayableModel) value;
        Component comp = null;
        if(index>0){
            checkbox = getCheckBox(disp, index);
            comp = checkbox;
        } else {
            Label label = new Label(disp.getDisplayableName());
            label.setAlignment(CENTER);
            comp = label;
        }
        comp.setStyle(styleLabel);
        comp.setVisible(true);

        if (isSelected) {
            setFocus(true);
            styleLabel.setFont(Screen.getRes().getFont("NokiaSansWideBold15"));
            styleContainer.setBgPainter(focusBGPainter);
            comp.setFocus(true);
            if(index>0){
                Image arrow = Screen.getRes().getImage("right_arrow");
                Label larrow = new Label(arrow);
                this.addComponent(BorderLayout.EAST,larrow);
            }
        } else {
            styleLabel.setFont(Screen.getRes().getFont("NokiaSansWide15"));
            setFocus(false);
            styleContainer.setBgPainter(bgPainter);
            comp.setFocus(false);
            this.addComponent(BorderLayout.EAST, new Label(" "));
        }

        addComponent(BorderLayout.CENTER, comp);

        return this;
    }

    public void updateCheckbox(int index) {
        CheckBox c = (CheckBox) checkboxes.elementAt(index);

        if(c.isSelected()){
            qtSelecteds+= -1;
        } else {
            qtSelecteds+= 1;
        }

        c.setSelected(!c.isSelected());
    }

    public void markAll() {
        super.markAll();
        qtSelecteds = this.realSize;
    }

    public void unmarkAll(){
        super.unmarkAll();
        qtSelecteds = 0;
    }

    public int getQtSelecteds(){
        return qtSelecteds;
    }

    public void setSelected(int index){
        if(index < checkboxes.size()){
            CheckBox c = (CheckBox) checkboxes.elementAt(index);

            if(!c.isSelected()){
                qtSelecteds+= 1;
                c.setSelected(true);
            }
        }
    }

    // Re-implemented to handle the first item as New Result. Specially for Result List
    public boolean[] getSelectedFlags() {
        if (checkboxes.size() < realSize) {
            for (int i = 0; i < realSize; i++) {
                getCheckBox(disp, i);
            }
        }
        int size = checkboxes.size();
        boolean[] selected = new boolean[size - 1];
        for(int i = 1; i < size; i++){
            CheckBox c = (CheckBox) checkboxes.elementAt(i);
            if(c.isSelected())
                selected[i-1] = true;
            else
                selected[i-1] = false;
        }
        return selected;

    }

}

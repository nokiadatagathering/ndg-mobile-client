/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.model.DisplayableModel;
import com.sun.lwuit.Component;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.layouts.BorderLayout;


/**
 *
 * @author mluz
 */
abstract class SimpleListCellRenderer extends DefaultNDGListCellRenderer {

    protected Label label;

    protected abstract String getText(Object value);


    public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
        DisplayableModel disp = (DisplayableModel) value;
        label = getLabel(disp);

        if (isSelected) {
            setFocus(true);
            label.setFocus(true);
            styleLabel.setFont(Screen.getRes().getFont("NokiaSansWideBold15"));
            styleContainer.setBgPainter(focusBGPainter);
        } else {
            styleLabel.setFont(Screen.getRes().getFont("NokiaSansWide15"));
            setFocus(false);
            label.setFocus(false);
            styleContainer.setBgPainter(bgPainter);
        }
        
        if(!contains(label))
            addComponent(BorderLayout.CENTER, label);
        

        return this;
    }

    public Component getListFocusComponent(List list) {
        label.setText(" ");
        label.setFocus(true);

        styleContainer.setBgPainter(focusBGPainter);
        this.setFocus(true);

        return this;
    }

    protected Label getLabel(DisplayableModel disp){
        Label l = new Label(disp.getDisplayableName());
        l.setStyle(styleLabel);        
        return l;
    }

}

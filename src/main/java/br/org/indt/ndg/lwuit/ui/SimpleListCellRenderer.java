package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.model.DisplayableItem;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
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
        DisplayableItem disp = (DisplayableItem) value;
        label = getLabel(disp);

        if (isSelected) {
            setFocus(true);
            label.setFocus(true);
            label.getStyle().setFont(NDGStyleToolbox.getInstance().listStyle.selectedFont);
            getStyle().setBgPainter(m_focusBGPainter);
        } else {
            label.getStyle().setFont(NDGStyleToolbox.getInstance().listStyle.unselectedFont);
            setFocus(false);
            label.setFocus(false);
            getStyle().setBgPainter(m_bgPainter);
        }
        
        if(!contains(label))
            addComponent(BorderLayout.CENTER, label);
        
        return this;
    }

    protected Label getLabel(DisplayableItem disp){
        Label l = new Label(disp.getDisplayableName());
        return l;
    }
}

package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import com.sun.lwuit.Component;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.layouts.BorderLayout;


public class UISettingsListCellRenderer extends SimpleListCellRenderer {

    protected String getText(Object value) {
        return (String)value;
    }


    public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
       if ( value instanceof String ) {
           label = new Label((String)value);
       } else {
           label = new Label( String.valueOf(value));
       }

        if (isSelected) {
            setFocus(true);
            label.getStyle().setFont(NDGStyleToolbox.getInstance().listStyle.selectedFont);
            getStyle().setBgPainter(m_focusBGPainter);
        } else {
            label.getStyle().setFont(NDGStyleToolbox.getInstance().listStyle.unselectedFont);
            setFocus(false);
            getStyle().setBgPainter(m_bgPainter);
        }

        if(!contains(label))
            addComponent(BorderLayout.CENTER, label);

        return this;
    }
}

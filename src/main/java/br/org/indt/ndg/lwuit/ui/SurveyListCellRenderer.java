package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.model.DisplayableItem;
import br.org.indt.ndg.lwuit.model.Survey;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.Component;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;

/**
 *
 * @author mluz
 */
class SurveyListCellRenderer extends SimpleListCellRenderer {

    protected String getText(Object value) {
        Survey survey = (Survey) value;
        return survey.getName();
    }

    public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
        DisplayableItem disp = (DisplayableItem) value;
        Label label = getLabel(disp);

        if(index==0)
            label.setAlignment(CENTER);

        if (isSelected) {
            setFocus(true);
            label.setFocus(true);
            label.getStyle().setFont( NDGStyleToolbox.getInstance().listStyle.selectedFont );
            label.getStyle().setFgColor( NDGStyleToolbox.getInstance().listStyle.selectedFontColor );
            getStyle().setBgPainter(m_focusBGPainter);
        } else {
            setFocus(false);
            label.setFocus(false);
            label.getStyle().setFont( NDGStyleToolbox.getInstance().listStyle.unselectedFont );
            label.getStyle().setFgColor( NDGStyleToolbox.getInstance().listStyle.unselectedFontColor );
            getStyle().setBgPainter(m_bgPainter);
        }
        if(!contains(label))
            addComponent(BorderLayout.CENTER, label);

        return this;
    }
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.model.DisplayableModel;
import br.org.indt.ndg.lwuit.model.Survey;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.Component;
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
        DisplayableModel disp = (DisplayableModel) value;
        label = getLabel(disp);

        if(index==0)
            label.setAlignment(CENTER);

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
}



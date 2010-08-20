/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.model.Question;
import com.sun.lwuit.Component;
import com.sun.lwuit.List;

/**
 *
 * @author Alexandre Martini
 */
public class QuestionListCellRenderer extends SimpleListCellRenderer{

    protected String getText(Object value) {
        return null;
    }

    public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
        super.getListCellRendererComponent(list, value, index, isSelected);
        Question question = (Question) value;
        if (question.isVisited())
            label.setIcon(isSelected ? Screen.getRes().getImage("check_highlighted") : Screen.getRes().getImage("check"));
        else
            label.setIcon(Screen.getRes().getImage("check_blank"));
        return this;
    }

    public Component getListFocusComponent(List list) {
        super.getListFocusComponent(list);
        label.setIcon(null);
        return this;
    }

}

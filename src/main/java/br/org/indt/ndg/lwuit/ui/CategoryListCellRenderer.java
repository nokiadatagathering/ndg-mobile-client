/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui;


import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.lwuit.model.Category;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.List;

import com.sun.lwuit.layouts.BorderLayout;

import com.sun.lwuit.Label;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.plaf.Style;
import com.sun.lwuit.plaf.UIManager;

/**
 *
 * @author mluz
 */
class CategoryListCellRenderer extends DefaultNDGListCellRenderer {

    private Label categoryLabel, questionsLabel, iconLabel;
    private Style styleSecondLabel = UIManager.getInstance().getComponentStyle("List");


    public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
        Category category = (Category)value;

        Container centerContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));

        categoryLabel = new Label(category.getName());
        categoryLabel.setStyle(styleLabel);
        categoryLabel.setIcon(null);

        questionsLabel = new Label(category.getTotalQuestions() + (category.getTotalQuestions() > 1 ? " " + Resources.QUESTIONS : " "+ Resources.QUESTION));
        questionsLabel.setStyle(styleSecondLabel);
        questionsLabel.setIcon(null);

        iconLabel = new Label(category.isFullFilled() ? Screen.getRes().getImage("check") : Screen.getRes().getImage("question"));
        iconLabel = new Label("");
        iconLabel.setStyle(styleLabel);

        Dimension dQuestionsLabel = questionsLabel.getPreferredSize();
        dQuestionsLabel.setHeight(dQuestionsLabel.getHeight()-8);
        questionsLabel.setPreferredSize(dQuestionsLabel);

        Dimension dcategoryLabel = categoryLabel.getPreferredSize();
        dcategoryLabel.setHeight(dcategoryLabel.getHeight()-8);
        categoryLabel.setPreferredSize(dcategoryLabel);

        if (isSelected) {
            styleLabel.setFont(Screen.getRes().getFont("NokiaSansWideBold15"));
            styleSecondLabel.setFont(Screen.getRes().getFont("NokiaSansWideBold13"));
            iconLabel.setIcon(category.isFullFilled() ? Screen.getRes().getImage("check_highlighted") : Screen.getRes().getImage("check_blank"));
            setFocus(true);
            categoryLabel.setFocus(true);
            questionsLabel.setFocus(true);
            iconLabel.setFocus(true);
            styleContainer.setBgPainter(focusBGPainter);
        } else {
            styleLabel.setFont(Screen.getRes().getFont("NokiaSansWide15"));
            styleSecondLabel.setFont(Screen.getRes().getFont("NokiaSansWide13"));
            iconLabel.setIcon(category.isFullFilled() ? Screen.getRes().getImage("check") : Screen.getRes().getImage("check_blank"));
            setFocus(false);
            categoryLabel.setFocus(false);
            questionsLabel.setFocus(false);
            iconLabel.setFocus(false);
            styleContainer.setBgPainter(bgPainter);
        }




        Label topAlign = new Label(" ");
        Label bottonAlign = new Label(" ");

        topAlign.setStyle(styleSecondLabel);
        bottonAlign.setStyle(styleSecondLabel);

        Dimension dTopAlign = topAlign.getPreferredSize();
        dTopAlign.setHeight(3);
        topAlign.setPreferredSize(dTopAlign);

        Dimension dBottonAlign = bottonAlign.getPreferredSize();
        dBottonAlign.setHeight(5);
        bottonAlign.setPreferredSize(dBottonAlign);

        centerContainer.addComponent(topAlign);
        centerContainer.addComponent(categoryLabel);
        centerContainer.addComponent(questionsLabel);
        centerContainer.addComponent(bottonAlign);

        addComponent(BorderLayout.CENTER, centerContainer);
        addComponent(BorderLayout.WEST, iconLabel);

        return this;
    }


    public Component getListFocusComponent(List list) {
        categoryLabel.setText(" ");
        categoryLabel.setFocus(true);

        questionsLabel.setText(" ");
        questionsLabel.setFocus(true);

        iconLabel.setIcon(null);
        iconLabel.setFocus(true);

        styleContainer.setBgPainter(focusBGPainter);
        this.setFocus(true);

        return this;
    }

}

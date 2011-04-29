package br.org.indt.ndg.lwuit.ui.renderers;

import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.lwuit.model.Category;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.List;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.Label;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.layouts.BoxLayout;

/**
 *
 * @author mluz
 */
public class CategoryListCellRenderer extends DefaultNDGListCellRenderer {

    private Label categoryLabel;
    private Label questionsLabel;
    private Label iconLabel;


    public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
        Category category = (Category)value;
        Container centerContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));

        categoryLabel = new Label(category.getName());
        categoryLabel.setIcon(null);
        categoryLabel.getStyle().setPadding(0,0,0,0);
        categoryLabel.getSelectedStyle().setPadding(0,0,0,0);
        categoryLabel.getStyle().setMargin(0,0,0,0);
        categoryLabel.getSelectedStyle().setMargin(0,0,0,0);

        questionsLabel = new Label(category.getQuestions().size() + (category.getQuestions().size() > 1 ? " " + Resources.QUESTIONS : " "+ Resources.QUESTION));
        questionsLabel.setIcon(null);
        questionsLabel.getStyle().setPadding(0,0,0, 0);
        questionsLabel.getSelectedStyle().setPadding(0,0,0,0);
        questionsLabel.getStyle().setMargin(0,0,0,0);
        questionsLabel.getSelectedStyle().setMargin(0,0,0,0);

        iconLabel = new Label("");

        if (isSelected) {
            setFocus(true);
            categoryLabel.setFocus(true);
            questionsLabel.setFocus(true);
            iconLabel.setFocus(true);
            categoryLabel.getStyle().setFont( NDGStyleToolbox.getInstance().listStyle.selectedFont );
            categoryLabel.getStyle().setFgColor( NDGStyleToolbox.getInstance().listStyle.selectedFontColor );
            questionsLabel.getStyle().setFont( NDGStyleToolbox.getInstance().listStyle.secondarySelectedFont );
            questionsLabel.getStyle().setFgColor( NDGStyleToolbox.getInstance().listStyle.selectedFontColor );
            getStyle().setFgColor( NDGStyleToolbox.getInstance().listStyle.selectedFontColor );
            iconLabel.setIcon(category.isFullFilled() ? Resources.check : Resources.question);
            getStyle().setBgPainter(m_focusBGPainter);
        } else {
            setFocus(false);
            categoryLabel.setFocus(false);
            questionsLabel.setFocus(false);
            iconLabel.setFocus(false);
            categoryLabel.getStyle().setFont( NDGStyleToolbox.getInstance().listStyle.unselectedFont );
            questionsLabel.getStyle().setFont( NDGStyleToolbox.getInstance().listStyle.secondaryUnselectedFont );
            categoryLabel.getStyle().setFgColor( NDGStyleToolbox.getInstance().listStyle.unselectedFontColor );
            questionsLabel.getStyle().setFgColor( NDGStyleToolbox.getInstance().listStyle.unselectedFontColor );
            iconLabel.setIcon(category.isFullFilled() ? Resources.check : Resources.question);
            getStyle().setBgPainter(m_bgPainter);
        }

        Label bottonAlign = new Label(" ");
        Dimension dBottonAlign = bottonAlign.getPreferredSize();
        dBottonAlign.setHeight(2);
        bottonAlign.setPreferredSize(dBottonAlign);

        centerContainer.addComponent(categoryLabel);
        centerContainer.addComponent(questionsLabel);
        centerContainer.addComponent(bottonAlign);

        addComponent(BorderLayout.CENTER, centerContainer);
        addComponent(BorderLayout.WEST, iconLabel);

        return this;
    }
}

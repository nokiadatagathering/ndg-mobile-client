/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.BackDisplayCategoryViewFormCommand;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Component;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.Font;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.plaf.UIManager;
import br.org.indt.ndg.lwuit.extended.ChoiceGroup;
import br.org.indt.ndg.lwuit.extended.ChoiceGroupListener;
import br.org.indt.ndg.mobile.AppMIDlet;
import com.sun.lwuit.Label;
import com.sun.lwuit.events.FocusListener;

public class DisplayCategoryForm extends Screen implements ActionListener, ChoiceGroupListener {

    private Font labelFont = Screen.getRes().getFont("NokiaSansWide13");
    private boolean enableCategories = false;
    private ChoiceGroup cg = null;

    protected void loadData() {
        enableCategories = AppMIDlet.getInstance().getSettings().getStructure().getCategoriesEnabled();
    }

    protected void customize() {
        setTitle(Resources.NEWUI_NOKIA_DATA_GATHERING, Resources.CATEGORY);
        form.removeAllCommands();
        form.addCommand(BackDisplayCategoryViewFormCommand.getInstance().getCommand());
        form.setCommandListener(this);
        form.removeAll();
        TextArea questionName = new TextArea(5, 20);
        questionName.setText(Resources.CATEGORYVIEW_CONFIGURATION);
        questionName.setStyle(UIManager.getInstance().getComponentStyle("Label"));
        questionName.getStyle().setFont(labelFont);
        questionName.setRows(questionName.getLines() - 1);
        questionName.setEditable(false);
        questionName.setFocusable(false);
        form.addComponent(questionName);

        String[] choices = new String[2];
        choices[0] = Resources.ON;
        choices[1] = Resources.OFF;
        int initItem;
        if (enableCategories) {
            initItem = 0;
        } else {
            initItem = 1;
        }
        cg = new ChoiceGroup(choices, initItem);
        cg.setCgListener(this);
        // for a better scroll
        questionName.setFocusable(true);
        questionName.addFocusListener(new FocusListener() {

            public void focusGained(Component c) {
                cg.requestFocus();
            }

            public void focusLost(Component c) {
            }
        });
        Label spaceBotton = new Label("spaceBotton");
        spaceBotton.getStyle().setFgColor(form.getStyle().getBgColor());
        spaceBotton.setFocusable(true);
        spaceBotton.addFocusListener(new FocusListener() {

            public void focusGained(Component arg0) {
                cg.setItemFocused(cg.size() - 1);
            }

            public void focusLost(Component arg0) {
            }
        });
        // add components
        form.addComponent(cg);
        form.addComponent(spaceBotton);
        cg.requestFocus();
    }

    public void actionPerformed(ActionEvent evt) {
        Object cmd = evt.getSource();
        if (cmd == BackDisplayCategoryViewFormCommand.getInstance().getCommand()) {
           BackDisplayCategoryViewFormCommand.getInstance().execute(null);
        } 
    }

    // Listener from ChoiceGroup
    public void itemChoosed(int i) {
        if (i == 0) {
            AppMIDlet.getInstance().getSettings().getStructure().setCategoriesEnabled(true);
            cg.setSelectedIndex(0);
        } else {
            AppMIDlet.getInstance().getSettings().getStructure().setCategoriesEnabled(false);
            cg.setSelectedIndex(1);
        }
        AppMIDlet.getInstance().getSettings().writeSettings();
    }
}


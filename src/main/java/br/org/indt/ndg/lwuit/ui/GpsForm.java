/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.BackGpsFormCommand;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Component;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import br.org.indt.ndg.lwuit.control.SurveysControl;
import br.org.indt.ndg.lwuit.control.ViewDetailsGpsFormCommand;
import com.sun.lwuit.Font;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.plaf.UIManager;
import br.org.indt.ndg.lwuit.extended.ChoiceGroup;
import br.org.indt.ndg.lwuit.extended.ChoiceGroupListener;
import com.sun.lwuit.Label;
import com.sun.lwuit.events.FocusListener;

public class GpsForm extends Screen implements ActionListener, ChoiceGroupListener {
    private Font labelFont = Screen.getRes().getFont("NokiaSansWide13");
    boolean gps_state = false;
    protected void loadData() {

        for (int itemNum=0; itemNum < SurveysControl.getInstance().getCurrentOldGpsForm().size(); itemNum++)
        {
            if (SurveysControl.getInstance().getCurrentOldGpsForm().get(itemNum) instanceof  javax.microedition.lcdui.ChoiceGroup) {
                gps_state = ((javax.microedition.lcdui.ChoiceGroup) SurveysControl.getInstance().getCurrentOldGpsForm().get(itemNum)).isSelected(0);
                break;
            }
        }
        
    }

    protected void customize() {
        setTitle(Resources.NEWUI_NOKIA_DATA_GATHERING, Resources.GPS);
        form.removeAllCommands();
        form.addCommand(BackGpsFormCommand.getInstance().getCommand());
        if (gps_state) form.addCommand(ViewDetailsGpsFormCommand.getInstance().getCommand());
        form.setCommandListener(this);

        form.removeAll();
        TextArea questionName = new TextArea(5,20);
        questionName.setText(Resources.GPSCONFIG);
        questionName.setStyle(UIManager.getInstance().getComponentStyle("Label"));
        questionName.getStyle().setFont(labelFont);
        questionName.setRows(questionName.getLines()-1);
        questionName.setEditable(false);
        questionName.setFocusable(false);
        form.addComponent(questionName);

        String[] choices = new String[2];
        choices[0] = Resources.ON;
        choices[1] = Resources.OFF;
        final ChoiceGroup cg;
        int initItem;
        if (gps_state) initItem = 0; else initItem = 1;
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
                cg.setItemFocused(cg.size()-1);
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
        if (cmd == ViewDetailsGpsFormCommand.getInstance().getCommand()) {
            ViewDetailsGpsFormCommand.getInstance().execute(null);
        } else {
            BackGpsFormCommand.getInstance().execute(null);
        }
    }

    // Listener from ChoiceGroup
    public void itemChoosed(int i) {
        if (i == 0) {
            form.addCommand(ViewDetailsGpsFormCommand.getInstance().getCommand());
        }
        else {
            form.removeCommand(ViewDetailsGpsFormCommand.getInstance().getCommand());
        }

        for (int itemNum=0; itemNum < SurveysControl.getInstance().getCurrentOldGpsForm().size(); itemNum++)
        {
            if (SurveysControl.getInstance().getCurrentOldGpsForm().get(itemNum) instanceof  javax.microedition.lcdui.ChoiceGroup) {
                if (i == 0)
                    ((javax.microedition.lcdui.ChoiceGroup) SurveysControl.getInstance().getCurrentOldGpsForm().get(itemNum)).setSelectedIndex(0, true);
                else
                    ((javax.microedition.lcdui.ChoiceGroup) SurveysControl.getInstance().getCurrentOldGpsForm().get(itemNum)).setSelectedIndex(1, true);

                SurveysControl.getInstance().getCurrentOldGpsForm().itemStateChanged((javax.microedition.lcdui.ChoiceGroup) SurveysControl.getInstance().getCurrentOldGpsForm().get(itemNum));
                break;
            }
        }
    }
}
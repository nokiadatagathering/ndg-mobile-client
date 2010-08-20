/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import br.org.indt.ndg.lwuit.control.AcceptAgreementCommand;
import br.org.indt.ndg.lwuit.control.DeclineAgreementCommand;
import com.sun.lwuit.Font;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.plaf.UIManager;


public class AgreementScreen extends Screen implements ActionListener {
    private Font labelFont = Screen.getRes().getFont("NokiaSansWide13");
    private String strText;

    protected void loadData() {
        strText = Resources.AGREEMENT_TEXT;
    }

    protected void customize() {
        form.removeAllCommands();
        form.addCommand(DeclineAgreementCommand.getInstance().getCommand());
        form.addCommand(AcceptAgreementCommand.getInstance().getCommand());
        form.setCommandListener(this);

        setTitle(Resources.NEWUI_NOKIA_DATA_GATHERING, "");
//        TextArea textAgreement = new TextArea(5,20);
//        textAgreement.setStyle(UIManager.getInstance().getComponentStyle("Label"));
//        textAgreement.getStyle().setFont(labelFont);
//        textAgreement.setEditable(true);
//        textAgreement.setFocusable(true);
//        textAgreement.setIsScrollVisible(true);
//        textAgreement.setSmoothScrolling(true);
//        textAgreement.setText(strText);
//        textAgreement.setRows(0);
//        form.addComponent(textAgreement);
        showText();
    }

    private void showText() {
        int strLength = 0, beginIndex = 0, endIndex = 0, offset = 100;
        strLength = strText.length();
        while (endIndex != strLength) {
            endIndex = endIndex + offset;
            if (endIndex >= strLength) {
                endIndex = strLength;
            }
            TextArea ta = new TextArea();
            ta.setStyle(UIManager.getInstance().getComponentStyle("Label"));
            ta.getStyle().setFont(labelFont);
            ta.setEditable(false);
            ta.setFocusable(true);
            //ta.setBorderPainted(false);

            //Skip to the end of the word if 100 is in the middle
            while (true) {
                if (endIndex >= strLength) {
                    break;
                }
                if (strText.charAt(endIndex) != ' ') {
                    endIndex++;
                }
                else {
                    break;
                }
            }

            ta.setText(strText.substring(beginIndex, endIndex));
            ta.setRows(0);
            form.addComponent(ta);
            beginIndex = endIndex;
        }
    }

    public void actionPerformed(ActionEvent evt) {
        Object cmd = evt.getSource();
        if (cmd == AcceptAgreementCommand.getInstance().getCommand()) {
            AcceptAgreementCommand.getInstance().execute(null);
        } else if (cmd == DeclineAgreementCommand.getInstance().getCommand()) {
            DeclineAgreementCommand.getInstance().execute(null);
        }
    }
}

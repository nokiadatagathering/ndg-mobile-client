/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.CancelSSSubmitCommand;
import br.org.indt.ndg.lwuit.control.SurveysControl;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Component;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.plaf.UIManager;

public class StatusScreenSubmit extends Screen implements ActionListener {

    //private String strText;
    private TextArea item;
    private Image image;
    private Label l;
    private boolean firstcreation = true;

    protected void loadData() {
        if (firstcreation) {
            item = new TextArea(3,20);
            item.setStyle(UIManager.getInstance().getComponentStyle("Label"));
            item.getStyle().setFont(Screen.getRes().getFont("NokiaSansWide13"));
            item.setEditable(false);
            item.setFocusable(false);

            image = Screen.getRes().getAnimation("wait2");
            l = new Label(image);
            l.setAlignment(Component.CENTER);
            firstcreation = false;
        }
    }

    protected void customize() {
        setTitle(Resources.NEWUI_NOKIA_DATA_GATHERING, "");

        form.removeAll();
        item.setText(Resources.SUBMIT_SERVER);
        form.addComponent(item);
        form.addComponent(l);

        form.addCommand(CancelSSSubmitCommand.getInstance().getCommand());
        form.setCommandListener(this);
    }

    public void actionPerformed(ActionEvent evt) {
        Object cmd = evt.getSource();
        if (cmd == CancelSSSubmitCommand.getInstance().getCommand()) {
            CancelSSSubmitCommand.getInstance().execute(null);
        }
    }


}

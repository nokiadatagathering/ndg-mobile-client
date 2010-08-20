/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.CancelUpdateClientAppCommand;
import br.org.indt.ndg.lwuit.control.SurveysControl;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Component;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.plaf.UIManager;

public class UpdateClientApp extends Screen implements ActionListener {
    private TextArea item;
    private Image image;
    private Label l;
    private boolean firstcreation = true;

    private String strText1, strText2;


    protected void loadData() {
        strText1 = SurveysControl.getInstance().getCurrentOldUpdateClientApp().getFormContentText1();
        strText2 = SurveysControl.getInstance().getCurrentOldUpdateClientApp().getFormContentText2();

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
        form.removeAll();
        form.removeAllCommands();

        setTitle(Resources.NEWUI_NOKIA_DATA_GATHERING, strText1);
        item.setText(strText2);
        form.addComponent(item);
        form.addComponent(l);

        form.addCommand(CancelUpdateClientAppCommand.getInstance().getCommand());
        
        form.setCommandListener(this);
    }

    public void actionPerformed(ActionEvent evt) {
        Object cmd = evt.getSource();
        if (cmd == CancelUpdateClientAppCommand.getInstance().getCommand()) {
            CancelUpdateClientAppCommand.getInstance().execute(null);
        }
    }

}

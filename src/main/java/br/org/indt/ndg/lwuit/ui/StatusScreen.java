package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.CancelSSSubmitCommand;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Component;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.plaf.UIManager;

public class StatusScreen extends Screen implements ActionListener {

    private TextArea item;
    private Image image;
    private Label label;

    protected void loadData() {
        if (item == null) {
            item = new TextArea(3,20);
            item.setEditable(false);
            item.setFocusable(false);

            image = Screen.getRes().getImage("wait2");
            label = new Label(image);
            label.setAlignment(Component.CENTER);
        }
        item.setUnselectedStyle(UIManager.getInstance().getComponentStyle("Label"));
        item.getStyle().setFont( NDGStyleToolbox.fontSmall );
    }

    protected void customize() {
        setTitle(Resources.NEWUI_NOKIA_DATA_GATHERING, "");

        form.removeAll();
        item.setText(Resources.SUBMIT_SERVER);
        form.addComponent(item);
        form.addComponent(label);

        form.addCommand(CancelSSSubmitCommand.getInstance().getCommand());
        try{
            form.removeCommandListener(this);
        } catch (NullPointerException npe ) {
            //during first initialisation remove throws exception.
            //this ensure that we have registered listener once
        }
        form.addCommandListener(this);
    }

    public void actionPerformed(ActionEvent evt) {
        Object cmd = evt.getSource();
        if (cmd == CancelSSSubmitCommand.getInstance().getCommand()) {
            CancelSSSubmitCommand.getInstance().execute(null);
        }
    }
}

package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.BackSimpleLocationCommand;
import br.org.indt.ndg.lwuit.control.OkSimpleLocationCommand;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.plaf.UIManager;

public class SimpleLocation extends Screen implements ActionListener {

    private String strText;
    private String title1, title2;
    private boolean bUpdating = false;
    private TextArea questionName;

    protected void loadData() {
        strText = AppMIDlet.getInstance().getLocationHandler().getLocationString();
        title1 = Resources.NEWUI_NOKIA_DATA_GATHERING;
        title2 = Resources.GPS_LOCAL;
    }

    protected void customize() {
        form.removeAllCommands();
        form.addCommand(BackSimpleLocationCommand.getInstance().getCommand());
        form.addCommand(OkSimpleLocationCommand.getInstance().getCommand());
        try{
            form.removeCommandListener(this);
        } catch (NullPointerException npe ) {
            //during first initialisation remove throws exception.
            //this ensure that we have registered listener once
        }
        form.addCommandListener(this);

        setTitle(title1, title2);
        questionName = new TextArea(5,20);
        questionName.setText(strText);
        questionName.setUnselectedStyle(UIManager.getInstance().getComponentStyle("Label"));
        questionName.getStyle().setFont(NDGStyleToolbox.fontSmall);
        questionName.setRows(questionName.getLines()-1);
        questionName.setEditable(false);
        questionName.setFocusable(false);
        form.removeAll();
        form.addComponent(questionName);

        // Starting thread for updating status from location provider(GPS Coordinates)
        bUpdating = true;
        Thread t = new Thread(new UpdateStatus());
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
    }

    public void actionPerformed(ActionEvent evt) {
        Object cmd = evt.getSource();
        bUpdating = false;
        if (cmd == BackSimpleLocationCommand.getInstance().getCommand()) {
            BackSimpleLocationCommand.getInstance().execute(null);
        } else {
            OkSimpleLocationCommand.getInstance().execute(null);
        }
    }

    class UpdateStatus implements Runnable {

        public void run() {
            while (bUpdating) {
                try {
                    strText = AppMIDlet.getInstance().getLocationHandler().getLocationString();
                    questionName.setText(strText);
                    Thread.sleep(1000);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}

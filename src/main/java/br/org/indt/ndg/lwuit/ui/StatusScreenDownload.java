package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.CancelSSDownloadCommand;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.download.DownloadNewSurveys;
import com.sun.lwuit.Component;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.plaf.UIManager;

public class StatusScreenDownload extends Screen implements ActionListener {

    private String strText;
    private TextArea item;
    private Image image;
    private Label l;
    private boolean firstcreation = true;

    protected void loadData() {
        strText =  DownloadNewSurveys.getInstance().ServerStatus();
        if (firstcreation) {
            item = new TextArea(3,20);
            item.setUnselectedStyle(UIManager.getInstance().getComponentStyle("Label"));
            item.getStyle().setFont( NDGStyleToolbox.fontSmall );
            item.setEditable(false);
            item.setFocusable(false);

            image = Screen.getRes().getImage("wait2");
            l = new Label(image);
            l.setAlignment(Component.CENTER);
            firstcreation = false;
        }
    }

    protected void customize() {
        setTitle(Resources.NEWUI_NOKIA_DATA_GATHERING, "");

        form.removeAll();
        item.setText(strText);
        form.addComponent(item);
        form.addComponent(l);

        form.addCommand(CancelSSDownloadCommand.getInstance().getCommand());
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
        if (cmd == CancelSSDownloadCommand.getInstance().getCommand()) {
            CancelSSDownloadCommand.getInstance().execute(null);
        }
    }

}

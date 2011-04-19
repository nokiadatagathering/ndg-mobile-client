package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.BackToSettingsFormCommand;
import br.org.indt.ndg.lwuit.extended.ChoiceGroup;
import br.org.indt.ndg.lwuit.extended.ChoiceGroupListener;
import br.org.indt.ndg.lwuit.extended.DateField;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Label;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;

/**
 *
 * @author damian.janicki
 */
public class SelectDateFormatForm extends Screen implements ActionListener, ChoiceGroupListener{
    private ChoiceGroup cg = null;

    protected void loadData() {
    }

    protected void customize() {
        setTitle(Resources.NEWUI_NOKIA_DATA_GATHERING, Resources.DATE_FORMAT);
        form.removeAllCommands();

        try{
            form.removeCommandListener(this);
        } catch (NullPointerException npe ) {
            //during first initialisation remove throws exception.
            //this ensure that we have registered listener once
        }
        form.addCommandListener(this);
        form.removeAll();

        Label label = new Label(Resources.AVAILABLE_DATE_FORMAT);
        label.getStyle().setFont( NDGStyleToolbox.fontSmall );
        label.setFocusable(false);
        form.addComponent(label);

        form.addCommand(BackToSettingsFormCommand.getInstance().getCommand());

        String[] choices = new String[]{Resources.DDMMYYYY, Resources.MMDDYYYY};
        int id = AppMIDlet.getInstance().getSettings().getStructure().getDateFormatId();

        int selected = 0;
        if(id == DateField.MMDDYYYY){
            selected = 1;
        }

        cg = new ChoiceGroup(choices, selected);
        cg.setCgListener(this);

        form.addComponent(cg);
    }

    public void actionPerformed(ActionEvent ae) {
        Object cmd = ae.getSource();
        if (cmd == BackToSettingsFormCommand.getInstance().getCommand()) {
            BackToSettingsFormCommand.getInstance().execute(null);
        }
    }

    public void itemChoosed(int i) {
        cg.setSelectedIndex(i);
        int dateFormat;

        if(i == 1 ){
            dateFormat = DateField.MMDDYYYY;
        }else{
            dateFormat = DateField.DDMMYYYY;
        }

        AppMIDlet.getInstance().getSettings().getStructure().setDateFormatId(dateFormat);
        AppMIDlet.getInstance().getSettings().writeSettings();
    }
}

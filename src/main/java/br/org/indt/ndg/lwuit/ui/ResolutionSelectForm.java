package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.BackResolutionSelectFormCommand;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.plaf.UIManager;
import br.org.indt.ndg.lwuit.extended.ChoiceGroup;
import br.org.indt.ndg.lwuit.extended.ChoiceGroupListener;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import br.org.indt.ndg.mobile.AppMIDlet;

public class ResolutionSelectForm extends Screen implements ActionListener, ChoiceGroupListener {
    private ChoiceGroup cg = null;

    protected void loadData() {
    }

    protected void customize() {
        setTitle(Resources.NEWUI_NOKIA_DATA_GATHERING, Resources.PHOTO_RESOLUTION);
        form.removeAllCommands();
        form.addCommand(BackResolutionSelectFormCommand.getInstance().getCommand());
        try{
            form.removeCommandListener(this);
        } catch (NullPointerException npe ) {
            //during first initialisation remove throws exception.
            //this ensure that we have registered listener once
        }
        form.addCommandListener(this);
        form.removeAll();
        TextArea questionName = new TextArea(5, 20);
        questionName.setText(Resources.RESOLUTIONS);
        questionName.setUnselectedStyle(UIManager.getInstance().getComponentStyle("Label"));
        questionName.getStyle().setFont( NDGStyleToolbox.fontSmall );
        questionName.setRows(questionName.getLines() - 1);
        questionName.setEditable(false);
        questionName.setFocusable(false);
        form.addComponent(questionName);

        String[] choices = AppMIDlet.getInstance().getSettings().getStructure().getResolutionList();

        cg = new ChoiceGroup(choices, AppMIDlet.getInstance().getSettings().getStructure().getPhotoResolutionId() );
        cg.setCgListener(this);

        form.addComponent(cg);

        cg.setItemFocused(AppMIDlet.getInstance().getSettings().getStructure().getPhotoResolutionId());
    }

    public void actionPerformed(ActionEvent evt) {
        Object cmd = evt.getSource();
        if (cmd == BackResolutionSelectFormCommand.getInstance().getCommand()) {
           BackResolutionSelectFormCommand.getInstance().execute(null);
        } 
    }

    // Listener from ChoiceGroup
    public void itemChoosed(int i) {
        cg.setSelectedIndex(i);
        AppMIDlet.getInstance().getSettings().getStructure().setPhotoResolutionId(i);
        AppMIDlet.getInstance().getSettings().writeSettings();
    }
}


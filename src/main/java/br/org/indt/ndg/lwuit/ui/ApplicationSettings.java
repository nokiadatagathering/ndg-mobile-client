package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.extended.ChoiceGroup;
import br.org.indt.ndg.lwuit.extended.ChoiceGroupListener;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.Utils;
import com.nokia.mid.appl.cmd.Local;
import com.sun.lwuit.Command;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.plaf.UIManager;

public class ApplicationSettings extends Screen implements ActionListener, ChoiceGroupListener {

    private ChoiceGroup m_protocolChoice = null;

    protected void loadData() {
        // nothing to do
    }

    private boolean isInitialized() {
        return ( m_protocolChoice != null );
    }

    protected void customize() {
        if (isInitialized())
            return;

        setTitle(Resources.NEWUI_NOKIA_DATA_GATHERING, Resources.PROTOCOL_SETTINGS );
        form.addCommand( new Command(Local.getText(Local.QTJ_CMD_BACK) ) );
        form.addCommandListener(this);

        TextArea protocolTypeQuestion = new TextArea(5, 20);
        protocolTypeQuestion.setText(Resources.RESOLUTIONS);
        protocolTypeQuestion.setUnselectedStyle(UIManager.getInstance().getComponentStyle("Label"));
        protocolTypeQuestion.getStyle().setFont( NDGStyleToolbox.fontSmall );
        protocolTypeQuestion.setRows(protocolTypeQuestion.getLines() - 1);
        protocolTypeQuestion.setEditable(false);
        protocolTypeQuestion.setFocusable(false);

        String[] choices = { Resources.NDG_STANDARD_NAME, Resources.OPENROSA_STANDARD_NAME };
        int selectedProtocol = 0;
        int currentProtocolId = AppMIDlet.getInstance().getSettings().getStructure().getProtocolId();
        if ( currentProtocolId == Utils.NDG_FORMAT ) {
            selectedProtocol = 0;
        } else if ( currentProtocolId == Utils.OPEN_ROSA_FORMAT ) {
            selectedProtocol = 1;
        }
        m_protocolChoice = new ChoiceGroup(choices, selectedProtocol );
        m_protocolChoice.setCgListener(this);

        form.addComponent(protocolTypeQuestion);
        form.addComponent(m_protocolChoice);

        m_protocolChoice.setItemFocused(selectedProtocol);
    }

    public void actionPerformed(ActionEvent evt) {
        Command cmd = evt.getCommand();
        if ( cmd.getCommandName().equals( Local.getText(Local.QTJ_CMD_BACK) ) ) {
            AppMIDlet.getInstance().setDisplayable(SurveyList.class);
        }
    }

    public void itemChoosed(int i) {
        m_protocolChoice.setSelectedIndex(i);
        int newProtocolId = 1;
        if ( i == 0 )
            newProtocolId = Utils.NDG_FORMAT;
        else if ( i == 1 )
            newProtocolId = Utils.OPEN_ROSA_FORMAT;
        AppMIDlet.getInstance().getSettings().getStructure().setProtocolId(newProtocolId);
        AppMIDlet.getInstance().getSettings().writeSettings();
    }

}

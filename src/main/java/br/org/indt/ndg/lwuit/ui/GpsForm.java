package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.BackGpsFormCommand;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Component;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import br.org.indt.ndg.lwuit.control.ViewDetailsGpsFormCommand;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.plaf.UIManager;
import br.org.indt.ndg.lwuit.extended.ChoiceGroup;
import br.org.indt.ndg.lwuit.extended.ChoiceGroupListener;
import br.org.indt.ndg.mobile.AppMIDlet;
import com.sun.lwuit.Label;
import com.sun.lwuit.events.FocusListener;
import javax.microedition.location.LocationProvider;

public class GpsForm extends Screen implements ActionListener, ChoiceGroupListener {

    private boolean gps_state = false;
    private boolean geoTagging_state = false;
    private ChoiceGroup useGpsChoice = null;
    private ChoiceGroup useGeoTagChoice = null;
    TextArea useGeoTagQuestion = null;

    protected void loadData() {
        gps_state = AppMIDlet.getInstance().getSettings().getStructure().getGpsConfigured();
        geoTagging_state = AppMIDlet.getInstance().getSettings().getStructure().getGeoTaggingConfigured();
    }

    protected void customize() {
        setTitle(Resources.NEWUI_NOKIA_DATA_GATHERING, Resources.GPS);
        form.removeAllCommands();
        form.addCommand(BackGpsFormCommand.getInstance().getCommand());
        try{
            form.removeCommandListener(this);
        } catch (NullPointerException npe ) {
            //during first initialisation remove throws exception.
            //this ensure that we have registered listener once
        }
        form.addCommandListener(this);
        form.removeAll();
        TextArea useGpsQuestion = new TextArea(5, 20);
        useGpsQuestion.setText(Resources.GPSCONFIG);
        useGpsQuestion.setUnselectedStyle(UIManager.getInstance().getComponentStyle("Label"));
        useGpsQuestion.setRows(useGpsQuestion.getLines() - 1);
        useGpsQuestion.setEditable(false);

        if (gps_state) {
            form.addCommand(ViewDetailsGpsFormCommand.getInstance().getCommand());
        }
        String[] choices = new String[2];
        choices[0] = Resources.ON;
        choices[1] = Resources.OFF;
        boolean showGeoTagChoice = false;
        int gpsInitItem;
        if (gps_state) {
            gpsInitItem = 0;
            showGeoTagChoice = true;
        } else {
            gpsInitItem = 1;
        }
        useGpsChoice = new ChoiceGroup(choices, gpsInitItem);
        useGpsChoice.setCgListener(this);
        // for a better scroll
        useGpsQuestion.setFocusable(true);
        useGpsQuestion.addFocusListener(new FocusListener() {

            public void focusGained(Component c) {
                useGpsChoice.requestFocus();
            }

            public void focusLost(Component c) {
            }
        });
        // is visible only when useGpsChoice[0] (GPS ON) is checked
        useGeoTagQuestion = new TextArea(5, 20);
        useGeoTagQuestion.setText(Resources.GEO_TAGGING_CONF);
        useGeoTagQuestion.setUnselectedStyle(UIManager.getInstance().getComponentStyle("Label"));
        useGeoTagQuestion.setRows(useGeoTagQuestion.getLines() - 1);
        useGeoTagQuestion.setEditable(false);
        useGeoTagQuestion.setFocusable(false);

        String[] geoTagChoices = new String[2];
        geoTagChoices[0] = Resources.YES;
        geoTagChoices[1] = Resources.NO;
        int geoTagInitItem;
        if (geoTagging_state) {
            geoTagInitItem = 0;
        } else {
            geoTagInitItem = 1;
        }
        useGeoTagChoice = new ChoiceGroup(geoTagChoices, geoTagInitItem);
        setUseGeoTagConfigurationVisible(showGeoTagChoice);
        useGeoTagChoice.setCgListener( new ChoiceGroupListener() {

            public void itemChoosed( int geoTagOption ) {
                boolean geoTagConfigured = false;
                if ( 0 == geoTagOption )
                    geoTagConfigured = true;
                AppMIDlet.getInstance().getSettings().getStructure().setGeoTaggingConfigured(geoTagConfigured);
                AppMIDlet.getInstance().getSettings().writeSettings();
            }
        });

        Label spaceBotton = new Label("spaceBotton");
        spaceBotton.getStyle().setFgColor(form.getStyle().getBgColor());
        spaceBotton.setFocusable(true);
        spaceBotton.addFocusListener(new FocusListener() {

            public void focusGained(Component arg0) {
                if (useGeoTagChoice.isVisible())
                    useGeoTagChoice.setItemFocused(useGeoTagChoice.size() - 1);
                else
                    useGpsChoice.setItemFocused(useGpsChoice.size() - 1);
            }

            public void focusLost(Component arg0) {
            }
        });
        // add components
        form.addComponent(useGpsQuestion);
        form.addComponent(useGpsChoice);
        form.addComponent(useGeoTagQuestion);
        form.addComponent(useGeoTagChoice);
        form.addComponent(spaceBotton);
        useGpsChoice.setItemFocused(gpsInitItem);
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
        boolean showGeoTagChoice = false;
        if (i == 0) {
            int status = AppMIDlet.getInstance().getLocationHandler().connect();
            if (status == LocationProvider.AVAILABLE
                    || status == LocationProvider.TEMPORARILY_UNAVAILABLE) {
                AppMIDlet.getInstance().getLocationHandler().updateServiceOn();
                AppMIDlet.getInstance().getSettings().getStructure().setGpsConfigured(true);
                form.addCommand(ViewDetailsGpsFormCommand.getInstance().getCommand());
                showGeoTagChoice = true;
            } else {
                GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
                GeneralAlert.getInstance().show(Resources.ERROR_TITLE, Resources.NO_LOCATION, GeneralAlert.ALARM);
                useGpsChoice.setSelectedIndex(1);
            }
        } else {
            AppMIDlet.getInstance().getSettings().getStructure().setGpsConfigured(false);
            form.removeCommand(ViewDetailsGpsFormCommand.getInstance().getCommand());
            AppMIDlet.getInstance().getLocationHandler().updateServiceOff();
            AppMIDlet.getInstance().getLocationHandler().disconnect();
        }
        AppMIDlet.getInstance().getSettings().writeSettings();
        setUseGeoTagConfigurationVisible(showGeoTagChoice);
    }

    private void setUseGeoTagConfigurationVisible( boolean visible ) {
        useGeoTagQuestion.setVisible(visible);
        useGeoTagChoice.setVisible(visible);
        form.repaint();
    }
}


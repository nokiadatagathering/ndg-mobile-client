package br.org.indt.ndg.mobile.settings;

import br.org.indt.ndg.mobile.AppMIDlet;

/**
 *
 * @author amartini
 */
public class IMEIHandler {

    public IMEIHandler(){
    }

    public boolean isIMEIRegistered(){
        return AppMIDlet.getInstance().getSettings().getStructure().getRegisteredFlag() == SettingsStructure.REGISTERED;
    }

    public void registerIMEI(){
        AppMIDlet.getInstance().getSettings().getStructure().setRegisteredFlag( SettingsStructure.REGISTERED );
        AppMIDlet.getInstance().getSettings().writeSettings();
    }
}

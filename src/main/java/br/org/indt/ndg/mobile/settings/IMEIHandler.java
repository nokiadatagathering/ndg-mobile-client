package br.org.indt.ndg.mobile.settings;

import br.org.indt.ndg.mobile.AppMIDlet;

/**
 *
 * @author amartini
 */
public class IMEIHandler {
    private static final int REGISTERED = 1;

    public IMEIHandler(){
    }

    public boolean isIMEIRegistered(){
        return AppMIDlet.getInstance().getSettings().getStructure().getRegisteredFlag() == REGISTERED;
    }

    public void registerIMEI(){
        AppMIDlet.getInstance().getSettings().getStructure().setRegisteredFlag( REGISTERED );
        AppMIDlet.getInstance().getSettings().writeSettings();
    }
}

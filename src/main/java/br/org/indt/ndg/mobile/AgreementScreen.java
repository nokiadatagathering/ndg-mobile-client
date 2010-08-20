/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.mobile;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDletStateChangeException;

public class AgreementScreen extends Form implements CommandListener {
    
    private static AgreementScreen myInstance = null;
    
    public AgreementScreen() {
        super("");
        append(Resources.AGREEMENT_TEXT);
        addCommand(Resources.CMD_ACCEPT);
        addCommand(Resources.CMD_DECLINE);
        setCommandListener(this);
    }

    private void exitApp() {
        try {
            AppMIDlet.getInstance().destroyApp(true);
        } catch (MIDletStateChangeException e) {
            AppMIDlet.getInstance().getGeneralAlert().showErrorExit(Resources.ELEAVE_MIDLET);
        }
    }

    public void commandAction(Command c, Displayable d) {
        if(c == Resources.CMD_ACCEPT){
            AppMIDlet instance = AppMIDlet.getInstance();
            instance.getSettings().getStructure().setAgreeFlag(0);
            instance.getSettings().writeSettings();
            instance.registerApp();
        }
        else if (c == Resources.CMD_DECLINE) {
            exitApp();
        }
    }

}

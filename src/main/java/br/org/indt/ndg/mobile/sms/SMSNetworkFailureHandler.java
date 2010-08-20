package br.org.indt.ndg.mobile.sms;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import java.util.Hashtable;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.wireless.messaging.Message;


public class SMSNetworkFailureHandler {
    private Hashtable unsent;
    
    public SMSNetworkFailureHandler(){
        unsent = new Hashtable();
    }

    public void doHandle() {
        AppMIDlet.getInstance().getGeneralAlert().showAlert(Resources.NETWORK_FAILURE, Resources.TRY_AGAIN_LATER);
        
        //unsent.p
    }

}

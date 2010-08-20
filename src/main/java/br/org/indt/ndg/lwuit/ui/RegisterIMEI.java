/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.CancelRegisterIMEICommand;
import br.org.indt.ndg.lwuit.control.Event;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.logging.Logger;
import br.org.indt.ndg.mobile.settings.IMEIHandler;
import com.sun.lwuit.Component;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.plaf.UIManager;
import java.io.DataInputStream;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;


/**
 *
 * @author kgomes
 */
public class RegisterIMEI extends Screen implements ActionListener {

    private String urlRegisterImei = "";
    private TextArea item;
    private Label l;
    Thread t = null;
    
    protected void loadData() {
       item = new TextArea(3,20);
       item.setStyle(UIManager.getInstance().getComponentStyle("Label"));
       item.getStyle().setFont(Screen.getRes().getFont("NokiaSansWide13"));
       item.setEditable(false);
       item.setFocusable(false);

       Image image = Screen.getRes().getAnimation("wait2");
       l = new Label(image);
       l.setAlignment(Component.CENTER);

       registerEvent(new RegisterIMEIOnShow(), ON_SHOW);
    }

    protected void customize() {
        setTitle(Resources.NEWUI_NOKIA_DATA_GATHERING, "");

        form.removeAll();
        form.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        item.setText(Resources.REGISTERING_APP);
        form.addComponent(item);
        form.addComponent(l);
        
        form.removeAllCommands();
        form.addCommand(CancelRegisterIMEICommand.getInstance().getCommand());
        form.setCommandListener(this);
    }

    public void actionPerformed(ActionEvent evt) {
        Object cmd = evt.getSource();
        if (cmd == CancelRegisterIMEICommand.getInstance().getCommand()) {
            CancelRegisterIMEICommand.getInstance().execute(null);
        }
    }

    public void doRegister(){
        RegisterIMEIRunnable ucr = new RegisterIMEIRunnable();
        t = new Thread(ucr);
        t.start();
    }

    class RegisterIMEIRunnable implements Runnable {

        public void run() {
            String msisdn = AppMIDlet.getInstance().getAppMsisdn();
            if(msisdn.startsWith("+")){
                msisdn = "%2b" + msisdn.substring(1);
            }
            urlRegisterImei = AppMIDlet.getInstance().getSettings().getStructure().getRegisterIMEIUrl();
            urlRegisterImei += "?imei="+AppMIDlet.getInstance().getIMEI();
            urlRegisterImei += "&msisdn="+msisdn;
            
            
            try {
                boolean gprs_support = AppMIDlet.getInstance().getSettings().getStructure().getGPRSSupport();
                boolean sms_support = AppMIDlet.getInstance().getSettings().getStructure().getSMSSupport();
                if(!gprs_support && !sms_support){
                    AppMIDlet.getInstance().getGeneralAlert().showAlert(Resources.NO_TRANSPORT, Resources.NO_TRANSPORT_SELECTED);
                } else {

                    HttpConnection httpConnection = (HttpConnection) Connector.open(urlRegisterImei);
                    httpConnection.setRequestMethod(HttpConnection.GET);
                    int responseCode = httpConnection.getResponseCode();
                    if (responseCode == httpConnection.HTTP_OK) {
                        DataInputStream httpInput = new DataInputStream(httpConnection.openDataInputStream());
                        int resp = httpInput.readInt();
                        if (resp < 0) {
                            exitApp();
                        }
                        else
                        {
                            IMEIHandler im = new IMEIHandler();
                            im.registerIMEI();
                            im.closeRegisterIMEI();
                            GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
                            GeneralAlert.getInstance().show(Resources.REGISTRATION_DONE, Resources.APP_REGISTERED,GeneralAlert.INFO);
                            AppMIDlet.getInstance().continueAppLoading();
                        }
                    }
                    else {
                        exitApp();
                    }
                }
            }catch (Exception ex) {                
                exitApp();
            }
        }

        private void exitApp() {
            GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
            GeneralAlert.getInstance().show(Resources.ERROR_TITLE, Resources.APP_REGISTRATION_FAILED, GeneralAlert.ERROR);
            CancelRegisterIMEICommand.getInstance().execute(null);
        }

    }

    class RegisterIMEIOnShow extends Event {
        protected void doAction(Object parameter) {
            doRegister();
        }
    }

}

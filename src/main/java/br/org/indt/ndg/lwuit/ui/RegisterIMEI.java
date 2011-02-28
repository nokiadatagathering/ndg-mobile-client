package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.CancelRegisterIMEICommand;
import br.org.indt.ndg.lwuit.control.Event;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.error.NetworkErrCode;
import br.org.indt.ndg.mobile.settings.IMEIHandler;
import com.sun.lwuit.Component;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.plaf.UIManager;
import java.io.DataInputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;


/**
 *
 * @author kgomes
 */
public class RegisterIMEI extends Screen implements ActionListener {

    private static final int SUCCESS = 1;
    private static final int MSISDN_ALREADY_REGISTERED = 2;
    private static final int FAILURE = -1;
    private static final int MSISDN_NOT_FOUND = -2;
    private static final int IMEI_ALREADY_EXISTS = -3;

    private String urlRegisterImei = "";
    private TextArea item;
    private Label l;
    Thread t = null;
    
    protected void loadData() {
       item = new TextArea(3,20);
       item.setUnselectedStyle(UIManager.getInstance().getComponentStyle("Label"));
       item.getStyle().setFont( NDGStyleToolbox.fontSmall );
       item.setEditable(false);
       item.setFocusable(false);

       Image image = Screen.getRes().getImage("wait2");
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
            if (msisdn.startsWith("+")) {
                msisdn = "%2b" + msisdn.substring(1);
            }
            urlRegisterImei = AppMIDlet.getInstance().getSettings().getStructure().getRegisterIMEIUrl();
            urlRegisterImei += "?imei=" + AppMIDlet.getInstance().getIMEI();
            urlRegisterImei += "&msisdn=" + msisdn;


            try {
                HttpConnection httpConnection = (HttpConnection) Connector.open(urlRegisterImei);
                httpConnection.setRequestMethod(HttpConnection.GET);
                int responseCode = httpConnection.getResponseCode();
                if (responseCode == httpConnection.HTTP_OK) {
                    DataInputStream httpInput = new DataInputStream(httpConnection.openDataInputStream());
                    int resp = httpInput.readInt();
                    if (resp < 0) {
                        GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
                        GeneralAlert.getInstance().show(Resources.ERROR_TITLE,
                                regStatus2Message(resp), GeneralAlert.ERROR);
                        CancelRegisterIMEICommand.getInstance().execute(null);
                    } else {
                        IMEIHandler im = new IMEIHandler();
                        im.registerIMEI();
                        GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
                        GeneralAlert.getInstance().show(Resources.REGISTRATION_DONE, regStatus2Message(resp), GeneralAlert.INFO);
                        AppMIDlet.getInstance().continueAppLoading();
                    }
                } else {
                    exitApp(NetworkErrCode.codeToString(String.valueOf(responseCode)));
                }
            } catch (Exception ex) {
                exitApp(ex.getMessage());
            }
        }

        private String regStatus2Message( int registrationErr ) {
            String message = null;
            switch( registrationErr ) {
                case IMEI_ALREADY_EXISTS:
                    message = Resources.ERROR_TITLE + " " + Resources.IMEI_ALREADY_EXISTS;
                    break;
                case MSISDN_NOT_FOUND:
                    message = Resources.ERROR_TITLE + " " + Resources.MSISDN_NOT_FOUND;
                    break;
                case FAILURE:
                    message = Resources.ERROR_TITLE + " " + Resources.REGISTRATION_FAILURE;
                    break;
                case SUCCESS:
                    message = Resources.APP_REGISTERED;
                    break;
                case MSISDN_ALREADY_REGISTERED:
                    message = Resources.MSISDN_ALREADY_REGISTERED;
                    break;
                default:
                    message = null;
                    break;
            }
            return message;
        }


        private void exitApp( String message ) {
            GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
            GeneralAlert.getInstance().showCodedAlert(Resources.ERROR_TITLE,
                    message.trim() , GeneralAlert.ERROR);
            CancelRegisterIMEICommand.getInstance().execute(null);
        }
    }

    class RegisterIMEIOnShow extends Event {
        protected void doAction(Object parameter) {
            doRegister();
        }
    }
}

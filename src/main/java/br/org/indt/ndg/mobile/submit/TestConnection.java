/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.mobile.submit;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.lwuit.control.Display;
import br.org.indt.ndg.mobile.Resources;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;
import javax.microedition.lcdui.Spacer;
import javax.microedition.lcdui.StringItem;
/**
 *
 * @author Administrador
 */
public class TestConnection extends Form implements CommandListener {
    
    private static TestConnection tc = null;
    private Thread tTestConn = null;
    private Thread tWaitAck = null;
    private TestConnectionRunnable tcr = null;
    private boolean bWaitingForAck = false;
    private boolean bPendingUserConfirmation = false;
    private String strTimeStamp = "";
    private int lastFormCreated = 0;
    private String FormContentText1 = "";
    private String FormContentText2 = "";

    public TestConnection() {
		super("");
        addCommand(Resources.CMD_CANCEL);
        addCommand(Resources.CMD_HIDE);
        setCommandListener(this);
    }

    public static TestConnection getInstance() {
        if (tc == null) {
            tc = new TestConnection();
        }
        return tc;
    }

    public void setLastTimeStamp(String _val) {
        strTimeStamp = _val;
        bWaitingForAck = true;
        CreateForm(Resources.CONNECTION_WAIT_FOR_ACK + "...");

        // Create TimeOut Thread
        tWaitAck = new Thread(new WaitAckRunnable());
        tWaitAck.start();
    }

    public int getLastFormCreated() {
        return lastFormCreated;
    }

    public String getFormContentText1() {
        return FormContentText1;
    }

    public String getFormContentText2() {
        return FormContentText2;
    }

    public void CreateForm(String _value) {
        this.deleteAll();
        this.append(new Spacer(this.getWidth(),25));
        StringItem item = new StringItem("", _value);
        this.append(item);

        this.append(new Spacer(this.getWidth(),25));
        Gauge gauge = new Gauge(null, false, Gauge.INDEFINITE, Gauge.CONTINUOUS_RUNNING);
        this.append(gauge);
        lastFormCreated = 1;
        FormContentText1 = "";
        FormContentText2 = _value;

        try { Thread.sleep(1000); } catch(Exception e){}
        //if (Display.getDisplay(AppMIDlet.getInstance()).getCurrent() == this) {
            AppMIDlet.getInstance().setDisplayable(this);
        //}
    }

    public void CreateForm2(String _header, String _text) {
        this.deleteAll();
        this.append(new Spacer(this.getWidth(),25));
        StringItem item = new StringItem("", _header);
        this.append(item);

        this.append(new Spacer(this.getWidth(),25));
        item = new StringItem("", _text);
        this.append(item);

        bPendingUserConfirmation = true;
        removeCommand(Resources.CMD_CANCEL);
        removeCommand(Resources.CMD_HIDE);
        addCommand(Resources.CMD_OK);
        lastFormCreated = 2;
        FormContentText1 = _header;
        FormContentText2 = _text;

        try { Thread.sleep(1000); } catch(Exception e){}
        if (Display.getDisplay(AppMIDlet.getInstance()).getCurrent() == this) {
            AppMIDlet.getInstance().setDisplayable(this);
        }
    }

    public void doTest() {
        if (!bPendingUserConfirmation) {
            if (bWaitingForAck) {
                CreateForm(Resources.CONNECTION_WAIT_FOR_ACK + "...");
            }
            else {
                CreateForm(Resources.TESTING_CONNECTION);
            }

            //AppMIDlet.getInstance().setDisplayable(this);

            boolean gprs_support = AppMIDlet.getInstance().getSettings().getStructure().getGPRSSupport();
            boolean sms_support = AppMIDlet.getInstance().getSettings().getStructure().getSMSSupport();
            if(!gprs_support && !sms_support){
                AppMIDlet.getInstance().getGeneralAlert().showAlert(Resources.NO_TRANSPORT, Resources.NO_TRANSPORT_SELECTED);
                return;
            }

            tcr = new TestConnectionRunnable();
            if (gprs_support) {
                tcr.setTypeTest(TestConnectionRunnable.TEST_GPRS);
                tTestConn = new Thread(tcr);
                tTestConn.start();
            }
            else if ((sms_support) && (!bWaitingForAck)) {
                tcr.setTypeTest(TestConnectionRunnable.TEST_SMS_FIRST_SEND);
                tTestConn = new Thread(tcr);
                tTestConn.start();
            }
        }
        else {
            AppMIDlet.getInstance().setDisplayable(this);
        }
    }

    public void handleIncomingACKConnectionTest(String imei, String timeStamp) {
        if (bWaitingForAck) {
            // Checking timeStamp
            if (timeStamp.equals(strTimeStamp)) {
                // Sending Ack to server
                tcr = new TestConnectionRunnable();
                tcr.setImeiTimeStamp(imei, timeStamp);
                tcr.setTypeTest(TestConnectionRunnable.TEST_SMS_SECOND_SEND);
                tTestConn = new Thread(tcr);
                tTestConn.start();

                bWaitingForAck = false;
            }
        }
    }

    public void commandAction(Command c, Displayable d) {
        if (c == Resources.CMD_CANCEL) {
            bWaitingForAck = false;
            bPendingUserConfirmation = false;
            tcr.setCanceled(true);
            tTestConn.interrupt();
            if (tWaitAck != null) {
                tWaitAck.interrupt();
            }
            AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getSurveyList());
        }
        else if (c == Resources.CMD_HIDE) {
            AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getSurveyList());
        }
        else if (c == Resources.CMD_OK) {
            bPendingUserConfirmation = false;
            removeCommand(Resources.CMD_OK);
            addCommand(Resources.CMD_CANCEL);
            addCommand(Resources.CMD_HIDE);
            AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getSurveyList());
        }
    }

    public class WaitAckRunnable implements Runnable {
        private static final int ACK_TIMEOUT = 180000; // 3 minutes

        public void run() {
            try {
                Thread.sleep(ACK_TIMEOUT);
                if (bWaitingForAck) {
                    TestConnection.getInstance().CreateForm2(Resources.SMS_LABEL, Resources.CONNECTION_FAILED+ "!");
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

    }

}
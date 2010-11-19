/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.mobile.submit;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.lwuit.ui.TestConnectionNewUI;
import br.org.indt.ndg.mobile.Resources;

/**
 *
 * @author Administrador
 */
public class TestConnection  {
    
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
    private boolean supperessUpdate = false;

    public TestConnection() {
    }

    public static TestConnection getInstance() {
        if (tc == null) {
            tc = new TestConnection();
        }
        return tc;
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
          lastFormCreated = 1;
          FormContentText1 = "";
          FormContentText2 = _value;
    }

    public void UpdateForm(String _header, String _text) {
          bPendingUserConfirmation = true;

          lastFormCreated = 2;
          FormContentText1 = _header;
          FormContentText2 = _text;

          if( !supperessUpdate ) {
              AppMIDlet.getInstance().setDisplayable(TestConnectionNewUI.class);
          }
    }

    public void doTest() {
        this.supperessUpdate = false;
        if (!bPendingUserConfirmation) {
            if (bWaitingForAck) {
                CreateForm(Resources.CONNECTION_WAIT_FOR_ACK + "...");
            }
            else {
                CreateForm(Resources.TESTING_CONNECTION);
            }


            tcr = new TestConnectionRunnable();
            tTestConn = new Thread(tcr);
            tTestConn.start();
        }
        AppMIDlet.getInstance().setDisplayable(TestConnectionNewUI.class );
    }

    public void handleIncomingACKConnectionTest(String imei, String timeStamp) {
        if (bWaitingForAck) {
            // Checking timeStamp
            if (timeStamp.equals(strTimeStamp)) {
                // Sending Ack to server
                tcr = new TestConnectionRunnable();
                tTestConn = new Thread(tcr);
                tTestConn.start();

                bWaitingForAck = false;
            }
        }
    }

    public void UserConfirmation() {
        bPendingUserConfirmation = false;
    }

    public void cancel() {
        bWaitingForAck = false;
        bPendingUserConfirmation = false;
        tcr.setCanceled(true);
        tTestConn.interrupt();
        if (tWaitAck != null) {
            tWaitAck.interrupt();
        }
    }

    public void suppressUpdate()
    {
        this.supperessUpdate = true;
    }
    
}
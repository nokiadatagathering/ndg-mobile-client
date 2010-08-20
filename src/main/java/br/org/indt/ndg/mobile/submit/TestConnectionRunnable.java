package br.org.indt.ndg.mobile.submit;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.logging.Logger;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.wireless.messaging.*;

public class TestConnectionRunnable implements Runnable {
    public static final int TEST_GPRS = 1;
    public static final int TEST_SMS_FIRST_SEND = 2;
    public static final int TEST_SMS_SECOND_SEND = 3;

    private boolean isCanceled = false;
    private int phoneFormat = 1;
    private String strImei = "";
    private String strTimeStamp = "";
    private int typeTest = 0;

    public void run() {
        if (typeTest == TEST_GPRS) {
            testGPRS();
        }
        else {
            testSMS();
        }
    }

    public void setCanceled(boolean _val) {
        isCanceled = _val;
    }

    public void setTypeTest(int _val) {
        typeTest = _val;
    }

    private void testGPRS() {
        String urlServlet = AppMIDlet.getInstance().getSettings().getStructure().getServerUrl();
        HttpConnection httpConn = null;
        try {
            httpConn = (HttpConnection) Connector.open(urlServlet);
            httpConn.setRequestMethod(HttpConnection.GET);
            int responseCode = httpConn.getResponseCode();
            //int responseCode = httpConn.HTTP_OK;
            if (!isCanceled) {
                if (responseCode == httpConn.HTTP_OK) {
                    TestConnection.getInstance().CreateForm2(Resources.GPRS_LABEL, Resources.CONNECTION_OK + "!");
                }
                else {
                    TestConnection.getInstance().CreateForm2(Resources.GPRS_LABEL, Resources.CONNECTION_FAILED+ "!");
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            if (!isCanceled) {
                TestConnection.getInstance().CreateForm2(Resources.GPRS_LABEL, Resources.CONNECTION_FAILED+ "!");
            }
        }
    }

    public void setImeiTimeStamp(String imei, String timeStamp) {
        strImei = imei;
        strTimeStamp = timeStamp;
    }

    private void testSMS() {
        try {
            phoneFormat = 1;
            if (typeTest == TEST_SMS_FIRST_SEND)
                sendSMS();
            else
                sendSMSAck();
        }
        catch (IOException t) {
            Logger.getInstance().logException("IOException (Level 1)");
            try {
                phoneFormat = 2;
                if (typeTest == TEST_SMS_FIRST_SEND)
                    sendSMS();
                else
                    sendSMSAck();
            }
            catch (IOException t2) {
               Logger.getInstance().logException("IOException (Level 2)");
               try {
                   phoneFormat = 3;
                   if (typeTest == TEST_SMS_FIRST_SEND)
                       sendSMS();
                   else
                       sendSMSAck();
                }
                catch (IOException t3) {
                    Logger.getInstance().logException("IOException (Level 3)");
                    AppMIDlet.getInstance().getGeneralAlert().showAlert(Resources.NETWORK_FAILURE, Resources.TRY_AGAIN_LATER);
                    Logger.getInstance().logException("Network Failure: " + t3.getMessage());
                }
            }
        }

        // Connection OK via SMS
        if (typeTest == TEST_SMS_SECOND_SEND) {
            TestConnection.getInstance().CreateForm2(Resources.SMS_LABEL, Resources.CONNECTION_OK + "!");
        }
    }

    private void sendSMS() throws IOException {
        // Montar número de telefone
        String phoneNumber = "";
        if (phoneFormat == 1) {
            phoneNumber = AppMIDlet.getInstance().getSettings().getStructure().getCountryCode() +
                             AppMIDlet.getInstance().getSettings().getStructure().getAreaCode() +
                             AppMIDlet.getInstance().getSettings().getStructure().getPhoneNumber();
        }
        else if (phoneFormat == 2) {
            phoneNumber =  AppMIDlet.getInstance().getSettings().getStructure().getAreaCode() +
                           AppMIDlet.getInstance().getSettings().getStructure().getPhoneNumber();
        }
        else {
            phoneNumber =  AppMIDlet.getInstance().getSettings().getStructure().getPhoneNumber();
        }

        String address = "sms://" + phoneNumber + ":" + AppMIDlet.getInstance().getSettings().getStructure().getSendingPort();

        //Montar mensagem de texto
        String strTimeStamp2 = "" + System.currentTimeMillis();
        String strSMS = "";
        strSMS += "5i" + AppMIDlet.getInstance().getIMEI();
        strSMS += "t" + strTimeStamp2;

        //Montar e enviar sms
        MessageConnection smsconn = null;
        TextMessage textMessage = null;
        try {
            smsconn = (MessageConnection) Connector.open(address);
            textMessage = (TextMessage)smsconn.newMessage(MessageConnection.TEXT_MESSAGE);
            textMessage.setPayloadText(strSMS);
            smsconn.send(textMessage);
        } catch (SecurityException ex) {
            Logger.getInstance().logException("SecurityException:" + ex.getMessage());
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        
        finally{
            TestConnection.getInstance().setLastTimeStamp(strTimeStamp2);
            textMessage = null;
            if (smsconn != null) {
                smsconn.close();
            }
        }
    }

    private void sendSMSAck() {
        // Montar número de telefone
        String phoneNumber = "";
        if (phoneFormat == 1) {
            phoneNumber = AppMIDlet.getInstance().getSettings().getStructure().getCountryCode() +
                             AppMIDlet.getInstance().getSettings().getStructure().getAreaCode() +
                             AppMIDlet.getInstance().getSettings().getStructure().getPhoneNumber();
        }
        else if (phoneFormat == 2) {
            phoneNumber =  AppMIDlet.getInstance().getSettings().getStructure().getAreaCode() +
                           AppMIDlet.getInstance().getSettings().getStructure().getPhoneNumber();
        }
        else {
            phoneNumber =  AppMIDlet.getInstance().getSettings().getStructure().getPhoneNumber();
        }
        String address = "sms://" + phoneNumber + ":" + AppMIDlet.getInstance().getSettings().getStructure().getSendingPort();

        //Montar mensagem de texto
        String strSMS = "";
        strSMS += "7i" + strImei;
        strSMS += "t" + strTimeStamp;

        //Montar e enviar sms
        MessageConnection smsconn = null;
        TextMessage textMessage = null;
        try {
            smsconn = (MessageConnection) Connector.open(address);
            textMessage = (TextMessage)smsconn.newMessage(MessageConnection.TEXT_MESSAGE);
            textMessage.setPayloadText(strSMS);
            smsconn.send(textMessage);
        } catch (SecurityException ex) {
            Logger.getInstance().logException("SecurityException: " + ex.getMessage());
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        finally{
            textMessage = null;
            if (smsconn != null) {
                try {
                    smsconn.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}

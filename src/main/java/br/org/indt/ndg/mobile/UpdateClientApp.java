/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.mobile;

import br.org.indt.ndg.mobile.logging.Logger;
import java.io.DataInputStream;
import java.io.IOException;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Spacer;
import javax.microedition.lcdui.StringItem;

public class UpdateClientApp extends Form implements CommandListener {

    private static UpdateClientApp uc = null;
    private String FormContentText1 = "";
    private String FormContentText2 = "";
    Thread t = null;
    private boolean isCanceled = false;
    private byte currentStep = '0';
    private final byte stepCheckVersion = '1';
    private final byte stepUpdateVersion = '2';

    public UpdateClientApp() {
        super("");
        setCommandListener(this);
    }

    public static UpdateClientApp getInstance() {
        if (uc == null) {
            uc = new UpdateClientApp();
        }
        return uc;
    }

    private void CreateForm(String _header, String _text) {
        this.deleteAll();
        this.append(new Spacer(this.getWidth(),25));
        StringItem item = new StringItem("", _header);
        this.append(item);

        this.append(new Spacer(this.getWidth(),25));
        item = new StringItem("", _text);
        this.append(item);

        addCommand(Resources.CMD_CANCEL);
        FormContentText1 = _header;
        FormContentText2 = _text;

        AppMIDlet.getInstance().setDisplayable(this);

        try { Thread.sleep(1000); } catch(Exception e){}
    }

    public void CheckForUpdate() {
        CreateForm("Check for Update", Resources.CONNECTING);
        currentStep = stepCheckVersion;
        UpdateClientAppRunnable ucr = new UpdateClientAppRunnable();
        t = new Thread(ucr);
        t.start();
    }

    public void commandAction(Command c, Displayable d) {
        if (c == Resources.CMD_CANCEL) {
            isCanceled = true;
            t.interrupt();
            AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getSurveyList());
        }
    }

    public String getFormContentText1() {
        return FormContentText1;
    }

    public String getFormContentText2() {
        return FormContentText2;
    }

    public void handleConfirmResult(Command _cmd) {
        if (_cmd == Resources.CMD_YES) {
            currentStep = stepUpdateVersion;
            UpdateClientAppRunnable ucr = new UpdateClientAppRunnable();
            t = new Thread(ucr);
            t.start();
        }
        else if (_cmd == Resources.CMD_NO) {
            AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getSurveyList());
        }
    }

    public class UpdateClientAppRunnable implements Runnable {
        private static final int MAX_DL_SIZE = 1024;
        HttpConnection httpConnection = null;
        public void run() {
            String urlServlet;
            if (currentStep == stepCheckVersion) {
                urlServlet = AppMIDlet.getInstance().getSettings().getStructure().getUpdateCheckURL() + "?do=currentVersion";
            }
            else {
                urlServlet = AppMIDlet.getInstance().getSettings().getStructure().getUpdateCheckURL() + "?do=updateMyClient";
            }
            try {
                httpConnection = (HttpConnection) Connector.open(urlServlet);
                httpConnection.setRequestMethod(HttpConnection.GET);
                int responseCode = httpConnection.getResponseCode();
                if (!isCanceled) {
                    if (responseCode == httpConnection.HTTP_OK) {
                        if (currentStep == stepCheckVersion) {
                            // read last client version from Server
                            downloadLastVersionInfo();
                        }
                    }
                    else if (responseCode == httpConnection.HTTP_MOVED_TEMP) {
                        String strNewUrl = httpConnection.getHeaderField("Location");
                        try {
                            AppMIDlet.getInstance().platformRequest(strNewUrl);
                        } catch (ConnectionNotFoundException ex) {
                            Logger.getInstance().logException(ex.getMessage() + " : " + strNewUrl);
                        }
                    }
                    else {
                        // Server Connection Error
                        AppMIDlet.getInstance().getGeneralAlert().showAlert(Resources.CHECK_FOR_UPDATE_TITLE, Resources.EDOWNLOAD_FAILED_HTTP_CODE + ": " + responseCode, "", AlertType.ERROR);
                    }
                }

            } catch (IOException ex) {
                if (!isCanceled) {
                    // Server Connection Error
                    AppMIDlet.getInstance().getGeneralAlert().showAlert(Resources.CHECK_FOR_UPDATE_TITLE, Resources.EDOWNLOAD_FAILED_HTTP_CODE + ": " + ex.getMessage(), "", AlertType.ERROR);
                }
            }
            finally {
                try { if (httpConnection != null) httpConnection.close(); } catch (IOException ex) {}
                AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getSurveyList());
            }
        }

        private void downloadLastVersionInfo() {
            DataInputStream httpInput = null;
            byte[] buffer = new byte[MAX_DL_SIZE];
            int bytesToRead = 0, bytesRead = 0;
            
            bytesToRead = (int) httpConnection.getLength();
            if (bytesToRead <= 0) {
                isCanceled = true;
                Logger.getInstance().log(Resources.EDOWNLOAD_FAILED_INVALID_DATA);
                AppMIDlet.getInstance().getGeneralAlert().showAlert(Resources.CHECK_FOR_UPDATE_TITLE, Resources.EDOWNLOAD_FAILED_INVALID_DATA, "", AlertType.ERROR);
                return;
            }

            try {
                httpInput = new DataInputStream(httpConnection.openDataInputStream());
                bytesRead = httpInput.read(buffer, 0, bytesToRead);
                if (bytesRead == bytesToRead) {
                   String strVersion = new String(buffer, 0, bytesRead);
                   int iVersion = VersionStrToInt(strVersion);
                   int iVersionApp = VersionStrToInt(AppMIDlet.getInstance().getSettings().getStructure().getAppVersion());
                   if (iVersionApp >= iVersion) {
                       AppMIDlet.getInstance().getGeneralAlert().showAlert(Resources.CHECK_FOR_UPDATE_TITLE, Resources.NDG_UPDATED, "UpdateClientApp", AlertType.INFO);
                       AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getSurveyList());
                   }
                   else {
                       AppMIDlet.getInstance().getGeneralAlert().showConfirmAlert(Resources.CHECK_FOR_UPDATE_TITLE, Resources.NDG_NOT_UPDATED, "UpdateClientApp");
                   }
                }
            } catch (IOException ex) {
                Logger.getInstance().logException(Resources.EDOWNLOAD_FAILED_ERROR_CODE + ": " + ex.getMessage());
                AppMIDlet.getInstance().getGeneralAlert().showAlert(Resources.CHECK_FOR_UPDATE_TITLE, Resources.EDOWNLOAD_FAILED_ERROR_CODE + ": " + ex.getMessage(), "", AlertType.ERROR);
            }
            finally {
                try { if (httpInput != null) httpInput.close(); } catch (IOException ex) {}
            }
        }

        private int VersionStrToInt(String version) {
            String strTemp = "";
            for (int i=0; i < version.length(); i++) {
               if (version.charAt(i) != '.') {
                   strTemp = strTemp + version.charAt(i);
               }
            }
            return Integer.parseInt(strTemp);
        }

    }

}

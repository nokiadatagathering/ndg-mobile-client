/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.mobile;

import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import br.org.indt.ndg.mobile.logging.Logger;
import java.io.DataInputStream;
import br.org.indt.ndg.lwuit.ui.SurveyList;
import java.io.IOException;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

public class UpdateClientApp {

    private static UpdateClientApp uc = null;
    Thread t = null;
    private boolean isCanceled = false;
    private byte currentStep = '0';
    private final byte stepCheckVersion = '1';
    private final byte stepUpdateVersion = '2';

    private UpdateClientApp() {
    }

    public static UpdateClientApp getInstance() {
        if (uc == null) {
            uc = new UpdateClientApp();
        }
        return uc;
    }

    public void CheckForUpdate() {
        currentStep = stepCheckVersion;
        UpdateClientAppRunnable ucr = new UpdateClientAppRunnable();
        t = new Thread(ucr);
        t.start();
    }

    public void Cancel() {
        isCanceled = true;
        t.interrupt();
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
                        GeneralAlert.getInstance().addCommand( GeneralAlert.DIALOG_OK, true );
                        GeneralAlert.getInstance().showCodedAlert(Resources.CHECK_FOR_UPDATE_TITLE,
                                String.valueOf(responseCode), GeneralAlert.ERROR );
                    }
                }

            } catch (IOException ex) {
                if (!isCanceled) {
                    // Server Connection Error
                    GeneralAlert.getInstance().addCommand( GeneralAlert.DIALOG_OK, true );
                    try{GeneralAlert.getInstance().showCodedAlert(Resources.CHECK_FOR_UPDATE_TITLE,
                        ex.getMessage(), GeneralAlert.ERROR );} catch(Exception ex1){/*workaround mostlikely LWUIT problem*/}
                }
            }
            catch (SecurityException ex) {
            }
            catch ( IllegalArgumentException ex )
            {
                if (!isCanceled) {
                    // Server Connection Error
                    GeneralAlert.getInstance().addCommand( GeneralAlert.DIALOG_OK, true );
                    GeneralAlert.getInstance().showCodedAlert(Resources.CHECK_FOR_UPDATE_TITLE,
                                                              ex.getMessage(), GeneralAlert.ERROR );
                }
            }
            finally {
                try { if (httpConnection != null) httpConnection.close(); } catch (IOException ex) {}
                isCanceled = false;
                AppMIDlet.getInstance().setDisplayable(SurveyList.class);
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
                GeneralAlert.getInstance().addCommand( GeneralAlert.DIALOG_OK, true );
                GeneralAlert.getInstance().show(Resources.CHECK_FOR_UPDATE_TITLE, Resources.EDOWNLOAD_FAILED_INVALID_DATA, GeneralAlert.ERROR );
                AppMIDlet.getInstance().setDisplayable(SurveyList.class );
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
                       
                       GeneralAlert.getInstance().addCommand( GeneralAlert.DIALOG_OK, true);
                       GeneralAlert.getInstance().show(Resources.CHECK_FOR_UPDATE_TITLE, Resources.NDG_UPDATED, GeneralAlert.INFO );
                       AppMIDlet.getInstance().setDisplayable(SurveyList.class);
                   }
                   else {
                       GeneralAlert.getInstance().addCommand( GeneralAlert.DIALOG_YES_NO, true );
                       if( GeneralAlert.RESULT_YES ==  GeneralAlert.getInstance().show(Resources.CHECK_FOR_UPDATE_TITLE, Resources.NDG_NOT_UPDATED, GeneralAlert.CONFIRMATION) ) {
                           currentStep = stepUpdateVersion;
                           UpdateClientAppRunnable ucr = new UpdateClientAppRunnable();
                           t = new Thread(ucr);
                           t.start();
                       } else {
                           AppMIDlet.getInstance().setDisplayable(SurveyList.class);
                       }
                   }
                }
            } catch (IOException ex) {
                Logger.getInstance().logException(Resources.EDOWNLOAD_FAILED_ERROR_CODE + ": " + ex.getMessage());
                isCanceled = false;
                GeneralAlert.getInstance().addCommand( GeneralAlert.DIALOG_OK, true );
                GeneralAlert.getInstance().showCodedAlert(Resources.CHECK_FOR_UPDATE_TITLE,
                                                          ex.getMessage(), GeneralAlert.ERROR );
                AppMIDlet.getInstance().setDisplayable(SurveyList.class);

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

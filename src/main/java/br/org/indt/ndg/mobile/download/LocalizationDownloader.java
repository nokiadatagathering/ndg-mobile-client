package br.org.indt.ndg.mobile.download;

import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import br.org.indt.ndg.lwuit.ui.WaitingScreen;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.NdgConsts;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.Utils;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.file.FileConnection;


public class LocalizationDownloader implements Runnable {

    private static final String FILE_TYPE_TEXTS = "Texts";
    private static final String FILE_TYPE_FONTS = "Fonts";
    private static final String FILE_TYPE_HEADER = "File-Type";
    private static final String LOCALE_HEADER = "locale";

    private HttpConnection httpConnection;
    private String urlAck;
    private String locale;
    private static int MTU = 1024;
    private LocalizationDownloaderListener listener = null;

    public LocalizationDownloader(LocalizationDownloaderListener listener) {
        this.listener = listener;
        updateRequestUrls();
    }

    private void updateRequestUrls() {
        String urlBase = AppMIDlet.getInstance().getSettings().getStructure().getLocalizationServingURL();
        urlAck = urlBase + "?do=ack&imei=" + AppMIDlet.getInstance().getIMEI();
    }

    public void downloadLocale(String locale){
        WaitingScreen.show(Resources.CONNECTING);
        this.locale = locale;
        Thread thisThread = new Thread(this);
        thisThread.setPriority( Thread.MIN_PRIORITY );
        thisThread.start();
    }

    public void run() {
        try { Thread.sleep(200); } catch(Exception e){}
        if(getViaServlet(urlAck, FILE_TYPE_TEXTS)){
            getViaServlet(urlAck, FILE_TYPE_FONTS);
            WaitingScreen.dispose();
            listener.localizationDowonloadFinished(locale);
        }else{
            WaitingScreen.dispose();
            listener.downloadFailed();
        }
        
    }

    public boolean getViaServlet(String url, String type) throws SecurityException {
        HttpConnection hc = null;
        InputStream is = null;
        FileConnection fc = null;
        DataOutputStream dos = null;

        String shortLocale = locale.substring(0, 2);

        boolean downloaded = false;

        try { 

            if(!Utils.fileExists(AppMIDlet.getInstance().getRootDir() + NdgConsts.LANGUAGE_DIR)){
                Utils.createDirectory(AppMIDlet.getInstance().getRootDir() + NdgConsts.LANGUAGE_DIR);
            }

            hc = (HttpConnection) Connector.open(url);
            hc.setRequestMethod(HttpConnection.GET);
            hc.setRequestProperty(LOCALE_HEADER, shortLocale);
            hc.setRequestProperty(FILE_TYPE_HEADER, type);

            is = hc.openInputStream();

            if (hc.getResponseCode() == HttpConnection.HTTP_OK) {
                String filePath = "";
                if(type.equals(FILE_TYPE_FONTS)){
                    filePath = AppMIDlet.getInstance().getRootDir() + NdgConsts.LANGUAGE_DIR + NdgConsts.FONTS_FILE_NAME + shortLocale + NdgConsts.RES_EXTENSION;
                }else{
                    filePath = Utils.prepereMessagesPath(locale);
                }

                fc = (FileConnection) Connector.open( filePath, Connector.READ_WRITE);
                if (!fc.exists()) {
                    fc.create();
                } else {
                    fc.truncate(0);
                    //check to do partial download or
                    //check to write over file with yes/no dialog
                }
                dos = fc.openDataOutputStream();

                int bytesread = 0;
                byte[] databyte = new byte[MTU];

                while ( (bytesread = is.read(databyte, 0, MTU)) != -1) {
                    dos.write(databyte, 0, bytesread);
                }
                if (bytesread == -1) {
                    downloaded = true;
                }
            }
        } catch (IOException ioe) {
        } finally {
            try {
                if (dos != null) {
                    dos.close();
                }
                if (fc != null) {
                    fc.close();
                }
                if (is != null) {
                    is.close();
                }
                if (hc != null) {
                    hc.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return downloaded;
    }
}

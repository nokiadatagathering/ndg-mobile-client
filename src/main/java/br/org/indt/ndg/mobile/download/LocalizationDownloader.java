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
import java.util.Hashtable;
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
        if(downloadFile(urlAck, FILE_TYPE_TEXTS)){
            downloadFile(urlAck, FILE_TYPE_FONTS);
            WaitingScreen.dispose();
            listener.localizationDowonloadFinished(locale);
        }else{
            WaitingScreen.dispose();
            listener.downloadFailed();
        }
        
    }

    private boolean downloadFile(String url, String type){

        if(!Utils.fileExists(AppMIDlet.getInstance().getRootDir() + NdgConsts.LANGUAGE_DIR)){
            Utils.createDirectory(AppMIDlet.getInstance().getRootDir() + NdgConsts.LANGUAGE_DIR);
        }

        String shortLocale = locale.substring(0, 2);
        String filePath = "";
        if(type.equals(FILE_TYPE_FONTS)){
            filePath = AppMIDlet.getInstance().getRootDir() + NdgConsts.LANGUAGE_DIR + NdgConsts.FONTS_FILE_NAME + shortLocale + NdgConsts.RES_EXTENSION;
        }else{
            filePath = Utils.prepereMessagesPath(locale);
        }

        FileConnection fileConn = null;
        DataOutputStream dataOutputStream = null;
        boolean downloaded = false;
        try {
            fileConn = (FileConnection) Connector.open( filePath, Connector.READ_WRITE);
            if (!fileConn.exists()) {
                fileConn.create();
            } else {
                fileConn.truncate(0);
                //check to do partial download or
                //check to write over file with yes/no dialog
            }
            dataOutputStream = fileConn.openDataOutputStream();

            Hashtable headers = new Hashtable();
            headers.put(LOCALE_HEADER, shortLocale);
            headers.put(FILE_TYPE_HEADER, type);

            downloaded = DownloadUtils.getViaServlet(url, headers, dataOutputStream);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        finally{
            try{
                if(dataOutputStream != null){
                    dataOutputStream.close();
                }
                if(fileConn != null){
                    fileConn.close();
                }
                if(!downloaded){
                    Utils.removeFile(filePath);
                }
            }catch(IOException ex){}
        }

        return downloaded;
    }
}

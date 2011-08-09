package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.download.LocalizationDownloader;
import br.org.indt.ndg.mobile.download.LocalizationDownloaderListener;
import com.sun.lwuit.Command;

/**
 *
 * @author damian.janicki
 */
public class LanguageUpdateCommand extends CommandControl{
    private static LanguageUpdateCommand instance = null;

    public static LanguageUpdateCommand getInstance(){
        if(instance == null){
            instance = new LanguageUpdateCommand();
        }
        return instance;
    }

    protected Command createCommand() {
        return new Command(Resources.UPDATE);
    }

    protected void doAction(Object parameter) {
        Object[] params = (Object[])parameter;
        if( params != null &&
            params[0] instanceof LocalizationDownloaderListener &&
            params[1] instanceof String
            )
        {
            LocalizationDownloader downloader = new LocalizationDownloader((LocalizationDownloaderListener)params[0]);
            downloader.downloadLocale((String)params[1]);
        }
    }

}

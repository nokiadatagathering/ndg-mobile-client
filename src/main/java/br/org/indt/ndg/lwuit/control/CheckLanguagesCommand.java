package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.download.LangListDownloaderListener;
import br.org.indt.ndg.mobile.download.LanguageListDownloader;
import com.sun.lwuit.Command;

/**
 *
 * @author damian.janicki
 */
public class CheckLanguagesCommand extends CommandControl{

    private static CheckLanguagesCommand instance;

    protected Command createCommand() {
        return new Command(Resources.CHECK);
    }

    public static CheckLanguagesCommand getInstance() {
        if (instance == null)
            instance = new CheckLanguagesCommand();
        return instance;
    }

    protected void doAction(Object parameter) {
        LangListDownloaderListener listener = (LangListDownloaderListener)parameter;
        LanguageListDownloader downloader = new LanguageListDownloader(listener);
        downloader.getLanguageList();
    }
    
}

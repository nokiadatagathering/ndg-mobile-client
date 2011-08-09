package br.org.indt.ndg.mobile.download;

/**
 *
 * @author damian.janicki
 */
public interface LocalizationDownloaderListener {
    void localizationDowonloadFinished(String locale);
    void downloadFailed();
}

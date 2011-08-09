package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.FileBrowserView;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

/**
 *
 * @author amartini
 */
public class BackPreviewLoadedFile extends CommandControl{

    private static BackPreviewLoadedFile instance;

    private BackPreviewLoadedFile(){}

    public static BackPreviewLoadedFile getInstance(){
        if(instance == null)
            instance = new BackPreviewLoadedFile();
        return instance;
    }

    protected Command createCommand() {
        return new Command( Resources.NEWUI_BACK );
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().setDisplayable(FileBrowserView.class);
    }

}

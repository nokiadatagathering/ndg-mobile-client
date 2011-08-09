package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.FileBrowserView;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

public class OpenFileBrowserCommand extends CommandControl{

    private static OpenFileBrowserCommand instance;

    private OpenFileBrowserCommand(){}

    public static OpenFileBrowserCommand getInstance(){
        if(instance == null)
            instance = new OpenFileBrowserCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command( Resources.LOAD_FROM_FILE );
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().setDisplayable(FileBrowserView.class);
    }
}

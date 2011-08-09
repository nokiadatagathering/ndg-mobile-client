package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.SelectLangScreen;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

/**
 *
 * @author damian.janicki
 */
public class OpenLangScreenCommand extends CommandControl{
    
    private static OpenLangScreenCommand instance;

    protected Command createCommand() {
        return new Command(Resources.LANGUAGE);
    }

    public static OpenLangScreenCommand getInstance() {
        if (instance == null)
            instance = new OpenLangScreenCommand();
        return instance;
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().setDisplayable(SelectLangScreen.class); 
    }
}

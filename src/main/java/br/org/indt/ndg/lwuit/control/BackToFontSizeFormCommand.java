package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.UISettingsFontSize;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

/**
 *
 * @author damian.janicki
 */
public class BackToFontSizeFormCommand extends BackCommand{
    private static BackToFontSizeFormCommand instance = null;

    public static BackToFontSizeFormCommand getInstance(){
        if(instance == null){
            instance = new BackToFontSizeFormCommand();
        }
        return instance;
    }
    protected Command createCommand() {
        return new Command( Resources.NEWUI_BACK );
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().setDisplayable( UISettingsFontSize.class );
    }
}

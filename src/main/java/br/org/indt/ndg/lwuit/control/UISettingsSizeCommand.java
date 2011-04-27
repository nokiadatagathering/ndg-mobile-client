package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.UISettingsFontSize;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;



public class UISettingsSizeCommand extends CommandControl {

    private static  UISettingsSizeCommand instance;

    protected Command createCommand() {
        return new Command( Resources.CMD_FONT_SIZES );
    }

    public static UISettingsSizeCommand getInstance() {
        if (instance == null)
            instance = new  UISettingsSizeCommand();
        return instance;
    }


    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().setDisplayable(UISettingsFontSize.class);
    }
}

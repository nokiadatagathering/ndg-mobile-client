package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.SelectDateFormatForm;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

/**
 *
 * @author damian.janicki
 */
public class SelectDateFormatCommand extends CommandControl{

    private static SelectDateFormatCommand instance;

    public static SelectDateFormatCommand getInstance(){
        if(instance == null){
            instance = new SelectDateFormatCommand();
        }
        return instance;
    }

    protected Command createCommand() {
        return new Command( Resources.DATE_FORMAT );
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().setDisplayable(SelectDateFormatForm.class);
    }
}

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.ResultList;
import com.sun.lwuit.Command;

/**
 *
 * @author mluz
 */
public class BackResultViewCommand extends BackCommand {

    private static BackResultViewCommand instance;

    protected Command createCommand() {
        return new Command(Resources.NEWUI_BACK);
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().getFileStores().resetQuestions();
        AppMIDlet.getInstance().getFileStores().resetResultStructure();
        AppMIDlet.getInstance().setResultList( new ResultList());
        AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.ResultList.class);
    }

    public static BackResultViewCommand getInstance() {
        if (instance == null)
            instance = new BackResultViewCommand();
        return instance;
    }
}

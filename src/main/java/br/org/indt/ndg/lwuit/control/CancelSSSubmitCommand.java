package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;
import br.org.indt.ndg.mobile.ResultList;

public class CancelSSSubmitCommand extends BackCommand {

    private static CancelSSSubmitCommand instance;

    public static CancelSSSubmitCommand getInstance() {
        if (instance == null)
            instance = new CancelSSSubmitCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command(Resources.NEWUI_CANCEL);
    }

    protected void doAction(Object parameter) {
         AppMIDlet.getInstance().getSubmitServer().cancel();
         AppMIDlet.getInstance().setResultList(new ResultList());  //updated result list
         AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.ResultList.class);
    }
}
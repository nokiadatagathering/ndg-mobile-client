package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.Screen;
import br.org.indt.ndg.lwuit.ui.SentResultList;
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
    private Screen m_returnScreen = null; // screen that will be shown with 'Back' command

    protected Command createCommand() {
        return new Command(Resources.NEWUI_BACK);
    }

    protected void doAction(Object parameter) {
        if ( m_returnScreen != null && m_returnScreen.getClass() == SentResultList.class ) {
            m_returnScreen = null;
            ViewSentResultsCommand.getInstance().registerToBeExecutedAsBackCommand();
            ViewSentResultsCommand.getInstance().execute(null);
        } else { // default
            AppMIDlet.getInstance().getFileStores().resetQuestions();
            AppMIDlet.getInstance().getFileStores().resetResultStructure();
            AppMIDlet.getInstance().setResultList( new ResultList());
            AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.ResultList.class);
        }
    }

    public static BackResultViewCommand getInstance() {
        if (instance == null)
            instance = new BackResultViewCommand();
        return instance;
    }

    /**
     * Sets a screen that will be used when Back is pressed. Only screens implicitly
     * listed in doAction method will be used
     * @param returnScreen
     */
    public void setReturnScreen( Screen returnScreen ) {
        m_returnScreen = returnScreen;
    }
}

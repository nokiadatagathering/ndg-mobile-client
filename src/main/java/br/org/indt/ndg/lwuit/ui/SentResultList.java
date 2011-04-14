package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.BackResultViewCommand;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.lwuit.control.BackSentResultListCommand;
import br.org.indt.ndg.lwuit.control.DeleteSentResultCommand;
import br.org.indt.ndg.lwuit.control.MarkAllResultsCommand;
import br.org.indt.ndg.lwuit.control.MoveToUnsentCommand;
import br.org.indt.ndg.lwuit.control.ResultControl;
import br.org.indt.ndg.lwuit.control.SurveysControl;
import br.org.indt.ndg.lwuit.control.UnmarkAllResultsCommand;
import br.org.indt.ndg.lwuit.control.ViewResultCommand;
import br.org.indt.ndg.lwuit.model.CheckableListModel;
import br.org.indt.ndg.lwuit.model.Result;
import br.org.indt.ndg.lwuit.ui.renderers.SentResultListCellRenderer;
import br.org.indt.ndg.mobile.FileSystem;
import com.sun.lwuit.List;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;


/**
 *
 * @author Alexandre Martini
 */
public class SentResultList extends Screen implements ActionListener {

    private Result[] results;
    private String path = Resources.SENT_LIST_TITLE;
    private String title;

    private List list;
    private CheckableListModel underlyingModel;

    protected void loadData() {
        AppMIDlet.getInstance().getFileSystem().useResults(FileSystem.USE_SENT_RESULTS);
        title = SurveysControl.getInstance().getSurvey().getDisplayableName();
        results = ResultControl.getInstance().getSentResults();
    }

    protected void customize() {
        setTitle(title, path);
        if (list != null)
            form.removeComponent(list);
        try {
            form.removeCommandListener(this);
        } catch (NullPointerException npe ) {
            //during first initialisation remove throws exception.
            //this ensure that we have registered listener once
        }
        form.addCommandListener(this);

        underlyingModel = new CheckableListModel(results, CheckableListModel.ALL_ITEMS_CHECKABLE);

        list = new List(underlyingModel);
        list.setItemGap(0);
        list.addActionListener(this);
        list.setListCellRenderer(new SentResultListCellRenderer());

        list.setFixedSelection(List.FIXED_NONE);
        form.addComponent(list);
        form.setScrollable(false);
        list.repaint();
        updateCommands();
    }

    public void actionPerformed(ActionEvent evt) {
        Object cmd = evt.getSource();
        if (cmd == MoveToUnsentCommand.getInstance().getCommand()) {
            MoveToUnsentCommand.getInstance().execute(underlyingModel);
        } else if (cmd == MarkAllResultsCommand.getInstance().getCommand()) {
            MarkAllResultsCommand.getInstance().execute(underlyingModel);
        } else if (cmd == UnmarkAllResultsCommand.getInstance().getCommand()) {
            UnmarkAllResultsCommand.getInstance().execute(underlyingModel);
        } else if (cmd == DeleteSentResultCommand.getInstance().getCommand()) {
            DeleteSentResultCommand.getInstance().execute(underlyingModel);
        } else if (cmd == BackSentResultListCommand.getInstance().getCommand()) {
            BackSentResultListCommand.getInstance().execute(this);
        } else if (cmd == ViewResultCommand.getInstance().getCommand()) {
            BackResultViewCommand.getInstance().setReturnScreen(this);
            ViewResultCommand.getInstance().execute(new Integer(getSelectedResult()));
        } else if(cmd == list){
            //CREATE COMMAND FOR THIS ACTION
            underlyingModel.updateCheckbox(getSelectedResult());
        } else {
            throw new IllegalArgumentException("Invalid command");
        }
        updateCommands();
    }

    private int getSelectedResult() {
        return list.getSelectedIndex();
    }

    private void updateCommands() {
        form.removeAllCommands();
        form.addCommand(BackSentResultListCommand.getInstance().getCommand());
        if ( results.length > 0 ){
            form.addCommand(UnmarkAllResultsCommand.getInstance().getCommand());
            form.addCommand(MarkAllResultsCommand.getInstance().getCommand());
            if ( underlyingModel.getCheckedCount() > 0 ) { // show only if at least one selected
                form.addCommand(DeleteSentResultCommand.getInstance().getCommand());
                form.addCommand(MoveToUnsentCommand.getInstance().getCommand());
            }
            form.addCommand(ViewResultCommand.getInstance().getCommand());
        }
    }
}

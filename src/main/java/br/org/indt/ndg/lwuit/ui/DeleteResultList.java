/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.DeleteList;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.lwuit.control.BackDeleteResultListCommand;
import br.org.indt.ndg.lwuit.control.DeleteResultNowCommand;
import br.org.indt.ndg.lwuit.control.MarkAllResultsCommand;
import br.org.indt.ndg.lwuit.control.ResultControl;
import br.org.indt.ndg.lwuit.control.UnmarkAllResultsCommand;
import br.org.indt.ndg.lwuit.model.Result;
import com.sun.lwuit.List;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.list.DefaultListModel;
import com.sun.lwuit.list.ListModel;

/**
 *
 * @author Alexandre Martini
 */
public class DeleteResultList extends Screen implements ActionListener{
    private Result[] results;
    private String path = Resources.NEWUI_DELETE_RESULTS;
    private String title;

    private List list;
    private ListModel underlyingModel;
    private CheckableListCellRenderer renderer;
    private DeleteList tempDeleteList;

    protected void loadData() {
        title = AppMIDlet.getInstance().getFileStores().getSurveyStructure().getTitle();
        results = ResultControl.getInstance().getResults();
        tempDeleteList = new DeleteList();
    }

    protected void customize() {
        setTitle(title, path);

        form.removeAllCommands();

        if (list != null)
            form.removeComponent(list);

        form.addCommand(BackDeleteResultListCommand.getInstance().getCommand());
        form.addCommand(UnmarkAllResultsCommand.getInstance().getCommand());
        form.addCommand(MarkAllResultsCommand.getInstance().getCommand());
        form.addCommand(DeleteResultNowCommand.getInstance().getCommand());

        form.setCommandListener(this);

        underlyingModel = new DefaultListModel(results);

        list = new List(underlyingModel);
        list.setItemGap(0);
        list.addActionListener(this);

        renderer = new CheckableListCellRenderer(list.size());
        list.setListCellRenderer(renderer);

        list.setFixedSelection(List.FIXED_NONE);
        form.addComponent(list);
        form.setScrollable(false);
    }

    public void actionPerformed(ActionEvent evt) {
        Object cmd = evt.getSource();
        if (cmd == DeleteResultNowCommand.getInstance().getCommand()) {
            DeleteResultNowCommand.getInstance().execute(renderer);
        }
        else if (cmd == MarkAllResultsCommand.getInstance().getCommand()) {
            MarkAllResultsCommand.getInstance().execute(renderer);
        }
        else if (cmd == UnmarkAllResultsCommand.getInstance().getCommand()) {
            UnmarkAllResultsCommand.getInstance().execute(renderer);
        }
        else if (cmd == BackDeleteResultListCommand.getInstance().getCommand()) {
            BackDeleteResultListCommand.getInstance().execute(tempDeleteList);
        }
        else if(cmd == list){
            //TODO: CREATE COMMAND FOR THIS ACTION
            renderer.updateCheckbox(list.getSelectedIndex());
        }
        else
            throw new IllegalArgumentException("Invalid command");
    }

}

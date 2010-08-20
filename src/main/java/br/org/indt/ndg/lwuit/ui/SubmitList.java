/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.lwuit.control.BackSubmitListCommand;
import br.org.indt.ndg.lwuit.control.MarkAllResultsCommand;
import br.org.indt.ndg.lwuit.control.SendResultNowCommand;
import br.org.indt.ndg.lwuit.control.ResultControl;
import br.org.indt.ndg.lwuit.control.SurveysControl;
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
public class SubmitList extends Screen implements ActionListener{
    private Result[] results;
    private String path = Resources.SUBMIT_LIST_TITLE;
    private String title;

    private List list;
    private ListModel underlyingModel;
    private CheckableListCellRenderer renderer;

    protected void loadData() {
        title = AppMIDlet.getInstance().getFileStores().getSurveyStructure().getTitle();
        results = ResultControl.getInstance().getResults();
    }

    protected void customize() {
        setTitle(title, path);

        form.removeAllCommands();

        if (list != null)
            form.removeComponent(list);

        form.addCommand(BackSubmitListCommand.getInstance().getCommand());
        form.addCommand(UnmarkAllResultsCommand.getInstance().getCommand());
        form.addCommand(MarkAllResultsCommand.getInstance().getCommand());
        form.addCommand(SendResultNowCommand.getInstance().getCommand());

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
        if (cmd == SendResultNowCommand.getInstance().getCommand()) {
            SendResultNowCommand.getInstance().execute(renderer);
        }
        else if (cmd == MarkAllResultsCommand.getInstance().getCommand()) {
            MarkAllResultsCommand.getInstance().execute(renderer);
        }
        else if (cmd == UnmarkAllResultsCommand.getInstance().getCommand()) {
            UnmarkAllResultsCommand.getInstance().execute(renderer);
        }
        else if (cmd == BackSubmitListCommand.getInstance().getCommand()) {
            BackSubmitListCommand.getInstance().execute(null);
        }
        else if(cmd == list){
            //CREATE COMMAND FOR THIS ACTION
            renderer.updateCheckbox(list.getSelectedIndex());            
        }
        else
            throw new IllegalArgumentException("Invalid command");

    }



}

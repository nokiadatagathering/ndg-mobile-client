/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.lwuit.control.BackSentResultListCommand;
import br.org.indt.ndg.lwuit.control.DeleteSentResultCommand;
import br.org.indt.ndg.lwuit.control.MarkAllResultsCommand;
import br.org.indt.ndg.lwuit.control.MoveToUnsentCommand;
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
public class SentResultList extends Screen implements ActionListener{

    private Result[] results;
    private String path = Resources.SENT_LIST_TITLE;
    private String title;
    //REMOVE LATER.
    //private SentList sentList;

    private List list;
    private ListModel underlyingModel;
    private CheckableListCellRenderer renderer;

    public CheckableListCellRenderer getRenderer() {
        return renderer;
    }

    protected void loadData() {
        title = AppMIDlet.getInstance().getFileStores().getSurveyStructure().getTitle();
        results = ResultControl.getInstance().getSentResults();
        //sentList = new SentList();
    }

//    public SentList getSentList(){
//        return sentList;
//    }

    protected void customize() {
        setTitle(title, path);

        form.removeAllCommands();

        if (list != null)
            form.removeComponent(list);
       
        form.addCommand(BackSentResultListCommand.getInstance().getCommand());
        if(results.length > 0){
            form.addCommand(DeleteSentResultCommand.getInstance().getCommand());
            form.addCommand(UnmarkAllResultsCommand.getInstance().getCommand());
            form.addCommand(MarkAllResultsCommand.getInstance().getCommand());
            form.addCommand(MoveToUnsentCommand.getInstance().getCommand());
        }

        form.setCommandListener(this);

        underlyingModel = new DefaultListModel(results);

        list = new List(underlyingModel);
        list.setItemGap(0);
        list.addActionListener(this);

        renderer = new SentResultListCellRenderer(list.size());
        list.setListCellRenderer(renderer);

        list.setFixedSelection(List.FIXED_NONE);
        form.addComponent(list);
        form.setScrollable(false);
    }

    public void actionPerformed(ActionEvent evt) {
        Object cmd = evt.getSource();
        if (cmd == MoveToUnsentCommand.getInstance().getCommand()) {
            MoveToUnsentCommand.getInstance().execute(this);
        }
        else if (cmd == MarkAllResultsCommand.getInstance().getCommand()) {
            MarkAllResultsCommand.getInstance().execute(renderer);
        }
        else if (cmd == UnmarkAllResultsCommand.getInstance().getCommand()) {
            UnmarkAllResultsCommand.getInstance().execute(renderer);
        }
        else if (cmd == DeleteSentResultCommand.getInstance().getCommand()) {
            DeleteSentResultCommand.getInstance().execute(renderer);
        }
        else if (cmd == BackSentResultListCommand.getInstance().getCommand()) {
            BackSentResultListCommand.getInstance().execute(this);
        }
        else if(cmd == list){
            //CREATE COMMAND FOR THIS ACTION
            renderer.updateCheckbox(list.getSelectedIndex());
        }
        else
            throw new IllegalArgumentException("Invalid command");
    }

}

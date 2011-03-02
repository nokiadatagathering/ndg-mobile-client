package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.lwuit.control.BackResultListCommand;

import br.org.indt.ndg.lwuit.control.DeleteResultNowCommand;
import br.org.indt.ndg.lwuit.control.ExitCommand;
import br.org.indt.ndg.lwuit.control.MarkAllResultsCommand;
import br.org.indt.ndg.lwuit.control.NewResultCommand;
import br.org.indt.ndg.lwuit.control.OpenResultCommand;
import br.org.indt.ndg.lwuit.control.ResultListOnShow;
import br.org.indt.ndg.lwuit.control.SendResultNowCommand;
import br.org.indt.ndg.lwuit.control.SurveysControl;
import br.org.indt.ndg.lwuit.control.UnmarkAllResultsCommand;
import br.org.indt.ndg.lwuit.control.ViewResultCommand;
import br.org.indt.ndg.lwuit.control.ViewSentResultsCommand;
import br.org.indt.ndg.lwuit.model.CheckableListModel;
import br.org.indt.ndg.lwuit.model.Result;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.List;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.SelectionListener;
import com.sun.lwuit.list.ListModel;

/**
 *
 * @author mluz
 */
public class ResultList extends Screen implements ActionListener {

    private String title2 = Resources.RESULTS_LIST_TITLE;
    private String title1;

    private ClickableList list = null;
    private CheckableListModel underlyingModel;
    private Result[] results;
    private boolean checked;

    private SurveysControl surveysControl = SurveysControl.getInstance();


    protected void loadData() {
        title1 = surveysControl.getOpenedSurveyTitle();
        results = SurveysControl.getInstance().getResultsFromOpenedSurvey();
        registerEvent(new ResultListOnShow(), ON_SHOW);
    }

    protected void customize() {
        setTitle(title1, title2);
        
        checked = false;

        form.removeAllCommands();
        if (list != null)
            form.removeComponent(list);

        /** Context Menu **/
        form.addGameKeyListener(Display.GAME_RIGHT, this);

        form.addCommand(BackResultListCommand.getInstance().getCommand());
        form.addCommand(ViewSentResultsCommand.getInstance().getCommand());
        form.addCommand(NewResultCommand.getInstance().getCommand());

        try{
            form.removeCommandListener(this);
        } catch (NullPointerException npe ) {
            //during first initialisation remove throws exception.
            //this ensure that we have registered listener once
        }
        form.addCommandListener(this);

        // Client 2.0 can use the list.modelChanged(int, int) callback to refresh Lists
        underlyingModel = new CheckableListModel(results, CheckableListModel.FIRST_ITEM_UNCHECKABLE);

        list = new ClickableList(underlyingModel);
        list.setItemGap(0);
        list.addActionListener(this);
        list.addSelectionListener(new HandleSelectedItem());
        list.setListCellRenderer(new ResultListCellRenderer());

        list.setFixedSelection(List.FIXED_NONE);
        form.addComponent(list);
        form.setScrollable(false);
        list.repaint();
    }

    public void actionPerformed(ActionEvent evt) {
        Object cmd = evt.getSource();

        if(cmd == list){
            if( (list.size() > 0) && (list.getSelectedIndex() > 0)) {
                if (list.getLastPointerPressedCoordinateX() > (int)(0.8*list.getWidth())) {
                    showContextMenu();
                }  else {
                    underlyingModel.updateCheckbox(list.getSelectedIndex());
                    setCommands();
                }
            }
            else if (list.getSelectedIndex() == 0) {
                 NewResultCommand.getInstance().execute(null);
            }
        } else if (cmd == BackResultListCommand.getInstance().getCommand()) {
            BackResultListCommand.getInstance().execute(null);
        } else if (cmd == NewResultCommand.getInstance().getCommand()) {
            NewResultCommand.getInstance().execute(null);
        } else if (cmd == OpenResultCommand.getInstance().getCommand() || cmd == list) {
            if (list.size() > 0)
                OpenResultCommand.getInstance().execute(new Integer(getSelectedResult()));
        } else if (cmd == ViewResultCommand.getInstance().getCommand()) {
                ViewResultCommand.getInstance().execute(new Integer(getSelectedResult()));
        } else if (cmd == SendResultNowCommand.getInstance().getCommand()) {
                if(underlyingModel.getQtSelecteds()==0){
                    underlyingModel.setChecked(list.getSelectedIndex());
                }
                SendResultNowCommand.getInstance().execute(underlyingModel.getSelectedFlags());
        } else if (cmd == DeleteResultNowCommand.getInstance().getCommand()) {
                if(underlyingModel.getQtSelecteds()==0){
                    underlyingModel.setChecked(list.getSelectedIndex());
                }
                DeleteResultNowCommand.getInstance().execute(underlyingModel.getSelectedFlags());
                
        } else if (cmd == ViewSentResultsCommand.getInstance().getCommand()) {
                ViewSentResultsCommand.getInstance().execute(null);
        } else if (cmd == MarkAllResultsCommand.getInstance().getCommand()) {
                MarkAllResultsCommand.getInstance().execute(underlyingModel);
                setCommands();
        } else if (cmd == UnmarkAllResultsCommand.getInstance().getCommand()) {
                UnmarkAllResultsCommand.getInstance().execute(underlyingModel);
                setCommands();
        } else if (cmd == ExitCommand.getInstance().getCommand()) {
                ExitCommand.getInstance().execute(null);
        }

        if (cmd instanceof Form) {
            if((list.size()>0) && (list.getSelectedIndex() > 0)){
                showContextMenu();
            }
        }
    }

    private void showContextMenu() {
        ResultListContextMenu resultContextMenu = new ResultListContextMenu(getSelectedResult(), list.size());
        resultContextMenu.show();
    }

    private void setCommands(){
        if(underlyingModel.getQtSelecteds()>1){
            if(!checked){
                form.removeCommand(ViewResultCommand.getInstance().getCommand());
                form.removeCommand(OpenResultCommand.getInstance().getCommand());
                checked = true;
            }
        }else{
            if(checked){
                form.addCommand(ViewResultCommand.getInstance().getCommand());
                form.addCommand(OpenResultCommand.getInstance().getCommand());
                checked = false;
            }
        }
    }

    private int getSelectedResult() {
        return list.getSelectedIndex() -1;
    }

    class HandleSelectedItem implements SelectionListener {

        public void selectionChanged(int oldSelected, int newSelected) {
            if (newSelected == 0) {
                form.removeCommand(UnmarkAllResultsCommand.getInstance().getCommand());
                form.removeCommand(MarkAllResultsCommand.getInstance().getCommand());
                form.removeCommand(DeleteResultNowCommand.getInstance().getCommand());
                form.removeCommand(SendResultNowCommand.getInstance().getCommand());
                form.removeCommand(ViewResultCommand.getInstance().getCommand());
                form.removeCommand(OpenResultCommand.getInstance().getCommand());
            }
            else
            {
                form.addCommand(UnmarkAllResultsCommand.getInstance().getCommand());
                form.addCommand(MarkAllResultsCommand.getInstance().getCommand());
                form.addCommand(DeleteResultNowCommand.getInstance().getCommand());
                form.addCommand(SendResultNowCommand.getInstance().getCommand());
                form.addCommand(ViewResultCommand.getInstance().getCommand());
                form.addCommand(OpenResultCommand.getInstance().getCommand());
            }
        }

    }

    /**
     * List class extension that remembers last pointer press X coordinate
     * It may be used to perform different actions based on which part of List item was clicked
     * NOTE: showing context menu in pointerPressed method led to undesired effects
     */
    private class ClickableList extends List {

        private int m_x = 0;

        public ClickableList(ListModel model) {
            super(model);
        }

        public void pointerPressed(int x, int y) {
            m_x = x;
            super.pointerPressed(x,y);
        }

        public int getLastPointerPressedCoordinateX() {
            return m_x;
        }
    };

}

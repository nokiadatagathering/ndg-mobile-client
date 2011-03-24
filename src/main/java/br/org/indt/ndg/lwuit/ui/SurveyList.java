package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.AppSettingsViewCommand;
import br.org.indt.ndg.lwuit.control.ResolutionSelectionViewCommand;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.lwuit.control.CheckNewSurveysCommand;
import br.org.indt.ndg.lwuit.control.DeleteSurveyCommand;
import br.org.indt.ndg.lwuit.control.ExitCommand;
import br.org.indt.ndg.lwuit.control.GPSCommand;
import br.org.indt.ndg.lwuit.control.OpenSurveyCommand;
import br.org.indt.ndg.lwuit.control.SelectStyleViewCommand;
import br.org.indt.ndg.lwuit.control.TestConnectionCommand;
import br.org.indt.ndg.lwuit.control.UpdateCommand;
import com.sun.lwuit.List;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.SelectionListener;
import com.sun.lwuit.list.DefaultListModel;
import com.sun.lwuit.list.ListModel;
import br.org.indt.ndg.lwuit.extended.AnimatedList;
import br.org.indt.ndg.mobile.AppMIDlet;
import java.util.Vector;


public class SurveyList extends Screen implements ActionListener{

    private String title2 = Resources.SURVEY_LIST_TITLE;//NEWUI_TITLE_SURVEY_LIST;
    private String title1 = Resources.NEWUI_NOKIA_DATA_GATHERING;
    private String checkNewSurveyItem = "-> " + Resources.CHECK_NEW_SURVEYS + " <-";
    private AnimatedList list;

    private Vector surveys;
    private ListModel underlyingModel;


    protected void loadData() {
        surveys = AppMIDlet.getInstance().getSurveyList().getList();

        if( surveys.size()<=0 || !surveys.elementAt(0).equals( checkNewSurveyItem ) ) {
            surveys.insertElementAt("-> " + Resources.CHECK_NEW_SURVEYS + " <-", 0);
        }
    }

    protected void customize() {
        setTitle(title1, title2);

        if (list != null)
            form.removeComponent(list);
        
        resetAllCommands();

        try{
            form.removeCommandListener(this);
        } catch (NullPointerException npe ) {
            //during first initialisation remove throws exception.
            //this ensure that we have registered listener once
        }
        form.addCommandListener(this);

        // Client 2.0 can use the list.modelChanged(int, int) callback to refresh Lists
        underlyingModel = new DefaultListModel(surveys);

        if( list != null ) {
            list.stopAnimation();
            list = null;
        }
        list = new AnimatedList(underlyingModel);
        list.setItemGap(0);
        list.addActionListener(this);
        list.addSelectionListener(new HandleSelectedItem());
        list.setFixedSelection(List.FIXED_NONE_CYCLIC);
        list.startAnimation();

        form.addComponent(list);
        form.setScrollable(false);
    }

    public void actionPerformed(ActionEvent evt) {
        try {
            Object cmd = evt.getSource();
            if (cmd == ExitCommand.getInstance().getCommand()) {
                ExitCommand.getInstance().execute(null);
            } else if (cmd == GPSCommand.getInstance().getCommand()) {
                GPSCommand.getInstance().execute(null);
            } else if (cmd == OpenSurveyCommand.getInstance().getCommand() || cmd == list) {
                if (list.size() > 0) {
                    if( list.getSelectedIndex() > 0)
                        OpenSurveyCommand.getInstance().execute(new Integer( list.getSelectedIndex() - 1 ));
                    else
                        CheckNewSurveysCommand.getInstance().execute(null);
                }
            } else if (cmd == CheckNewSurveysCommand.getInstance().getCommand()) {
                CheckNewSurveysCommand.getInstance().execute(null);
            } else if (cmd == UpdateCommand.getInstance().getCommand()) {
                UpdateCommand.getInstance().execute(null);
            } else if (cmd == DeleteSurveyCommand.getInstance().getCommand()) {
                DeleteSurveyCommand.getInstance().execute(new Integer( list.getSelectedIndex() - 1 ));
            } else if (cmd == AppSettingsViewCommand.getInstance().getCommand()) {
                AppSettingsViewCommand.getInstance().execute(null);
            } else if (cmd == TestConnectionCommand.getInstance().getCommand()) {
                TestConnectionCommand.getInstance().execute(null);
            } else if ( cmd == ResolutionSelectionViewCommand.getInstance().getCommand() ){
                ResolutionSelectionViewCommand.getInstance().execute(null);
            }else if ( cmd == SelectStyleViewCommand.getInstance().getCommand()) {
                SelectStyleViewCommand.getInstance().execute(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetAllCommands() {
        form.removeAllCommands();
        form.addCommand(ExitCommand.getInstance().getCommand());
        form.addCommand(UpdateCommand.getInstance().getCommand());
        form.addCommand(TestConnectionCommand.getInstance().getCommand());
        form.addCommand(CheckNewSurveysCommand.getInstance().getCommand());
        form.addCommand(DeleteSurveyCommand.getInstance().getCommand());
        form.addCommand(AppSettingsViewCommand.getInstance().getCommand());
        form.addCommand(SelectStyleViewCommand.getInstance().getCommand());
        form.addCommand(ResolutionSelectionViewCommand.getInstance().getCommand());
        form.addCommand(GPSCommand.getInstance().getCommand());
        if (surveys.size() > 0)
            form.addCommand(OpenSurveyCommand.getInstance().getCommand());
    }


    private class HandleSelectedItem implements SelectionListener{

        public HandleSelectedItem() {
        }

        public void selectionChanged(int oldSelected, int newSelected) {
            if( newSelected == 0) {
                form.removeCommand(DeleteSurveyCommand.getInstance().getCommand());
            } else {
                resetAllCommands();
            }
        }
    }
}

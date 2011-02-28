package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.ResolutionSelectionViewCommand;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.lwuit.control.CheckNewSurveysCommand;
import br.org.indt.ndg.lwuit.control.DeleteSurveyCommand;
import br.org.indt.ndg.lwuit.control.ExitCommand;
import br.org.indt.ndg.lwuit.control.GPSCommand;
import br.org.indt.ndg.lwuit.control.OpenSurveyCommand;
import br.org.indt.ndg.lwuit.control.SelectStyleViewCommand;
import br.org.indt.ndg.lwuit.control.SurveysControl;
import br.org.indt.ndg.lwuit.control.TestConnectionCommand;
import br.org.indt.ndg.lwuit.control.UpdateCommand;
import br.org.indt.ndg.lwuit.model.Survey;
import com.sun.lwuit.List;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.SelectionListener;
import com.sun.lwuit.list.DefaultListModel;
import com.sun.lwuit.list.ListModel;


public class SurveyList extends Screen implements ActionListener{

    private String title2 = Resources.SURVEY_LIST_TITLE;//NEWUI_TITLE_SURVEY_LIST;
    private String title1 = Resources.NEWUI_NOKIA_DATA_GATHERING;
   
    private List list;

    private Survey[] surveys;
    private ListModel underlyingModel;


    protected void loadData() {
        surveys =  SurveysControl.getInstance().getAllSurveys();
    }

    protected void customize() {
        setTitle(title1, title2);

        form.removeAllCommands();

        if (list != null)
            form.removeComponent(list);
        
        form.addCommand(ExitCommand.getInstance().getCommand());
        form.addCommand(UpdateCommand.getInstance().getCommand());
        form.addCommand(TestConnectionCommand.getInstance().getCommand());
        form.addCommand(CheckNewSurveysCommand.getInstance().getCommand());
        form.addCommand(SelectStyleViewCommand.getInstance().getCommand());
        form.addCommand(ResolutionSelectionViewCommand.getInstance().getCommand());
        form.addCommand(GPSCommand.getInstance().getCommand());
        if (surveys.length > 0)
            form.addCommand(OpenSurveyCommand.getInstance().getCommand());

        try{
            form.removeCommandListener(this);
        } catch (NullPointerException npe ) {
            //during first initialisation remove throws exception.
            //this ensure that we have registered listener once
        }
        form.addCommandListener(this);

        // Client 2.0 can use the list.modelChanged(int, int) callback to refresh Lists
        underlyingModel = new DefaultListModel(surveys);

        list = new List(underlyingModel);
        list.setItemGap(0);
        list.addActionListener(this);

        SurveyListCellRenderer slcr = new SurveyListCellRenderer();
        list.setListCellRenderer(slcr);
        list.addSelectionListener(new HandleSelectedItem());

        list.setFixedSelection(List.FIXED_NONE_CYCLIC);
        form.addComponent(list);
        form.setScrollable(false);
    }

    private int getSelectedIndex() {
        return list.getSelectedIndex() - 1;
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
                    if( getSelectedIndex() >= 0)
                        OpenSurveyCommand.getInstance().execute(new Integer(getSelectedIndex()));
                    else
                        CheckNewSurveysCommand.getInstance().execute(null);
                }
            } else if (cmd == CheckNewSurveysCommand.getInstance().getCommand()) {
                CheckNewSurveysCommand.getInstance().execute(null);
            } else if (cmd == UpdateCommand.getInstance().getCommand()) {
                UpdateCommand.getInstance().execute(null);
            } else if (cmd == DeleteSurveyCommand.getInstance().getCommand()) {
                DeleteSurveyCommand.getInstance().execute(new Integer(getSelectedIndex()));
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


    private class HandleSelectedItem implements SelectionListener{

        public HandleSelectedItem() {
        }

        public void selectionChanged(int oldSelected, int newSelected) {
            if( newSelected == 0)
            {
                form.removeCommand(DeleteSurveyCommand.getInstance().getCommand());
            }
            else
            {
                form.removeAllCommands();
                form.addCommand(ExitCommand.getInstance().getCommand());
                form.addCommand(UpdateCommand.getInstance().getCommand());
                form.addCommand(TestConnectionCommand.getInstance().getCommand());
                form.addCommand(CheckNewSurveysCommand.getInstance().getCommand());
                form.addCommand(DeleteSurveyCommand.getInstance().getCommand());
                form.addCommand(SelectStyleViewCommand.getInstance().getCommand());
                form.addCommand(ResolutionSelectionViewCommand.getInstance().getCommand());
                form.addCommand(GPSCommand.getInstance().getCommand());
                if (surveys.length > 0)
                    form.addCommand(OpenSurveyCommand.getInstance().getCommand());
            }
        }
    }
}

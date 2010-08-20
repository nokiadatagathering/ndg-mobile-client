package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.lwuit.control.CheckNewSurveysCommand;
import br.org.indt.ndg.lwuit.control.DeleteSurveyCommand;
import br.org.indt.ndg.lwuit.control.ExitCommand;
import br.org.indt.ndg.lwuit.control.GPSCommand;
import br.org.indt.ndg.lwuit.control.OpenSurveyCommand;
import br.org.indt.ndg.lwuit.control.SurveyListOnShow;
import br.org.indt.ndg.lwuit.control.SurveysControl;
import br.org.indt.ndg.lwuit.control.TestConnectionCommand;
import br.org.indt.ndg.lwuit.control.UpdateCommand;
import br.org.indt.ndg.lwuit.model.Survey;
import br.org.indt.ndg.mobile.submit.TestConnection;
import com.sun.lwuit.Command;
import com.sun.lwuit.List;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.list.DefaultListModel;
import com.sun.lwuit.list.ListModel;


public class SurveyList extends Screen implements ActionListener{

    private String title2 = Resources.SURVEY_LIST_TITLE;//NEWUI_TITLE_SURVEY_LIST;
    private String title1 = Resources.NEWUI_NOKIA_DATA_GATHERING;

    private SurveysControl surveysControl = SurveysControl.getInstance();
    
    private List list;

    private int totalSurveys;
    private Survey[] surveys;
    private ListModel underlyingModel;


    protected void loadData() {
        totalSurveys = surveysControl.getTotal();
        surveys = surveysControl.getAllSurveys();
        registerEvent(new SurveyListOnShow(surveys.length), ON_CREATE);
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
        if (totalSurveys > 0)
            form.addCommand(DeleteSurveyCommand.getInstance().getCommand());
        form.addCommand(GPSCommand.getInstance().getCommand());
        if (totalSurveys > 0)
            form.addCommand(OpenSurveyCommand.getInstance().getCommand());

        form.setCommandListener(this);

        // Client 2.0 can use the list.modelChanged(int, int) callback to refresh Lists
        underlyingModel = new DefaultListModel(surveys);

        list = new List(underlyingModel);
        list.setItemGap(0);
        list.addActionListener(this);

        SurveyListCellRenderer slcr = new SurveyListCellRenderer();
        list.setListCellRenderer(slcr);

        list.setFixedSelection(List.FIXED_NONE_CYCLIC);
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
                if (list.size() > 0)
                    OpenSurveyCommand.getInstance().execute(new Integer(list.getSelectedIndex()));
            } else if (cmd == CheckNewSurveysCommand.getInstance().getCommand()) {
                CheckNewSurveysCommand.getInstance().execute(null);
            } else if (cmd == UpdateCommand.getInstance().getCommand()) {
                UpdateCommand.getInstance().execute(null);
            } else if (cmd == DeleteSurveyCommand.getInstance().getCommand()) {
                DeleteSurveyCommand.getInstance().execute(new Integer(list.getSelectedIndex()));
            } else if (cmd == TestConnectionCommand.getInstance().getCommand()) {
                TestConnectionCommand.getInstance().execute(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

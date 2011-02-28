package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.CancelCheckSurveyListCommand;
import br.org.indt.ndg.lwuit.control.DownloadCheckSurveyListCommand;
import br.org.indt.ndg.lwuit.control.SurveysControl;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;

public class CheckNewSurveyList extends Screen implements ActionListener {

    private String strSurveys;

    protected void loadData() {
        String [] surveys = SurveysControl.getInstance().getAvailableSurveysToDownload();
        strSurveys = "\n";
        for (int i = 0; i < surveys.length; i++) {
            strSurveys += "# " + surveys[i] + "\n";
        }
    }

    protected void customize() {
        form.removeAll();
        form.removeAllCommands();

        setTitle(Resources.NEWUI_NOKIA_DATA_GATHERING, Resources.DOWNLOAD_SURVEYS);

        TextArea item = new TextArea(5,20);
        item.getSelectedStyle().setBgColor( NDGStyleToolbox.getInstance().listStyle.bgUnselectedColor );
        item.getSelectedStyle().setFgColor( NDGStyleToolbox.getInstance().listStyle.unselectedFontColor );
        item.getStyle().setFont( NDGStyleToolbox.fontSmall );
        item.setEditable(false);
        item.setFocusable(true);
        item.setText(strSurveys);
        item.setRows(item.getLines()-1);
        form.addComponent(item);

        form.addCommand(CancelCheckSurveyListCommand.getInstance().getCommand());
        if (!strSurveys.equals("\n")) {
            form.addCommand(DownloadCheckSurveyListCommand.getInstance().getCommand());
        }
        try{
            form.removeCommandListener(this);
        } catch (NullPointerException npe ) {
            //during first initialisation remove throws exception.
            //this ensure that we have registered listener once
        }
        form.addCommandListener(this);
    }

    public void actionPerformed(ActionEvent evt) {
        Object cmd = evt.getSource();
        if (cmd == DownloadCheckSurveyListCommand.getInstance().getCommand()) {
            DownloadCheckSurveyListCommand.getInstance().execute(null);
        } else {
            CancelCheckSurveyListCommand.getInstance().execute(null);
        }
    }

}

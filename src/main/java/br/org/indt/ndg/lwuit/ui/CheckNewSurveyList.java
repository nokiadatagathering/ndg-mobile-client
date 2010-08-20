/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.CancelCheckSurveyListCommand;
import br.org.indt.ndg.lwuit.control.DownloadCheckSurveyListCommand;
import br.org.indt.ndg.lwuit.control.SurveysControl;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.plaf.UIManager;

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
        item.setStyle(UIManager.getInstance().getComponentStyle("Label"));
        item.getStyle().setFont(Screen.getRes().getFont("NokiaSansWide13"));
        item.setEditable(false);
        item.setFocusable(false);
        item.setText(strSurveys);
        item.setRows(item.getLines()-1);
        form.addComponent(item);

        form.addCommand(CancelCheckSurveyListCommand.getInstance().getCommand());
        if (!strSurveys.equals("\n")) {
            form.addCommand(DownloadCheckSurveyListCommand.getInstance().getCommand());
        }
        form.setCommandListener(this);
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

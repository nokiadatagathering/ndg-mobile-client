/*
*  Nokia Data Gathering
*
*  Copyright (C) 2011 Nokia Corporation
*
*  This program is free software; you can redistribute it and/or
*  modify it under the terms of the GNU Lesser General Public
*  License as published by the Free Software Foundation; either
*  version 2.1 of the License, or (at your option) any later version.
*
*  This program is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
*  Lesser General Public License for more details.
*
*  You should have received a copy of the GNU Lesser General Public License
*  along with this program.  If not, see <http://www.gnu.org/licenses/
*/

package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.lwuit.control.CheckNewSurveysCommand;
import br.org.indt.ndg.lwuit.control.DeleteSurveyCommand;
import br.org.indt.ndg.lwuit.control.ExitCommand;
import br.org.indt.ndg.lwuit.control.OpenSettingsForm;
import br.org.indt.ndg.lwuit.control.OpenSurveyCommand;
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
            } else if (cmd == TestConnectionCommand.getInstance().getCommand()) {
                TestConnectionCommand.getInstance().execute(null);
            } else if ( cmd == OpenSettingsForm.getInstance().getCommand() ){
                OpenSettingsForm.getInstance().execute(cmd);
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
        form.addCommand(OpenSettingsForm.getInstance().getCommand());
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

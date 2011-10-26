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

import br.org.indt.ndg.lwuit.control.OpenRosaInterviewSaveCommand;
import br.org.indt.ndg.lwuit.control.OpenFileBrowserCommand;
import br.org.indt.ndg.lwuit.control.OpenRosaBackCommand;
import br.org.indt.ndg.lwuit.control.RemovePhotoCommand;
import br.org.indt.ndg.lwuit.control.SaveResultsObserver;
import br.org.indt.ndg.lwuit.control.ShowPhotoCommand;
import br.org.indt.ndg.lwuit.control.SurveysControl;
import br.org.indt.ndg.lwuit.control.TakePhotoCommand;
import br.org.indt.ndg.lwuit.ui.openrosa.OpenRosaWidgetFactory;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.NdgConsts;
import br.org.indt.ndg.mobile.Resources;
import com.nokia.xfolite.xforms.dom.UserInterface;
import com.nokia.xfolite.xml.dom.WidgetFactory;
import com.sun.lwuit.Container;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.plaf.Border;

/**
 *
 * @author damian.janicki
 */

public class OpenRosaInterviewForm extends OpenRosaScreen implements UserInterface, ActionListener, SaveResultsObserver{

    private Container rootContainer;
    private OpenRosaWidgetFactory widgetFactory = null;

    private String title1;
    private String title2;

    public OpenRosaInterviewForm() {
    }

    protected void loadData() {
        title1 = SurveysControl.getInstance().getSurveyTitle();
        title2 = Resources.NEW_INTERVIEW;
        rootContainer = new Container();
    }

    protected void customize() {
        form.removeAllCommands();
        form.removeAll();

        form.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        form.getContentPane().getStyle().setBorder(Border.createEmpty(), false);
        form.setScrollAnimationSpeed(500);
        form.setFocusScrolling(true);

        form.addCommand(OpenRosaBackCommand.getInstance().getCommand());
        form.addCommand(OpenRosaInterviewSaveCommand.getInstance().getCommand());

        try {
            form.removeCommandListener(this);
        } catch (NullPointerException npe) {
        }
        form.addCommandListener(this);
        form.addComponent(rootContainer);
        setTitle(title1, title2);

        createXFormsDocument();

        String dirName = AppMIDlet.getInstance().getFileSystem().getSurveyDirName();
        String file = AppMIDlet.getInstance().getRootDir() + dirName + NdgConsts.SURVEY_NAME;
        addResultData();
        load(file);
    }

    public void actionPerformed(ActionEvent ae) {
        Object cmd = ae.getSource();
        if (cmd == OpenRosaBackCommand.getInstance().getCommand()) {
            GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_YES_NO, true);
            if(widgetFactory.isFormChanged() && GeneralAlert.RESULT_YES ==  GeneralAlert.getInstance().show( Resources.CMD_SAVE,
                                             Resources.SAVE_SURVEY_QUESTION,
                                             GeneralAlert.CONFIRMATION)){
                saveInterview();
            }else{
                OpenRosaBackCommand.getInstance().execute(null);
            }
        } else if (cmd == OpenRosaInterviewSaveCommand.getInstance().getCommand()) {
            if(widgetFactory.isFormChanged()){
                saveInterview();
            }
        } else if( cmd == OpenFileBrowserCommand.getInstance().getCommand() ) {
            OpenFileBrowserCommand.getInstance().execute(null);
        } else if ( cmd == TakePhotoCommand.getInstance().getCommand() ) {
            TakePhotoCommand.getInstance().execute(null);
        } else if ( cmd == ShowPhotoCommand.getInstance().getCommand() ) {
            ShowPhotoCommand.getInstance().execute(null);
        } else if ( cmd == RemovePhotoCommand.getInstance().getCommand() ) {
            RemovePhotoCommand.getInstance().execute(null);
        }
    }

    private void saveInterview(){
        if (widgetFactory.commitValues()) {
            OpenRosaInterviewSaveCommand.getInstance().setObserver(this);
            OpenRosaInterviewSaveCommand.getInstance().execute(getXFormsDocument());
        }
    }

    public void onResultsSaved() {
        AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.ResultList.class);
    }

    public WidgetFactory createWidgetFactory() {
        widgetFactory = new OpenRosaWidgetFactory(rootContainer);
        return widgetFactory;
    }
}

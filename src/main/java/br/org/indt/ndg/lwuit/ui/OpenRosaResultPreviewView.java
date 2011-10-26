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

import br.org.indt.ndg.lwuit.control.BackResultViewCommand;
import br.org.indt.ndg.lwuit.control.DeleteCurrentResultCommand;
import br.org.indt.ndg.lwuit.control.OpenResultCommand;
import br.org.indt.ndg.lwuit.control.SendResultCommand;
import br.org.indt.ndg.lwuit.control.SurveysControl;
import br.org.indt.ndg.lwuit.ui.openrosa.OpenRosaResultWidgetFactory;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.FileSystem;
import br.org.indt.ndg.mobile.NdgConsts;
import br.org.indt.ndg.mobile.Resources;
import com.nokia.xfolite.xforms.dom.UserInterface;
import com.nokia.xfolite.xml.dom.WidgetFactory;
import com.sun.lwuit.Container;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;

/**
 *
 * @author damian.janicki
 */
public class OpenRosaResultPreviewView extends OpenRosaScreen implements UserInterface, ActionListener {

    private Container rootContainer;

    private String title1;
    private String title2 = Resources.RESULTS_LIST_TITLE;

    public WidgetFactory createWidgetFactory() {
        return new OpenRosaResultWidgetFactory(rootContainer);
    }

    protected void loadData() {
        title1 = SurveysControl.getInstance().getSurveyTitle();
        rootContainer = new Container();
    }

    protected void customize() {
        form.removeAllCommands();
        form.removeAll();

        form.setCyclicFocus(false);

        form.addCommand(BackResultViewCommand.getInstance().getCommand());
        if ( !(AppMIDlet.getInstance().getFileSystem().resultsInUse() == FileSystem.USE_SENT_RESULTS) ) {
            // Open option not available if view opened in SentResults mode
            form.addCommand(SendResultCommand.getInstance().getCommand());
            form.addCommand(DeleteCurrentResultCommand.getInstance().getCommand());
            form.addCommand(OpenResultCommand.getInstance().getCommand());
        }

        form.setSmoothScrolling(true);
        try {
            form.removeCommandListener(this);
        } catch (NullPointerException npe) {
            //during first initialisation remove throws exception.
            //this ensure that we have registered listener once
        }
        form.addCommandListener(this);
        setTitle(title1, title2);
        form.addComponent(rootContainer);

        createXFormsDocument();

        String dirName = AppMIDlet.getInstance().getFileSystem().getSurveyDirName();
        String file = AppMIDlet.getInstance().getRootDir() + dirName + NdgConsts.SURVEY_NAME;
        addResultData();
        load(file);
    }

    public void actionPerformed(ActionEvent evt) {
        Object cmd = evt.getSource();
        if (cmd == OpenResultCommand.getInstance().getCommand()) {
            OpenResultCommand.getInstance().execute(null);
        } else if (cmd == BackResultViewCommand.getInstance().getCommand()) {
            BackResultViewCommand.getInstance().execute(null);
        } else if (cmd == DeleteCurrentResultCommand.getInstance().getCommand()) {
            DeleteCurrentResultCommand.getInstance().execute(null);
        } else if (cmd == SendResultCommand.getInstance().getCommand()) {
            SendResultCommand.getInstance().execute(null);
        }
    }
}

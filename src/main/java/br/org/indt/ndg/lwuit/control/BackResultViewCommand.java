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

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.Screen;
import br.org.indt.ndg.lwuit.ui.SentResultList;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.ResultList;
import com.sun.lwuit.Command;

/**
 *
 * @author mluz
 */
public class BackResultViewCommand extends BackCommand {

    private static BackResultViewCommand instance;
    private Screen m_returnScreen = null; // screen that will be shown with 'Back' command

    protected Command createCommand() {
        return new Command(Resources.NEWUI_BACK);
    }

    protected void doAction(Object parameter) {
        if ( m_returnScreen != null && m_returnScreen.getClass() == SentResultList.class ) {
            m_returnScreen = null;
            ViewSentResultsCommand.getInstance().registerToBeExecutedAsBackCommand();
            ViewSentResultsCommand.getInstance().execute(null);
        } else { // default
            AppMIDlet.getInstance().getFileStores().resetQuestions();
            AppMIDlet.getInstance().getFileStores().resetResultStructure();
            AppMIDlet.getInstance().setResultList( new ResultList());
            AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.ResultList.class);
        }
    }

    public static BackResultViewCommand getInstance() {
        if (instance == null)
            instance = new BackResultViewCommand();
        return instance;
    }

    /**
     * Sets a screen that will be used when Back is pressed. Only screens implicitly
     * listed in doAction method will be used
     * @param returnScreen
     */
    public void setReturnScreen( Screen returnScreen ) {
        m_returnScreen = returnScreen;
    }
}

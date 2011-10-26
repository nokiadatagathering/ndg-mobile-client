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

import br.org.indt.ndg.lwuit.ui.NDGLookAndFeel;
import br.org.indt.ndg.lwuit.ui.SentResultList;
import br.org.indt.ndg.lwuit.ui.WaitingScreen;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.FileSystem;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

/**
 *
 * @author mluz
 */
public class ViewSentResultsCommand extends CommandControl {

    private static ViewSentResultsCommand instance;
    private boolean m_executeAsBackCommand = false;
    protected Command createCommand() {
        return new Command(Resources.NEWUI_VIEW_SENT_RESULTS);
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().getFileSystem().useResults(FileSystem.USE_SENT_RESULTS);

        WaitingScreen.show(Resources.CMD_VIEW);
        ViewSentResultsRunnable vrr = new ViewSentResultsRunnable();
        Thread t = new Thread(vrr);  //create new thread to compensate for waitingform
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
    }

    /**
     * Because action is executed in different thread doAfter would execute before
     * action is finished thus most probably would execute in unexpected manner
     */
    public void doBefore() {}
    public void doAfter(){}

    public static ViewSentResultsCommand getInstance() {
        if (instance == null)
            instance = new ViewSentResultsCommand();
        return instance;
    }

    class ViewSentResultsRunnable implements Runnable {
        public void run() {
            try { Thread.sleep(200); } catch(Exception e){}
            AppMIDlet.getInstance().getFileSystem().loadSentFiles();
            if (m_executeAsBackCommand) {
                NDGLookAndFeel.setDefaultFormTransitionInReversed();
            }
            AppMIDlet.getInstance().setDisplayable(SentResultList.class);
            if (m_executeAsBackCommand) {
                m_executeAsBackCommand = false;
                NDGLookAndFeel.setDefaultFormTransitionInForward();
            }
        }
    }

    public void registerToBeExecutedAsBackCommand() {
        m_executeAsBackCommand = true;
    }

}
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

import br.org.indt.ndg.lwuit.ui.WaitingScreen;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.FileSystem;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;
import java.util.Date;

/**
 *
 * @author mluz
 */
public class NewResultCommand extends CommandControl {

    private static NewResultCommand instance;

    protected Command createCommand() {
        return new Command(Resources.NEWUI_NEW_RESULT);
    }

    protected void doAction(Object parameter) {
        WaitingScreen.show(Resources.PROCESSING);
        NewResultRunnable orr = new NewResultRunnable();
        Thread t = new Thread(orr);  //create new thread to compensate for waitingform
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
    }

    public static NewResultCommand getInstance() {
        if (instance == null)
            instance = new NewResultCommand();
        return instance;
    }

    class NewResultRunnable implements Runnable {
        public void run() {
            try {
                try { Thread.sleep(200); } catch(Exception e){}
                AppMIDlet.getInstance().getFileStores().resetQuestions();
                AppMIDlet.getInstance().getFileSystem().useResults(FileSystem.USE_NOT_SENT_RESULTS);
                AppMIDlet.getInstance().getFileSystem().setLocalFile(false);
                AppMIDlet.getInstance().getFileStores().resetResultStructure();

                SurveysControl.getInstance().reset();
                SurveysControl.getInstance().prepareEmptyResults();
                AppMIDlet.getInstance().setTimeTracker((new Date()).getTime());  //to keep track of time used to create new survey
                AppMIDlet.getInstance().showInterview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
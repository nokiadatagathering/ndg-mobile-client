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

import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import br.org.indt.ndg.lwuit.ui.WaitingScreen;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.Utils;
import com.sun.lwuit.Command;
import java.util.Date;

/**
 *
 * @author mluz
 */
public class OpenResultCommand extends CommandControl {

    private static OpenResultCommand instance;

    protected Command createCommand() {
        return new Command(Resources.NEWUI_OPEN_RESULT);
    }

    protected void doAction(Object parameter) {
        if (parameter != null) {
            int selectedIndex = ((Integer)parameter).intValue();
            AppMIDlet.getInstance().getFileSystem().setResultCurrentIndex(selectedIndex);
        }
        
        WaitingScreen.show(Resources.PROCESSING);
        OpenResultRunnable orr = new OpenResultRunnable();
        Thread t = new Thread(orr);  //create new thread to compensate for waitingform
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
    }

    public static OpenResultCommand getInstance() {
        if (instance == null)
            instance = new OpenResultCommand();
        return instance;
    }

    class OpenResultRunnable implements Runnable {
        public void run() {
            try { Thread.sleep(200); } catch(Exception e){}

            SurveysControl.getInstance().reset();

            if (AppMIDlet.getInstance().getFileSystem().getResultFilename() != null) {
                AppMIDlet.getInstance().getFileSystem().setLocalFile(true);

                if( Utils.isCurrentDirXForm() ){
                    AppMIDlet.getInstance().getFileStores().loadXFormResult();
                    AppMIDlet.getInstance().showInterview();
                }else{
                    AppMIDlet.getInstance().getFileStores().parseResultFile();
                    if (AppMIDlet.getInstance().getFileStores().getError()) {
                        GeneralAlert.getInstance().addCommand( ExitCommand.getInstance());
                        GeneralAlert.getInstance().show(Resources.ERROR_TITLE, Resources.EPARSE_GENERAL, GeneralAlert.ERROR );
                    } else {
                        SurveysControl.getInstance().setResult( AppMIDlet.getInstance().getFileStores().getResultStructure() );
                        AppMIDlet.getInstance().getFileStores().loadAnswers();
                        AppMIDlet.getInstance().setTimeTracker((new Date()).getTime());  //to keep track of time increment used to create new survey
                        AppMIDlet.getInstance().showInterview();
                    }
                }
            }
        }
    }
}

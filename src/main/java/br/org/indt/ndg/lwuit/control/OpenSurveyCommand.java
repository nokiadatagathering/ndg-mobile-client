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
import br.org.indt.ndg.mobile.FileSystem;
import br.org.indt.ndg.mobile.NdgConsts;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.ResultList;
import br.org.indt.ndg.mobile.SurveyList;
import com.sun.lwuit.Command;

/**
 *
 * @author mluz
 */
public class OpenSurveyCommand extends CommandControl {

    //private boolean inProcess = false;

    private static OpenSurveyCommand instance;

    protected Command createCommand() {
        return new Command(Resources.CMD_OPEN);
    }

    public static OpenSurveyCommand getInstance() {
        if (instance == null)
            instance = new OpenSurveyCommand();
        return instance;
    }

    protected void doAction(Object parameter) {
        int selectedIndex = ((Integer)parameter).intValue();

        AppMIDlet.getInstance().getFileSystem().useResults(FileSystem.USE_NOT_SENT_RESULTS);
        AppMIDlet.getInstance().getFileSystem().setSurveyCurrentIndex(selectedIndex);
        AppMIDlet.getInstance().getFileSystem().setResultCurrentIndex(selectedIndex);

        WaitingScreen.show(Resources.LOADING_SURVEYS);

        OpenSurveyRunnable osr = new OpenSurveyRunnable();
        Thread t = new Thread(osr);  //create new thread to compensate for waitingform
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
    }

    class OpenSurveyRunnable implements Runnable {

        public void run() {

            try { Thread.sleep(200); } catch (InterruptedException ex) { ex.printStackTrace(); }

            System.out.println("Name: "+ AppMIDlet.getInstance().getRootDir() + AppMIDlet.getInstance().getFileSystem().getSurveyDirName() + NdgConsts.SURVEY_NAME);

            boolean isValidSurvey = true;
            try {
                if (isValidSurvey) {
                    AppMIDlet.getInstance().getFileStores().parseSurveyFile();
                    if (AppMIDlet.getInstance().getFileStores().getErrorkParser()){
                          GeneralAlert.getInstance().addCommand( ExitCommand.getInstance());
                          GeneralAlert.getInstance().show(Resources.ERROR_TITLE, Resources.EPARSE_SURVEY, GeneralAlert.ERROR );
                    }
                    else {
                        AppMIDlet.getInstance().getFileSystem().loadResultFiles();
                        if (!AppMIDlet.getInstance().getFileSystem().getError()) {
                            AppMIDlet.getInstance().getFileSystem().setResultListIndex(0);
                            AppMIDlet.getInstance().setResultList(new ResultList());
                            AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.ResultList.class);
                        } else {
                            AppMIDlet.getInstance().getFileSystem().setError(false);
                            GeneralAlert.getInstance().addCommand( ExitCommand.getInstance());
                            GeneralAlert.getInstance().show(Resources.ERROR_TITLE, Resources.EPARSE_RESULT, GeneralAlert.ERROR );
                        }
                    }
                }
                else {

                    GeneralAlert.getInstance().addCommand( GeneralAlert.DIALOG_OK, true);
                    GeneralAlert.getInstance().show(Resources.ERROR_TITLE ,Resources.EINVALID_SURVEY, GeneralAlert.ERROR);
                    AppMIDlet.getInstance().setSurveyList(new SurveyList());
                    AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.SurveyList.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

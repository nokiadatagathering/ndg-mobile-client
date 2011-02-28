/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import br.org.indt.ndg.lwuit.ui.WaitingScreen;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.ResultList;
import br.org.indt.ndg.mobile.SurveyList;
import com.nokia.mid.appl.cmd.Local;
import com.sun.lwuit.Command;

/**
 *
 * @author mluz
 */
public class OpenSurveyCommand extends CommandControl {

    //private boolean inProcess = false;

    private static OpenSurveyCommand instance;

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CMD_OPEN));
    }

    public static OpenSurveyCommand getInstance() {
        if (instance == null)
            instance = new OpenSurveyCommand();
        return instance;
    }

    protected void doAction(Object parameter) {
        int selectedIndex = ((Integer)parameter).intValue();

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

            System.out.println("Name: "+Resources.ROOT_DIR + AppMIDlet.getInstance().getFileSystem().getSurveyDirName() + Resources.SURVEY_NAME);

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

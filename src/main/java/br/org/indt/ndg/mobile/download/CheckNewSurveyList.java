/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.mobile.download;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

/**
 *
 * @author MSS01
 */
public class CheckNewSurveyList extends List implements CommandListener{
    
    
    public CheckNewSurveyList(String[] surveysTitles) {
        super(Resources.DOWNLOAD_SURVEYS, List.IMPLICIT, surveysTitles, null);
        addCommand(Resources.CMD_CANCEL);
        if(this.size() > 0){
            addCommand(Resources.CMD_DOWNLOAD);
        }
        setCommandListener(this);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == Resources.CMD_CANCEL) {
            AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getSurveyList());
        } else if (c == Resources.CMD_DOWNLOAD) {
            DownloadNewSurveys.getInstance().download();
        }
    }

}

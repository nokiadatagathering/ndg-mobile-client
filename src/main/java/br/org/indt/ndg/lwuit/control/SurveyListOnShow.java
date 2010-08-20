/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;


/**
 *
 * @author mluz
 */
public class SurveyListOnShow extends Event {

    private int totalSurveys;

    public SurveyListOnShow(int totalSurveys) {
        this.totalSurveys = totalSurveys;
    }

    protected void doAction(Object parameter) {
        if (totalSurveys == 0) {
            AppMIDlet.getInstance().getGeneralAlert().showAlert(Resources.NO_SURVEYS, Resources.THERE_ARE_NO_SURVEYS);
        }
    }

}

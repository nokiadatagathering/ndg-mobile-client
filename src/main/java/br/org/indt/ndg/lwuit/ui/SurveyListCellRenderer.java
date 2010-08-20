/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.model.Survey;

/**
 *
 * @author mluz
 */
class SurveyListCellRenderer extends SimpleListCellRenderer {

    protected String getText(Object value) {
        Survey survey = (Survey) value;
        return survey.getName();
    }

}



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import com.sun.lwuit.animations.CommonTransitions;
import com.sun.lwuit.plaf.UIManager;

/**
 *
 * @author mluz
 */
public class ResultListOnShow extends Event {

    protected void doAction(Object parameter) {
        UIManager.getInstance().getLookAndFeel().setDefaultFormTransitionIn(CommonTransitions.createSlide(CommonTransitions.SLIDE_HORIZONTAL, false, 500));
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import com.sun.lwuit.Command;
import com.sun.lwuit.animations.CommonTransitions;
import com.sun.lwuit.plaf.UIManager;

/**
 *
 * @author mluz
 */
public abstract class BackCommand extends CommandControl {
    
    public void doBefore() {
        UIManager.getInstance().getLookAndFeel().setDefaultFormTransitionIn(CommonTransitions.createSlide(CommonTransitions.SLIDE_HORIZONTAL, true, 500));
    }

    public void doAfter() {
        UIManager.getInstance().getLookAndFeel().setDefaultFormTransitionIn(CommonTransitions.createSlide(CommonTransitions.SLIDE_HORIZONTAL, false, 500));
    }

    protected abstract Command createCommand();

    protected abstract void doAction(Object parameter);

    

}

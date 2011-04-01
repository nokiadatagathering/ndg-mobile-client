package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.NDGLookAndFeel;
import com.sun.lwuit.Command;

/**
 *
 * @author mluz
 */
public abstract class BackCommand extends CommandControl {
    
    public void doBefore() {
        NDGLookAndFeel.setDefaultFormTransitionInReversed();
    }

    public void doAfter() {
        NDGLookAndFeel.setDefaultFormTransitionInForward();
    }

    protected abstract Command createCommand();

    protected abstract void doAction(Object parameter);

    

}

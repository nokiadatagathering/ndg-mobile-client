/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

/**
 *
 * @author mluz
 */
public abstract class Event {

    public void execute(Object parameter) {
        if (!inProcess){
            inProcess = true;
            doBefore();
            doAction(parameter);
            doAfter();
            inProcess = false;
        }
    };

    public void doBefore() {}

    public void doAfter() {}

    private boolean inProcess;

    // @deprecated
    private void startLazy(final Object parameter, final int sleepTime) {
        if (!inProcess){
            inProcess = true;
            doAction(parameter);
            inProcess = false;
        }
    }

    // @deprecated
    private void startLazy(final Object parameter) {
        startLazy(parameter, 800); //default time
    }

    protected abstract void doAction(Object parameter);

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import com.sun.lwuit.Command;

/**
 *
 * @author mluz
 */
public abstract class CommandControl extends Event {

    private Command cmd;

    public Command getCommand() {
        return cmd;
    }

    public CommandControl() {
        cmd = createCommand();
    }

    protected abstract Command createCommand();

}

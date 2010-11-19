/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import com.nokia.mid.appl.cmd.Local;
import com.sun.lwuit.Command;
import java.util.Vector;

/**
 *
 * @author mluz
 */
public class SaveResultCommand extends CommandControl {

    private static SaveResultCommand instance;

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CMD_SAVE));
    }

    protected void doAction(Object parameter) {
        Vector questions = (Vector)parameter;
        PersistenceManager.getInstance().save(questions);//to be checked or refactored
    }

    public static SaveResultCommand getInstance() {
        if (instance == null)
            instance = new SaveResultCommand();
        return instance;
    }

}


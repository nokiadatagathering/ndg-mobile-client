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
 * @author mturiel
 */
public class SaveInterviewForm2Command extends CommandControl {

    private static SaveInterviewForm2Command instance;

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CMD_SAVE));
    }

    protected void doAction(Object parameter /*The Vector With Quetions*/) {
        Vector vQuestions = (Vector) parameter;
        PersistenceManager.getInstance().save(vQuestions);
    }

    public static SaveInterviewForm2Command getInstance() {
        if (instance == null)
            instance = new SaveInterviewForm2Command();
        return instance;
    }
}
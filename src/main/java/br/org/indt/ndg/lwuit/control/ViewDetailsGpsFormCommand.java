/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.SimpleLocation;
import br.org.indt.ndg.mobile.AppMIDlet;
import com.sun.lwuit.Command;
import com.nokia.mid.appl.cmd.Local;

public class ViewDetailsGpsFormCommand extends CommandControl {

    private static ViewDetailsGpsFormCommand instance;

    public static ViewDetailsGpsFormCommand getInstance() {
        if (instance == null)
            instance = new ViewDetailsGpsFormCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command(Local.getText(Local.QTJ_CMD_VIEW_GPS_DETAILS));
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().setDisplayable( SimpleLocation.class );
    }
}
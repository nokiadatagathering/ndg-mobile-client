/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.settings.GpsForm;
import com.sun.lwuit.Command;

/**
 *
 * @author mluz
 */
public class GPSCommand extends CommandControl {

    private static GPSCommand instance;

    protected Command createCommand() {
        return new Command(Resources.GPS);
    }

    public static GPSCommand getInstance() {
        if (instance == null)
            instance = new GPSCommand();
        return instance;
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().setDisplayable(new GpsForm());
    }
}

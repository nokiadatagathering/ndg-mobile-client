package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.SimpleLocation;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

public class ViewDetailsGpsFormCommand extends CommandControl {

    private static ViewDetailsGpsFormCommand instance;

    public static ViewDetailsGpsFormCommand getInstance() {
        if (instance == null)
            instance = new ViewDetailsGpsFormCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command( Resources.CMD_VIEW_GPS_DETAILS );
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().setDisplayable( SimpleLocation.class );
    }
}
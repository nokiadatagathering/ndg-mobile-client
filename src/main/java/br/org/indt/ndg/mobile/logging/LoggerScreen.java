/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.mobile.logging;

import br.org.indt.ndg.lwuit.ui.Screen;
import com.sun.lwuit.Label;

public class LoggerScreen extends Screen{


    protected void loadData() {

    }

    protected void customize() {
        form.addComponent( new Label(Logger.getInstance().lastMessage()) );
    }

}

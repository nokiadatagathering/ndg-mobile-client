/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.model.DisplayableModel;
import br.org.indt.ndg.lwuit.model.Result;
import com.sun.lwuit.CheckBox;

/**
 *
 * @author Alexandre Martini
 */
public class SentResultListCellRenderer extends CheckableListCellRenderer{
    protected CheckBox getCheckBox(DisplayableModel disp, int index){
        CheckBox c = super.getCheckBox(disp, index);
        Result result = (Result) disp;
        if(result.getPhisicallyFileName().startsWith("p_")){
            c.setIcon(Screen.getRes().getImage("hourglass"));
        }
        return c;
    }

    public SentResultListCellRenderer(int realSize) {
        super(realSize);
    }

}

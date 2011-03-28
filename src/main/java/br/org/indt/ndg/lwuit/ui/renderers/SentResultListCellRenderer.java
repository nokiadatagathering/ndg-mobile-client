package br.org.indt.ndg.lwuit.ui.renderers;

import br.org.indt.ndg.lwuit.model.CheckableItem;
import br.org.indt.ndg.lwuit.model.Result;
import br.org.indt.ndg.lwuit.ui.Screen;

/**
 *
 * @author Alexandre Martini
 */
public class SentResultListCellRenderer extends CheckableListCellRenderer{

    public SentResultListCellRenderer() {
        super();
    }

    protected void prepareCheckBox(CheckableItem disp){
        super.prepareCheckBox(disp);
        Result result = (Result) disp;
        if(result.getPhisicallyFileName().startsWith("p_")){
            m_rendererCheckbox.setIcon(Screen.getRes().getImage("hourglass"));
        }
    }
}
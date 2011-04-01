package br.org.indt.ndg.lwuit.extended;

import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import com.sun.lwuit.Component;
import com.sun.lwuit.Painter;
import com.sun.lwuit.events.FocusListener;
import com.sun.lwuit.painter.BackgroundPainter;

/**
 *
 * @author Divus Tecnologia
 */
public class ComboBox extends com.sun.lwuit.ComboBox implements FocusListener{

    protected Painter focusBGPainter;
    protected BackgroundPainter bgPainter;
    private boolean hasOther;
    private String txtOther;

    public ComboBox() {
        super();
        focusBGPainter = new ListFocusBGPainter(this);
        bgPainter = new ListBGPainter(this);
        addFocusListener(this);
        getStyle().setBgPainter(bgPainter);
        getSelectedStyle().setBgPainter(focusBGPainter);
    }

    public void focusGained(Component cmp) {
        getStyle().setBgPainter(focusBGPainter);
        getStyle().setFont( NDGStyleToolbox.getInstance().listStyle.selectedFont );
    }

    public void focusLost(Component cmp) {
        getStyle().setBgPainter(bgPainter);
        getStyle().setFont( NDGStyleToolbox.getInstance().listStyle.unselectedFont );
    }

    public void setOther(boolean _val) {
        hasOther = _val;
    }

    public boolean hasOther() {
        return hasOther;
    }

    public void setOtherText(String _val) {
        txtOther = _val;
    }

    public String getOtherText() {
        return txtOther;
    }
}

package br.org.indt.ndg.lwuit.extended;

import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import com.sun.lwuit.Component;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Painter;
import com.sun.lwuit.events.FocusListener;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.painter.BackgroundPainter;

/**
 *
 * @author mluz
 */
public class RadioButton extends com.sun.lwuit.RadioButton implements FocusListener {

    protected Painter focusBGPainter;
    protected BackgroundPainter bgPainter;
    private boolean hasOther;
    private String txtOther;

    public RadioButton(String text) {
        super(text);
        focusBGPainter = new FocusBGPainter();
        bgPainter = new BackgroundPainter(this);
        addFocusListener(this);
        getSelectedStyle().setFont(NDGStyleToolbox.fontMediumBold, false);
        getUnselectedStyle().setFont(NDGStyleToolbox.fontMedium, false);
    }

    public void focusGained(Component cmp) {
        getStyle().setBgPainter(focusBGPainter);
        getStyle().setFont( NDGStyleToolbox.fontMediumBold );
    }

    public void focusLost(Component cmp) {
        getStyle().setBgPainter(bgPainter);
        getStyle().setFont( NDGStyleToolbox.fontMedium );
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

    class FocusBGPainter implements Painter {

        public void paint(Graphics g, Rectangle rect) {
            int width = rect.getSize().getWidth();
            int height = rect.getSize().getHeight();
            
            int endColor = NDGStyleToolbox.getInstance().listStyle.bgSelectedEndColor;
            int startColor = NDGStyleToolbox.getInstance().listStyle.bgSelectedStartColor;
            g.fillLinearGradient(startColor, endColor, rect.getX(), rect.getY(), width, height, false);

            int borderColor = NDGStyleToolbox.getInstance().listStyle.bgUnselectedColor;
            g.setColor(borderColor);
            g.fillRect(rect.getX(), rect.getY(), 1, 1);
            g.fillRect(rect.getX()+width-1, rect.getY(), 1, 1);
            g.fillRect(rect.getX(), rect.getY()+height-1, 1, 1);
            g.fillRect(rect.getX()+width-1, rect.getY()+height-1, 1, 1);
        }
    }
}

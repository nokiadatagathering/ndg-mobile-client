/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.extended;

import br.org.indt.ndg.lwuit.ui.*;
import com.sun.lwuit.Component;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Painter;
import com.sun.lwuit.events.FocusListener;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.painter.BackgroundPainter;
import com.sun.lwuit.plaf.UIManager;

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
    }

    public void focusGained(Component cmp) {
        getStyle().setBgPainter(focusBGPainter);
        getStyle().setFont(Screen.getRes().getFont("NokiaSansWideBold15"));
    }

    public void focusLost(Component cmp) {
        getStyle().setBgPainter(bgPainter);
        getStyle().setFont(Screen.getRes().getFont("NokiaSansWide15"));
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

            int endColor = UIManager.getInstance().getComponentStyle("").getFgColor();
            int startColor = getStyle().getBgSelectionColor();
            g.fillLinearGradient(startColor, endColor, rect.getX(), rect.getY(), width, height, false);

            int borderColor = getStyle().getBgColor();
            g.setColor(borderColor);
            g.fillRect(rect.getX(), rect.getY(), 1, 1);
            g.fillRect(rect.getX()+width-1, rect.getY(), 1, 1);
            g.fillRect(rect.getX(), rect.getY()+height-1, 1, 1);
            g.fillRect(rect.getX()+width-1, rect.getY()+height-1, 1, 1);

        }

    }

}

package br.org.indt.ndg.lwuit.extended;

import br.org.indt.ndg.lwuit.ui.NDGLookAndFeel;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import com.sun.lwuit.Component;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.Painter;
import com.sun.lwuit.events.FocusListener;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.painter.BackgroundPainter;

/**
 *
 * @author mluz
 */
public class CheckBox extends com.sun.lwuit.CheckBox implements FocusListener {

    protected Painter focusBGPainter;
    protected BackgroundPainter bgPainter;
    private boolean hasOther;
    private String txtOther;
    private PointerListener mPointerListener = null;

    public CheckBox(String text) {
        super(text);
        focusBGPainter = new FocusBGPainter();
        bgPainter = new BackgroundPainter(this);
        addFocusListener(this);
        hasOther = false;
        getSelectedStyle().setFont(NDGStyleToolbox.fontMediumBold, false);
        getUnselectedStyle().setFont(NDGStyleToolbox.fontMedium, false);
    }

    public CheckBox(String text, PointerListener listener) {
        this(text);
        mPointerListener = listener;
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

    public void paint(Graphics g) {
        super.paint(g);
        if ( isSelected() && hasOther()) {
            Image arrow = NDGLookAndFeel.getRightContextMenuImage(getWidth());
            int x = getX() + getWidth() - (int)(arrow.getWidth()*1.5);
            int y = getY() + (getHeight() - arrow.getHeight())/2;
            g.drawImage(arrow, x, y);
        }
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

    /**
     * Assumptions when listener should be informed:
     * - it MUST have details
     * - if checkbox is selected and right 1/5 od component is pointed
     */
    public void pointerPressed(int x, int y) {
        if ( mPointerListener!= null && hasOther() && (x > (int)(0.8 * getWidth())) && isSelected() ) {
            mPointerListener.pointerPressed(x, y);
        } else {
            super.pointerPressed(x,y);
        }
    }

    public void pointerReleased(int x, int y) {
        if ( mPointerListener!= null && hasOther() && (x > (int)(0.8 * getWidth())) && isSelected() ) {
            mPointerListener.pointerPressed(x, y);
        } else {
            super.pointerReleased(x,y);
        }
    }

}

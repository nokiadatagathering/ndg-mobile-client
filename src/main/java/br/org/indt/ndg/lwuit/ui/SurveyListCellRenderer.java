package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.extended.ListBGPainter;
import br.org.indt.ndg.lwuit.extended.ListFocusBGPainter;
import br.org.indt.ndg.lwuit.model.Survey;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import com.sun.lwuit.Component;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.Painter;
import com.sun.lwuit.list.DefaultListCellRenderer;
import com.sun.lwuit.painter.BackgroundPainter;
import com.sun.lwuit.plaf.Style;

public class SurveyListCellRenderer extends DefaultListCellRenderer {

    final private int PADDING = 5;
    final private int TOUCH_SCREEN_VERTICAL_PADDING = 10;
    private int position = 0;
    protected final BackgroundPainter m_bgPainter;
    protected final Painter m_focusBGPainter;
    protected final Label m_transitionLabel = new Label();

    public SurveyListCellRenderer() {
        super();
        m_bgPainter = new ListBGPainter(this);
        m_focusBGPainter = new ListFocusBGPainter(this);
    }

    public void resetPosition() {
        position = 0;
    }

    public void incrementPosition() {
        if(position + PADDING >= Integer.MAX_VALUE) {
            resetPosition();
        }
        position += PADDING;
    }

    public void paint(Graphics g) {
        if (hasFocus()) {
            String text = getText();
            text = text.concat("   ");
            Style s = getStyle();
            int width = s.getFont().stringWidth(text);
            int fontHeight = s.getFont().getHeight();
            int yPos = getY()+ (getHeight()-fontHeight)/2;
            if (getWidth() >= width + PADDING) { // otherwise a letter is cut
                super.paint(g);
                return;
            }
            int actualPosition = position % width;
            g.setColor(NDGStyleToolbox.getInstance().listStyle.selectedFontColor);
            g.translate(-actualPosition, 0);
            g.drawString(text, getX(), yPos);
            g.translate(width, 0);
            g.drawString(text, getX(), yPos);
            g.translate(actualPosition - width, 0);
        } else {
            super.paint(g);
        }
    }

    protected String getText(Object value) {
        Survey survey = (Survey) value;
        return survey.getName();
    }

    public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {

        super.getListCellRendererComponent(list, value, index, isSelected);
        if (index == 0) {
            setAlignment(LEFT);
        }
        setText(getText(value));
        if (com.sun.lwuit.Display.getInstance().isTouchScreenDevice()) {
            getStyle().setPadding(TOUCH_SCREEN_VERTICAL_PADDING, TOUCH_SCREEN_VERTICAL_PADDING, 0, 0);
        }
        if (isSelected) {
            setFocus(true);
            this.getStyle().setFont(NDGStyleToolbox.getInstance().listStyle.selectedFont);
            getStyle().setFgColor(NDGStyleToolbox.getInstance().listStyle.selectedFontColor);
            getStyle().setBgPainter(m_focusBGPainter);
        } else {
            setFocus(false);
            getStyle().setFont(NDGStyleToolbox.getInstance().listStyle.unselectedFont);
            getStyle().setFgColor(NDGStyleToolbox.getInstance().listStyle.unselectedFontColor);
            getStyle().setBgPainter(m_bgPainter);
        }
        return this;
    }

    public Component getListFocusComponent(List list) {
        m_transitionLabel.setFocus(true);
        m_transitionLabel.getStyle().setMargin(0, 0, 0, 0);
        m_transitionLabel.getStyle().setBgPainter(m_focusBGPainter);
        return m_transitionLabel;
    }
}
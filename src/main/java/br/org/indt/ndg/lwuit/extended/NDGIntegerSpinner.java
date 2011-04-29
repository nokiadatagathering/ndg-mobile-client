package br.org.indt.ndg.lwuit.extended;

import br.org.indt.ndg.lwuit.ui.Screen;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import com.sun.lwuit.Button;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.DataChangedListener;
import com.sun.lwuit.events.FocusListener;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.painter.BackgroundPainter;
import com.sun.lwuit.plaf.Border;
import java.util.Vector;

/**
 *
 * @author tomasz.baniak
 */
public class NDGIntegerSpinner extends Container implements FocusListener {
    final private Button m_decreaseButton = new Button(Screen.getRes().getImage("right_arrow").rotate(180));
    final private Button m_increaseButton = new Button(Screen.getRes().getImage("right_arrow"));
    final private ExtenededTextArea m_valueDisplay = new ExtenededTextArea("0");

    final private BackgroundPainter unfocusedPainter = new UnfocusedBGPainter(this);
    final private BackgroundPainter focusedPainter = new FocusedBGPainter(this);

    final private int INCREASE_BY = 1;
    final private int DECREASE_BY = 1;

    private Vector m_dataChangeClients;
    private int m_maxValue;
    private int m_minValue;

    public NDGIntegerSpinner() {
        super(new BorderLayout());
        init();
        m_maxValue = Integer.MAX_VALUE;
        m_minValue = Integer.MIN_VALUE;
    }

    public NDGIntegerSpinner( int min, int max ) {
        super(new BorderLayout());
        init();
        m_maxValue = max;
        m_minValue = min;
    }

    private void init() {
        m_dataChangeClients = new Vector();
        m_decreaseButton.getSelectedStyle().setBgTransparency(0);
        m_valueDisplay.getSelectedStyle().setBgTransparency(0);
        m_increaseButton.getSelectedStyle().setBgTransparency(0);
        m_valueDisplay.getSelectedStyle().setFgColor(NDGStyleToolbox.getInstance().listStyle.selectedFontColor);
        m_valueDisplay.getSelectedStyle().setFont(NDGStyleToolbox.getInstance().listStyle.selectedFont);
        m_decreaseButton.getUnselectedStyle().setBgTransparency(0);
        m_valueDisplay.getUnselectedStyle().setBgTransparency(0);
        m_increaseButton.getUnselectedStyle().setBgTransparency(0);
        m_valueDisplay.getUnselectedStyle().setFgColor(NDGStyleToolbox.getInstance().listStyle.unselectedFontColor);
        m_valueDisplay.getUnselectedStyle().setFont(NDGStyleToolbox.getInstance().listStyle.unselectedFont);
        m_decreaseButton.setAlignment(CENTER);
        m_valueDisplay.setAlignment(CENTER);
        m_increaseButton.setAlignment(CENTER);
        m_decreaseButton.getUnselectedStyle().setBorder(Border.createEmpty());
        m_valueDisplay.getUnselectedStyle().setBorder(Border.createEmpty());
        m_increaseButton.getUnselectedStyle().setBorder(Border.createEmpty());
        m_decreaseButton.getSelectedStyle().setBorder(Border.createEmpty());
        m_valueDisplay.getSelectedStyle().setBorder(Border.createEmpty());
        m_increaseButton.getSelectedStyle().setBorder(Border.createEmpty());
        m_decreaseButton.getPressedStyle().setBorder(Border.createEmpty());
        m_increaseButton.getPressedStyle().setBorder(Border.createEmpty());
        m_decreaseButton.getStyle().setPadding(0,0,30,30);
        m_increaseButton.getStyle().setPadding(0,0,30,30);
        m_decreaseButton.getSelectedStyle().setPadding(0,0,30,30);
        m_increaseButton.getSelectedStyle().setPadding(0,0,30,30);
        m_increaseButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                addValue(INCREASE_BY);
            }
        });
        m_decreaseButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                subValue(DECREASE_BY);
            }
        });
        m_valueDisplay.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                validateAndCorrectDisplay();
                update();
            }
        });
        m_increaseButton.addFocusListener(new DecreaseIncreaseFocusListener());
        m_decreaseButton.addFocusListener(new DecreaseIncreaseFocusListener());
        m_valueDisplay.addFocusListener(this);
        m_valueDisplay.setNextFocusRight(m_valueDisplay);
        m_valueDisplay.setNextFocusLeft(m_valueDisplay);
        addComponent(BorderLayout.WEST, m_decreaseButton);
        addComponent(BorderLayout.CENTER, m_valueDisplay);
        addComponent(BorderLayout.EAST, m_increaseButton);
    }

    public void setValue( int value ) {
        m_valueDisplay.setText( String.valueOf(value) );
        update();
    }

    public void addDataChangeObserver( DataChangedListener client ) {
        if ( client != null ) {
            m_dataChangeClients.addElement( client );
        }
    }

    private void update() {
        for( int i = 0; i< m_dataChangeClients.size(); i++ ) {
            ((DataChangedListener)m_dataChangeClients.elementAt(i)).dataChanged( DataChangedListener.CHANGED, 0 );
        }
    }

    private void addValue( int addedValue ) {
        int newValue = getValue();
        if( newValue + addedValue > m_maxValue ) {
            newValue = m_maxValue;
        } else {
            newValue += addedValue;
        }
        setValue(newValue);
    }

    private void subValue( int subValue ) {
        int newValue = getValue();
        if( newValue - subValue < m_minValue ) {
            newValue = m_minValue;
        } else {
            newValue -= subValue;
        }
        setValue(newValue);
    }

    /*
     * Validates entered value.
     * If it is not a valid integer entered value is reset to 0.
     */
    private void validateAndCorrectDisplay() {
        int value = 0;
        try {
            value = Integer.parseInt(m_valueDisplay.getText().trim());
        } catch ( NumberFormatException e ) {
            value = 0;
        }
        if ( value > m_maxValue ) {
            value = m_maxValue;
        } else if( value < m_minValue ) {
            value = m_minValue;
        }
        m_valueDisplay.setText(String.valueOf(value));
    }

    /*
     * Returns currently selected value
     * Value is validated before return
     */
    public int getValue() {
        validateAndCorrectDisplay();
        return Integer.parseInt(m_valueDisplay.getText());
    }

    public void setNextFocusDown(Component nextFocusDown) {
        super.setNextFocusDown(nextFocusDown);
        m_valueDisplay.setNextFocusDown(nextFocusDown);
    }
    public void setNextFocusUp(Component nextFocusUp) {
        super.setNextFocusUp(nextFocusUp);
        m_valueDisplay.setNextFocusUp(nextFocusUp);
    }

    public void repaint() {
        if ( hasFocus() || m_decreaseButton.hasFocus() || m_increaseButton.hasFocus() || m_valueDisplay.hasFocus() ) {
            getStyle().setBgPainter(focusedPainter);
        } else {
            getStyle().setBgPainter(unfocusedPainter);
        }
        super.repaint();
    }

    /*
     * Paint whole component background based on it's children focus
     */
    public void focusGained(Component cmp) {
        repaint();
    }
    public void focusLost(Component cmp) {
        repaint();
    }

    /*
     * Painters for selected/unselected spinner states
     */
    class FocusedBGPainter extends BackgroundPainter {
        
        FocusedBGPainter(Component parent) {
            super(parent);
        }

        public void paint(Graphics g, Rectangle rect) {
            int width = rect.getSize().getWidth();
            int height = rect.getSize().getHeight();
            int endColor = NDGStyleToolbox.getInstance().listStyle.bgSelectedEndColor;
            int startColor = NDGStyleToolbox.getInstance().listStyle.bgSelectedStartColor;
            g.fillLinearGradient(startColor, endColor, rect.getX(), rect.getY(), width, height, false);
        }
    };

    class UnfocusedBGPainter extends BackgroundPainter {

        UnfocusedBGPainter(Component parent) {
            super(parent);
        }
        
        public void paint(Graphics g, Rectangle rect) {
            int height = rect.getSize().getHeight();
            g.setColor(NDGStyleToolbox.getInstance().listStyle.bgUnselectedColor);
            g.fillRect(rect.getX(), rect.getY(), rect.getSize().getWidth(), height );
        }
    }

    /*
     * Ensures that focus is returned to value display
     */
    class DecreaseIncreaseFocusListener implements FocusListener {

        public void focusGained(Component cmp) {
            m_valueDisplay.requestFocus();
        }

        public void focusLost(Component cmp) {
        }
    }

    /*
     * Interprets Left/Right keypress as decrease/increase value
     */
    private class ExtenededTextArea extends TextArea {

        private ExtenededTextArea(String string) {
            super(string);
            setConstraint(TextArea.NUMERIC);
        }

        public void keyPressed(int keyCode) {
            int gameAction = Display.getInstance().getGameAction(keyCode);
            if ( gameAction == Display.GAME_LEFT ) {
                subValue(DECREASE_BY);
            } else if ( gameAction == Display.GAME_RIGHT ) {
                addValue(INCREASE_BY);
            } //else {
//                super.keyPressed(keyCode);
// Temporarily removing typed input, will only use the arrows on hw keyboard phones
//            }
        }
    }
}

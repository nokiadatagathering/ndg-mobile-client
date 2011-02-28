package br.org.indt.ndg.lwuit.extended;

import com.sun.lwuit.Display;
import com.sun.lwuit.TextField;
import com.sun.lwuit.events.DataChangedListener;
import com.sun.lwuit.impl.midp.VirtualKeyboard;

/**
 *
 * @author mluz
 */
public class NumericField extends TextField implements DataChangedListener {

    private int length;


    public NumericField(int length) {
        super();
        this.length = length;
        setInputModeOrder(new String[]{"123"});
        setInputMode("123");
        addDataChangeListener(this);
        setOverwriteMode(false);
        setUseSoftkeys(false); // do not use T9 and Clear

        if(Display.getInstance().isTouchScreenDevice()) {
            VirtualKeyboard onScreenKeyboard = new VirtualKeyboard();
            onScreenKeyboard.setInputModeOrder(new String[] {VirtualKeyboard.NUMBERS_MODE} );
            VirtualKeyboard.bindVirtualKeyboard(this, onScreenKeyboard);
        }
    }

    private void checkLength() {
        String text = "" + getText();
        if (text.length()> length) {
            text = text.substring(0, length);
            setText(text);
        }
    }    

    public void dataChanged(int type, int index) {   
        checkLength();
    }
}

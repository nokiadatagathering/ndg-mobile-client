/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.extended;

import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.TextField;
import com.sun.lwuit.events.DataChangedListener;
import com.sun.lwuit.events.FocusListener;

/**
 *
 * @author mluz
 */
public class NumericField extends TextField implements DataChangedListener, FocusListener {

    private int length;
    private boolean decimal;

    public NumericField(int length, boolean decimal) {
        super();
        setInputModeOrder(new String[]{"123"});
        setInputMode("123");
        this.length = length;
        this.decimal = decimal;
        addDataChangeListener(this);
        addFocusListener(this);
        setOverwriteMode(false);
    }


    private void checkNumeric(int index) {
        String text = "" + getText();
        if (text.length()> 0) {
            char c = text.charAt(index-1);
            if (!(c >= '0' && c <= '9') && (c!='.')) {
                if (decimal) {
                    switch (c) {
                        case 'r':
                        case 'R': text = text.replace(c, '1'); break;
                        case 't':
                        case 'T': text = text.replace(c, '2'); break;
                        case 'y':
                        case 'Y': text = text.replace(c, '3'); break;
                        case 'f':
                        case 'F': text = text.replace(c, '4'); break;
                        case 'g':
                        case 'G': text = text.replace(c, '5'); break;
                        case 'h':
                        case 'H': text = text.replace(c, '6'); break;
                        case 'v':
                        case 'V': text = text.replace(c, '7'); break;
                        case 'b':
                        case 'B': text = text.replace(c, '8'); break;
                        case 'n':
                        case 'N': text = text.replace(c, '9'); break;
                        case 'm':
                        case 'M': text = text.replace(c, '0'); break;
                        case 'k':
                        case 'K': text = addMinus(c, text, index); break;
                        case ',':
                        case 'U':
                        case 'u':
                        case '*':
                        case ':': text = addDot(c, text, index);//text = text.replace(c, '.'); break;
                        default : text = text.substring(0, index-1) + text.substring(index); break;
                    }
                }
                else {
                    switch (c) {
                        case 'r':
                        case 'R': text = text.replace(c, '1'); break;
                        case 't':
                        case 'T': text = text.replace(c, '2'); break;
                        case 'y':
                        case 'Y': text = text.replace(c, '3'); break;
                        case 'f':
                        case 'F': text = text.replace(c, '4'); break;
                        case 'g':
                        case 'G': text = text.replace(c, '5'); break;
                        case 'h':
                        case 'H': text = text.replace(c, '6'); break;
                        case 'v':
                        case 'V': text = text.replace(c, '7'); break;
                        case 'b':
                        case 'B': text = text.replace(c, '8'); break;
                        case 'n':
                        case 'N': text = text.replace(c, '9'); break;
                        case 'm':
                        case 'M': text = text.replace(c, '0'); break;
                        case 'k':
                        case 'K': text = addMinus(c, text, index); break;
                        default : text = text.substring(0, index-1) + text.substring(index); break;
                    }
                }
                setText(text);
                commitChange();
            } else {
                if (c == '.') {
                    if ((!decimal) || (text.indexOf('.') != text.lastIndexOf('.'))) {
                        text = text.substring(0, index-1) + text.substring(index);
                        setText(text);
                        commitChange();
                    }
                }

            }
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
        if (type == 1) checkNumeric(index);
    }


    protected Command installCommands(Command clear, Command t9) {
        Form f = getComponentForm();
        Command[] originalCommands = new Command[f.getCommandCount()];
        for(int iter = 0 ; iter < originalCommands.length ; iter++) {
            originalCommands[iter] = f.getCommand(iter);
        }
        Command retVal = super.installCommands(clear, t9);
        f.removeAllCommands();
        for(int iter = originalCommands.length - 1 ; iter >= 0 ; iter--) {
            f.addCommand(originalCommands[iter]);
        }
        originalCommands = null;
        return retVal;
    }

    public void focusGained(Component arg0) {
        keyPressed(Display.GAME_FIRE);
    }

    public void focusLost(Component arg0) {
    }

    private String addMinus(char c, String text, int index) {
        if (text.length() == 1) {
            text = text.replace(c, '-');
        }
        else {
            text = text.substring(0, index-1) + text.substring(index);
        }

        return text;
    }

    private String addDot(char c, String text, int index) {
        if (text.length() > 2) {
            text = text.replace(c, '.');
        }
        else {
            text = text.substring(0, index-1) + text.substring(index);
        }

        return text;
    }
    
}

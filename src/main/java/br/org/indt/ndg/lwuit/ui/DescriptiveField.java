/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui;

import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.TextField;
import com.sun.lwuit.events.DataChangedListener;
import com.sun.lwuit.events.FocusListener;
import java.util.Hashtable;

/**
 *
 * @author mluz
 */
public class DescriptiveField extends TextField implements DataChangedListener, FocusListener {
    
    private int length;
    
    public static final int NONE = -1;
    public static final int GRAVE = 0;
    public static final int ACUTE = 1;
    public static final int CIRCUMFLEX = 2;
    public static final int TILDE = 3;
    private int accent = NONE;

    public static Hashtable hChar = null;


    public DescriptiveField(int length) {
        super();
        setInputMode("abc");
        this.length = length;
        addDataChangeListener(this);
        addFocusListener(this);

        if (hChar == null) {
            hChar = new Hashtable();
            hChar.put("A", new Integer(192));
            hChar.put("E", new Integer(200));
            hChar.put("I", new Integer(204));
            hChar.put("N", new Integer(206));
            hChar.put("O", new Integer(210));
            hChar.put("U", new Integer(217));

            hChar.put("a", new Integer(224));
            hChar.put("e", new Integer(232));
            hChar.put("i", new Integer(236));
            hChar.put("n", new Integer(238));
            hChar.put("o", new Integer(242));
            hChar.put("u", new Integer(249));
        }
        
    }

    private void checkLength() {
        String text = "" + getText();
        if (text.length()> length) {
            text = text.substring(0, length);
            setText(text);
        }
    }

    private void checkDesc(int index) {
        boolean showDialog = false;
        String text = "" + getText();
        if (text.length() > 0) {
            char c = text.charAt(index-1);
            switch (c) {
                case '*':
                    showDialog = true;
                    text = text.substring(0, index-1) + text.substring(index);
                    break;
            }
            
            if (accent != NONE) {
                String strChar = "" + c;
                if (hChar.containsKey(strChar)) {
                    int iChar = ((Integer)hChar.get(strChar)).intValue();
                    iChar += accent;
                    char cChar = (char) iChar;
                    text = text.substring(0, index-1) + cChar;
                }
                accent = NONE;
            }
            else {
                Character char2 = new Character(c);
                switch (char2.hashCode()) {
                    // Acute accent
                    case 61441: text = text.substring(0, index-1) + text.substring(index); // add nothing to text
                                accent = ACUTE;
                                break;
                     // Grave accent
                    case 61442: text = text.substring(0, index-1) + text.substring(index); // add nothing to text
                                accent = GRAVE;
                                break;
                    // Tilde accent
                    case 61443: text = text.substring(0, index-1) + text.substring(index); // add nothing to text
                                accent = TILDE;
                                break;
                     // Circumflex accent
                    case 61444: text = text.substring(0, index-1) + text.substring(index); // add nothing to text
                                accent = CIRCUMFLEX;
                                break;
                    default: accent = NONE; break;
                }
            }

            setText(text);
            commitChange();
            if (showDialog) {
                this.showSymbolDialog();
            }
        }
    }

    public void dataChanged(int type, int index) {
        checkLength();
        if (type == 1) checkDesc(index);
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

}

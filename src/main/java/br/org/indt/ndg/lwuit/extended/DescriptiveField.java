package br.org.indt.ndg.lwuit.extended;

import com.sun.lwuit.Command;
import com.sun.lwuit.Form;
import com.sun.lwuit.TextField;
import com.sun.lwuit.events.DataChangedListener;

/**
 *
 * @author mluz
 */
public class DescriptiveField extends TextField implements DataChangedListener {
    
    private int length;
    
    public DescriptiveField(int length) {
        super();
        this.length = length;
        setInputMode("abc");
        setGrowByContent(true);
        setSingleLineTextArea(false);
        addDataChangeListener(this);
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

 
    protected Command installCommands(Command clear, Command t9) {
        Form f = getComponentForm();
        Command[] originalCommands = new Command[f.getCommandCount()];
        for(int iter = 0 ; iter < originalCommands.length ; iter++) {
            originalCommands[iter] = f.getCommand(iter);
        }
        f.removeAllCommands();
        Command retVal = super.installCommands(clear, t9);
        for(int iter = originalCommands.length - 1 ; iter >= 0 ; iter--) {
            f.addCommand(originalCommands[iter]);
        }
        return retVal;
    }

    protected void removeCommands(Command clear, Command t9, Command originalClear) {
        //when context menu is shown, text field is hidden and deinitialize() is called on it, which calls removeCommands
        //this is a workaround
        if(isInitialized()) {
            super.removeCommands(clear, t9, originalClear);
        }
    }   
}

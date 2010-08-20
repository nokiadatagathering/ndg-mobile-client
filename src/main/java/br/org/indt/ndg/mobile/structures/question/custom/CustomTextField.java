package br.org.indt.ndg.mobile.structures.question.custom;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;

public class CustomTextField extends Form implements CommandListener {
    
    private TextField tf;
    private boolean finalQuestion = false;
    
    public CustomTextField(String description) {
        super(description);
        
        //addCommand(Resources.CMD_NEXT);
        addCommand(Resources.CMD_OK);
        setCommandListener(this);
        
        createWidget();
    }
    
    public void createWidget() {
        
        tf = new TextField("", null, 50, TextField.ANY);
        
        this.append(tf);
    }
    
    public void setFinalQuestion(boolean _bool) {
        finalQuestion = _bool;
    }
    
    public void commandAction(Command command, Displayable displayable) {
        if (command == Resources.CMD_OK)
            AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getPreviousScreen());
        else if (command == Resources.CMD_NEXT) {
            AppMIDlet.getInstance().getQuestionList().updateList();
            
            if (finalQuestion) {
                AppMIDlet.getInstance().getCategoryList().updateList();
                AppMIDlet.getInstance().getCategoryList().getNextQuestion();
            } else {
                AppMIDlet.getInstance().getQuestionList().getNextQuestion();
            }
        }
    }
    
    public String getString() {
        return tf.getString();
    }
    
    public void setString(String _value) {
        tf.setString(_value);
    }
}

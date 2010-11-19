package br.org.indt.ndg.mobile.structures.question;

import javax.microedition.lcdui.TextField;
import java.io.PrintStream;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.error.NullWidgetException;
import java.util.Hashtable;
import java.util.Vector;

public class TypeTextFieldString extends Question {
    private int length;
    private TextField textWidget = null;

    private Hashtable choices = new Hashtable();
    private Vector choicesOrdered = new Vector();


    public void setLength(int _length) { this.length = _length; }
    public int getLength() { return this.length; }
    
    public Hashtable getChoices() { return choices; }
    public Vector getOrderedChoices() { return choicesOrdered; }


    public TypeTextFieldString() {
        length = 10;
    }
    
    public TextField getWidget() {
        if (isNew()) {            
            if (this.getEdit()) textWidget = new TextField("", null, getLength(), TextField.ANY);
            else textWidget = new TextField("", null, getLength(), TextField.UNEDITABLE);
            setIsNew(false);
        }
        
        return textWidget;
    }

    public void addChoice(String choice, String _other) {
       choices.put(choice, _other);
    }

    public void addChoiceOrdered(String choice) {
        choicesOrdered.addElement(choice);
    }

    public void save(PrintStream _output, StringBuffer bfSMS){
        if (textWidget!=null){// && (textWidget.getString().length() != 0) ) {            
            String temp;
            if(textWidget.getString().length() == 0){
                temp = " ";
            }
            else
                temp = AppMIDlet.getInstance().u2x(textWidget.getString());
            _output.print("<str>");
            _output.print(temp);
            _output.println("</str>");
            bfSMS.append(textWidget.getString());
        }
    }                        
    
    public String getDisplayValue() throws NullWidgetException{
        if(textWidget == null){
            throw new NullWidgetException();
        }
        return textWidget.getString();
    }

    public boolean passConstraints() {
        return true;
    }
    
}
package br.org.indt.ndg.mobile.structures.question;

import javax.microedition.lcdui.TextField;
import java.io.PrintStream;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.AppMIDlet;

public class TypeTextFieldInteger extends Question {
    private TextField intWidget=null;
    private int length;    
    private int low;
    private int high;
    
    public void setLength(int _length) { this.length = _length; }
    public int getLength() { return this.length; }
    
    public TypeTextFieldInteger() {
        low = Resources.NOENTRY;
        high = Resources.NOENTRY;
        length = 10;
    }
    
    public TextField getWidget() {
        if (isNew()) {
            if (this.getEdit()) intWidget = new TextField("", null, getLength(), TextField.NUMERIC);
            //if (this.getEdit()) intWidget = new TextField("", null, getLength(), TextField.NUMERIC);
            else intWidget = new TextField("", null, getLength(), TextField.UNEDITABLE);
            setIsNew(false);
        }
        
        return intWidget;
    }

    public void save(PrintStream _output, StringBuffer bfSMS){
        if (intWidget!=null) {            
            _output.print("<int>");
            _output.print(intWidget.getString());
            _output.println("</int>");           
            bfSMS.append(intWidget.getString());
        }         
    }

    public String getLowConstraint(){
        return String.valueOf(low);
    }

    public String getHighConstraint(){
        return String.valueOf(high);
    }
    
    public void setHighConstraint(String _high) {
        try{
            high = Integer.parseInt(_high);        
        }
        catch(NumberFormatException nfe){
            high = Resources.NOENTRY;
        }
    }
    
    public void setLowConstraint(String _low) {
        try{
            low = Integer.parseInt(_low);        
        }
        catch(NumberFormatException nfe){
            low = Resources.NOENTRY;
        }
    }
    
    private boolean passLowConstraint() {
        boolean result = true;
        if (low == Resources.NOENTRY) result = true;
        else {
            try {
                if (Integer.parseInt(intWidget.getString()) >= low) result = true;
                else {
                    AppMIDlet.getInstance().getGeneralAlert().showAlert(Resources.INTEGER, Resources.VALUE_GREATER + low);
                    result = false;
                }
            }
            catch(NumberFormatException ex) {
                AppMIDlet.getInstance().getGeneralAlert().showAlert(Resources.INTEGER, "Invalid Number!");
                result = false;
            }
        }
        return result;
    }

    private boolean passHighConstraint() {
        boolean result = true;
        if (high == Resources.NOENTRY) result = true;
        else {
            try {
                if (Integer.parseInt(intWidget.getString()) <= high) result = true;
                else {
                    AppMIDlet.getInstance().getGeneralAlert().showAlert(Resources.INTEGER, Resources.VALUE_LOWER + high);
                    result = false;
                }
            }
            catch(NumberFormatException ex) {
                AppMIDlet.getInstance().getGeneralAlert().showAlert(Resources.INTEGER, "Invalid Number!");
                result = false;
            }
        }
        return result;
    }
    
    public boolean passConstraints() {
        if (intWidget.getString().equals("")) {
            //AppMIDlet.getInstance().getGeneralAlert().showAlert("Integer", "Please enter a value.");
            //return false;  //first check if empty string
            intWidget.setString("0");
        }
            
        if (passLowConstraint())
            if (passHighConstraint()) 
                return true;
            else 
                return false;
        else 
            return false;
    }
    
    public String getDisplayValue(){
        return intWidget.getString();
    }
}
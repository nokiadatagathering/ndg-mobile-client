/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.mobile.structures.question;

import javax.microedition.lcdui.TextField;
import java.io.PrintStream;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.AppMIDlet;

/**
 *
  */
public class TypeTextFieldDecimal extends Question{
    private TextField decimalWidget=null;
    private int length;
    private double low;
    private double high;
    
    
    public void setLength(int _length) { this.length = _length; }
    public int getLength() { return this.length; }
    
    public TypeTextFieldDecimal() {
        low = Resources.NOENTRY;
        high = Resources.NOENTRY;
        length = 10;
    }
    
    public TextField getWidget() {
        if (isNew()) {
            if (this.getEdit()) decimalWidget = new TextField("", null, getLength(), TextField.DECIMAL);            
            else decimalWidget = new TextField("", null, getLength(), TextField.UNEDITABLE);
            setIsNew(false);
        }
        
        return decimalWidget;
    }
    
    public void save(PrintStream _output, StringBuffer bfSMS){
        if (decimalWidget!=null) {            
            _output.print("<decimal>");
            _output.print(decimalWidget.getString());
            _output.println("</decimal>");           
            bfSMS.append(decimalWidget.getString());
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
            high = Double.parseDouble(_high);        
        }
        catch(NumberFormatException nfe){
            high = Resources.NOENTRY;
        }
    }
    
    public void setLowConstraint(String _low) {        
        try{
            low = Double.parseDouble(_low);        
        }
        catch(NumberFormatException nfe){
            low = Resources.NOENTRY;
        }
    }
    
    private boolean passLowConstraint() {
        if (low == Resources.NOENTRY) return true;
        else {
            if (Double.parseDouble(decimalWidget.getString()) >= low) return true;            
            else {
                AppMIDlet.getInstance().getGeneralAlert().showAlert("Decimal", "Value is not >= " + low);
                return false;
            }
        }
    }
    
    private boolean passHighConstraint() {
        if (high == Resources.NOENTRY) return true;
        else {
            if (Double.parseDouble(decimalWidget.getString()) <= high) return true;            
            else {
                AppMIDlet.getInstance().getGeneralAlert().showAlert("Decimal", "Value is not <= " + high);
                return false;
            }
        }
    }
    
    public boolean passConstraints() {
        if (decimalWidget.getString().equals("")) {
            //AppMIDlet.getInstance().getGeneralAlert().showAlert("Integer", "Please enter a value.");
            //return false;  //first check if empty string
            decimalWidget.setString("0.0");
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
        return decimalWidget.getString();
    }

}

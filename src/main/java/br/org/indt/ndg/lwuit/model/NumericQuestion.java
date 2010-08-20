/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.model;

import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import br.org.indt.ndg.mobile.Resources;
import java.io.PrintStream;

/**
 *
 * @author mluz
 */
public class NumericQuestion extends Question {

    private boolean decimal;
    private int length;
    private double low;
    private double high;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isDecimal() {
        return decimal;
    }

    public void setDecimal(boolean decimal) {
        this.decimal = decimal;
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
        boolean result = true;
        String strValue = (String) this.getAnswer().getValue();
        if ( (low == Resources.NOENTRY) || (strValue.equals("")) ) result = true;
        else {
            double value = Double.parseDouble(strValue);
            if (value >= low) result = true;
            else {
                String strTitle;
                if (decimal) strTitle = Resources.DECIMAL;
                else strTitle = Resources.INTEGER;
                GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
                GeneralAlert.getInstance().show(strTitle, Resources.VALUE_GREATER + low, GeneralAlert.WARNING);
                result = false;
            }
        }
        return result;
    }

    private boolean passHighConstraint() {
        boolean result = true;
        String strValue = (String) this.getAnswer().getValue();
        if ( (high == Resources.NOENTRY) || (strValue.equals("")) ) result = true;
        else {
            double value = Double.parseDouble(strValue);
            if (value <= high) result = true;
            else {
                String strTitle;
                if (decimal) strTitle = Resources.DECIMAL;
                else strTitle = Resources.INTEGER;
                GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
                GeneralAlert.getInstance().show(strTitle, Resources.VALUE_LOWER + high, GeneralAlert.WARNING);
                result = false;
            }
        }
        return result;
    }

    public boolean passConstraints() {
        if (passLowConstraint())
            if (passHighConstraint())
                return true;
            else
                return false;
        else
            return false;
    }

    public void save(PrintStream _output){
        String value = (String)this.getAnswer().getValue();
        String type = "";
        if(decimal){
            type = "decimal";
        }else{
            type = "int";
        }
        if (value!=null) {
            _output.print("<" + type + ">");
            _output.print(value);
            _output.println("</" + type + ">");
        }
    }

}

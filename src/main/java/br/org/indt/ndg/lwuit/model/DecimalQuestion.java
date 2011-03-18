package br.org.indt.ndg.lwuit.model;

import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import br.org.indt.ndg.mobile.Resources;

public class DecimalQuestion extends NumericQuestion {
    private double low;
    private double high;

   public void setHighConstraint(String _high) {
        try{
            high = Double.parseDouble(_high);
        }
        catch(NumberFormatException nfe){
            high = Double.POSITIVE_INFINITY;
        }
    }

    public void setLowConstraint(String _low) {
        try{
            low = Double.parseDouble(_low);
        }
        catch(NumberFormatException nfe){
            low = Double.NEGATIVE_INFINITY;
        }
    }

     protected boolean passLowConstraint( NumericAnswer aAnswer ) {
        boolean result = true;
        String strValue = (String)aAnswer.getValue();
        if ( strValue.equals("") ) result = true;
        else {
            try
            {
                double value = Double.parseDouble(strValue);
                if (value >= low) result = true;
                else {
                    GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
                    GeneralAlert.getInstance().show(Resources.DECIMAL, Resources.VALUE_GREATER + low, GeneralAlert.WARNING);
                    result = false;
                }
            }
            catch(NumberFormatException ex)
            {
                GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
                GeneralAlert.getInstance().show(Resources.DECIMAL, Resources.NOTPROPERDECIMAL +" \""+strValue+"\"", GeneralAlert.WARNING);
                result = false;
            }
        }
        return result;
    }

    protected boolean passHighConstraint( NumericAnswer aAnswer ) {
        boolean result = true;
        String strValue = (String)aAnswer.getValue();
        if ( strValue.equals("") ) result = true;
        else {
            try
            {
                double value = Double.parseDouble(strValue);
                if (value <= high) result = true;
                else {
                    GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
                    GeneralAlert.getInstance().show( Resources.DECIMAL, Resources.VALUE_LOWER + high, GeneralAlert.WARNING);
                    result = false;
                }
            }
            catch(NumberFormatException ex)
            {
                GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
                GeneralAlert.getInstance().show(Resources.DECIMAL, Resources.NOTPROPERDECIMAL + " \""+strValue+"\"", GeneralAlert.WARNING);
                result = false;
            }
        }
        return result;
    }

    public String getType()
    {
        return "_decimal";
    }

    public NDGAnswer getAnswerModel() {
        return new DecimalAnswer();
    }
}

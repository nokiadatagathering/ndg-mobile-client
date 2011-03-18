package br.org.indt.ndg.lwuit.model;

public class DecimalAnswer extends NumericAnswer {

    public String getValueString() {
        if( getValue() != null )
        {
            return (String)getValue();
        }
        else
        {
            return "";
        }
    }

    public String getElementName() {
        return "decimal";
    }
}

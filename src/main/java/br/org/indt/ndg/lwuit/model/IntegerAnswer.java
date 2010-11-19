package br.org.indt.ndg.lwuit.model;

public class IntegerAnswer extends NumberAnswer {
    
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
}
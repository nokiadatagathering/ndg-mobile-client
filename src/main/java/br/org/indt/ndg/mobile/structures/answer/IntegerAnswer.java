package br.org.indt.ndg.mobile.structures.answer;

public class IntegerAnswer extends Answer {
    private int value=0;
    
    public void setValue(int _value) {        
        value = _value; 
    }
    public int getValue() { return value; }    
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.mobile.structures.answer;

public class DecimalAnswer extends Answer {
    private double value=0;
    
    public void setValue(double _value) {        
        value = _value; 
    }
    public double getValue() { return value; }   

}

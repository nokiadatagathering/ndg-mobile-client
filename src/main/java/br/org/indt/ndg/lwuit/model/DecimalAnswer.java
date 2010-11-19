/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.model;

public class DecimalAnswer extends NumberAnswer {
    
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

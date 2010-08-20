/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.mobile.util;


public class Utils {
    public static String convertDoubleNumberFromSciNotation(String numberInScientificNotation){
        int index = -1;
        if( (index = numberInScientificNotation.lastIndexOf('E')) != -1) {
            short length = (short) numberInScientificNotation.length();
            String exponential = numberInScientificNotation.substring(index + 1, length);
            String prefix = numberInScientificNotation.substring(0, 1);
            String suffix = numberInScientificNotation.substring(2, index);
            int expo = Integer.parseInt(exponential);
            
            String number = prefix + suffix;
            if(suffix.length() < expo){
                int numberOfZerosToAdd = expo - suffix.length();
                int i = 0;
                while(i < numberOfZerosToAdd){
                    number += "0";
                    i++;
                }
            }
            return number;
        }
        return numberInScientificNotation;
    }
}

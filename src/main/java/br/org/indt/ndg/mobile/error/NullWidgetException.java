/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.mobile.error;

public class NullWidgetException extends Exception {
    public NullWidgetException(){
        super("Widget Should Not be Null");
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.model;

/**
 *
 * @author mluz
 */
public class Answer extends NDGAnswer{

    private Object value;

    public Answer(){}

    public Answer(Object value){
        this.value = value;
    }

    public Object getValue(){
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}

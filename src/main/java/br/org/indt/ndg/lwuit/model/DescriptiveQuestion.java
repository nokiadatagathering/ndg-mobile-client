/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.model;

import br.org.indt.ndg.mobile.AppMIDlet;
import java.io.PrintStream;
import java.util.Vector;

/**
 *
 * @author mluz
 */
public class DescriptiveQuestion extends Question {

    private int length;
    private Vector choices;
    private Vector others;
    private Vector othersText;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getChoiceText(int index) {
        if(index<choices.size()){
            return (String)choices.elementAt(index);
        }
        return "";
    }

    public Vector getChoices() {
        return choices;
    }

    public void setChoices(Vector choices) {
        this.choices = choices;
    }

    public Vector getOthers() {
        return others;
    }

    public void setOthers(Vector others) {
        this.others = others;
    }

    public void setOthersText(Vector othersText) {
        this.othersText = othersText;
    }

    public Vector getOthersText() {
        return othersText;
    }

    public void save(PrintStream _output){
        if (this.getAnswer().getValue()!=null){
            String value = (String)this.getAnswer().getValue();
            String temp;
            if(value.length() == 0){
                temp = " ";
            }
            else
                temp = AppMIDlet.getInstance().u2x(value);
            _output.print("<str>");
            _output.print(temp);
            _output.println("</str>");
            
        }
    }
    

}

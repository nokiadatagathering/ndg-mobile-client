package br.org.indt.ndg.lwuit.model;

import java.util.Vector;

/**
 *
 * @author mluz
 */
public class DescriptiveQuestion extends NDGQuestion {

    private int length;
    private Vector choices = new Vector();
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

    public void addChoice(String choices) {
        this.choices.addElement( choices );
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

    public NDGAnswer getAnswerModel() {
        return new DescriptiveAnswer();
    }
}

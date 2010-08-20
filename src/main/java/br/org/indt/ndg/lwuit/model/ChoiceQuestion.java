/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.model;

import java.io.PrintStream;
import java.util.Vector;

/**
 *
 * @author mluz
 */
public class ChoiceQuestion extends Question {

    private Vector choices;
    private Vector others;
    private Vector othersText;
    private boolean exclusive;
    private boolean inverse;
    private int choiceItem;
    private int catTo;
    private int skipTo;
    private boolean skipEnabled;

    //get and set skip logic variable methods
    public void setSkipEnabled(boolean _bool) { skipEnabled = _bool; }
    public boolean getSkipEnabled() { return skipEnabled; }
    
    public void setCatTo(int _val) { catTo = _val; }
    public int getCatTo() {return catTo;}

    public void setSkipTo(int _val) { skipTo = _val; }
    public int getSkipTo() {return skipTo;}
    
    public void setChoiceItem(int _val) { choiceItem = _val; }
    public int getChoiceItem() {return choiceItem; }
    
    public void setInverse(boolean _val) { this.inverse = _val; }
    public boolean isInverse() { return inverse; }
    /////////////////////////////////////////

    public boolean isExclusive() {
        return exclusive;
    }

    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
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

    public Vector getOthersText() {
        return othersText;
    }

    public String getChoiceText(int index) {
        if(index<choices.size()){
            return (String)choices.elementAt(index);
        }
        return "";
    }

    public void setOthersText(Vector othersText) {
        this.othersText = othersText;
    }

    public void save(PrintStream _output){
        Vector vSelecteds = null;

        if(exclusive){
            vSelecteds = new Vector();
            if (!((String)this.getAnswer().getValue()).equals("")) {
                vSelecteds.addElement((String) this.getAnswer().getValue());
            }
        }else {
            vSelecteds = (Vector) this.getAnswer().getValue();
        }

        for (int index=0; index < vSelecteds.size(); index++) {
            String selectedIndex = (String) vSelecteds.elementAt(index);
            if (((String) others.elementAt(Integer.parseInt(selectedIndex))).equals("1")) {
                _output.print("<other " + "index=\"" + selectedIndex + "\">");
                _output.print(othersText.elementAt(Integer.parseInt(selectedIndex)));
                _output.println("</other>");
            } else {
                _output.print("<item>");
                _output.print(selectedIndex);
                _output.println("</item>");
            }
        }
    }

}

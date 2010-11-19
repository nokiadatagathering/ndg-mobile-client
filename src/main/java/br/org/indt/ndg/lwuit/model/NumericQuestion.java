/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.model;

import java.io.PrintStream;

/**
 *
 * @author mluz
 */
public abstract class NumericQuestion extends Question {
    private int length;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    abstract public void setHighConstraint(String _high);
    abstract public void setLowConstraint(String _low);

    abstract protected boolean passLowConstraint();
    abstract protected boolean passHighConstraint();
    abstract public String GetType();

    public boolean passConstraints() {
        if (passLowConstraint())
            if (passHighConstraint())
                return true;
            else
                return false;
        else
            return false;
    }

    public void save(PrintStream _output){
        String value = ((NumberAnswer)this.getAnswer()).getValueString();

        if (value!=null) {
            _output.print("<" + GetType() + ">");
            _output.print(value);
            _output.println("</" + GetType() + ">");
        }
    }
}

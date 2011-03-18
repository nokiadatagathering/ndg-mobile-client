package br.org.indt.ndg.lwuit.model;

import java.io.PrintStream;

public abstract class NumericAnswer extends NDGAnswer {

    public NumericAnswer() {
        setValue("");
    }

    public void save( PrintStream _output ){
        String value = getValueString();

        if ( value!=null ) {
            _output.print("<" + getElementName() + ">");
            _output.print(value);
            _output.println("</" + getElementName() + ">");
        }
    }

    public abstract String getValueString();

    public abstract String getElementName();
}

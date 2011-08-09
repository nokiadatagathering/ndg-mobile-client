package br.org.indt.ndg.lwuit.model;

import br.org.indt.ndg.mobile.Utils;
import java.io.PrintStream;

public class DescriptiveAnswer extends NDGAnswer {

    public DescriptiveAnswer() {
        super();
        setValue("");
    }

    public void save( PrintStream _output ){
        String value = (String)getValue();
        if ( value!=null ){
            String temp;
            if(value.length() == 0) {
                temp = " ";
            }
            else {
                temp = Utils.u2x(value);
            }
        _output.print("<str>");
        _output.print(temp);
        _output.println("</str>");
        }
    }
}

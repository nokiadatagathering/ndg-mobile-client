package br.org.indt.ndg.lwuit.model;

import java.io.PrintStream;

public class BoolAnswer extends NDGAnswer {
    private String index="0";

    public void setIndex( String _index ) {
        index = _index;
    }

    public String getIndex() {
        return index;
    }

    public void save( PrintStream _output ) {
 //       throw new UnsupportedOperationException("Not supported yet.");
    }
}

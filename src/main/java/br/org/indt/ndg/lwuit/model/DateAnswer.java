package br.org.indt.ndg.lwuit.model;

import java.io.PrintStream;
import java.util.Date;

public class DateAnswer extends NDGAnswer {

    public DateAnswer() {
        super();
        Date date = new Date();
        Long datelong = new Long(date.getTime());
        setValue( String.valueOf( datelong.longValue() ) );
    }

    public void setDate(long _date) {
        setValue( String.valueOf( _date) );
    }

    public long getDate() {
        return Long.parseLong( (String)getValue() );
    }

    public void save( PrintStream _output ){
        String value = (String)getValue();
        if (value!=null) {
            _output.print("<date>");
            _output.print( value );
            _output.println("</date>");
        }
    }
}

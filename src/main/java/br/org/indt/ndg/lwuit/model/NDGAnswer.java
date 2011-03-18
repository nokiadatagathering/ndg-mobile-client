package br.org.indt.ndg.lwuit.model;

import java.io.PrintStream;

/**
 *
 * @author alexandre martini
 */
public abstract class NDGAnswer {
    private int answer_id;
    private String type;
    private boolean visited;

    private Object value;


    public Object getValue(){
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setType(String _type) {
        type = _type;
    }

    public void setId( int _id ) {
        answer_id = _id;
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return answer_id;
    }

    public boolean getVisited() {
        return visited;
    }

    public void setVisited(String _boolean) {
        if (_boolean.equals("true")) visited=true;
        else visited=false;
    }

    public void setVisited( boolean  aVisited ) {
        visited = aVisited;
    }

    abstract public void save( PrintStream _output );
}

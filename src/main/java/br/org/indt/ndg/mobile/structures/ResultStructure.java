package br.org.indt.ndg.mobile.structures;

import br.org.indt.ndg.lwuit.model.CategoryAnswer;
import java.util.Vector;
import java.util.Hashtable;

public class ResultStructure {

    private String latitude = null;
    private String longitude = null;
    public String getLongitude(){ return longitude; }
    public void setLongitude(String _longitude) { longitude = _longitude; }
    public String getLatitude(){ return latitude; }
    public void setLatitude(String _latitude) { latitude = _latitude; }

    private Vector/*<CategoryAnwser>*/ answers = new Vector();

    public void addAnswer( CategoryAnswer _answer) {
        answers.addElement(_answer);
    }

    public Hashtable getAnswers( int _category ) {
        return (Hashtable) answers.elementAt(_category );
    }

    public CategoryAnswer getCategoryAnswers( String _categoryId ) {
        return (CategoryAnswer) answers.elementAt( Integer.parseInt(_categoryId) - 1 );
    }

    public Vector/*<CategoryAnwser>*/ getAllAnwsers() {
        return answers;
    }
}
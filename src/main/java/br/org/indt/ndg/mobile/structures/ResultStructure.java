package br.org.indt.ndg.mobile.structures;

import java.util.Vector;
import java.util.Hashtable;

public class ResultStructure {

    private String latitude = null;
    private String longitude = null;
    public String getLongitude(){ return longitude; }
    public void setLongitude(String _longitude) { longitude = _longitude; }
    public String getLatitude(){ return latitude; }
    public void setLatitude(String _latitude) { latitude = _latitude; }

    private Vector answers = new Vector();
    
    public void addAnswer(Hashtable _answer) { answers.addElement(_answer);}

    public Hashtable getAnswers(int _category) { return (Hashtable) answers.elementAt(_category); }
}
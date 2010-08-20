package br.org.indt.ndg.mobile.structures;

import java.util.Vector;
import java.util.Hashtable;

public class ResultStructure {
    private String result_id;
    private int survey_id;
    private String user_id;
    private long timeTaken;

    private Vector catnames = new Vector();
    private Vector catids = new Vector();
    private Vector categories = new Vector();
    
    public void setResultId(String _id) { result_id = _id; }
    public void setSurveyId(int _id) { survey_id = _id; }
    public void setUserId(String _id) { user_id = _id; }
    public void setTimeTaken(long _time) { timeTaken = _time; }
    
    public void addCatId(String _id) { catids.addElement(_id); }
    public void addCatName(String _name) { catnames.addElement(_name); }
    public void addCategory(Hashtable _category) { categories.addElement(_category);}
    
    public String getResultId() { return result_id; }
    public int getSurveyId() { return survey_id; }
    public String getUserId() { return user_id; }
    public long getTimeTaken() { return timeTaken; }
    public int getNumCategories() { return categories.size(); }
    public String getCatName(int _index) { return (String) catnames.elementAt(_index); }
    public Hashtable getAnswers(int _category) { return (Hashtable) categories.elementAt(_category); }
}
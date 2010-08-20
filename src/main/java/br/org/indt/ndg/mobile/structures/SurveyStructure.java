package br.org.indt.ndg.mobile.structures;

import br.org.indt.ndg.lwuit.model.NDGQuestion;
import br.org.indt.ndg.mobile.error.NullWidgetException;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.logging.Logger;
import br.org.indt.ndg.mobile.structures.question.Question;
import br.org.indt.ndg.mobile.structures.question.TypeChoice;
import br.org.indt.ndg.mobile.structures.question.TypeTextFieldString;
import com.twmacinta.util.MD5;


public class SurveyStructure {
    private boolean isValid = false;
    private String survey_title;
    private int survey_id;
    private int user_id;
    
    private Vector catnames = new Vector();
    private Vector catids = new Vector();
    private Vector categories = new Vector();
    private int [] catvisited = null;
    private int DisplayIdCategory=0;
    private int DisplayIdQuestion=0;
   
    
    public void setIdNumber(int _id) {
        //survey.setId(_id);
        this.survey_id = _id; 
    }
    
    public void setTitle(String _title) { 
        this.survey_title = _title; 
    }
    
    public int getNumCategories() {
        return categories.size();
    }
    
    public void setUserId(int _id) { 
        this.user_id = _id; 
    }
    
    public int getIdNumber() { 
        return this.survey_id; 
    }
    
    public String getTitle() { 
        return this.survey_title; 
    }
    
    public String getUserId() {
        return AppMIDlet.getInstance().getIMEI();
    } 

    public Vector getQuestions(int _category) { 
        return (Vector) categories.elementAt(_category);
    }
    
    public void initVisitedArray() {
        //initialize category visited array
        int [] intArray = new int[getNumCategories()];
        
        for (int i=0; i<getNumCategories(); i++) 
            intArray[i]= getQuestions(i).size();
        
        catvisited = intArray;
    }
    
    public void decVisitedArray(int _index) {
        catvisited[_index]--;
    }
    
    public int getVisitedValue(int _index) {       
        return catvisited[_index];
    }
    
    public void addCategory(Vector _category) {
        categories.addElement(_category);
    }
    
    public void addCatName(String _name) {
        catnames.addElement(_name);
    }
    
    public void addCatId(String _id) {
        catids.addElement(_id);
    }
    
    public Vector getCatNames() {
        return catnames;
    }
    
    public String getCatName(int _index) {
        return (String) catnames.elementAt(_index);
    }
    
    public String getCatID(int _index) {
        return (String) catids.elementAt(_index);
    }
    
    public void setDisplayId(String _ids) {
        DisplayIdCategory = Integer.parseInt(String.valueOf(_ids.toCharArray()[0]));
        DisplayIdQuestion = Integer.parseInt(String.valueOf(_ids.toCharArray()[2]));
    }
    
    public int getDisplayCategoryId() {
        return DisplayIdCategory-1;
    }
    
    public int getDisplayQuestionId() {
        return DisplayIdQuestion-1;
    }    

    public String getDisplayValue() throws NullWidgetException, Exception {
         NDGQuestion question = (NDGQuestion) ((Vector) categories.elementAt(getDisplayCategoryId())).elementAt(getDisplayQuestionId());
         if(question.isNew()){
             return "";
         }
         if(question.getType().equals("_choice")){
             return "";
         }
         return question.getDisplayValue();
    }
}


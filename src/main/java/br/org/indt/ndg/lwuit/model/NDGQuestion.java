/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.model;

import br.org.indt.ndg.mobile.AppMIDlet;
import java.io.PrintStream;

/**
 *
 * @author alexandre martini
 */
public abstract class NDGQuestion implements Persistable{
    private int question_id;
    private String type;
    protected boolean visited = false;
    private boolean firstTime = true;
    private String description;
    private boolean isNew = true;
    private boolean editable = true;

    private String categoryId;
    private String categoryName;
    private String questionId;

    private boolean skiped = false;

    public void setSkiped(boolean _val) {
        skiped = _val;
    }

    public boolean getSkiped() {
        return skiped;
    }

    public void save(PrintStream _output){
    };

    public void setIdNumber(int _id_number) {
        this.question_id = _id_number;
    }
    public void setType(String _type) {
        this.type = _type;
    }

    public void setVisited(boolean _visited) {
        this.visited = _visited;
    }
    public void setVisited(boolean _visited, int selected) {
        this.visited = _visited;

        if (firstTime && visited) {
            AppMIDlet.getInstance().getFileStores().getSurveyStructure().decVisitedArray(selected);
            firstTime = false;
        }
    }
    public void setFirstTime() {
        firstTime = true;
    }
    public void setIsNew(boolean _isNew) {
        isNew = _isNew;
    }
    public void setEdit(String _edit) {
        if (_edit.equals("false")) this.editable = false;
    }
    public void setDescription(String _description) {
        this.description = _description;
    }

    public int getIdNumber() {
        return this.question_id;
    }
    public String getType() {
        return this.type;
    }
    public boolean getVisited() {
        return this.visited;
    }
    public boolean isNew() {
        return isNew;
    }
    public boolean getEdit() {
        return this.editable;
    }
    public String getDescription() {
        return this.description;
    }

    public void setCategoryId(String _val) {
        categoryId = _val;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryName(String _val) {
        categoryName = _val;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setQuestionId(String _val) {
        questionId = _val;
    }

    public String getQuestionId() {
        return questionId;
    }

    public abstract boolean passConstraints();

    public abstract Answer getAnswer();

    public abstract void setAnswer(Answer answer);

    public abstract String getName();

    public abstract void setName(String name);

    
    public abstract String getDisplayValue() throws Exception;
    
}

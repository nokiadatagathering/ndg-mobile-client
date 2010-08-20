package br.org.indt.ndg.mobile;

import br.org.indt.ndg.lwuit.model.NDGQuestion;
import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import br.org.indt.ndg.mobile.structures.SurveyStructure;
import br.org.indt.ndg.mobile.structures.question.Question;

public class QuestionList extends List implements CommandListener {
    
    private SurveyStructure survey;
    private Vector catQuestions;
    private int selectedCategory;
    private int selected=0;
    public NDGQuestion question = null;
    public QuestionForm questionForm = null;
    
    public QuestionList(int _selectedCategory) {
        super(Resources.QUESTION_LIST_TITLE, List.IMPLICIT);
        
        this.setFitPolicy(this.TEXT_WRAP_ON);
        
        selectedCategory = _selectedCategory;
        
        survey = AppMIDlet.getInstance().getFileStores().getSurveyStructure();
        
        catQuestions = survey.getQuestions(_selectedCategory);

        String name;
        int size = catQuestions.size();
        for (int i=0; i< size; i++) {
            NDGQuestion question = (NDGQuestion) catQuestions.elementAt(i);
            name = (String) (question.getDescription());
            if (question.getVisited()) this.append(name, Resources.check);
            else this.append(name, Resources.question);
        }
        if (size > 0) {
            addCommand(Resources.CMD_RESULTS);
        }
        addCommand(Resources.CMD_BACK);
        
        setCommandListener(this);
    }
    
    //needed to update checkmarks
    public void updateList() {
        //int selected = this.getSelectedIndex();
        this.set(selected, (String) (((NDGQuestion) catQuestions.elementAt(selected)).getDescription()), Resources.check);
        
        //update current focus to next item if one exists
        if (selected < this.size()-1) {
            this.setSelectedIndex(selected+1, true);
        }
        
        AppMIDlet.getInstance().setQuestionList(this);
    }
    
    public void getNextQuestion() {
        selected = this.getSelectedIndex();
      
        question =  (NDGQuestion) catQuestions.elementAt(selected);
        question.setVisited(true, selectedCategory);
        
        if (selected < this.size()-1) {
            questionForm = new QuestionForm(question);
        } else {
            //new category
            questionForm = new QuestionForm(question, true);
        }   
    }
    
    public void getNextQuestion(int index) {
        selected = index;
      
        question =  (NDGQuestion) catQuestions.elementAt(selected);
        question.setVisited(true, selectedCategory);
        
        if (selected < this.size()-1) {
            questionForm = new QuestionForm(question);
        } else {
            //new category
            questionForm = new QuestionForm(question, true);
        }
    }
    
    public void start() {
        question =  (NDGQuestion) catQuestions.elementAt(0);
        question.setVisited(true, selectedCategory);
        if (catQuestions.size() > 1)
            questionForm = new QuestionForm(question);
        else
            questionForm = new QuestionForm(question, true);
    }
    
    public void commandAction(Command c, Displayable d) {
        selected = this.getSelectedIndex();
        if (c == Resources.CMD_BACK) {
            AppMIDlet.getInstance().getCategoryList().updateList();
            AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getCategoryList());
        } else if (c == Resources.CMD_RESULTS || c == SELECT_COMMAND) {
            question =  (NDGQuestion) catQuestions.elementAt(selected);
            question.setVisited(true, selectedCategory);
            if (selected < catQuestions.size()-1)
                questionForm = new QuestionForm(question);
            else 
                questionForm = new QuestionForm(question, true);
        }

    }
}

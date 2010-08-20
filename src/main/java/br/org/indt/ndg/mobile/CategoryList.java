package br.org.indt.ndg.mobile;

import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import br.org.indt.ndg.mobile.structures.SurveyStructure;
import br.org.indt.ndg.mobile.xmlhandle.Results;
import br.org.indt.ndg.mobile.error.WaitingForm;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;

public class CategoryList extends List implements CommandListener{
    
    private SurveyStructure survey;
    private int selected=0;

    public CategoryList() {
        super(Resources.CATEGORY_LIST_TITLE, List.IMPLICIT);
        
        this.setFitPolicy(this.TEXT_WRAP_ON);
        
        survey = AppMIDlet.getInstance().getFileStores().getSurveyStructure();
        
        Vector catnames = survey.getCatNames();
        populateList(catnames);
        
        addCommand(Resources.CMD_START);
        addCommand(Resources.CMD_RESULTS);        
        addCommand(Resources.CMD_SAVE);
        
        addCommand(Resources.CMD_BACK);
        
        setCommandListener(this);
    }
    
    public String getCurrentCatName() {
        return (String) survey.getCatNames().elementAt(selected);
    }

    public int getCurrentCategoryIndex(){
        return selected;
    }
    
    private void populateList(Vector catnames){       
        int size = catnames.size();
        for (int i=0; i < size; i++) {
            Vector questions  = survey.getQuestions(i);
            int questionsCount = questions.size();
            String questionStr = "";
            //if(questionsCount > 0){
                 questionStr = (questionsCount > 1 ? " " + Resources.QUESTIONS : " "+ Resources.QUESTION);
                 String categoryText = (String) catnames.elementAt(i) + "\n" + questionsCount + questionStr;
                if (survey.getVisitedValue(i) > 0) this.append(categoryText, Resources.question);
                else this.append(categoryText, Resources.check);
            //}
        }
    }
    
    public void updateList() {
        Vector catnames = survey.getCatNames();        
        this.deleteAll();
        populateList(catnames);        
        
        //update current focus to next item if one exists
        if (selected < this.size()-1) {
            this.setSelectedIndex(selected+1, true);
        }
        AppMIDlet.getInstance().setCategoryList(this);
    }
    
    public void getNextQuestion() {
        selected++;
        if (selected < survey.getCatNames().size()) {
            while ((survey.getQuestions(selected).size() <= 0)) {
                selected++;
                if (selected >= survey.getCatNames().size()) {
                    break;
                }
            }
        }
        if (selected < survey.getCatNames().size()) {
            AppMIDlet.getInstance().setCategoryList(this);
            AppMIDlet.getInstance().setQuestionList(new QuestionList(selected));
            AppMIDlet.getInstance().getQuestionList().getNextQuestion();
        }
        else {
            Alert alert = new Alert(Resources.END_OF_SURVEY, Resources.PRESS_OK_TO_SAVE, null, AlertType.INFO);
            alert.setTimeout(Alert.FOREVER);
            alert.addCommand(Resources.CMD_OK);
            alert.setCommandListener(new CommandListener() {
                public void commandAction(Command c, Displayable d) {
                    AppMIDlet.getInstance().getFileSystem().saveResult();//this line was replicated in SurveysControl.getCommandsCurrentAlert()
                }
                
            });
            AppMIDlet.getInstance().setDisplayable(alert);
        }
    }
    
    public void getNextQuestion(int [] index) {
        selected = index[0];
        AppMIDlet.getInstance().setCategoryList(this);
        AppMIDlet.getInstance().setQuestionList(new QuestionList(selected));
        AppMIDlet.getInstance().getQuestionList().getNextQuestion(index[1]);
    }
    
    public void commandAction(Command c, Displayable d) {
        selected = this.getSelectedIndex();
        if (c == Resources.CMD_BACK) {
            AppMIDlet.getInstance().setDisplayable(new AlertSave());
            
        } else if (c == Resources.CMD_RESULTS || c == SELECT_COMMAND) {
            AppMIDlet.getInstance().setQuestionList(new QuestionList(selected));
            AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getQuestionList());
            
        } else if (c == Resources.CMD_START) {
            selected = 0;
            while ((survey.getQuestions(selected).size() <= 0)) {
                selected++;
                if (selected >= survey.getCatNames().size()) {
                    break;
                }
            }
            AppMIDlet.getInstance().setCategoryList(this);
            AppMIDlet.getInstance().setQuestionList(new QuestionList(selected));
            AppMIDlet.getInstance().getQuestionList().start();

        } else if (c == Resources.CMD_SAVE) {
            AppMIDlet.getInstance().getFileSystem().saveResult();
        }
    }

}

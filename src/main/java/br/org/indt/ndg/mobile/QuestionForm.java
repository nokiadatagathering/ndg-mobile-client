package br.org.indt.ndg.mobile;

import br.org.indt.ndg.lwuit.model.NDGQuestion;
import br.org.indt.ndg.lwuit.model.TimeQuestion;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;

import br.org.indt.ndg.mobile.structures.question.Question;
import br.org.indt.ndg.mobile.structures.question.TypeChoice;
import br.org.indt.ndg.mobile.structures.question.TypeDate;
import br.org.indt.ndg.mobile.structures.question.TypeTextFieldDecimal;
import br.org.indt.ndg.mobile.structures.question.TypeTextFieldInteger;
import br.org.indt.ndg.mobile.structures.question.TypeTextFieldString;
import br.org.indt.ndg.mobile.structures.question.TypeTime;
import br.org.indt.ndg.mobile.structures.question.custom.CustomChoiceGroup;


public class QuestionForm implements CommandListener {
    
    public Form currentForm;
    private boolean finalQuestion=false;
    public NDGQuestion question = null;
    
    
    public QuestionForm(NDGQuestion _question, boolean _finalQuestion) {
        question = _question;
        
        finalQuestion = _finalQuestion;
        
        CreateForm();
    }
    
    public QuestionForm(NDGQuestion _question) {
        question = _question;
        
        CreateForm();
    }
    
    private void CreateForm() {
        currentForm = new Form(Resources.QUESTION_LIST_TITLE);
        
        currentForm.addCommand(Resources.CMD_NEXT);
        currentForm.addCommand(Resources.CMD_BACK);
        currentForm.setCommandListener(this);
        
        StringItem si = new StringItem("", question.getDescription());
        si.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE));
        
        currentForm.append(si);
        
        if (question.getType().equals("_str")) {
            currentForm.append(((TypeTextFieldString) question).getWidget());
        } else if (question.getType().equals("_int")) {
            currentForm.append(((TypeTextFieldInteger) question).getWidget());
        } else if (question.getType().equals("_decimal")) {
            currentForm.append(((TypeTextFieldDecimal) question).getWidget());
        } else if (question.getType().equals("_choice")) {
            currentForm = ((TypeChoice) question).getWidget();
            if (finalQuestion) ((CustomChoiceGroup) currentForm).setFinalQuestion(true);
        } else if (question.getType().equals("_date")) {
            currentForm.append(((TypeDate) question).getWidget());
        } /*else if (question.getType().equals("_time")) {
            //currentForm.append(((TypeTime) question).getWidget());
            currentForm.append( ((TypeTime) question).getWidget() );
        }*/
        
        currentForm.setTitle(AppMIDlet.getInstance().getCategoryList().getCurrentCatName().toUpperCase());
        
        AppMIDlet.getInstance().setDisplayable(currentForm);
    }
    
    private boolean passConstraints() {
//        if (question.getType().equals("_int")) {
//            if (((TypeTextFieldInteger) question).passConstraints()) return true;
//            else return false;
//        }
//        else if (question.getType().equals("_decimal")) {
//            if (((TypeTextFieldDecimal) question).passConstraints()) return true;
//            else return false;
//        }
//        else if (question.getType().equals("_date")) {
//            if (((TypeDate) question).passConstraints()) return true;
//            else return false;
//        } else return true;
        return question.passConstraints();
    }
    
    public void commandAction(Command c, Displayable d) {
        if (c == Resources.CMD_BACK) {
            if (passConstraints()) {
                currentForm.deleteAll();  //this is necessary to reuse the widgets
                AppMIDlet.getInstance().getQuestionList().updateList();
                AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getQuestionList());
            }
        } else if (c == Resources.CMD_NEXT) {
            if (passConstraints()) {
                currentForm.deleteAll();  //this is necessary to reuse the widgets
                AppMIDlet.getInstance().getQuestionList().updateList();
                
                if (finalQuestion) {
                    AppMIDlet.getInstance().getCategoryList().updateList();
                    AppMIDlet.getInstance().getCategoryList().getNextQuestion();
                } else {
                    AppMIDlet.getInstance().getQuestionList().getNextQuestion();
                }
                
            }
        }
    }
  
}

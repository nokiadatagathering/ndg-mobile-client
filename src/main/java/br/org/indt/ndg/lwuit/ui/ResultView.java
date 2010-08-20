/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.lwuit.control.BackResultViewCommand;
import br.org.indt.ndg.lwuit.control.DeleteCurrentResultCommand;
import br.org.indt.ndg.lwuit.control.OpenResultCommand;
import br.org.indt.ndg.lwuit.control.SendResultCommand;
import br.org.indt.ndg.lwuit.control.SurveysControl;
import br.org.indt.ndg.lwuit.extended.DateField;
import br.org.indt.ndg.lwuit.extended.TimeField;
import br.org.indt.ndg.lwuit.model.Answer;
import br.org.indt.ndg.lwuit.model.Category;
import br.org.indt.ndg.lwuit.model.ChoiceQuestion;
import br.org.indt.ndg.lwuit.model.NDGQuestion;
import br.org.indt.ndg.lwuit.model.Survey;
import br.org.indt.ndg.lwuit.model.TimeQuestion;
import br.org.indt.ndg.mobile.multimedia.Picture;
import com.sun.lwuit.Container;
import com.sun.lwuit.Font;
import com.sun.lwuit.Label;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.layouts.BoxLayout;
import java.lang.String;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author mluz
 */
public class ResultView extends Screen implements ActionListener {

    private String title2 = Resources.RESULTS_LIST_TITLE;
    private String title1;
    private Font categoryFont = Screen.getRes().getFont("NokiaSansWide14Bold");
    private Font questionFont = Screen.getRes().getFont("NokiaSansWideItalic14");
    private Survey survey;


    private SurveysControl surveysControl = SurveysControl.getInstance();

    protected void loadData() {
        title1 = surveysControl.getOpenedSurveyTitle();
        //survey = surveysControl.getCurrentSurvey();
        survey = surveysControl.getCurrentSurveyNewModel();
    }

    private void setPreferredHeight(Label label, int labelHeight) {
        Dimension d = label.getPreferredSize();
        d.setHeight(labelHeight);
        label.setPreferredSize(d);
    }

    protected void customize() {
        int labelheight = 16;
        int labelheightspace = 8; 

        int focusableRange = 9;//default value for 240 height
        if (form.getHeight() == 320)
            focusableRange = 14;  

        form.removeAllCommands();
        form.removeAll();
        
        form.setCyclicFocus(false);
        
        form.addCommand(BackResultViewCommand.getInstance().getCommand());
        form.addCommand(SendResultCommand.getInstance().getCommand());
        form.addCommand(DeleteCurrentResultCommand.getInstance().getCommand());
        form.addCommand(OpenResultCommand.getInstance().getCommand());
        
        form.setCommandListener(this);
        setTitle(title1, title2);

        Category[] categories = survey.getCategories();
        int countFocusable = focusableRange; //first item is focusable
        int length = categories.length;
        for (int i=0; i < length; i++) {
            Category category = categories[i];
            Label labelCategory = new Label(category.getName());
            labelCategory.getStyle().setFont(categoryFont);
            setPreferredHeight(labelCategory, labelheight);
            if (countFocusable == focusableRange) {
                labelCategory.setFocusable(true);
                countFocusable = 0;
            }
            countFocusable++;
            form.addComponent(labelCategory);
            NDGQuestion[] questions = category.getQuestions();
            int size = questions.length;
            for (int j=0; j < size; j++) {
                NDGQuestion question = questions[j];
                Label labelQuestion = new Label(question.getName() + ":");
                labelQuestion.getStyle().setFont(questionFont);
                setPreferredHeight(labelQuestion, labelheight);
                Label labelAnswer = getFormattedAnswer(question, labelheight);

                Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
                container.addComponent(labelQuestion);
                container.addComponent(labelAnswer);
                form.addComponent(container);
                countFocusable++;
                if (countFocusable == focusableRange) {
                    container.setFocusable(true);
                    countFocusable = 1;
                }
            }
            Label space = new Label(" ");
            setPreferredHeight(space, labelheightspace);
            form.addComponent(space);
            countFocusable++;
            if ((countFocusable == focusableRange)||(i == categories.length-1)) {
                space.setFocusable(true);
                countFocusable = 1;
            }
        }

    }

    private Label getFormattedAnswer(NDGQuestion question, int labelheight){
        Label labelAnswer = null;
        Answer answer = question.getAnswer();

        if(question.getType().equals("_img")){
              byte[] image = (byte[]) question.getAnswer().getValue();
              if(image != null){
                 Picture picture = Picture.createPicture(image);
                 labelAnswer = new Label(picture.getThumbnail());
              }else{
                  labelAnswer = new Label("");
              }
            
        } else {
            if(question.getType().equals("_choice")){
                ChoiceQuestion cquestion = (ChoiceQuestion) question;
                String choiceAnswer = "";
                int nIndex;
                if(!cquestion.isExclusive()){
                    Vector vChoicesAnswer = (Vector) cquestion.getAnswer().getValue();
                    Vector vOthersAnswer = (Vector) cquestion.getOthersText();
                    int vsize = vChoicesAnswer.size();
                    for (int k = 0; k < vsize; k++) {
                        nIndex = Integer.parseInt( (String) vChoicesAnswer.elementAt(k) );
                        String other = (String)vOthersAnswer.elementAt(k);
                        choiceAnswer+= cquestion.getChoiceText(nIndex);
                        if(other.length()>0)
                            choiceAnswer+= "="+other;
                        if((k+1)<vsize)
                            choiceAnswer+= " - ";
                    }
                } else {
                    nIndex = Integer.parseInt( (String) question.getAnswer().getValue() );
                    choiceAnswer = cquestion.getChoiceText(nIndex);
                }
                labelAnswer = new Label(choiceAnswer);
            } else if(question.getType().equals("_date")) {
                DateField dfDate = new DateField(DateField.DDMMYYYY);
                long datelong = Long.parseLong((String)question.getAnswer().getValue());
                dfDate.setDate(new Date(datelong));
                labelAnswer = new Label(dfDate.getText());
            }
            else if (question.getType().equals("_time")) {
               
                TimeQuestion timeQuestion = (TimeQuestion) question;
                TimeField tfDate = null;
                if(timeQuestion.getConvention()==12){
                   tfDate = new TimeField(TimeField.HHMM);                   
                }else{
                   tfDate = new TimeField(TimeField.HHMM1);                   
                }

                long timelong = Long.parseLong((String)question.getAnswer().getValue());                

                
                tfDate.setTime(new Date(timelong));
                String convention = "";

                if(timeQuestion.getAm_pm() == 1){
                    convention = " am";
                }else if(timeQuestion.getAm_pm() == 2){
                    convention = " pm";
                }

               
                labelAnswer = new Label(tfDate.getText()+convention);
            } else {
                labelAnswer = new Label((String)answer.getValue());
            }
            setPreferredHeight(labelAnswer, labelheight);
        }

        return labelAnswer;
    }

    public void actionPerformed(ActionEvent evt) {
        Object cmd = evt.getSource();
        if (cmd == OpenResultCommand.getInstance().getCommand())
            OpenResultCommand.getInstance().execute(null);
        else if (cmd == BackResultViewCommand.getInstance().getCommand())
            BackResultViewCommand.getInstance().execute(null);
        else if (cmd == DeleteCurrentResultCommand.getInstance().getCommand())
            DeleteCurrentResultCommand.getInstance().execute(null);
        else if (cmd == SendResultCommand.getInstance().getCommand())
            SendResultCommand.getInstance().execute(null);
    }



}

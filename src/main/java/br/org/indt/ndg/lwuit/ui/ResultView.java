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
import br.org.indt.ndg.lwuit.model.NumberAnswer;
import br.org.indt.ndg.lwuit.model.Survey;
import br.org.indt.ndg.lwuit.model.TimeQuestion;
import br.org.indt.ndg.mobile.multimedia.Picture;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.Font;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.plaf.UIManager;
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
    private Font answerFont = Screen.getRes().getFont("NokiaSansWide14");
    private Survey survey;
    int labelheight = 18;
    int labelheightspace = 8;

    private SurveysControl surveysControl = SurveysControl.getInstance();

    protected void loadData() {
        title1 = surveysControl.getOpenedSurveyTitle();
        survey = surveysControl.getCurrentSurveyNewModel();
    }

    private void setPreferredHeight(Component component, int labelHeight) {
        Dimension d = component.getPreferredSize();
        d.setHeight(labelHeight);
        component.setPreferredSize(d);
    }

    protected void customize() {

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
        form.setSmoothScrolling(true);
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
                TextArea componentQuestion = createWrappedTextArea(
                        question.getName() + ":", questionFont);
                componentQuestion.getStyle().setFont(questionFont);
                Component componentAnswer = getFormattedAnswer(question, labelheight);
                Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
                container.addComponent(componentQuestion);
                container.addComponent(componentAnswer);
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

    private Component getFormattedAnswer(NDGQuestion question, int labelheight){
        Component componentAnswer = null;
        Answer answer = question.getAnswer();
        if (question.getType().equals("_img")) {
            byte[] image = (byte[]) question.getAnswer().getValue();
            if (image != null) {
                Picture picture = Picture.createPicture(image);
                componentAnswer = new Label(picture.getThumbnail());
            } else {
                componentAnswer = new Label("");
            }
        } else {
            if(question.getType().equals("_choice")){
                ChoiceQuestion cquestion = (ChoiceQuestion) question;
                String choiceAnswer = " ";
                int nIndex;
                if(!cquestion.isExclusive()){
                    Vector vChoicesAnswer = (Vector) cquestion.getAnswer().getValue();
                    Vector vOthersAnswer = (Vector) cquestion.getOthersText();
                    int vsize = vChoicesAnswer.size();
                    for (int k = 0; k < vsize; k++) {
                        nIndex = Integer.parseInt( (String) vChoicesAnswer.elementAt(k) );
                        String other = (String)vOthersAnswer.elementAt(k);
                        choiceAnswer+= cquestion.getChoiceText(nIndex);
                        if(other.length()>0) {
                            choiceAnswer+= "="+other;
                        }
                        choiceAnswer += "\n";
                        if((k+1)<vsize) {
                            choiceAnswer+= " ";
                        }
                    }
                    componentAnswer = createWrappedTextArea(choiceAnswer, answerFont);

                } else {
                    try {
                        nIndex = Integer.parseInt( (String) question.getAnswer().getValue() );
                        choiceAnswer += cquestion.getChoiceText(nIndex);
                        Vector vOthersAnswer = (Vector) cquestion.getOthersText();
                        String other = (String)vOthersAnswer.elementAt(0);
                        if(other.length()>0) {
                            choiceAnswer += "="+other;
                        }
                        componentAnswer = createWrappedTextArea(choiceAnswer, answerFont);
                    } catch( NumberFormatException ex ) {
                        componentAnswer = createWrappedTextArea(choiceAnswer, answerFont); // empty question
                    }
                }
            } else if(question.getType().equals("_date")) {
                DateField dfDate = new DateField(DateField.DDMMYYYY);
                long datelong = Long.parseLong((String)question.getAnswer().getValue());
                dfDate.setDate(new Date(datelong));
                componentAnswer = new Label(dfDate.getText());
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
                componentAnswer = new Label(tfDate.getText()+convention);
            }
            else if ( question.getType().equals("_int") || question.getType().equals("_decimal"))
            {
                componentAnswer = createWrappedTextArea(((NumberAnswer)answer).getValueString(), answerFont) ;

            } else {
                componentAnswer = createWrappedTextArea((String)answer.getValue(), answerFont);
            }
        }
        return componentAnswer;
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

        private TextArea createWrappedTextArea(String name, Font font) {
        TextArea item = new TextArea();
        item.setStyle(UIManager.getInstance().getComponentStyle("Label"));
        item.getStyle().setFont(font);
        item.setEditable(false);
        item.setFocusable(true);
        item.setColumns(20);
        item.setRows(1);
        item.setGrowByContent(false);
        item.setText(name);

        int pw = Display.getInstance().getDisplayWidth();
        int w = item.getStyle().getFont().stringWidth(name);
        if (w > pw)
        {
            item.setGrowByContent(true);
            item.setRows(2);
        }
        else
        {
            item.setGrowByContent(false);
            item.setRows(1);
        }
        return item;
    }
}

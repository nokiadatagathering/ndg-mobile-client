/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.extended.NumericField;
import br.org.indt.ndg.lwuit.extended.ChoiceGroup;
import br.org.indt.ndg.lwuit.control.BackQuestionFormCommand;
import br.org.indt.ndg.lwuit.control.DetailsQuestionFormCommand;
import br.org.indt.ndg.lwuit.control.NextQuestionFormCommand;
import br.org.indt.ndg.lwuit.control.OkQuestionFormCommand;
import br.org.indt.ndg.lwuit.control.SurveysControl;
import br.org.indt.ndg.lwuit.model.ChoiceQuestion;
import br.org.indt.ndg.lwuit.model.DateQuestion;
import br.org.indt.ndg.lwuit.model.DescriptiveQuestion;
import br.org.indt.ndg.lwuit.model.DetailsQuestion;
import br.org.indt.ndg.lwuit.model.NumericQuestion;
import br.org.indt.ndg.lwuit.model.Question;
import br.org.indt.ndg.lwuit.extended.ChoiceGroupListener;
import br.org.indt.ndg.lwuit.extended.DateField;
import br.org.indt.ndg.lwuit.extended.TimeField;
import br.org.indt.ndg.lwuit.model.ImageQuestion;
import br.org.indt.ndg.lwuit.model.NDGQuestion;
import br.org.indt.ndg.lwuit.model.TimeQuestion;
import com.sun.lwuit.Component;
import com.sun.lwuit.Font;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.animations.CommonTransitions;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.FocusListener;
import com.sun.lwuit.plaf.UIManager;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author mluz
 */
public class QuestionForm extends Screen implements ActionListener, ChoiceGroupListener {

    private String title1;
    private String title2;

    private SurveysControl surveysControl = SurveysControl.getInstance();
    private Font labelFont = Screen.getRes().getFont("NokiaSansWideBold15");

    private NDGQuestion question;

    private DescriptiveField df;
    private NumericField nf;
    private ChoiceGroup cg;
    private DateField datefield;

    private TimeField timefield;

    public Object getAnswer() {
        if ((question instanceof DescriptiveQuestion)||(question instanceof DetailsQuestion)) {
            return df.getText();
        } else if (question instanceof NumericQuestion) {
            return nf.getText();
        } else if (question instanceof ChoiceQuestion) {
            ChoiceQuestion cq = (ChoiceQuestion)question;
            if (cq.isExclusive()) {
                return new Integer(cg.getSelectedIndex());
            } else {
                return cg.getMarks();
            }
        } else if (question instanceof DateQuestion) {
            return datefield.getDate();
        } else if (question instanceof TimeQuestion) {
            return timefield.getTime();
        }
        return null;
    }

    public NDGQuestion getQuestion() {
        return question;
    }

    protected void loadData() {
        title1 = surveysControl.getOpenedSurveyTitle();
        title2 = surveysControl.getCategoryNameFromCurrentQuestion();
        question = surveysControl.getCurrentQuestion();
    }

    protected void customize() {
        createScreen();
        //UIManager.getInstance().getLookAndFeel().setDefaultFormTransitionIn(CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, false, 500));
        setTitle(title1, title2);

        form.addCommand(BackQuestionFormCommand.getInstance().getCommand());
        form.addCommand(NextQuestionFormCommand.getInstance().getCommand());
        form.setCommandListener(this);

        TextArea questionName = new TextArea(5,20);
        questionName.setText(question.getName());
        questionName.setStyle(UIManager.getInstance().getComponentStyle("Label"));
        questionName.getStyle().setFont(labelFont);
        questionName.setRows(questionName.getLines()-1);
        questionName.setEditable(false);
        questionName.setFocusable(false);
        form.addComponent(questionName);

        if (question instanceof DescriptiveQuestion) {
            df = new DescriptiveField(((DescriptiveQuestion)question).getLength());
            df.setText((String)question.getAnswer().getValue());
            form.addComponent(df);
        } else if (question instanceof NumericQuestion) {
            nf = new NumericField(((NumericQuestion)question).getLength(), ((NumericQuestion)question).isDecimal());
            nf.setText((String)question.getAnswer().getValue());
            form.addComponent(nf);
        } else if (question instanceof ChoiceQuestion) {
            ChoiceQuestion cq = (ChoiceQuestion)question;
            Vector vChoices = cq.getChoices();
            int totalChoices = vChoices.size();
            String[] choices = new String[totalChoices];
            for (int i=0; i<totalChoices; i++) {
                choices[i] = (String)vChoices.elementAt(i);
            }

            if (cq.isExclusive()) {
                cg = new ChoiceGroup(choices, Integer.parseInt((String)cq.getAnswer().getValue()));
                cg.setCgListener(this);
                if (surveysControl.isDetailsShowed(Integer.parseInt((String)cq.getAnswer().getValue()))) {
                    form.addCommand(DetailsQuestionFormCommand.getInstance().getCommand());
                }
            } else {
                boolean[] marks = (boolean[])cq.getAnswer().getValue();
                cg = new ChoiceGroup(choices, marks);
                cg.setCgListener(this);
            }

            // for a better scroll
            questionName.setFocusable(true);
            questionName.addFocusListener(new FocusListener() {

                public void focusGained(Component c) {
                    cg.requestFocus();
                }

                public void focusLost(Component c) {
                }
            });

            Label spaceBotton = new Label("spaceBotton");
            spaceBotton.getStyle().setFgColor(form.getStyle().getBgColor());
            spaceBotton.setFocusable(true);
            spaceBotton.addFocusListener(new FocusListener() {

                public void focusGained(Component arg0) {
                    cg.setItemFocused(cg.size()-1);
                }

                public void focusLost(Component arg0) {
                }
            });

            // add components
            form.addComponent(cg);
            form.addComponent(spaceBotton);
            cg.requestFocus();

        } else if (question instanceof DetailsQuestion) {
            form.removeAllCommands();
            df = new DescriptiveField(50);
            form.addComponent(df);
            df.setText((String)question.getAnswer().getValue());
            form.addCommand(OkQuestionFormCommand.getInstance().getCommand());
        } else if (question instanceof DateQuestion) {
            datefield = new DateField(DateField.DDMMYYYY);
            datefield.setDate((Date)question.getAnswer().getValue());
            form.addComponent(datefield);
        } else if (question instanceof TimeQuestion) {
            if ( ((TimeQuestion)question).getConvention() == 1) {
                timefield = new TimeField(TimeField.HHMM);
                timefield.setTime((Date)question.getAnswer().getValue());
                form.addComponent(timefield);
            } else if (((TimeQuestion)question).getConvention() == 2) {
                timefield = new TimeField(TimeField.HHMM1);
                timefield.setTime((Date)question.getAnswer().getValue());
                form.addComponent(timefield);
            }

        }
        else if(question instanceof ImageQuestion){
            df = new DescriptiveField(150);
            df.setText((String)question.getAnswer().getValue());
            form.addComponent(df);
        }
    }


    public void actionPerformed(ActionEvent evt) {
        surveysControl.commitValue(this, false);
        Object cmd = evt.getSource();
        if (cmd == BackQuestionFormCommand.getInstance().getCommand()) {
            if (question instanceof ChoiceQuestion)
                BackQuestionFormCommand.getInstance().execute(question);
            else
                BackQuestionFormCommand.getInstance().execute(null);
        } else if (cmd == NextQuestionFormCommand.getInstance().getCommand()) {
            if (question instanceof ChoiceQuestion)
                NextQuestionFormCommand.getInstance().execute(question);
            else
                NextQuestionFormCommand.getInstance().execute(null);
        } else if (cmd == OkQuestionFormCommand.getInstance().getCommand()) {
            OkQuestionFormCommand.getInstance().execute(null);
        } else if (cmd == DetailsQuestionFormCommand.getInstance().getCommand()) {
            DetailsQuestionFormCommand.getInstance().execute(null);
        }
    }

    // Listener from ChoiceGroup
    public void itemChoosed(int i) {
        if (surveysControl.isDetailsShowed(i)) {
            form.addCommand(DetailsQuestionFormCommand.getInstance().getCommand());
        }
        else {
            form.removeCommand(DetailsQuestionFormCommand.getInstance().getCommand());
        }
        surveysControl.commitValue(this, true);
    }
}

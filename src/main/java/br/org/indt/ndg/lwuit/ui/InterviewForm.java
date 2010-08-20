/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.BackInterviewFormCommand;
import br.org.indt.ndg.lwuit.control.PersistenceManager;
import br.org.indt.ndg.lwuit.control.SaveInterviewFormCommand;
import br.org.indt.ndg.lwuit.control.SurveysControl;
import br.org.indt.ndg.lwuit.extended.CheckBox;
import br.org.indt.ndg.lwuit.extended.DateField;
import br.org.indt.ndg.lwuit.extended.NumericField;
import br.org.indt.ndg.lwuit.extended.RadioButton;
import br.org.indt.ndg.lwuit.model.ChoiceQuestion;
import br.org.indt.ndg.lwuit.model.DateQuestion;
import br.org.indt.ndg.lwuit.model.DescriptiveQuestion;
import br.org.indt.ndg.lwuit.model.ImageQuestion;
import br.org.indt.ndg.lwuit.model.NDGQuestion;
import br.org.indt.ndg.lwuit.model.NumericQuestion;
import br.org.indt.ndg.lwuit.model.Question;
import br.org.indt.ndg.lwuit.ui.camera.NDGCameraManager;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.multimedia.Camera;
import com.sun.lwuit.Button;
import com.sun.lwuit.ButtonGroup;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.FocusListener;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.plaf.Border;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author mturiel, amartini
 */
public class InterviewForm extends Screen implements FocusListener, ActionListener {

    private NDGQuestion currentQuestion;
    private Vector vContainers;
    private Vector vGroups;
    private Vector vQuestions;
    private int colorWhite = -1;
    private int grayColor = -1;
    private Border containerInactiveBorder;
    private Border containerActiveBorder;

    private int focusIndex = 0;
    private int lastFocusIndex = -1;
    private int lastAddedContainer = -1;
    private String title1, title2;

    private int catTo = -1;
    private int skipTo = -1;
    private boolean forceSkipFalse = false;

    private Command cmdNext;

    protected void loadData() {
        vContainers = new Vector();
        vGroups = new Vector();
        vQuestions = new Vector();
        title1 = SurveysControl.getInstance().getOpenedSurveyTitle();
        title2 = Resources.NEW_INTERVIEW;
        catTo = -1;
        skipTo = -1;

        cmdNext = new Command(br.org.indt.ndg.mobile.Resources.NEWUI_NEXT);        
    }
        
    protected void customize() {
        createScreen();
        form.removeAllCommands();
        form.removeAll();

        form.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        form.getContentPane().getStyle().setBorder(Border.createEmpty(), false);
        form.setScrollAnimationSpeed(500);

        lastFocusIndex = -1;
        lastAddedContainer = -1;
        SurveysControl.getInstance().resetQuestion();
        currentQuestion = SurveysControl.getInstance().getNextQuestion();
        while (currentQuestion != null) {
            if (currentQuestion instanceof DescriptiveQuestion) {
                CreateDescriptiveField(currentQuestion);
            }
            else if (currentQuestion instanceof NumericQuestion) {
                CreateNumericField(currentQuestion);
            }
            else if (currentQuestion instanceof DateQuestion) {
                CreateDateField(currentQuestion);
            }
            else if (currentQuestion instanceof ChoiceQuestion) {
                if (((ChoiceQuestion)currentQuestion).isExclusive()) {
                    CreateExclusiveChoiceField(currentQuestion);
                }
                else {
                    CreateChoiceField(currentQuestion);
                }
            }
            else if(currentQuestion instanceof ImageQuestion){
                createImageField(currentQuestion);
            }

            int catId = Integer.parseInt(currentQuestion.getCategoryId());
            int questId = Integer.parseInt(currentQuestion.getQuestionId());
            if (checkIfSkiped(catId, questId)) {
                currentQuestion.setSkiped(true);
                Component[] group = (Component[]) vGroups.lastElement();
                for (int i = 0; i < group.length; i++) {
                    group[i].setEnabled(false);
                }
            }
            else {
                currentQuestion.setSkiped(false);
            }

            vQuestions.addElement(currentQuestion);
            currentQuestion = SurveysControl.getInstance().getNextQuestion();
            lastAddedContainer++;
        }        

        form.addCommand(cmdNext);
        form.addCommand(BackInterviewFormCommand.getInstance().getCommand());
        //form.addCommand(SaveInterviewFormCommand.getInstance().getCommand());
        form.setCommandListener(this);

        if (PersistenceManager.getInstance().isEditing()) {
            title2 = Resources.EDITING;
        }
        setTitle(title1, title2);
    }

    public void focusGained(Component cmp) {
        focusIndex = form.getContentPane().getComponentIndex(cmp);
        focusIndex = focusIndex / 2;

        // Update last question and test constraints
        if (lastFocusIndex != -1) {
            UpdateAnswer(); // Update Answer with new Value
            if (!validateAnswer()) {
                return;
            }
        }

        // Skip questions/containers until next NOT SKIP question reached.
        while (((Question) vQuestions.elementAt(focusIndex)).getSkiped()) {
            if (focusIndex < lastFocusIndex) {
                if ( (focusIndex-1) > -1) {
                    focusIndex--;
                }
                else return;
            }
            else {
                if ( ((focusIndex+1)*2) < form.getContentPane().getComponentCount()) {
                    focusIndex++;
                }
                else return;
            }
        }

        if (colorWhite < 0 || grayColor <0) {
            colorWhite = 0x00FFFFFF;

            grayColor = ((Container) vContainers.elementAt(0)).getStyle().getBgColor();
        }

        if (containerInactiveBorder == null) {
            //containerInactiveBorder = ((Container) vContainers.elementAt(0)).getStyle().getBorder();
            containerInactiveBorder = Border.createBevelLowered(0x7b7b7b, 0x7b7b7b, 0x7b7b7b, 0x7b7b7b);
            containerActiveBorder = Border.createBevelLowered(0x69b510, 0x69b510, 0x69b510, 0x69b510);
        }

        Container activeContainer = (Container) vContainers.elementAt(focusIndex);
        Container prevContainer = ((focusIndex-1) >= 0) ? (Container) vContainers.elementAt(focusIndex-1) : null;
        Container nextContainer = ((focusIndex+1) < vContainers.size()) ? (Container) vContainers.elementAt(focusIndex+1) : null;

        if (prevContainer != null) prevContainer.getStyle().setBorder(containerInactiveBorder, false);
        activeContainer.getStyle().setBorder(containerActiveBorder, false);
        if (nextContainer != null) nextContainer.getStyle().setBorder(containerInactiveBorder, false);

        if (prevContainer != null) prevContainer.refreshTheme();
        activeContainer.refreshTheme();
        if (nextContainer != null) nextContainer.refreshTheme();

        setComponentGroupBgColor((Component[])vGroups.elementAt(focusIndex), colorWhite);
        if (prevContainer != null) setComponentGroupBgColor((Component[])vGroups.elementAt(focusIndex-1), grayColor);
        if (nextContainer != null) setComponentGroupBgColor((Component[])vGroups.elementAt(focusIndex+1), grayColor);

        Component[] group = (Component[]) vGroups.elementAt(focusIndex);
        if (focusIndex < lastFocusIndex) {
            // Focus comming from down
            if ((group[0] instanceof RadioButton) || group[0] instanceof CheckBox) {
                group[0].requestFocus();
                form.scrollComponentToVisible(group[0]);
            }
            else {
                group[0].requestFocus();
                form.scrollComponentToVisible(group[0]);
            }
        }
        else {
            // Focus comming from up
            group[0].requestFocus();
            if (group.length > 5) {
                form.scrollComponentToVisible(group[3]);
            }
            else {
                form.scrollComponentToVisible(group[group.length-1]);
            }
        }
    }

    public void focusLost(Component cmp) {
        lastFocusIndex = focusIndex;
    }

    public void actionPerformed(ActionEvent evt) {
        Object cmd = evt.getSource();

        // Radio Buttons More details
        if ( (cmd instanceof RadioButton) ) {
            Component[] group = (Component[]) vGroups.elementAt(focusIndex);
            for (int i = 0; i < group.length; i++) {
                if (cmd == group[i]) {
                    RadioButton rb = (RadioButton) group[i];
                    if ( (rb.hasOther()) && (rb.isSelected()) ) {
                        DetailsForm.show(rb.getText(), rb.getOtherText());
                        rb.setOtherText(SurveysControl.getInstance().getItemOtherText());
                    }
                    //if ( (cb.hasOther()) && (!cb.isSelected()) ) {
                    //    cb.setOtherText("");
                    //}
                }
            }
        }

        // Check Box More details
        else if ( (cmd instanceof CheckBox) ) {
            Component[] group = (Component[]) vGroups.elementAt(focusIndex);
            for (int i = 0; i < group.length; i++) {
                if (cmd == group[i]) {
                    CheckBox cb = (CheckBox) group[i];
                    if ( (cb.hasOther()) && (cb.isSelected()) ) {
                        DetailsForm.show(cb.getText(), cb.getOtherText());
                        cb.setOtherText(SurveysControl.getInstance().getItemOtherText());
                    }
                    //if ( (cb.hasOther()) && (!cb.isSelected()) ) {
                    //    cb.setOtherText("");
                    //}
                }
            }
        }

        //Radio Buttons pressed OR Command NEXT pressed
        if ( (cmd instanceof RadioButton) || (cmd == cmdNext) ) {
            // Check if Skip (Skip Logic) only if Question is instance of ChoiceQuestion
            Question question = (Question) vQuestions.elementAt(focusIndex);
            if(question instanceof ChoiceQuestion) {
                ChoiceQuestion cq = (ChoiceQuestion) question;
                if (cq.getSkipEnabled()) {
                    // Check if selected RadioButton has Skip
                    int selectedChoiceItem = 0;
                    Component[] group = (Component[]) vGroups.elementAt(focusIndex);
                    for (; selectedChoiceItem < group.length-2; selectedChoiceItem++) {
                        RadioButton rb = (RadioButton) group[selectedChoiceItem];
                        if (rb.isSelected()) {
                            break;
                        }
                    }
                    boolean sentence1 = ( (selectedChoiceItem != cq.getChoiceItem()) && (cq.isInverse()) );
                    boolean sentence2 = ( (selectedChoiceItem == cq.getChoiceItem()) && (!cq.isInverse()) );
                    if (sentence1 || sentence2) { // The selected RadioButton HAS Skip
                        // Get the correct container to "skip to" based on Category ID and Question ID
                        int catId, questId;
                        for (int i = 0; i < vQuestions.size(); i++) {
                            catId = Integer.parseInt(((Question) vQuestions.elementAt(i)).getCategoryId());
                            questId = Integer.parseInt(((Question) vQuestions.elementAt(i)).getQuestionId());
                            if ((catId == cq.getCatTo()) && (questId == cq.getSkipTo())) {
                                focusIndex++; // go the next question
                                for (; focusIndex < i; focusIndex++) {
                                    ((Question) vQuestions.elementAt(focusIndex)).setSkiped(true);
                                    Component[] group2 = (Component[]) vGroups.elementAt(focusIndex);
                                    for (int i2 = 0; i2 < group2.length; i2++) {
                                        group2[i2].setEnabled(false);
                                    }
                                }
                                focusIndex = (i-1); //Update focusIndex // (-1) We must come back one index to focus the current question
                                break;
                            }
                        }
                    }
                    else { // The selected RadioButton DOES NOT HAVE Skip
                        int catId, questId;
                        for (int i = 0; i < vQuestions.size(); i++) {
                            catId = Integer.parseInt(((Question) vQuestions.elementAt(i)).getCategoryId());
                            questId = Integer.parseInt(((Question) vQuestions.elementAt(i)).getQuestionId());
                            int nIndex = focusIndex;
                            if ((catId == cq.getCatTo()) && (questId == cq.getSkipTo())) {
                                nIndex++; // go the next question
                                for (; nIndex < i; nIndex++) {
                                    ((Question) vQuestions.elementAt(nIndex)).setSkiped(false);
                                    Component[] group2 = (Component[]) vGroups.elementAt(nIndex);
                                    for (int i2 = 0; i2 < group2.length; i2++) {
                                        group2[i2].setEnabled(true);
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }

            // Advance to the next container
            int cmpCount = form.getContentPane().getComponentCount();
            if ( ((focusIndex+1)*2) < cmpCount) {
                Component[] group = (Component[]) vGroups.elementAt(focusIndex+1);
                group[group.length-1].requestFocus();
                if (group.length > 5) {
                    form.scrollComponentToVisible(group[3]);
                }
                else {
                    form.scrollComponentToVisible(group[group.length-1]);
                }
            }
            else { //Last Question Reached (Save automatically)
                if (validateAnswer()) {
                    GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
                    GeneralAlert.getInstance().show(Resources.CMD_SAVE.getLabel(), Resources.PRESS_OK_TO_SAVE, GeneralAlert.INFO);
                    SaveResult();
                }
            }
        }

        //Image was clicked
        else if(cmd instanceof Button){
            Question question = (Question) vQuestions.elementAt(focusIndex);
            if(question != null && question instanceof ImageQuestion){
                //NDGCameraManager.getInstance().displayCamera(this, (ImageQuestion) question);
            }
        }

        // Back Command pressed
        else if (cmd == BackInterviewFormCommand.getInstance().getCommand()) {
            GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_YES_NO, true);
            int resultCmdIndex = GeneralAlert.getInstance().show(Resources.CMD_SAVE.getLabel(), Resources.SAVE_MODIFICATIONS, GeneralAlert.CONFIRMATION);
            if (resultCmdIndex == GeneralAlert.RESULT_YES) {
                if (validateAnswer()) {
                    SaveResult();
                }
            }
            else {
                BackInterviewFormCommand.getInstance().execute(null);
            }
            
        }
    }

    private void UpdateAnswer() {
        // Set Visited
        ((NDGQuestion) vQuestions.elementAt(lastFocusIndex)).setVisited(true);

        Question question = (Question) vQuestions.elementAt(lastFocusIndex);
        Component[] group = (Component[]) vGroups.elementAt(lastFocusIndex);
        if (question instanceof DescriptiveQuestion) {
            question.getAnswer().setValue(((DescriptiveField) group[0]).getText());
        }
        else if (question instanceof NumericQuestion) {
            question.getAnswer().setValue(((NumericField) group[0]).getText());
        }
        else if (question instanceof DateQuestion) {
            Date date = ((DateField) group[0]).getDate();
            Long datelong = new Long(date.getTime());
            question.getAnswer().setValue(datelong.toString());
        }
        else if (question instanceof ChoiceQuestion) {
            ChoiceQuestion cq =  (ChoiceQuestion) question;
            if (cq.isExclusive()) {
                // Update selected index and Others Texts
                String selectedIndex = "";
                Vector vOthersText = new Vector();
                for (int i = 0; i < group.length-2; i++) {
                    RadioButton rb = (RadioButton) group[i];
                    if (rb.isSelected()) {
                        selectedIndex = (Integer.toString(i));
                    }
                    vOthersText.addElement(rb.getOtherText());
                }
                cq.getAnswer().setValue(selectedIndex);
                cq.setOthersText(vOthersText);
                ////
            }
            else {
                // Update selected elements and Others Texts
                Vector vIndexes = new Vector();
                Vector vOthersText = new Vector();
                for (int i = 0; i < group.length-2; i++) {
                    CheckBox cb = (CheckBox) group[i];
                    if (cb.isSelected()) {
                        vIndexes.addElement(Integer.toString(i));
                    }
                    vOthersText.addElement(cb.getOtherText());
                }
                cq.getAnswer().setValue(vIndexes);
                cq.setOthersText(vOthersText);
                ////
            }
        }
    }

    private void setComponentGroupBgColor(Component[] cg, int color) {
        for (int i=0; i<cg.length; i++) {
            cg[i].getStyle().setBgColor(color, true);
        }
    }

    private void CreateDescriptiveField(NDGQuestion obj) {
        DescriptiveQuestion question = (DescriptiveQuestion) obj;
        Container c;
        c = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        c.getStyle().setBorder(Border.createBevelLowered(0x7b7b7b, 0x7b7b7b, 0x7b7b7b, 0x7b7b7b));
        Label l1 = new Label(question.getName());
        c.addComponent(l1);
        DescriptiveField tfDesc = new DescriptiveField(((DescriptiveQuestion)question).getLength());
        tfDesc.setText((String)question.getAnswer().getValue());
        tfDesc.setInputMode("Abc");
        tfDesc.setEditable(true);
        if (lastAddedContainer > -1) tfDesc.setNextFocusUp(((Container) vContainers.elementAt(lastAddedContainer)));
        c.addComponent(tfDesc);
        form.addComponent(c);
        c.setFocusable(true);
        c.addFocusListener(this);
        form.addComponent(new Label(""));
        Component[] cg;
        cg = new Component[3];
        cg[0] = tfDesc;
        cg[1] = l1;
        cg[2] = c;
        vContainers.addElement(c);
        vGroups.addElement(cg);
    }

    private void CreateNumericField(NDGQuestion obj) {
        NumericQuestion question = (NumericQuestion) obj;
        Container c;
        c = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        c.getStyle().setBorder(Border.createBevelLowered(0x7b7b7b, 0x7b7b7b, 0x7b7b7b, 0x7b7b7b));
        Label l2 = new Label(question.getName());
        c.addComponent(l2);
        NumericField nfNumber = new NumericField(((NumericQuestion)question).getLength(), ((NumericQuestion)question).isDecimal());
        nfNumber.setText((String)question.getAnswer().getValue());
        if (lastAddedContainer > -1) nfNumber.setNextFocusUp(((Container) vContainers.elementAt(lastAddedContainer)));
        c.addComponent(nfNumber);
        form.addComponent(c);
        c.setFocusable(true);
        c.addFocusListener(this);
        form.addComponent(new Label(""));
        Component[] cg;
        cg = new Component[3];
        cg[0] = nfNumber;
        cg[1] = l2;
        cg[2] = c;
        vContainers.addElement(c);
        vGroups.addElement(cg);
    }

    private void CreateDateField(NDGQuestion obj) {
        DateQuestion question = (DateQuestion) obj;
        Container c;
        c = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        c.getStyle().setBorder(Border.createBevelLowered(0x7b7b7b, 0x7b7b7b, 0x7b7b7b, 0x7b7b7b));
        Label l3 = new Label(question.getName());
        c.addComponent(l3);
        DateField dfDate = new DateField(DateField.DDMMYYYY);
        long datelong = Long.parseLong((String)question.getAnswer().getValue());
        dfDate.setDate(new Date(datelong));
        dfDate.setEditable(true);
        if (lastAddedContainer > -1) dfDate.setNextFocusUp(((Container) vContainers.elementAt(lastAddedContainer)));
        c.addComponent(dfDate);
        form.addComponent(c);
        c.setFocusable(true);
        c.addFocusListener(this);
        form.addComponent(new Label(""));
        Component[] cg;
        cg = new Component[3];
        cg[0] = dfDate;
        cg[1] = l3;
        cg[2] = c;
        vContainers.addElement(c);
        vGroups.addElement(cg);
    }

    private void CreateExclusiveChoiceField(NDGQuestion obj) {
        ChoiceQuestion question = (ChoiceQuestion) obj;
        Container c;
        c = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        c.getStyle().setBorder(Border.createBevelLowered(0x7b7b7b, 0x7b7b7b, 0x7b7b7b, 0x7b7b7b));
        Label l3 = new Label(question.getName());
        c.addComponent(l3);
        
        ButtonGroup groupButton = new ButtonGroup();
        Vector vChoices = question.getChoices();
        Vector vOthers = question.getOthers();
        int totalChoices = vChoices.size();
        String[] choices = new String[totalChoices];
        for (int i = 0; i < totalChoices; i++) {
            choices[i] = (String)vChoices.elementAt(i);
            RadioButton rb = new RadioButton(choices[i]);
            rb.addActionListener(this);
            rb.setOther(((String)vOthers.elementAt(i)).equals("1"));
            rb.setOtherText(""); // Initializes with empty string
            if (i == 0) {
                if (lastAddedContainer > -1) rb.setNextFocusUp(((Container) vContainers.elementAt(lastAddedContainer)));
            }
            groupButton.add(rb);
            c.addComponent(rb);
        }
        String answerValue = (String) question.getAnswer().getValue();
        if (answerValue != null && !answerValue.equals("")) {
            groupButton.setSelected(Integer.parseInt((String)question.getAnswer().getValue()));
            int index = Integer.parseInt((String)question.getAnswer().getValue());
            RadioButton rb = (RadioButton) groupButton.getRadioButton(index);
            Vector v = question.getOthersText();
            String str = (String) v.elementAt(0);
            rb.setOtherText(str);
        }
        form.addComponent(c);
        c.setFocusable(true);
        c.addFocusListener(this);
        form.addComponent(new Label(""));
        Component[] cg;
        cg = new Component[totalChoices+2];
        for (int i = 0; i < totalChoices; i++) {
            cg[i] = groupButton.getRadioButton(i);
        }
        cg[totalChoices] = l3;
        cg[totalChoices+1] = c;
        vContainers.addElement(c);
        vGroups.addElement(cg);

        if (!checkIfSkiped(Integer.parseInt(question.getCategoryId()), Integer.parseInt(question.getQuestionId())))
        {
            // SkipLogic Stuff
            catTo = -1;
            skipTo = -1;

            if (question.getSkipEnabled()) {
                // Check if selected RadioButton has Skip
                int selectedChoiceItem = 0;
                Component[] group = (Component[]) vGroups.lastElement();
                for (; selectedChoiceItem < group.length-2; selectedChoiceItem++) {
                    RadioButton rb = (RadioButton) group[selectedChoiceItem];
                    if (rb.isSelected()) {
                        break;
                    }
                }
                if (selectedChoiceItem < (group.length-2)) {
                    boolean sentence1 = ( (selectedChoiceItem != question.getChoiceItem()) && (question.isInverse()) );
                    boolean sentence2 = ( (selectedChoiceItem == question.getChoiceItem()) && (!question.isInverse()) );
                    if (sentence1 || sentence2)
                    {
                        catTo = question.getCatTo();
                        skipTo = question.getSkipTo();
                        forceSkipFalse = true;
                    }
                }
            }
            //
        }
    }

    private void CreateChoiceField(NDGQuestion obj) {
        ChoiceQuestion question = (ChoiceQuestion) obj;
        Container c;
        c = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        c.getStyle().setBorder(Border.createBevelLowered(0x7b7b7b, 0x7b7b7b, 0x7b7b7b, 0x7b7b7b));
        Label l3 = new Label(question.getName());
        c.addComponent(l3);

        Vector groupButton = new Vector();
        Vector vChoices = question.getChoices();
        Vector vOthers = question.getOthers();
        int totalChoices = vChoices.size();
        String[] choices = new String[totalChoices];
        for (int i = 0; i < totalChoices; i++) {
            choices[i] = (String)vChoices.elementAt(i);
            CheckBox cb = new CheckBox(choices[i]);
            cb.setOther(((String)vOthers.elementAt(i)).equals("1"));
            cb.setOtherText(""); // Initializes with empty string
            cb.addActionListener(this); //Vai servir para o More Details
            if (i == 0) {
                if (lastAddedContainer > -1) cb.setNextFocusUp(((Container) vContainers.elementAt(lastAddedContainer)));
                //cb.addFocusListener(this); //Faz bugar a passagem de perguntas para baixo
            }
            groupButton.addElement(cb);
            c.addComponent(cb);
        }
        int nIndex;
        Vector vChoicesAnswer = (Vector) question.getAnswer().getValue();
        for (int i = 0; i < vChoicesAnswer.size(); i++) {
            nIndex = Integer.parseInt( (String) vChoicesAnswer.elementAt(i) );
            ((CheckBox) groupButton.elementAt(nIndex)).setSelected(true);
            ((CheckBox) groupButton.elementAt(nIndex)).setOtherText((String) question.getOthersText().elementAt(i));
        }
        
        form.addComponent(c);
        c.setFocusable(true);
        c.addFocusListener(this);
        form.addComponent(new Label(""));
        Component[] cg;
        cg = new Component[totalChoices+2];
        for (int i = 0; i < totalChoices; i++) {
            cg[i] = (CheckBox) groupButton.elementAt(i);
        }
        cg[totalChoices] = l3;
        cg[totalChoices+1] = c;
        vContainers.addElement(c);
        vGroups.addElement(cg);
    }


    private void createImageField(NDGQuestion obj) {
        Image img = Screen.getRes().getImage("camera-icon");

        ImageQuestion question = (ImageQuestion) obj;
        Container c;
        c = new Container(new BoxLayout(BoxLayout.X_AXIS));
        c.getStyle().setBorder(Border.createBevelLowered(0x7b7b7b, 0x7b7b7b, 0x7b7b7b, 0x7b7b7b));
                      
        byte[] image01 = (byte[]) question.getAnswer().getValue();

        Button thumbnailImageButton;
        if(image01 == null || image01.length == 0){
            thumbnailImageButton = new Button();
            thumbnailImageButton.setIcon(img);
        }
        else{
            //decode
            Image pic01 = Image.createImage(image01, 0, image01.length);
            thumbnailImageButton = new Button();
            thumbnailImageButton.setIcon(pic01.scaled(Camera.THUMBNAIL_WIDTH, Camera.THUMBNAIL_HEIGHT));
        }
        thumbnailImageButton.setAlignment(Component.RIGHT);// setTextPosition(Component.BOTTOM);
        thumbnailImageButton.addActionListener(this);

        if (lastAddedContainer > -1)
            thumbnailImageButton.setNextFocusUp(((Container) vContainers.elementAt(lastAddedContainer)));
        c.addComponent(thumbnailImageButton);
        Label l1 = new Label(question.getName());
        c.addComponent(l1);
        form.addComponent(c);
        c.setFocusable(true);
        c.addFocusListener(this);
        
        form.addComponent(new Label(""));
        Component[] cg;
        cg = new Component[3];
        cg[0] = thumbnailImageButton;
        cg[1] = l1;
        cg[2] = c;
        vContainers.addElement(c);
        vGroups.addElement(cg);
    }
    
    public void show(){
        form.show();
    }
    public Vector getGroups(){
        return vGroups;
    }
    public int getFocusIndex(){
        return focusIndex;
    }

    private boolean checkIfSkiped(int catId, int questId) {
        if (forceSkipFalse) { // Avoid skipping the Exclusive Choice itself
            forceSkipFalse = false;
            return false;
        }
        
        boolean result = false;
        if (catId < catTo) {
            result = true;
        }
        else if (catId == catTo) {
             if   (questId < skipTo) {
                result = true;
             }
        }
        return result;
    }

    private void SaveResult() {
        // Save
        SaveInterviewFormCommand.getInstance().execute(vQuestions);
    }
    
    private boolean validateAnswer() {
        // Update Answer
        UpdateAnswer();

        // Check Pass constraints
        boolean result = true;
        Question question = (Question) vQuestions.elementAt(lastFocusIndex);
        if (!question.passConstraints()) {
            Component[] group = (Component[]) vGroups.elementAt(lastFocusIndex);
            group[group.length-1].requestFocus();
            result = false;
        }
        return result;
    }
}

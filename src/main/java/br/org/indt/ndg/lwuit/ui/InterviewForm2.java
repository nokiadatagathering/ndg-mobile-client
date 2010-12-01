/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.BackInterview2FormCommand;
import br.org.indt.ndg.lwuit.control.PersistenceManager;
import br.org.indt.ndg.lwuit.control.SaveInterviewForm2Command;
import br.org.indt.ndg.lwuit.control.SurveysControl;
import br.org.indt.ndg.lwuit.extended.CheckBox;
import br.org.indt.ndg.lwuit.extended.DateField;
import br.org.indt.ndg.lwuit.extended.TimeField;
import br.org.indt.ndg.lwuit.extended.NumericField;
import br.org.indt.ndg.lwuit.extended.FilterProxyListModel;
import br.org.indt.ndg.lwuit.extended.List;
import br.org.indt.ndg.lwuit.extended.RadioButton;
import br.org.indt.ndg.lwuit.model.ChoiceQuestion;
import br.org.indt.ndg.lwuit.model.DateQuestion;
import br.org.indt.ndg.lwuit.model.DecimalQuestion;
import br.org.indt.ndg.lwuit.model.DescriptiveQuestion;
import br.org.indt.ndg.lwuit.model.ImageQuestion;
import br.org.indt.ndg.lwuit.model.NDGQuestion;
import br.org.indt.ndg.lwuit.model.NumberAnswer;
import br.org.indt.ndg.lwuit.model.NumericQuestion;
import br.org.indt.ndg.lwuit.model.Question;
import br.org.indt.ndg.lwuit.model.TimeQuestion;
import br.org.indt.ndg.lwuit.ui.camera.NDGCameraManager;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.multimedia.Picture;
import com.sun.lwuit.Button;
import com.sun.lwuit.ButtonGroup;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.TextField;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.DataChangedListener;
import com.sun.lwuit.events.FocusListener;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.plaf.Border;
import com.sun.lwuit.list.DefaultListModel;
import com.sun.lwuit.list.ListModel;
import com.sun.lwuit.events.SelectionListener;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.list.ListCellRenderer;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

/**
 *
 * @author mturiel
 */
public class InterviewForm2 extends Screen implements FocusListener, ActionListener {

    private NDGQuestion currentQuestion;
    private Vector vContainers;
    private Vector vGroups;
    private Vector vQuestions;
    private int colorWhite = -1;
    private int grayColor = -1;
    private Border containerInactiveBorder;
    private Border containerActiveBorder;
    private int focusIndex = 0;
    private int pageIndex = 1;
    public boolean skipGameKey = false;
    private boolean answerChanged = false;
    
    private String title1, title2;
    private int catTo = -1;
    private int skipTo = -1;
    private boolean forceSkipFalse = false;
    private final int NUMBER_OF_QUESTION_TO_LOAD = 5;
    private final int NUMBER_OF_COLUMNS = 20;
    
    private Container lastContainer = null;
    private boolean newPage = false;
    private boolean goingUp = false;

    protected void loadData() {

        createScreen();
        vContainers = new Vector();
        vGroups = new Vector();
        vQuestions = new Vector();
        title1 = SurveysControl.getInstance().getOpenedSurveyTitle();
        title2 = Resources.NEW_INTERVIEW;
        catTo = -1;
        skipTo = -1;
        skipGameKey = false;
        setModifiedInterview(false);
        //lastFocusIndex = 0;
        lastContainer = null;
        focusIndex = 0;
        pageIndex = 1;
        loadAllQuestions();

        form.addGameKeyListener(Display.GAME_DOWN, new HandleGameKey());
        form.addGameKeyListener(Display.GAME_UP, new HandleGameKey());
    }

    protected void customize() {
        form.removeAll();
        form.removeAllCommands();
        form.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        form.getContentPane().getStyle().setBorder(Border.createEmpty(), false);
        form.setScrollAnimationSpeed(500);
        form.setCyclicFocus(false);

        //form.addCommand(cmdNext);
        form.addCommand(BackInterview2FormCommand.getInstance().getCommand());
        form.addCommand(SaveInterviewForm2Command.getInstance().getCommand());
        form.setCommandListener(this);

        if (PersistenceManager.getInstance().isEditing()) {
            title2 = Resources.EDITING;
        }
        setTitle(title1, title2);

        // Load first page of components and questions
        showQuestions((NUMBER_OF_QUESTION_TO_LOAD * pageIndex) - NUMBER_OF_QUESTION_TO_LOAD, true);
        updateContainerBorder();
    }

    private void loadAllQuestions() {
        SurveysControl.getInstance().resetQuestion();
        currentQuestion = SurveysControl.getInstance().getNextQuestion();

        NDGQuestion question = currentQuestion;
        while (question != null) {

            int catId = Integer.parseInt(question.getCategoryId());
            int questId = Integer.parseInt(question.getQuestionId());

            if (question instanceof ChoiceQuestion) {
                updateSkipInfo();
            }

            if (question instanceof TimeQuestion && question.getFirstTime()) {
                clearAMPMInfo();
            }

            if (checkIfSkiped(catId, questId)) {
                question.setSkiped(true);
            } else {
                question.setSkiped(false);
            }

            vQuestions.addElement(question);
            question = SurveysControl.getInstance().getNextQuestion();
        }

        currentQuestion = question;
    }

    private void showQuestions(int start, boolean first) {

        vContainers.removeAllElements();
        vGroups.removeAllElements();
        form.removeAll();
        int end = start + NUMBER_OF_QUESTION_TO_LOAD;
        if (end > vQuestions.size()) {
            end = vQuestions.size();
        }
        for (int i = start; i < end; i++) {
            currentQuestion = (NDGQuestion) vQuestions.elementAt(i);
            if (currentQuestion instanceof DescriptiveQuestion) {
                CreateDescriptiveField(currentQuestion);
            } else if (currentQuestion instanceof NumericQuestion) {
                CreateNumericField(currentQuestion);
            } else if (currentQuestion instanceof DateQuestion) {
                CreateDateField(currentQuestion);
            } else if (currentQuestion instanceof ChoiceQuestion) {
                if (((ChoiceQuestion) currentQuestion).isExclusive()) {
                    if (((ChoiceQuestion) currentQuestion).getChoices().size() <= 4) {
                        CreateExclusiveChoiceField(currentQuestion);
                    } else { //if there are more 4 choices (radiobutton)
                        CreateExclusiveChoiceFieldAutoComplete(currentQuestion);
                    }
                } else {
                    //if (((ChoiceQuestion) currentQuestion).getChoices().size() <= 4) {
                    CreateChoiceField(currentQuestion);
                    /*} else { //if there are more 4 choices (checkbox)
                    CreateExclusiveChoiceFieldAutoComplete1(currentQuestion);
                    }*/
                }
            } else if (currentQuestion instanceof ImageQuestion) {
                createImageField(currentQuestion);
            } else if (currentQuestion instanceof TimeQuestion) {
                if (((TimeQuestion) currentQuestion).getConvention() == 24) {
                    CreateTimeField(currentQuestion);
                } else if (((TimeQuestion) currentQuestion).getConvention() == 12) {
                    CreateTimeField12(currentQuestion);
                }


            }

            if (currentQuestion.getSkiped()) {
                Component[] group = (Component[]) vGroups.lastElement();
                for (int j = 0; j < group.length; j++) {
                    group[j].setEnabled(false);
                }
            }
        }

        focusIndex = (first) ? 0 : NUMBER_OF_QUESTION_TO_LOAD - 1;

        int nIndex = ((NUMBER_OF_QUESTION_TO_LOAD * pageIndex) - NUMBER_OF_QUESTION_TO_LOAD) + (focusIndex);
        while (((NDGQuestion) vQuestions.elementAt(nIndex)).getSkiped()) {
            if (first) {
                focusIndex++;
            } else {
                focusIndex--;
            }
            nIndex = ((NUMBER_OF_QUESTION_TO_LOAD * pageIndex) - NUMBER_OF_QUESTION_TO_LOAD) + (focusIndex);
        }

        if (end == vQuestions.size()) { // If last page then add an empty TextArea to force Scrooling the Screen
            TextArea endTA = new TextArea("");
            endTA.setRows((start + NUMBER_OF_QUESTION_TO_LOAD - end) * 2);
            endTA.setFocusable(false);
            endTA.setEditable(false);
            endTA.setVisible(false);
            form.addComponent(endTA);
        }

        //  currentQuestion = (NDGQuestion) vQuestions.elementAt(nIndex); //JMA

        newPage = true;
        Component[] group = (Component[]) vGroups.elementAt(focusIndex);
        group[0].requestFocus();
    }

    private void updateContainerBorder() {
        if (colorWhite < 0 || grayColor < 0) {
            colorWhite = 0x00FFFFFF;
            grayColor = ((Container) vContainers.elementAt(0)).getStyle().getBgColor();
        }

        if (containerInactiveBorder == null) {
            containerInactiveBorder = Border.createBevelLowered(0x7b7b7b, 0x7b7b7b, 0x7b7b7b, 0x7b7b7b);
            containerActiveBorder = Border.createBevelLowered(0x69b510, 0x69b510, 0x69b510, 0x69b510);
        }

        Container activeContainer = (Container) vContainers.elementAt(focusIndex);
        Container prevContainer = ((focusIndex - 1) >= 0) ? (Container) vContainers.elementAt(focusIndex - 1) : null;
        Container nextContainer = ((focusIndex + 1) < vContainers.size()) ? (Container) vContainers.elementAt(focusIndex + 1) : null;

        if (prevContainer != null) {
            prevContainer.getStyle().setBorder(containerInactiveBorder, false);
        }
        activeContainer.getStyle().setBorder(containerActiveBorder, false);
        if (nextContainer != null) {
            nextContainer.getStyle().setBorder(containerInactiveBorder, false);
        }

        if (prevContainer != null) {
            prevContainer.refreshTheme();
        }
        activeContainer.refreshTheme();
        if (nextContainer != null) {
            nextContainer.refreshTheme();
        }

        Component[] group = (Component[]) vGroups.elementAt(focusIndex);

        if ((!newPage) || (goingUp)) {
            if (group.length > 6) {
                form.scrollComponentToVisible(group[group.length - 2]);
            } else if (group.length > 3) {
                form.scrollComponentToVisible(group[1]);
            } else {
                form.scrollComponentToVisible(activeContainer);
            }
            if (goingUp) {
                goingUp = false;
            }
        }
        if (newPage) {
            newPage = false;
        }
        //form.scrollComponentToVisible(activeContainer);
    }

    private void UpdateAnswer() {

        // Set Index to Update
        int nIndex = ((NUMBER_OF_QUESTION_TO_LOAD * pageIndex) - NUMBER_OF_QUESTION_TO_LOAD) + focusIndex;

        // Set Visited
        ((NDGQuestion) vQuestions.elementAt(nIndex)).setVisited(true);

        Question question = (Question) vQuestions.elementAt(nIndex);
        Component[] group = (Component[]) vGroups.elementAt(focusIndex);
        if (question instanceof DescriptiveQuestion) {
            question.getAnswer().setValue(((DescriptiveField) group[0]).getText());
        } else if (question instanceof NumericQuestion) {
            question.getAnswer().setValue(((NumericField) group[0]).getText());
        } else if (question instanceof DateQuestion) {
            Date date = ((DateField) group[0]).getDate();
            Long datelong = new Long(date.getTime());
            question.getAnswer().setValue(datelong.toString());
        } else if (question instanceof TimeQuestion) {
            Date time = ((TimeField) group[0]).getTime();
            Long timelong = new Long(time.getTime());
            question.getAnswer().setValue(timelong.toString());
        } else if (question instanceof ChoiceQuestion) {
            ChoiceQuestion cq = (ChoiceQuestion) question;
            if (cq.isExclusive()) {
                if (cq.getChoices().size() <= 4) {
                    // Update selected index and Others Texts
                    String selectedIndex = "";
                    Vector vOthersText = new Vector();
                    for (int i = 0; i < group.length - 2; i++) {

                        RadioButton rb = (RadioButton) group[i];
                        if (rb.isSelected()) {
                            selectedIndex = (Integer.toString(i));
                        }
                        vOthersText.addElement(rb.getOtherText());
                    }
                    cq.getAnswer().setValue(selectedIndex);
                    cq.setOthersText(vOthersText);
                    ////
                } else { // Se cq.getChoices().size() > 4
                    // Update selected index and Others Texts
                    String selectedIndex = "";
                    Vector vOthersText = new Vector();
                    for (int i = 1; i < group.length - 3; i++) {

                        RadioButton rb = (RadioButton) group[i];
                        
                        if (rb.isSelected()) {
                            selectedIndex = (Integer.toString(i - 1));
                        }
                        vOthersText.addElement(rb.getOtherText());
                    }
                    cq.getAnswer().setValue(selectedIndex);
                    cq.setOthersText(vOthersText);
                    ////
                }
            } else {
                // Update selected elements and Others Texts
                Vector vIndexes = new Vector();
                Vector vOthersText = new Vector();
                for (int i = 0; i < group.length - 2; i++) {
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

    private void SaveResult() {
        // Save
        SaveInterviewForm2Command.getInstance().execute(vQuestions);
    }

    private boolean validateAnswer() {
        int nIndex = ((NUMBER_OF_QUESTION_TO_LOAD * pageIndex) - NUMBER_OF_QUESTION_TO_LOAD) + focusIndex;

        // Check Pass constraints
        boolean result = true;
        Question question = (Question) vQuestions.elementAt(nIndex);
        if (!question.passConstraints()) {
            Component[] group = (Component[]) vGroups.elementAt(focusIndex);
            group[0].requestFocus();
            result = false;
        }
        return result;
    }

    private void setModifiedInterview(boolean _val) {
        answerChanged = _val;
    }

    private boolean isModifiedInterview() {
        return answerChanged;
    }

    /////////////////////////Skip Logic Methods ////////////////////////////////
    private boolean checkIfSkiped(int catId, int questId) {
        if (forceSkipFalse) { // Avoid skipping the Exclusive Choice itself
            forceSkipFalse = false;
            return false;
        }

        boolean result = false;
        if (catId < catTo) {
            result = true;
        } else if (catId == catTo) {
            if (questId < skipTo) {
                result = true;
            }
        }
        return result;
    }

    private void updateSkipInfo() {

        if (currentQuestion instanceof ChoiceQuestion) {
            ChoiceQuestion question = (ChoiceQuestion) currentQuestion;
            if (!checkIfSkiped(Integer.parseInt(question.getCategoryId()), Integer.parseInt(question.getQuestionId()))) {
                // SkipLogic Stuff
                catTo = -1;
                skipTo = -1;

                if (question.getSkipEnabled()) {
                    // Check if selected RadioButton has Skip
                    int selectedChoiceItem = 0;
                    String answerValue = (String) question.getAnswer().getValue();
                    if (answerValue != null && !answerValue.equals("")) {
                        selectedChoiceItem = Integer.parseInt((String) question.getAnswer().getValue());
                        boolean sentence1 = ((selectedChoiceItem != ((ChoiceQuestion) currentQuestion).getChoiceItem()) && (((ChoiceQuestion) currentQuestion).isInverse()));
                        boolean sentence2 = ((selectedChoiceItem == ((ChoiceQuestion) currentQuestion).getChoiceItem()) && (!((ChoiceQuestion) currentQuestion).isInverse()));
                        if (sentence1 || sentence2) {
                            catTo = ((ChoiceQuestion) currentQuestion).getCatTo();
                            skipTo = ((ChoiceQuestion) currentQuestion).getSkipTo();
                            forceSkipFalse = true;
                        }
                    }
                }
            }
        }
    }

    private void clearAMPMInfo() {
        if (currentQuestion instanceof TimeQuestion) {
            ((TimeQuestion) currentQuestion).setAm_pm(-1); // clears
        }
    }
    
    private int findIndex(int catId, int questId) {
        //int nIndex = ((NUMBER_OF_QUESTION_TO_LOAD * pageIndex) - NUMBER_OF_QUESTION_TO_LOAD) + focusIndex;
        int nIndex = 0;
        for (; nIndex < vQuestions.size(); nIndex++) {
            currentQuestion = (NDGQuestion) vQuestions.elementAt(nIndex);

            if (Integer.parseInt(currentQuestion.getCategoryId()) == catId && Integer.parseInt(currentQuestion.getQuestionId()) == questId) {
                break;
            }
        }
        if (nIndex >= vQuestions.size()) {
            nIndex = -1; // Not Found
        }
        return nIndex;
    }

    public Vector getGroups() {
        return vGroups;
    }

    public int getFocusIndex() {
        return focusIndex;
    }

    public void show() {
        form.show();
    }

    ////////////////////////////////////////////////////////////////////////////
    ///////////////////////////// Handle Events ////////////////////////////////
    public void focusGained(Component cmp) {
    }

    public void focusLost(Component cmp) {
        lastContainer = cmp.getParent();
        skipGameKey = false;
    }

    public void actionPerformed(ActionEvent evt) { /*Class InteviewForm2 */
        Object cmd = evt.getSource();

        // Back Command pressed
        if (cmd == BackInterview2FormCommand.getInstance().getCommand()) {
            if (isModifiedInterview()) {
                GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_YES_NO, true);
                int resultCmdIndex = GeneralAlert.getInstance().show(Resources.CMD_SAVE, Resources.SAVE_MODIFICATIONS, GeneralAlert.CONFIRMATION);
                if (resultCmdIndex == GeneralAlert.RESULT_YES) {
                    UpdateAnswer();
                    if (validateAnswer()) {
                        SaveResult();
                    }
                } else {
                    BackInterview2FormCommand.getInstance().execute(null);
                }
            } else {
                BackInterview2FormCommand.getInstance().execute(null);
            }

        } else if (cmd == SaveInterviewForm2Command.getInstance().getCommand()) {
            UpdateAnswer();
            if (validateAnswer()) {
                SaveResult();
            }
        } //Image was clicked
        else if (cmd instanceof Button) {
            Question question = (Question) vQuestions.elementAt(((NUMBER_OF_QUESTION_TO_LOAD * pageIndex) - NUMBER_OF_QUESTION_TO_LOAD) + (focusIndex));
            if (question != null && question instanceof ImageQuestion) {
                NDGCameraManager.getInstance().displayCamera(this, (ImageQuestion) question);
            }
            // Modification in InterviewForm content
            setModifiedInterview(true);
        }
    }

    // This is Handle is used to mitigate the undesirable KeyDown and KeyUp Events that occur when pressing the characters V, P, and R.
    class HandleSpecialBuggedLetters implements DataChangedListener {

        public void dataChanged(int arg0, int arg1) {
            skipGameKey = true;

            // Modification in InterviewForm content
            setModifiedInterview(true);
        }
    }

    class HandleGameKey implements ActionListener {

        public void actionPerformed(ActionEvent evt) {

            int gameKey = evt.getKeyEvent();

            if (skipGameKey) {
                skipGameKey = false;
                return;
            }

            if ((focusIndex > -1) && (focusIndex < NUMBER_OF_QUESTION_TO_LOAD)) {

                UpdateAnswer();
                if (!validateAnswer()) {
                    return;
                }
            }

            int nIndex = 0;
            if (gameKey == Display.GAME_DOWN) {
                nIndex = ((NUMBER_OF_QUESTION_TO_LOAD * pageIndex) - NUMBER_OF_QUESTION_TO_LOAD) + (focusIndex + 1);
            } else {
                nIndex = ((NUMBER_OF_QUESTION_TO_LOAD * pageIndex) - NUMBER_OF_QUESTION_TO_LOAD) + (focusIndex - 1);
            }

            if ((nIndex > -1) && (nIndex < vQuestions.size())) {

                if (gameKey == Display.GAME_DOWN) {
                    focusIndex++;
                } else {
                    focusIndex--;
                }

                currentQuestion = (NDGQuestion) vQuestions.elementAt(nIndex);

                if (currentQuestion.getSkiped()) {
                    actionPerformed(evt);
                    return;
                }
            }

            switch (gameKey) {
                case Display.GAME_DOWN: {
                    if (goingUp) {
                        goingUp = false;
                    }
                    if (((focusIndex) * 2) < form.getContentPane().getComponentCount()) {
                        //focusIndex++;
                        updateContainerBorder();
                    } else {
                        if (((NUMBER_OF_QUESTION_TO_LOAD * (pageIndex + 1)) - NUMBER_OF_QUESTION_TO_LOAD) < vQuestions.size()) {
                            pageIndex++;
                            showQuestions((NUMBER_OF_QUESTION_TO_LOAD * pageIndex) - NUMBER_OF_QUESTION_TO_LOAD, true);
                            updateContainerBorder();
                            
                        }
                    }
                }
                break;
                case Display.GAME_UP: {
                    goingUp = true;
                    if ((focusIndex) > -1) {
                        //focusIndex--;
                        updateContainerBorder();
                    } else {
                        if (((NUMBER_OF_QUESTION_TO_LOAD * (pageIndex - 1)) - NUMBER_OF_QUESTION_TO_LOAD) > -1) {
                            pageIndex--;
                            showQuestions((NUMBER_OF_QUESTION_TO_LOAD * pageIndex) - NUMBER_OF_QUESTION_TO_LOAD, false);
                            updateContainerBorder();
                        }
                    }
                }
                break;
            }



        }
    }

    class HandleChoiceFocus implements FocusListener {

        public void focusGained(Component cmp) {
            if (cmp.getParent() != lastContainer) {
                skipGameKey = false;
            } else {
                skipGameKey = true;
            }
        }

        public void focusLost(Component cmp) {
            lastContainer = cmp.getParent();

            skipGameKey = false;
        }
    }

    class HandleChoiceFocusList implements FocusListener {

        public void focusGained(Component cmp) {
            //(cmp.getParent()).setY(120);
            //form.scrollComponentToVisible(cmp.getParent());
            if (cmp.getParent().getParent() != lastContainer) {

                skipGameKey = false;
            } else {
                skipGameKey = true;
            }
        }

        public void focusLost(Component cmp) {
            ((List) cmp).setSelectedIndex(0);
            lastContainer = cmp.getParent().getParent();
            skipGameKey = false;
        }
    }

    class HandleMoreDetails implements ActionListener {

        public void actionPerformed(ActionEvent evt) {

            // Modification in InterviewForm content
            setModifiedInterview(true);

            Object cmd = evt.getSource();

            // Check Box More details
            if ((cmd instanceof CheckBox)) {

                Component[] group = (Component[]) vGroups.elementAt(focusIndex);
                for (int i = 0; i < group.length; i++) {
                    if (cmd == group[i]) {
                        CheckBox cb = (CheckBox) group[i];
                        if ((cb.hasOther()) && (cb.isSelected())) {
                            DetailsForm.show(cb.getText(), cb.getOtherText());
                            cb.setOtherText(SurveysControl.getInstance().getItemOtherText());
                        }
                        if ((cb.hasOther()) && (!cb.isSelected())) {
                            cb.setOtherText("");
                        }
                    }
                }
            } // Radio Button More details
            else if ((cmd instanceof RadioButton)) {

                if (currentQuestion instanceof TimeQuestion) {
                    if (((TimeQuestion) currentQuestion).getConvention() != 24) {
                        if (((RadioButton) cmd).getText().equals("am")) {
                            ((TimeQuestion) currentQuestion).setAm_pm(TimeQuestion.AM);
                        } else {
                            if (((RadioButton) cmd).getText().equals("pm")) {
                                  ((TimeQuestion) currentQuestion).setAm_pm(TimeQuestion.PM);
                            }
                        }
                    }
                }
                Component[] group = (Component[]) vGroups.elementAt(focusIndex);

                for (int i = 0; i < group.length; i++) {
                    if (cmd == group[i]) {
                        RadioButton rb = (RadioButton) group[i];
                        if ((rb.hasOther()) && (rb.isSelected())) {
                            DetailsForm.show(rb.getText(), rb.getOtherText());
                            rb.setOtherText(SurveysControl.getInstance().getItemOtherText());
                        }
                        if ((rb.hasOther()) && (!rb.isSelected())) {
                            rb.setOtherText("");
                        }
                    }
                }
                UpdateAnswer();
                currentQuestion = (NDGQuestion) vQuestions.elementAt(((NUMBER_OF_QUESTION_TO_LOAD * pageIndex) - NUMBER_OF_QUESTION_TO_LOAD) + focusIndex);

                updateSkipInfo();

                if ((catTo != -1) && (skipTo != -1)) {
                    // The selected RadioButton HAS Skip
                    int nLastIndex = findIndex(catTo, skipTo);
                    if (nLastIndex > -1) {
                        int nIndex = ((NUMBER_OF_QUESTION_TO_LOAD * pageIndex) - NUMBER_OF_QUESTION_TO_LOAD) + (focusIndex);
                        for (nIndex = nIndex + 1; nIndex < nLastIndex; nIndex++) {
                            currentQuestion = (NDGQuestion) vQuestions.elementAt(nIndex);
                            currentQuestion.setSkiped(true);
                        }
                    }

                    int newFocusIndex = nLastIndex % NUMBER_OF_QUESTION_TO_LOAD;
                    int newPageIndex = ((int) Math.floor(nLastIndex / NUMBER_OF_QUESTION_TO_LOAD)) + 1;

                    if (newPageIndex == pageIndex) {
                        // Update components
                        for (int i = focusIndex + 1; i < newFocusIndex; i++) {
                            group = (Component[]) vGroups.elementAt(i);
                            for (int j = 0; j < group.length; j++) {
                                group[j].setEnabled(false);
                            }
                        }
                        focusIndex = newFocusIndex;
                        group = (Component[]) vGroups.elementAt(focusIndex);
                        updateContainerBorder();
                        group[0].requestFocus();
                    } else {
                        pageIndex = newPageIndex;
                        if (((NUMBER_OF_QUESTION_TO_LOAD * (pageIndex)) - NUMBER_OF_QUESTION_TO_LOAD) < vQuestions.size()) {
                            showQuestions((NUMBER_OF_QUESTION_TO_LOAD * pageIndex) - NUMBER_OF_QUESTION_TO_LOAD, true);
                            updateContainerBorder();
                        }
                    }
                } // The selected RadioButton DOES NOT HAVE Skip
                else {
                    int nLastIndex;
                    if (currentQuestion instanceof TimeQuestion) {
                        nLastIndex = findIndex(-1, -1);
                    } else {
                        nLastIndex = findIndex(((ChoiceQuestion) currentQuestion).getCatTo(), ((ChoiceQuestion) currentQuestion).getSkipTo());
                    }
                    if (nLastIndex > -1) {
                        int nIndex = ((NUMBER_OF_QUESTION_TO_LOAD * pageIndex) - NUMBER_OF_QUESTION_TO_LOAD) + (focusIndex);
                        for (nIndex = nIndex + 1; nIndex < nLastIndex; nIndex++) {
                            currentQuestion = (NDGQuestion) vQuestions.elementAt(nIndex);
                            currentQuestion.setSkiped(false);
                        }
                    }

                    int newFocusIndex = nLastIndex % NUMBER_OF_QUESTION_TO_LOAD;
                    int newPageIndex = ((int) Math.floor(nLastIndex / NUMBER_OF_QUESTION_TO_LOAD)) + 1;

                    if (newPageIndex == pageIndex) {
                        // Update components
                        for (int i = focusIndex + 1; i < newFocusIndex; i++) {
                            group = (Component[]) vGroups.elementAt(i);
                            for (int j = 0; j < group.length; j++) {
                                group[j].setEnabled(true);
                            }
                        }
                    }

                    //if((focusIndex + 1) < NUMBER_OF_QUESTION_TO_LOAD){
                    if (((focusIndex + 1) * 2) < form.getContentPane().getComponentCount()) {
                        //if  ( (focusIndex+1)  < vQuestions.size() ) {
                        focusIndex++;
                        int nIndex = ((NUMBER_OF_QUESTION_TO_LOAD * pageIndex) - NUMBER_OF_QUESTION_TO_LOAD) + (focusIndex);
                        if( nIndex < vQuestions.size() )
                        {
                            currentQuestion = (NDGQuestion) vQuestions.elementAt(nIndex);
                            group = (Component[]) vGroups.elementAt(focusIndex); // ibb
                            group[0].requestFocus();
                            updateContainerBorder();
                        } else {
                            focusIndex--;
                        }

                    } else {
                        if (((NUMBER_OF_QUESTION_TO_LOAD * (pageIndex + 1)) - NUMBER_OF_QUESTION_TO_LOAD) < vQuestions.size()) {
                            pageIndex++;
                            showQuestions((NUMBER_OF_QUESTION_TO_LOAD * pageIndex) - NUMBER_OF_QUESTION_TO_LOAD, true);
                            updateContainerBorder();
                        }
                    }
                }



            } else if ((cmd instanceof List)) {

                final List list = (List) cmd;
                if (list.getRenderer() instanceof CheckBox) {

                    ((OptionSelectable) list.getSelectedItem()).toggleSelection();

                    //skipGameKey = false;

                    Component[] group = (Component[]) vGroups.elementAt(focusIndex);

                    ((CheckBox) group[list.getSelectedIndex()]).setSelected(((OptionSelectable) list.getSelectedItem()).getSelected());
                } else if (list.getRenderer() instanceof RadioButton) {
                    ((OptionSelectableRadio) list.getSelectedItem()).toggleSelection();
                    //(list.getModel().getItemAt())
                    //int selItem = list.getModel().getSelectedIndex();//list.getSelectedIndex();
                    int filterOffset = list.getSelectedIndex();
                    int selItem = ((FilterProxyListModel)(list.getModel())).getFilterOffset(filterOffset);
                    
                    Component[] group = (Component[]) vGroups.elementAt(focusIndex);

                    for (int i = 0; i < (group.length); i++) {
                        if (group[i] instanceof RadioButton) {
                            RadioButton rb = (RadioButton) group[i];
                            rb.setSelected(false);
                            if (rb.hasOther()) {
                                rb.setOtherText("");
                            }
                        }
                    }
                    if (selItem >= 0) {
                        RadioButton rb = (RadioButton) group[selItem + 1];
                        rb.setSelected(true);

                        if ((rb.hasOther()) && (rb.isSelected())) {
                            DetailsForm.show(rb.getText(), rb.getOtherText());
                            rb.setOtherText(SurveysControl.getInstance().getItemOtherText());
                        }
                    }

                    UpdateAnswer();
                    currentQuestion = (NDGQuestion) vQuestions.elementAt(((NUMBER_OF_QUESTION_TO_LOAD * pageIndex) - NUMBER_OF_QUESTION_TO_LOAD) + focusIndex);
                    updateSkipInfo();

                    if ((catTo != -1) && (skipTo != -1)) {
                        // The selected RadioButton HAS Skip
                        int nLastIndex = findIndex(catTo, skipTo);
                        if (nLastIndex > -1) {
                            int nIndex = ((NUMBER_OF_QUESTION_TO_LOAD * pageIndex) - NUMBER_OF_QUESTION_TO_LOAD) + (focusIndex);
                            for (nIndex = nIndex + 1; nIndex < nLastIndex; nIndex++) {
                                currentQuestion = (NDGQuestion) vQuestions.elementAt(nIndex);
                                currentQuestion.setSkiped(true);
                            }
                        }

                        int newFocusIndex = nLastIndex % NUMBER_OF_QUESTION_TO_LOAD;
                        int newPageIndex = ((int) Math.floor(nLastIndex / NUMBER_OF_QUESTION_TO_LOAD)) + 1;

                        if (newPageIndex == pageIndex) {
                            // Update components
                            for (int i = focusIndex + 1; i < newFocusIndex; i++) {
                                group = (Component[]) vGroups.elementAt(i);
                                for (int j = 1; j < (group.length + 1); j++) {
                                    group[j].setEnabled(false);
                                }
                            }
                            focusIndex = newFocusIndex;
                            group = (Component[]) vGroups.elementAt(focusIndex);

                            updateContainerBorder();
                            group[0].requestFocus();
                        } else {
                            pageIndex = newPageIndex;
                            if (((NUMBER_OF_QUESTION_TO_LOAD * (pageIndex)) - NUMBER_OF_QUESTION_TO_LOAD) < vQuestions.size()) {
                                showQuestions((NUMBER_OF_QUESTION_TO_LOAD * pageIndex) - NUMBER_OF_QUESTION_TO_LOAD, true);
                                updateContainerBorder();
                            }
                        }
                    } // The selected RadioButton DOES NOT HAVE Skip
                    else {
                        int nLastIndex = findIndex(((ChoiceQuestion) currentQuestion).getCatTo(), ((ChoiceQuestion) currentQuestion).getSkipTo());
                        if (nLastIndex > -1) {
                            int nIndex = ((NUMBER_OF_QUESTION_TO_LOAD * pageIndex) - NUMBER_OF_QUESTION_TO_LOAD) + (focusIndex);
                            for (nIndex = nIndex + 1; nIndex < nLastIndex; nIndex++) {
                                currentQuestion = (NDGQuestion) vQuestions.elementAt(nIndex);
                                currentQuestion.setSkiped(false);
                            }
                        }

                        int newFocusIndex = nLastIndex % NUMBER_OF_QUESTION_TO_LOAD;
                        int newPageIndex = ((int) Math.floor(nLastIndex / NUMBER_OF_QUESTION_TO_LOAD)) + 1;


                        if (newPageIndex == pageIndex) {
                            // Update components
                            for (int i = focusIndex + 1; i < newFocusIndex; i++) {
                                group = (Component[]) vGroups.elementAt(i);
                                for (int j = 1; j < (group.length + 1); j++) {
                                    group[j].setEnabled(true);
                                }
                            }
                        }

                        //if((focusIndex + 1) < NUMBER_OF_QUESTION_TO_LOAD){
                        if (((focusIndex + 1) * 2) < form.getContentPane().getComponentCount()) {
                            //if  ( (focusIndex+1)  < vQuestions.size() ) {
                            focusIndex++;
                            if (focusIndex < vGroups.size()) {
                                int nIndex = ((NUMBER_OF_QUESTION_TO_LOAD * pageIndex) - NUMBER_OF_QUESTION_TO_LOAD) + (focusIndex);
                                currentQuestion = (NDGQuestion) vQuestions.elementAt(nIndex);
                                group = (Component[]) vGroups.elementAt(focusIndex);
                                group[0].requestFocus();
                                updateContainerBorder();
                            } else {
                                focusIndex--;
                            }
                        } else {
                            if (((NUMBER_OF_QUESTION_TO_LOAD * (pageIndex + 1)) - NUMBER_OF_QUESTION_TO_LOAD) < vQuestions.size()) {
                                pageIndex++;
                                showQuestions((NUMBER_OF_QUESTION_TO_LOAD * pageIndex) - NUMBER_OF_QUESTION_TO_LOAD, true);
                                updateContainerBorder();
                            }
                        }
                    }

                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    /////////////////////////// Descriptive Question ///////////////////////////
    void CreateDescriptiveField(NDGQuestion obj) {
        DescriptiveQuestion question = (DescriptiveQuestion) obj;
        final Container c;
        c = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        c.getStyle().setBorder(Border.createBevelLowered(0x7b7b7b, 0x7b7b7b, 0x7b7b7b, 0x7b7b7b));
        TextArea qname = createQuestionName(question.getName());
        c.addComponent(qname);
        //DescriptiveField tfDesc = new DescriptiveField(((DescriptiveQuestion) question).getLength());
        final DescriptiveField tfDesc = new DescriptiveField(((DescriptiveQuestion) question).getLength());
        tfDesc.setText((String) question.getAnswer().getValue());
        tfDesc.setInputMode("Abc");
        tfDesc.setEditable(true);
        //tfDesc.addFocusListener(this);
        //tfDesc.addDataChangeListener(new HandleSpecialBuggedLetters());
        c.addComponent(tfDesc);

        tfDesc.addFocusListener(new FocusListener() {

            public void focusGained(Component cmp) {
                if (cmp.getParent() != lastContainer) {
                    skipGameKey = false;
                } else {
                    skipGameKey = true;
                }
            }

            public void focusLost(final Component cmp) {
                lastContainer = cmp.getParent();
                skipGameKey = false;
            }
        });

        if (question.getChoices().size() >= 1) {

            ////////////////////////////////////////////////////////////
            Vector vChoices = question.getChoices();
            int totalChoices = vChoices.size();
            String[] choices = new String[totalChoices];

            final ListModel underlyingModel = new DefaultListModel();
            for (int i = 0; i < totalChoices; i++) {
                choices[i] = (String) vChoices.elementAt(i);
                underlyingModel.addItem(choices[i]);
            }

            final FilterProxyListModel proxyModel = new FilterProxyListModel(underlyingModel);
            proxyModel.setMaxDisplay(4);
            final List choice = new List(proxyModel);

            choice.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent ae) {
                    tfDesc.setText(((List) ae.getSource()).getSelectedItem().toString());
                }
            });

            choice.addFocusListener(new HandleChoiceFocusList());

            tfDesc.addDataChangeListener(new DataChangedListener() {

                public void dataChanged(int arg0, int arg1) {
                    skipGameKey = true;
                    proxyModel.filter(tfDesc.getText());
                    setModifiedInterview(true);

                }
            });

            c.addComponent(choice);
            form.addComponent(c);

            form.addComponent(new Label(""));
            Component[] cg;
            cg = new Component[4];
            cg[0] = tfDesc;
            cg[1] = choice;
            cg[2] = qname;
            cg[3] = c;
            vContainers.addElement(c);
            vGroups.addElement(cg);
            ////////////////////////////////////////////////////////////
        } else {
            tfDesc.addDataChangeListener(new HandleSpecialBuggedLetters());
            form.addComponent(c);

            form.addComponent(new Label(""));
            Component[] cg;

            cg = new Component[3];
            cg[0] = tfDesc;
            cg[1] = qname;
            cg[2] = c;
            vContainers.addElement(c);
            vGroups.addElement(cg);
        }

        //vContainers.addElement(c);
        //vGroups.addElement(cg);
    }
    ////////////////////////////////////////////////////////////////////////////

    ///////////////////////////// Numeric Question /////////////////////////////
    private void CreateNumericField(NDGQuestion obj) {
        NumericQuestion question = (NumericQuestion) obj;
        Container c;
        c = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        c.getStyle().setBorder(Border.createBevelLowered(0x7b7b7b, 0x7b7b7b, 0x7b7b7b, 0x7b7b7b));
        TextArea qname = createQuestionName(question.getName());
        c.addComponent(qname);

        NumericField nfNumber = new NumericField(((NumericQuestion) question).getLength(), question instanceof DecimalQuestion );
        String value =((NumberAnswer)question.getAnswer()).getValueString();
        nfNumber.setText(value);
        nfNumber.addFocusListener(this);
        nfNumber.addDataChangeListener(new HandleSpecialBuggedLetters());

        c.addComponent(nfNumber);
        form.addComponent(c);
        form.addComponent(new Label(""));
        Component[] cg;
        cg = new Component[3];
        cg[0] = nfNumber;
        cg[1] = qname;
        cg[2] = c;
        vContainers.addElement(c);
        vGroups.addElement(cg);
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////// Timer Question 24///////////////////////////////
    private void CreateTimeField(NDGQuestion obj) {
        TimeQuestion question = (TimeQuestion) obj;
        Container c;
        c = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        c.getStyle().setBorder(Border.createBevelLowered(0x7b7b7b, 0x7b7b7b, 0x7b7b7b, 0x7b7b7b));
        TextArea qname = createQuestionName(question.getName());
        c.addComponent(qname);
        TimeField tfTime = new TimeField(TimeField.HHMM1);

        long datelong = Long.parseLong((String) question.getAnswer().getValue());

        tfTime.setTime(new Date(datelong));
        tfTime.setEditable(true);
        tfTime.addFocusListener(this);
        tfTime.addDataChangeListener(new HandleSpecialBuggedLetters());

        c.addComponent(tfTime);
        form.addComponent(c);
        form.addComponent(new Label(""));
        Component[] cg;
        cg = new Component[3];
        cg[0] = tfTime;
        cg[1] = qname;
        cg[2] = c;
        vContainers.addElement(c);
        vGroups.addElement(cg);
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////// Timer Question 12///////////////////////////////
    private void CreateTimeField12(NDGQuestion obj) {

        TimeQuestion question = (TimeQuestion) obj;
        Container c;
        c = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        c.getStyle().setBorder(Border.createBevelLowered(0x7b7b7b, 0x7b7b7b, 0x7b7b7b, 0x7b7b7b));
        TextArea qname = createQuestionName(question.getName());
        c.addComponent(qname);
        TimeField tfTime = new TimeField(TimeField.HHMM);
        tfTime.addFocusListener(new HandleChoiceFocus());

        ButtonGroup groupButton = new ButtonGroup();
        final RadioButton am = new RadioButton("am");
        final RadioButton pm = new RadioButton("pm");
        am.addFocusListener(new HandleChoiceFocus());
        pm.addFocusListener(new HandleChoiceFocus());

        groupButton.add(am);
        groupButton.add(pm);

        am.addActionListener(new HandleMoreDetails());
        pm.addActionListener(new HandleMoreDetails());

        long datelong = Long.parseLong((String) question.getAnswer().getValue());
        Date date = new Date(datelong);

        if (((TimeQuestion) question).getAm_pm() == 1) {
            groupButton.setSelected(am);
        } else if (((TimeQuestion) question).getAm_pm() == 2) {
            groupButton.setSelected(pm);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            if (cal.get(Calendar.AM_PM) == Calendar.AM) {
                groupButton.setSelected(am);
                ((TimeQuestion) currentQuestion).setAm_pm(TimeQuestion.AM);
            } else {
                groupButton.setSelected(pm);
                ((TimeQuestion) currentQuestion).setAm_pm(TimeQuestion.PM);
            }
        }

        tfTime.setTime(date);
        tfTime.setEditable(true);
        tfTime.addFocusListener(this);
        tfTime.addDataChangeListener(new HandleSpecialBuggedLetters());

        c.addComponent(tfTime);
        c.addComponent(am);
        c.addComponent(pm);

        form.addComponent(c);
        form.addComponent(new Label(""));
        Component[] cg;
        cg = new Component[5];
        cg[0] = tfTime;
        cg[1] = qname;
        cg[2] = am;
        cg[3] = pm;
        cg[4] = c;
        vContainers.addElement(c);
        vGroups.addElement(cg);
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////// Date Question ///////////////////////////////
    private void CreateDateField(NDGQuestion obj) {
        DateQuestion question = (DateQuestion) obj;
        Container c;
        c = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        c.getStyle().setBorder(Border.createBevelLowered(0x7b7b7b, 0x7b7b7b, 0x7b7b7b, 0x7b7b7b));
        TextArea qname = createQuestionName(question.getName());
        c.addComponent(qname);
        DateField dfDate = new DateField(DateField.DDMMYYYY);
        long datelong = Long.parseLong((String) question.getAnswer().getValue());
        dfDate.setDate(new Date(datelong));
        dfDate.setEditable(true);
        dfDate.addFocusListener(this);
        dfDate.addDataChangeListener(new HandleSpecialBuggedLetters());

        c.addComponent(dfDate);
        form.addComponent(c);
        form.addComponent(new Label(""));
        Component[] cg;
        cg = new Component[3];
        cg[0] = dfDate;
        cg[1] = qname;
        //cg[1] = l3;
        cg[2] = c;
        vContainers.addElement(c);
        vGroups.addElement(cg);
    }
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////// Choice Multiple Question ////////////////////////
    private void CreateChoiceField(NDGQuestion obj) {
        ChoiceQuestion question = (ChoiceQuestion) obj;
        Container c;
        c = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        c.getStyle().setBorder(Border.createBevelLowered(0x7b7b7b, 0x7b7b7b, 0x7b7b7b, 0x7b7b7b));
        TextArea qname = createQuestionName(question.getName());
        c.addComponent(qname);

        Vector groupButton = new Vector();
        Vector vChoices = question.getChoices();
        Vector vOthers = question.getOthers();
        int totalChoices = vChoices.size();
        String[] choices = new String[totalChoices];
        for (int i = 0; i < totalChoices; i++) {
            choices[i] = (String) vChoices.elementAt(i);
            CheckBox cb = new CheckBox(choices[i]);
            cb.setOther(((String) vOthers.elementAt(i)).equals("1"));
            cb.setOtherText(""); // Initializes with empty string
            cb.addActionListener(new HandleMoreDetails()); // More Details
            cb.addFocusListener(new HandleChoiceFocus()); // Controls when changing to a new question
            groupButton.addElement(cb);
            c.addComponent(cb);
        }
        int nIndex;
        Vector vChoicesAnswer = (Vector) question.getAnswer().getValue();
        for (int i = 0; i < vChoicesAnswer.size(); i++) {
            nIndex = Integer.parseInt((String) vChoicesAnswer.elementAt(i));
            ((CheckBox) groupButton.elementAt(nIndex)).setSelected(true);
            ((CheckBox) groupButton.elementAt(nIndex)).setOtherText((String) question.getOthersText().elementAt(i));
        }

        form.addComponent(c);
        form.addComponent(new Label(""));
        Component[] cg;
        cg = new Component[totalChoices + 2];
        for (int i = 0; i < totalChoices; i++) {
            cg[i] = (CheckBox) groupButton.elementAt(i);
        }
        cg[totalChoices] = qname;
        cg[totalChoices + 1] = c;
        vContainers.addElement(c);
        vGroups.addElement(cg);
    }
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////// Choice Exclusive Question ////////////////////////
    private void CreateExclusiveChoiceField(NDGQuestion obj) {
        ChoiceQuestion question = (ChoiceQuestion) obj;
        Container c;
        c = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        c.getStyle().setBorder(Border.createBevelLowered(0x7b7b7b, 0x7b7b7b, 0x7b7b7b, 0x7b7b7b));
        TextArea qname = createQuestionName(question.getName());
        c.addComponent(qname);

        ButtonGroup groupButton = new ButtonGroup();
        Vector vChoices = question.getChoices();
        Vector vOthers = question.getOthers();
        int totalChoices = vChoices.size();
        String[] choices = new String[totalChoices];
        for (int i = 0; i < totalChoices; i++) {
            choices[i] = (String) vChoices.elementAt(i);
            RadioButton rb = new RadioButton(choices[i]);
            rb.setOther(((String) vOthers.elementAt(i)).equals("1"));
            rb.setOtherText(""); // Initializes with empty string
            rb.addActionListener(new HandleMoreDetails()); // More Details
            rb.addFocusListener(new HandleChoiceFocus()); // Controls when changing to a new question
            groupButton.add(rb);
            c.addComponent(rb);
        }
        String answerValue = (String) question.getAnswer().getValue();
        if (answerValue != null && !answerValue.equals("")) {
            groupButton.setSelected(Integer.parseInt((String) question.getAnswer().getValue()));
            int index = Integer.parseInt((String) question.getAnswer().getValue());
            RadioButton rb = (RadioButton) groupButton.getRadioButton(index);
            Vector v = question.getOthersText();
            String str = (String) v.elementAt(0);
            rb.setOtherText(str);
        }
        form.addComponent(c);
        form.addComponent(new Label(""));
        Component[] cg;
        cg = new Component[totalChoices + 2];
        for (int i = 0; i < totalChoices; i++) {
            cg[i] = groupButton.getRadioButton(i);
        }
        cg[totalChoices] = qname;
        cg[totalChoices + 1] = c;
        vContainers.addElement(c);
        vGroups.addElement(cg);
    }
    ///////////////////////////// Image Question ///////////////////////////////
    ////////////////////////////////////////////////////////////////////////////

    private void createImageField(NDGQuestion obj) {

        ImageQuestion question = (ImageQuestion) obj;
        Container c;
        c = new Container(new BoxLayout(BoxLayout.X_AXIS));
        c.getStyle().setBorder(Border.createBevelLowered(0x7b7b7b, 0x7b7b7b, 0x7b7b7b, 0x7b7b7b));

        byte[] image01 = (byte[]) question.getAnswer().getValue();

        Button thumbnailImageButton;
        if (image01 == null || image01.length == 0) {
            Image img = Screen.getRes().getImage("camera-icon");
            thumbnailImageButton = new Button();
            thumbnailImageButton.setIcon(img);
        } else {
            //decode
            Picture picture = Picture.createPicture(image01);
            thumbnailImageButton = new Button();
            thumbnailImageButton.setIcon(picture.getThumbnail());
        }
        thumbnailImageButton.setAlignment(Component.RIGHT);
        thumbnailImageButton.addActionListener(this);

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
    ////////////////////////////////////////////////////////////////////////////

    private TextArea createQuestionName(String name) {
        TextArea questionName = new TextArea();
        questionName.setEditable(false);
        questionName.setFocusable(false);
        questionName.setColumns(NUMBER_OF_COLUMNS);
        questionName.setRows(1);
        questionName.setGrowByContent(false);
        questionName.setText(name);

        int pw = Display.getInstance().getDisplayWidth();
        int w = questionName.getStyle().getFont().stringWidth(name);
        if (w > pw)
        {
            questionName.setGrowByContent(true);
            questionName.setRows(2);
        }
        else
        {
            questionName.setGrowByContent(false);
            questionName.setRows(1);
        }

        return questionName;
    }

    private class TextFieldEx extends TextField {

        protected Command installCommands(Command clear, Command t9) {
            com.sun.lwuit.Form f = getComponentForm();
            Command[] originalCommands = new Command[f.getCommandCount()];
            for (int iter = 0; iter < originalCommands.length; iter++) {
                originalCommands[iter] = f.getCommand(iter);
            }
            f.removeAllCommands();
            Command retVal = super.installCommands(clear, t9);
            for (int iter = originalCommands.length - 1; iter >= 0; iter--) {
                f.addCommand(originalCommands[iter]);
            }
            return retVal;
        }
    }

    private void CreateExclusiveChoiceFieldAutoComplete(NDGQuestion obj) {

        ChoiceQuestion question = (ChoiceQuestion) obj;
        final Container c;

        c = new Container(new BoxLayout(BoxLayout.Y_AXIS));

        c.getStyle().setBorder(Border.createBevelLowered(0x7b7b7b, 0x7b7b7b, 0x7b7b7b, 0x7b7b7b));
        TextArea qname = createQuestionName(question.getName());
        c.addComponent(qname);

        final TextFieldEx exclusiveChoiceTextField = new TextFieldEx();

        exclusiveChoiceTextField.addFocusListener(new FocusListener() {

            public void focusGained(Component cmp) {

                if (cmp.getParent() != lastContainer) {
                    skipGameKey = false;
                } else {
                    skipGameKey = true;
                }
            }

            public void focusLost(final Component cmp) {
                lastContainer = cmp.getParent();
                skipGameKey = false;
            }
        });

        final ButtonGroup groupButton = new ButtonGroup();

        Vector vChoices = question.getChoices();
        Vector vOthers = question.getOthers();
        int totalChoices = vChoices.size();
        String[] choices = new String[totalChoices];

        final ListModel underlyingModel = new DefaultListModel();
        final ManagerOptionSelectableRadio managerOptionSelectableRadio = new ManagerOptionSelectableRadio();
        for (int i = 0; i < totalChoices; i++) {
            choices[i] = (String) vChoices.elementAt(i);
            RadioButton rb = new RadioButton(choices[i]);
            //underlyingModel.addItem(choices[i]);
           
            underlyingModel.addItem(new OptionSelectableRadio(choices[i], managerOptionSelectableRadio));

            rb.setOther(((String) vOthers.elementAt(i)).equals("1"));
            rb.setOtherText(""); // Initializes with empty string
            //rb.addActionListener(new HandleMoreDetails()); // More Details
            //rb.addFocusListener(new HandleChoiceFocus()); // Controls when changing to a new question
            
            groupButton.add(rb);

            //c.addComponent(rb);
        }

        String answerValue = (String) question.getAnswer().getValue();

        if (answerValue != null && !answerValue.equals("")) {
            groupButton.setSelected(Integer.parseInt((String) question.getAnswer().getValue()));
            ((OptionSelectableRadio) underlyingModel.getItemAt(Integer.parseInt((String) question.getAnswer().getValue()))).toggleSelection();

        }



        final FilterProxyListModel proxyModel = new FilterProxyListModel(underlyingModel);
        proxyModel.setMaxDisplay(-1);//Unlimited.
        //proxyModel.setMaxDisplay(4);//Fixed in four.


        final List choice = new List(proxyModel);

        choice.setPaintFocusBehindList(true);

        //choice.setFixedSelection(List.FIXED_NONE_CYCLIC);
        //choice.setSmoothScrolling(true);     

        //choice.setPreferredSize(new Dimension(30,120));

        choice.setListCellRenderer(new RadioButtonRenderer(""));
        choice.addActionListener(new HandleMoreDetails());
        choice.addFocusListener(new HandleChoiceFocusList());
        choice.addSelectionListener(new SelectionListener() {

            public void selectionChanged(int i, int i1) {
                skipGameKey = true;
                //                throw new UnsupportedOperationException("Not supported yet.");
            }
        });

        exclusiveChoiceTextField.addDataChangeListener(new DataChangedListener() {

            public void dataChanged(int arg0, int arg1) {
                skipGameKey = true;
                proxyModel.filter(exclusiveChoiceTextField.getText());
            }
        });

        proxyModel.addSelectionListener(new SelectionListener() {

            public void selectionChanged(int oldSelected, int newSelected) {
                skipGameKey = true;
            }
        });

        Container cList = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        cList.addComponent(choice);
        cList.setPreferredSize(new Dimension(30, 90));
        cList.getStyle().setBorder(Border.createBevelLowered(0x7b7b7b, 0x7b7b7b, 0x7b7b7b, 0x7b7b7b));

        c.addComponent(exclusiveChoiceTextField);
        c.addComponent(cList);

        form.addComponent(c);
        form.addComponent(new Label(""));

        Component[] cg;
        cg = new Component[totalChoices + 3];
        cg[0] = exclusiveChoiceTextField;
        for (int i = 1; i < (totalChoices + 1); i++) {
            cg[i] = groupButton.getRadioButton(i - 1);
        }
        //cg[totalChoices] = field;
        cg[totalChoices + 1] = cList;
        cg[totalChoices + 2] = c;

        vContainers.addElement(c);
        vGroups.addElement(cg);

    }

    private void CreateExclusiveChoiceFieldAutoComplete1(NDGQuestion obj) {
        final ChoiceQuestion question = (ChoiceQuestion) obj;
        Container c;
        c = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        c.getStyle().setBorder(Border.createBevelLowered(0x7b7b7b, 0x7b7b7b, 0x7b7b7b, 0x7b7b7b));
        TextArea qname = createQuestionName(question.getName());
        c.addComponent(qname);

        final TextField field = new TextField();

        field.addFocusListener(new FocusListener() {

            public void focusGained(Component cmp) {
                (cmp.getParent()).setY(150);
                form.scrollComponentToVisible(cmp.getParent());
            }

            public void focusLost(Component cmp) {
                lastContainer = cmp.getParent();
            }
        });

        final Vector groupButton = new Vector();
        Vector vChoices = question.getChoices();
        Vector vOthers = question.getOthers();
        int totalChoices = vChoices.size();
        String[] choices = new String[totalChoices];

        final ListModel underlyingModel = new DefaultListModel();
        for (int i = 0; i < totalChoices; i++) {
            choices[i] = (String) vChoices.elementAt(i);
            CheckBox cb = new CheckBox(choices[i]);
            //underlyingModel.addItem(choices[i]);
            underlyingModel.addItem(new OptionSelectable(choices[i]));

            cb.setOther(((String) vOthers.elementAt(i)).equals("1"));
            cb.setOtherText(""); // Initializes with empty string
            //cb.addActionListener(new HandleMoreDetails()); // More Details
            //cb.addFocusListener(new HandleChoiceFocus()); // Controls when changing to a new question
            groupButton.addElement(cb);
            //c.addComponent(cb);
        }

        final FilterProxyListModel proxyModel = new FilterProxyListModel(underlyingModel);
        proxyModel.setMaxDisplay(3);

        field.addDataChangeListener(new DataChangedListener() {

            public void dataChanged(int arg0, int arg1) {
                proxyModel.filter(field.getText());
            }
        });

        List choice = new List(proxyModel);

        choice.setListCellRenderer(new CheckBoxRenderer(""));

        choice.addActionListener(new HandleMoreDetails());
        choice.addFocusListener(new HandleChoiceFocusList());
        choice.addSelectionListener(new SelectionListener() {

            public void selectionChanged(int oldSelected, int newSelected) {
                skipGameKey = true;

            }
        });
        c.addComponent(field);
        c.addComponent(choice);



        form.addComponent(c);
        form.addComponent(new Label(""));
        Component[] cg;
        cg = new Component[totalChoices + 2];
        for (int i = 0; i < totalChoices; i++) {
            cg[i] = (CheckBox) groupButton.elementAt(i);
        }
        cg[totalChoices] = qname;
        cg[totalChoices + 1] = c;
        vContainers.addElement(c);
        vGroups.addElement(cg);
    }

    class OptionSelectable {

        private boolean selected = false;
        private String value;

        public OptionSelectable(String value) {
            this.value = value;
        }

        //public void toggleSelection() {
        public boolean toggleSelection() {
            this.selected = !selected;
            //return selected;
            return selected;
        }

        public String toString() {
            return value;
        }

        public boolean getSelected() {
            return selected;
        }
    }

    class OptionSelectableRadio {

        private boolean selected = false;
        private String value;
        ManagerOptionSelectableRadio group;

        public OptionSelectableRadio(String value, ManagerOptionSelectableRadio group) {
            this.value = value;
            this.group = group;
            this.group.addOption(this);
        }

        //public void toggleSelection() {
        public boolean toggleSelection() {
            this.selected = !selected;
            if (this.selected) {

                group.selectOption(this);
            }

            return selected;
        }

        public String toString() {
            return value;
        }

        public boolean getSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
            if (this.selected) {
                group.selectOption(this);
            }
        }

        public boolean equals(Object obj) {
            return value.equals(obj);
        }

        public int hashCode() {
            return value.hashCode();
        }
    }

    class ManagerOptionSelectableRadio {

        Vector options = new Vector();

        public void addOption(OptionSelectableRadio option) {
            if (!options.contains(option)) {
                options.addElement(option);
            }
        }
        //todo remove from vector

        public void selectOption(OptionSelectableRadio option) {
            if (option.getSelected()) {
                Enumeration options = this.options.elements();
                while (options.hasMoreElements()) {
                    OptionSelectableRadio opt = (OptionSelectableRadio) options.nextElement();
                    if (!opt.toString().equals(option.toString())) {

                        opt.setSelected(false);
                    }
                }

            }
        }
    }

    class CheckBoxRenderer extends CheckBox implements ListCellRenderer {

        public CheckBoxRenderer(String text) {
            super(text);
        }

        public Component getListCellRendererComponent(com.sun.lwuit.List list, Object o, int i, boolean isSelected) {
            setText(o.toString());
            setSelected(((OptionSelectable) o).getSelected());
            if (isSelected) {
                getStyle().setBgColor(0x69b510);
            } else {
                getStyle().setBgColor(0xffffff);
            }
            return this;
        }

        public Component getListFocusComponent(com.sun.lwuit.List list) {
            //throw new UnsupportedOperationException("Not supported yet.");
            return this;
        }
    }

    class RadioButtonRenderer extends RadioButton implements ListCellRenderer {

        public RadioButtonRenderer(String text) {
            super(text);


        }

        public Component getListCellRendererComponent(com.sun.lwuit.List list, Object o, int i, boolean isSelected) {
            if( o == null )
            {
                return this;
            }
            setSelected(((OptionSelectableRadio) o).getSelected());
            //setSelected(false);
            setText(o.toString());

            if (isSelected)
            {
                setFocus(true);
                getStyle().setBgPainter(focusBGPainter);
                getStyle().setFont(Screen.getRes().getFont("NokiaSansWideBold15"));
            }
            else
            {
                setFocus(false);
                getStyle().setBgPainter(bgPainter);
                getStyle().setFont(Screen.getRes().getFont("NokiaSansWide15"));
            }
            return this;
        }

        public Component getListFocusComponent(com.sun.lwuit.List list) {

            return this;
        }
    }
}

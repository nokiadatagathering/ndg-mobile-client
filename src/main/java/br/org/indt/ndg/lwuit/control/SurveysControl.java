/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.extended.TimeField;
import br.org.indt.ndg.mobile.AlertSave;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.DeleteList;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.settings.GpsForm;
import br.org.indt.ndg.mobile.structures.SurveyStructure;
import br.org.indt.ndg.mobile.structures.question.TypeChoice;
import br.org.indt.ndg.mobile.structures.question.TypeTextFieldDecimal;
import br.org.indt.ndg.mobile.structures.question.TypeTextFieldInteger;
import br.org.indt.ndg.mobile.structures.question.TypeTextFieldString;
import br.org.indt.ndg.mobile.structures.question.custom.CustomChoiceGroup;
import br.org.indt.ndg.mobile.structures.question.custom.CustomTextField;
import br.org.indt.ndg.lwuit.model.Answer;
import br.org.indt.ndg.lwuit.model.Category;
import br.org.indt.ndg.lwuit.model.ChoiceQuestion;
import br.org.indt.ndg.lwuit.model.DateQuestion;
import br.org.indt.ndg.lwuit.model.DescriptiveQuestion;
import br.org.indt.ndg.lwuit.model.DetailsQuestion;
import br.org.indt.ndg.lwuit.model.ImageAnswer;
import br.org.indt.ndg.lwuit.model.ImageQuestion;
import br.org.indt.ndg.lwuit.model.NDGAnswer;
import br.org.indt.ndg.lwuit.model.NDGQuestion;
import br.org.indt.ndg.lwuit.model.NumericQuestion;
import br.org.indt.ndg.lwuit.model.Question;
import br.org.indt.ndg.lwuit.model.Result;
import br.org.indt.ndg.lwuit.model.Survey;
import br.org.indt.ndg.lwuit.model.TimeQuestion;
import br.org.indt.ndg.lwuit.ui.Alert;
import br.org.indt.ndg.mobile.CategoryList;
import br.org.indt.ndg.mobile.FileSystem;
import br.org.indt.ndg.mobile.download.CheckNewSurveyList;
import br.org.indt.ndg.mobile.download.StatusScreen;
import br.org.indt.ndg.mobile.sms.SMSUtils;
import br.org.indt.ndg.mobile.structures.answer.ChoiceAnswer;
import br.org.indt.ndg.mobile.structures.answer.DateAnswer;
import br.org.indt.ndg.mobile.structures.answer.DecimalAnswer;
import br.org.indt.ndg.mobile.structures.answer.IntegerAnswer;
import br.org.indt.ndg.mobile.structures.answer.StringAnswer;
import br.org.indt.ndg.mobile.structures.answer.TimeAnswer;
import br.org.indt.ndg.mobile.structures.question.TypeDate;
import br.org.indt.ndg.mobile.structures.question.TypeTime;
import br.org.indt.ndg.mobile.submit.SubmitResultRunnable;
import br.org.indt.ndg.mobile.submit.SubmitServer;
import com.nokia.mid.appl.cmd.Local;
import com.sun.lwuit.Command;
import com.sun.lwuit.events.ActionEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

/**
 *
 * @author mluz, mturiel, amartini
 */
public class SurveysControl {

    private static SurveysControl instance;
    private javax.microedition.lcdui.Form oldQuestionForm;

    public static SurveysControl getInstance() {
        if (instance == null)
            instance = new SurveysControl();
        return instance;
    }

    public void commitValue(br.org.indt.ndg.lwuit.ui.QuestionForm questionForm, boolean fireListener) {
        NDGQuestion question = questionForm.getQuestion();
        Object answer = questionForm.getAnswer();
        Form currentForm = null;
        if (question instanceof DetailsQuestion)
            currentForm = getCurrentQuestionFormOldUI();
        else
            currentForm = AppMIDlet.getInstance().getQuestionList().questionForm.currentForm;
        if ((question instanceof DescriptiveQuestion)||(question instanceof DetailsQuestion)) {
            TextField tf = (TextField)currentForm.get(currentForm.size()-1);
            tf.setString((String)answer);
        } else if (question instanceof NumericQuestion) {
            TextField tf = (TextField)currentForm.get(currentForm.size()-1);
            tf.setString((String)answer);
        } else if (question instanceof ChoiceQuestion) {
            ChoiceQuestion cq = (ChoiceQuestion)question;
            ChoiceGroup cgOld = (ChoiceGroup)currentForm.get(currentForm.size()-1);
            if (cq.isExclusive()) {
                cgOld.setSelectedIndex(((Integer)answer).intValue(), true);
                if (fireListener)
                    ((CustomChoiceGroup)currentForm).itemStateChanged(cgOld);
            } else {
                cgOld.setSelectedFlags((boolean[])answer);
                if (fireListener)
                    ((CustomChoiceGroup)currentForm).itemStateChanged(cgOld);
            }
        } else if (question instanceof DateQuestion) {
            javax.microedition.lcdui.DateField datefield =
                    (javax.microedition.lcdui.DateField)currentForm.get(currentForm.size()-1);
            datefield.setDate((Date)answer);
        } else if (question instanceof TimeQuestion) {
            javax.microedition.lcdui.DateField datefield =
                    (javax.microedition.lcdui.DateField)currentForm.get(currentForm.size()-1);
            datefield.setDate((Date)answer);
        }
    }

    public String getCategoryNameFromCurrentQuestion() {
        return AppMIDlet.getInstance().getCategoryList().getCurrentCatName().toUpperCase();
    }

    public NDGQuestion getCurrentQuestion() {
        NDGQuestion question = null;
        
        NDGQuestion questionOld =
                AppMIDlet.getInstance().getQuestionList().question;

        if (questionOld.getType().equals("_str")) {

            question = new DescriptiveQuestion();

            question.setName(questionOld.getDescription().trim());

            ((DescriptiveQuestion)question).setLength(((TypeTextFieldString) questionOld).getLength());

            Answer answer = new Answer();
            // to get the textfield answer from old ui is need to get a form
            answer.setValue(((TextField)(getCurrentQuestionFormOldUI().get(getCurrentQuestionFormOldUI().size()-1))).getString());
            question.setAnswer(answer);
        } else if (questionOld.getType().equals("_int")) {
            question = new NumericQuestion();
            question.setName(questionOld.getDescription().trim());
            ((NumericQuestion)question).setLength(((TypeTextFieldInteger) questionOld).getLength());
            ((NumericQuestion)question).setDecimal(false);
            Answer answer = new Answer();
            // to get the textfield answer from old ui is need to get a form
            answer.setValue(((TextField)(getCurrentQuestionFormOldUI().get(getCurrentQuestionFormOldUI().size()-1))).getString());
            question.setAnswer(answer);
        } else if (questionOld.getType().equals("_decimal")) {
            question = new NumericQuestion();
            question.setName(questionOld.getDescription().trim());
            ((NumericQuestion)question).setLength(((TypeTextFieldDecimal) questionOld).getLength());
            ((NumericQuestion)question).setDecimal(true);
            Answer answer = new Answer();
            // to get the textfield answer from old ui is need to get a form
            answer.setValue(((TextField)(getCurrentQuestionFormOldUI().get(getCurrentQuestionFormOldUI().size()-1))).getString());
            question.setAnswer(answer);
        } else if (questionOld.getType().equals("_choice")) {
            if (getCurrentQuestionFormOldUI() instanceof CustomTextField) {
                Form detailsForm = getCurrentQuestionFormOldUI();
                // choice question with details
                question = new DetailsQuestion();
                question.setName(detailsForm.getTitle().trim());
                Answer answer = new Answer();
                answer.setValue(((TextField)(detailsForm.get(detailsForm.size()-1))).getString());
                question.setAnswer(answer);
            } else {
                question = new ChoiceQuestion();
                question.setName(questionOld.getDescription().trim());
                ((ChoiceQuestion)question).setChoices(((TypeChoice)questionOld).getOrderedChoices());
                ((ChoiceQuestion)question).setExclusive(((TypeChoice)questionOld).getSelectType().equalsIgnoreCase("exclusive"));
                Answer answer = new Answer();
                CustomChoiceGroup ccg = ((TypeChoice)questionOld).getWidget();
                javax.microedition.lcdui.ChoiceGroup cg = (ChoiceGroup)ccg.get(ccg.size()-1);
                   //     ((ChoiceGroup)((((TypeChoice)questionOld).getWidget()).get((((TypeChoice)questionOld).getWidget()).size()-1)));
                if (((ChoiceQuestion)question).isExclusive()) {
                    answer.setValue(""+cg.getSelectedIndex());
                } else {
                    boolean[] selectedArray = new boolean[cg.size()];
                    cg.getSelectedFlags(selectedArray);
                    answer.setValue(selectedArray);
                }
                question.setAnswer(answer);
            }
        } else if (questionOld.getType().equals("_date")) {
            question = new DateQuestion();
            question.setName(questionOld.getDescription().trim());
            Answer answer = new Answer();
            javax.microedition.lcdui.DateField dfOld = (javax.microedition.lcdui.DateField)getCurrentQuestionFormOldUI().get(getCurrentQuestionFormOldUI().size()-1);
            answer.setValue(dfOld.getDate());
            question.setAnswer(answer);
        } else if (questionOld.getType().equals("_time")) {
            question = new TimeQuestion();
            question.setName(questionOld.getDescription().trim());
            Answer answer = new Answer();
            //javax.microedition.lcdui.DateField dfOld = (javax.microedition.lcdui.DateField)getCurrentQuestionFormOldUI().get(getCurrentQuestionFormOldUI().size()-1);
            TimeField tfOld = ((TimeField)getCurrentQuestion().getAnswer().getValue());
            answer.setValue(tfOld.getTime());
            
            question.setAnswer(answer);
        }
        //ATTENTION
        //in this case, the question is a subclass of
        //br.org.indt.ndg.lwuit.model.Question;
        //this is all part of a huge refactoring, where we are trying to insert a new question type
        //but not related to the old NDG architecture, ie, not extending
        //br.org.indt.ndg.mobile.structures.question.Question
        else {
            if(questionOld.getType().equals("_img")){
                NDGQuestion ndgQuestion = questionOld;
                ImageQuestion imageQuestion = (br.org.indt.ndg.lwuit.model.ImageQuestion) ndgQuestion;
                imageQuestion.setName(questionOld.getDescription());
                CategoryList list = AppMIDlet.getInstance().getCategoryList();
                NDGAnswer answer = AppMIDlet.getInstance().getFileStores().loadAnswerById(list.getCurrentCategoryIndex(), imageQuestion.getIdNumber());
                if(answer == null){
                    answer = new ImageAnswer();
                }
                imageQuestion.setAnswer((br.org.indt.ndg.lwuit.model.Answer)answer);
                question = imageQuestion;
            }
        }
        return question;

    }

    public int getSurveyIdNumber(){
        return AppMIDlet.getInstance().getFileStores().getSurveyStructure().getIdNumber();
    }

    public String [] getSurveyDisplayIds() {
        String displayIds[] = new String[2];
        displayIds[0] = "" + (AppMIDlet.getInstance().getFileStores().getSurveyStructure().getDisplayCategoryId() + 1);
        displayIds[1] = "" + (AppMIDlet.getInstance().getFileStores().getSurveyStructure().getDisplayQuestionId() + 1);
        return displayIds;
    }

    public Survey getCurrentSurvey() {
        Survey survey = new Survey();
        SurveyStructure surveyOldFormat = AppMIDlet.getInstance().getFileStores().getSurveyStructure();
        int totalCategories = surveyOldFormat.getNumCategories();
        Category[] categories = new Category[totalCategories];
        for (int i=0; i<totalCategories; i++) {
            Category category = new Category();
            category.setName(surveyOldFormat.getCatName(i));
            Vector vQuestions = surveyOldFormat.getQuestions(i);
            int totalQuestions = vQuestions.size();
            NDGQuestion[] questions = new Question[totalQuestions];
            for (int j=0; j<totalQuestions; j++){
                NDGQuestion questionOldFormat = (NDGQuestion) vQuestions.elementAt(j);
                Question question = new Question();
                question.setName(questionOldFormat.getDescription());
                question.setType(questionOldFormat.getType());
                Answer answer = new Answer();
                if (questionOldFormat.getVisited())
                    answer.setValue(AppMIDlet.getInstance().getResultView().getAnswerFormat(questionOldFormat).getText());
                else
                    answer.setValue(Resources.NOT_VISITED);
                question.setAnswer(answer);
                questions[j] = question;
            }
            category.setQuestions(questions);
            categories[i] = category;
        }
        survey.setCategories(categories);
        return  survey;
    }

    public String getTextFromGps() {
        return AppMIDlet.getInstance().getSimpleLocation().getString();
    }

    public String getTitleCurrentAlert() {
        if (alertSave != null)
            return Resources.CMD_SAVE.getLabel(); // because old alert save screen don't have title
        if (oldAlert != null)
            return oldAlert.getTitle();
        return AppMIDlet.getInstance().getGeneralAlert().getTitle();
    }

    public String getTextCurrentAlert() {
        if (alertSave != null)
            return alertSave.getTitle();
        if (oldAlert != null)
            return oldAlert.getString();
        return AppMIDlet.getInstance().getGeneralAlert().getString();
    }

    public Command[] getCommandsCurrentAlert() {
        com.sun.lwuit.Command[] newCmds = null;
        if (alertSave != null) {
            newCmds = new com.sun.lwuit.Command[2];
            newCmds[0] = new com.sun.lwuit.Command(Resources.YES) {
                public void actionPerformed(ActionEvent evt) {
                    Alert.dispose(); // first thing to do is dispose the Alert
                    ChoiceGroup cg = (ChoiceGroup)alertSave.get(0);
                    cg.setSelectedIndex(0, true); // yes option
                    alertSave.commandAction(Resources.CMD_OK, null);
                    alertSave = null;
                }
            };
            newCmds[1] = new com.sun.lwuit.Command(Resources.NO) {
                public void actionPerformed(ActionEvent evt) {
                    Alert.dispose(); // first thing to do is dispose the Alert
                    ChoiceGroup cg = (ChoiceGroup)alertSave.get(0);
                    cg.setSelectedIndex(1, true); // no option
                    alertSave.commandAction(Resources.CMD_OK, null);
                    alertSave = null;
                }
            };
            return newCmds;
        } else if (oldAlert != null) {
            // there is one case only = end survey alert
            newCmds = new com.sun.lwuit.Command[1];
            newCmds[0] = new com.sun.lwuit.Command(Resources.CMD_OK.getLabel()) {
                public void actionPerformed(ActionEvent evt) {
                    Alert.dispose(); // first thing to do is dispose the Alert
                    // there is no way to dispath for the listener
                    // the line below was copied from CategoryList.java :82
                    AppMIDlet.getInstance().getFileSystem().saveResult();
                    oldAlert = null;
                }
            };
            return newCmds;
        }
        
        final javax.microedition.lcdui.Command[] oldCmds = AppMIDlet.getInstance().getGeneralAlert().getCommands();
        if (oldCmds != null) {
            newCmds = new com.sun.lwuit.Command[oldCmds.length];
            for (int i=0; i<oldCmds.length; i++) {
                final int j = i;
                com.sun.lwuit.Command cmd = new Command(oldCmds[i].getLabel()) {
                    public void actionPerformed(ActionEvent evt) {
                        Alert.dispose(); // first thing to do is dispose the Alert
                        AppMIDlet.getInstance().getGeneralAlert().commandAction(oldCmds[j], null);
                        /*new Event() {

                            public void execute(Object parameter) {
                                startLazy(parameter);
                            }

                            protected void lazyExec(Object parameter) {
                                AppMIDlet.getInstance().getGeneralAlert().commandAction((javax.microedition.lcdui.Command)parameter, null);
                            }

                        }.execute(oldCmds[j]);*/
                        //AppMIDlet.getInstance().getGeneralAlert().commandAction(, null);
                    }
                };
                newCmds[i] = cmd;
            }
        } else {
            // have the dismiss commands only
            newCmds = new com.sun.lwuit.Command[1];
            com.sun.lwuit.Command cmd = new Command(Resources.CMD_OK.getLabel()) {
                    public void actionPerformed(ActionEvent evt) {
                        AppMIDlet.getInstance().getGeneralAlert().commandAction(javax.microedition.lcdui.Alert.DISMISS_COMMAND, null);
                    }
                };
            newCmds[0] = cmd;
        }
        return newCmds;
    }

    public int getTypeCurrentAlert() {
        if (alertSave != null) {
            return Alert.CONFIRMATION;
        }

        AlertType at;

        if (oldAlert != null)
            at = oldAlert.getType();
        else
            at = AppMIDlet.getInstance().getGeneralAlert().getType();
        
        int result = Alert.ERROR;
        if (at == AlertType.CONFIRMATION)
            result = Alert.CONFIRMATION;
        else if (at == AlertType.ERROR)
            result = Alert.ERROR;
        else if (at == AlertType.INFO)
            result = Alert.INFO;
        else if (at == AlertType.WARNING)
            result = Alert.WARNING;
        else if (at == AlertType.ALARM)
            result = Alert.ALARM;
        return result;
    }


    public int getTotal() {
        javax.microedition.lcdui.List list = AppMIDlet.getInstance().getSurveyList();
        return list.size();
    }

    public Survey[] getAllSurveys() {
        javax.microedition.lcdui.List list = AppMIDlet.getInstance().getSurveyList();
        int total = list.size();
        Survey[] v = new Survey[total];
        for (int i=0; i < total; i++ ) {
            Survey survey = new Survey();
            survey.setName(list.getString(i));
            v[i] = survey;
        }
        return v;
    }

    // this method is a workaround to adapt the new UI/Control for old NDG component
    public Category[] getCategoriesFromOpenedSurvey() {
        javax.microedition.lcdui.List list = AppMIDlet.getInstance().getCategoryList();
        int totalCategoriesFromNDG = list.size();
        Category[] categories = new Category[totalCategoriesFromNDG];
        for (int i=0; i < totalCategoriesFromNDG; i++ ) {
            Category category = new Category();
            String text = list.getString(i);
            int index = text.indexOf('\n');
            String catName = text.substring(0, index);
            String questions = text.substring(index+1);
            int totalQuestions = Integer.parseInt(questions.substring(0, questions.indexOf(" ")));
            category.setName(catName);
            category.setTotalQuestions(totalQuestions);
            category.setFullFilled((list.getImage(i) == Resources.check));
            categories[i] = category;
        }
        return categories;
    }

    public Result[] getResultsFromOpenedSurvey() {
        javax.microedition.lcdui.List list = AppMIDlet.getInstance().getResultList();
        int totalResultsFromNDG = list.size();
        Result[] results = new Result[totalResultsFromNDG+1];
        Result result = new Result();
        result.setName(Local.getText(Local.QTJ_CMD_NEW_SURVEY));
        results[0] = result;
        for (int i=0; i < totalResultsFromNDG; i++ ) {
            result = new Result();
            String text = list.getString(i);
            result.setName(text);
            results[i+1] = result;
        }
        return results;
    }

    public String getOpenedSurveyTitle() {
        return AppMIDlet.getInstance().getFileStores().getSurveyStructure().getTitle();
    }

    private DeleteList currentOldDeleteList;

    private AlertSave alertSave = null;

    public boolean isDetailsShowed(int i) {
        String name = (String) (((CustomChoiceGroup)oldQuestionForm).choicesOrdered.elementAt(i));
        String value = (String) (((CustomChoiceGroup)oldQuestionForm).choices.get(name));
        if (value.equals("1")) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isGPSConnected() {
        if (AppMIDlet.getInstance().getSimpleLocation() != null)
            return AppMIDlet.getInstance().getSimpleLocation().isConnected();
        else return false;
    }
    public void setAlertSaveOldScreen(Displayable nextDisplayable) {
        if (nextDisplayable != null)
            alertSave = (AlertSave)nextDisplayable;
    }

    void setCurrentOldDeleteList(Displayable nextDisplayable) {
        currentOldDeleteList = (DeleteList) nextDisplayable;
    }

    DeleteList getCurrentOldDeleteList() {
        return currentOldDeleteList;
    }

    void setCurrentQuestionFormOldUI(Displayable nextDisplayable) {
        oldQuestionForm = (javax.microedition.lcdui.Form) nextDisplayable;
    }

    javax.microedition.lcdui.Form getCurrentQuestionFormOldUI() {
        return oldQuestionForm;
    }

    private javax.microedition.lcdui.Alert oldAlert;
    void setOldAlert(Displayable nextDisplayable) {
        oldAlert = (javax.microedition.lcdui.Alert)nextDisplayable;
    }

    private GpsForm currentOldGpsForm;
    public void setCurrentOldGpsForm(Displayable nextDisplayable) {
        currentOldGpsForm = (GpsForm) nextDisplayable;
    }

    public GpsForm getCurrentOldGpsForm() {
        return currentOldGpsForm;
    }

    private CheckNewSurveyList currentOldSurveyList;
    public void setCurrentOldSurveyList(Displayable nextDisplayable) {
        currentOldSurveyList = (CheckNewSurveyList) nextDisplayable;
    }

    public CheckNewSurveyList getCurrentOldSurveyList() {
        return currentOldSurveyList;
    }

    public String[] getAvailableSurveysToDownload() {
        String[] result = new String[currentOldSurveyList.size()];
        for (int i = 0; i < currentOldSurveyList.size(); i++) {
            result[i] = currentOldSurveyList.getString(i);
        }

//        String[] result = new String[2];
//        result[0] = "Student Test";
//        result[1] = "Student Test2";

        return result;
    }

    private StatusScreen currentOldStatusScreenDownload;
    public void setCurrentOldStatusScreenDownload(Displayable nextDisplayable) {
        currentOldStatusScreenDownload = (StatusScreen) nextDisplayable;
    }

    public StatusScreen getCurrentOldStatusScreenDownload() {
        return currentOldStatusScreenDownload;
    }

    private br.org.indt.ndg.mobile.submit.StatusScreen currentOldStatusScreenSubmit;
    public void setCurrentOldStatusScreenSubmit(Displayable nextDisplayable) {
        currentOldStatusScreenSubmit = (br.org.indt.ndg.mobile.submit.StatusScreen) nextDisplayable;
    }

    public br.org.indt.ndg.mobile.submit.StatusScreen getCurrentOldStatusScreenSubmit() {
        return currentOldStatusScreenSubmit;
    }

    private br.org.indt.ndg.mobile.UpdateClientApp currentOldUpdateClientApp;
    public void setCurrentOldUpdateClientApp(Displayable nextDisplayable) {
        currentOldUpdateClientApp = (br.org.indt.ndg.mobile.UpdateClientApp) nextDisplayable;
    }

    public br.org.indt.ndg.mobile.UpdateClientApp getCurrentOldUpdateClientApp() {
        return currentOldUpdateClientApp;
    }

    private br.org.indt.ndg.mobile.SentList currentOldSentList;
    public void setCurrentOldSentList(Displayable nextDisplayable) {
        currentOldSentList = (br.org.indt.ndg.mobile.SentList) nextDisplayable;
    }

    public br.org.indt.ndg.mobile.SentList getCurrentOldSentList() {
        return currentOldSentList;
    }

    // mturiel
    // New Interview Form Methods

    private String tmpOtherText;
    public void setItemOtherText(String _val) {
        tmpOtherText = _val;
    }

    public String getItemOtherText() {
        return tmpOtherText;
    }

    private int categoryIndex = 0, questionIndex = 0;
    Survey newModelSurvey = null;

    // Reset Question Given
    public void resetQuestion() {
        categoryIndex = 0;
        questionIndex = 0;
        newModelSurvey = null;
    }

    // Get Question
    public NDGQuestion getNextQuestion() {
        NDGQuestion question = null;
        if (newModelSurvey == null) {
            newModelSurvey = getCurrentSurveyNewModel();
        }

        if (categoryIndex > -1) {
            question = newModelSurvey.getCategories()[categoryIndex].getQuestions()[questionIndex];
            // Set Category Index and Name useful for Save Operation
            question.setCategoryId(Integer.toString(categoryIndex+1));
            question.setCategoryName(newModelSurvey.getCategories()[categoryIndex].getName());
            question.setQuestionId(Integer.toString(questionIndex+1));

            if (questionIndex < (newModelSurvey.getCategories()[categoryIndex].getQuestions().length-1)) {
                questionIndex++;
            }
            else {
                if (categoryIndex < (newModelSurvey.getCategories().length-1)) {
                    categoryIndex++;
                    questionIndex = 0;
                }
                else
                {
                    categoryIndex = -1;
                }
            }
        }

        return question;

        //
    }

    public Survey getCurrentSurveyNewModel() {
        Survey survey = new Survey();

        SurveyStructure surveyOldFormat = AppMIDlet.getInstance().getFileStores().getSurveyStructure();

        int totalCategories = surveyOldFormat.getNumCategories();
        Category[] categories = new Category[totalCategories];
        for (int i=0; i<totalCategories; i++) {
            Category category = new Category();
            category.setName(surveyOldFormat.getCatName(i));
            Vector vQuestions = surveyOldFormat.getQuestions(i);

            Hashtable answers = null;
            NDGAnswer currentAnswer = null;
            boolean onEdit = (AppMIDlet.getInstance().getFileStores().getResultStructure() != null);
            if (onEdit)
                answers = AppMIDlet.getInstance().getFileStores().getResultStructure().getAnswers(i);

            int totalQuestions = vQuestions.size();
            NDGQuestion[] questions = new Question[totalQuestions];
            for (int j=0; j<totalQuestions; j++) {
                NDGQuestion questionOldFormat = (NDGQuestion) vQuestions.elementAt(j);

                if (onEdit)
                    currentAnswer = (NDGAnswer) answers.get(String.valueOf(questionOldFormat.getIdNumber()));

                if (questionOldFormat.getType().equals("_str")) {
                    DescriptiveQuestion question = new DescriptiveQuestion();
                    question.setName(questionOldFormat.getDescription());
                    question.setLength(((TypeTextFieldString) questionOldFormat).getLength());
                    question.setChoices(((TypeTextFieldString) questionOldFormat).getOrderedChoices()); //ibb
                    question.setVisited(questionOldFormat.getVisited());
                    question.setType("_str");

                    Answer answer = new Answer();
                    if ((questionOldFormat.getVisited()) && (onEdit))
                        answer.setValue(((StringAnswer) currentAnswer).getValue());
                    else
                        answer.setValue("");
                    question.setAnswer(answer);
                    questions[j] = question;
                }
                else if (questionOldFormat.getType().equals("_int")) {
                    NumericQuestion question = new NumericQuestion();
                    question.setName(questionOldFormat.getDescription());
                    question.setLength(((TypeTextFieldInteger) questionOldFormat).getLength());
                    question.setDecimal(false);
                    question.setLowConstraint(((TypeTextFieldInteger) questionOldFormat).getLowConstraint());
                    question.setHighConstraint(((TypeTextFieldInteger) questionOldFormat).getHighConstraint());
                    question.setVisited(questionOldFormat.getVisited());
                    question.setType("_int");

                    Answer answer = new Answer();
                    if ((questionOldFormat.getVisited()) && (onEdit))
                        answer.setValue(Integer.toString(((IntegerAnswer) currentAnswer).getValue()));
                    else
                        answer.setValue("");
                    question.setAnswer(answer);
                    questions[j] = question;
                }
                else if (questionOldFormat.getType().equals("_decimal")) {
                    NumericQuestion question = new NumericQuestion();
                    question.setName(questionOldFormat.getDescription());
                    question.setLength(((TypeTextFieldDecimal) questionOldFormat).getLength());
                    question.setDecimal(true);
                    question.setLowConstraint(((TypeTextFieldDecimal) questionOldFormat).getLowConstraint());
                    question.setHighConstraint(((TypeTextFieldDecimal) questionOldFormat).getHighConstraint());
                    question.setVisited(questionOldFormat.getVisited());
                    question.setType("_decimal");

                    Answer answer = new Answer();
                    if ((questionOldFormat.getVisited()) && (onEdit))
                        answer.setValue(Double.toString(((DecimalAnswer) currentAnswer).getValue()));
                    else
                        answer.setValue("");
                    question.setAnswer(answer);
                    questions[j] = question;
                }
                else if (questionOldFormat.getType().equals("_date")) {
                    DateQuestion question = new DateQuestion();
                    question.setName(questionOldFormat.getDescription());
                    question.setLowConstraint(((TypeDate) questionOldFormat).getLowConstraint());
                    question.setHighConstraint(((TypeDate) questionOldFormat).getHighConstraint());
                    question.setVisited(questionOldFormat.getVisited());
                    question.setType("_date");

                    Answer answer = new Answer();
                    if ((questionOldFormat.getVisited()) && (onEdit)) {
                        long date = ((DateAnswer) currentAnswer).getDate();
                        Long datelong = new Long(date);
                        answer.setValue(datelong.toString());
                    }
                    else {
                        Date date = new Date();
                        Long datelong = new Long(date.getTime());
                        answer.setValue(datelong.toString());
                    }
                    question.setAnswer(answer);
                    questions[j] = question;
                }
                else if (questionOldFormat.getType().equals("_choice")) {
                    ChoiceQuestion question = new ChoiceQuestion();
                    question.setName(questionOldFormat.getDescription());
                    question.setExclusive(((TypeChoice) questionOldFormat).getSelectType().equals("exclusive"));
                    question.setChoices(((TypeChoice) questionOldFormat).getOrderedChoices());
                    question.setType("_choice");
                    // Get Others information
                    Vector vOthers = new Vector();
                    Hashtable hOthers = ((TypeChoice) questionOldFormat).getChoices();
                    Vector vKeys = ((TypeChoice) questionOldFormat).getOrderedChoices();
                    for (int i3 = 0; i3 < vKeys.size(); i3++) {
                        vOthers.addElement(hOthers.get((String) vKeys.elementAt(i3)));
                    }
                    question.setOthers(vOthers);

                    Vector vOthersText = new Vector();
                    if ((questionOldFormat.getVisited()) && (onEdit)) {
                        Vector vIndexes2 = ((ChoiceAnswer) currentAnswer).getSelectedIndexes();
                        for (int i2 = 0; i2 < vIndexes2.size(); i2++) {
                            String str1 = (String) vIndexes2.elementAt(i2);
                            String str2 = ((ChoiceAnswer) currentAnswer).getOtherText(str1);
                            if (str2 != null)
                                vOthersText.addElement(str2);
                            else
                                vOthersText.addElement("");
                        }
                    }
                    question.setOthersText(vOthersText);
                    //
                    question.setVisited(questionOldFormat.getVisited());

                    Answer answer = new Answer();
                    if (question.isExclusive()) {
                        if ((questionOldFormat.getVisited()) && (onEdit)) {
                            Vector vIndexes = ((ChoiceAnswer) currentAnswer).getSelectedIndexes();
                            if(vIndexes.size() > 0)
                                answer.setValue((String) vIndexes.elementAt(0));
                            else
                                answer.setValue("");
                        }
                        else
                            answer.setValue("");
                    }
                    else {
                        if ((questionOldFormat.getVisited()) && (onEdit)) {
                            Vector vIndexes = ((ChoiceAnswer) currentAnswer).getSelectedIndexes();
                            answer.setValue(vIndexes);
                        }
                        else {
                            Vector vIndexes = new Vector(0);
                            answer.setValue(vIndexes);
                        }
                    }
                    question.setAnswer(answer);

                    // SetSkip Logic Info
                    question.setSkipEnabled(((TypeChoice) questionOldFormat).getSkipEnabled());
                    if (question.getSkipEnabled()) {
                        int [] nArray = ((TypeChoice) questionOldFormat).getJumpInts();
                        question.setCatTo(nArray[0]+1);
                        question.setSkipTo(nArray[1]+1);
                        question.setChoiceItem(((TypeChoice) questionOldFormat).getOperand());
                        question.setInverse(((TypeChoice) questionOldFormat).getOperator().equals("1"));
                    }

                    questions[j] = question;
                } else if (questionOldFormat.getType().equals("_time")) {
                    TimeQuestion question = new TimeQuestion();
                    question.setName(questionOldFormat.getDescription());
                    question.setConvention(Integer.parseInt(((TypeTime)questionOldFormat).getConvention()));
                    
                    question.setVisited(questionOldFormat.getVisited());
                    question.setType("_time");

                    Answer answer = new Answer();
                    if ((questionOldFormat.getVisited()) && (onEdit)) {

                        long time = ((TimeAnswer) currentAnswer).getTime();
                        question.setAm_pm(((TimeAnswer) currentAnswer).getConvention());

                        Long timelong = new Long(time);
                        answer.setValue(timelong.toString());                       
                    }
                    else {
                        Date time = new Date();
                        Long timelong = new Long(time.getTime());
                        answer.setValue(timelong.toString());
                    }
                    question.setAnswer(answer);
                    
                    questions[j] = question;
                }
                //ATTENTION
                //in this case, the question is a subclass of
                //br.org.indt.ndg.lwuit.model.Question;
                //this is all part of a huge refactoring, where we are trying to insert a new question type
                //but not related to the old NDG architecture, ie, not extending
                //br.org.indt.ndg.mobile.structures.question.Question
                else{
                    if(questionOldFormat.getType().equals("_img")){
                        NDGQuestion ndgQuestion = questionOldFormat;
                        ImageQuestion imageQuestion = (br.org.indt.ndg.lwuit.model.ImageQuestion) ndgQuestion;
                        imageQuestion.setName(questionOldFormat.getDescription());
                        imageQuestion.setType("_img");
                        //CategoryList list = AppMIDlet.getInstance().getCategoryList();
                        //NDGAnswer answer = AppMIDlet.getInstance().getFileStores().loadAnswerById(list.getCurrentCategoryIndex(), imageQuestion.getIdNumber());

                        NDGAnswer answer;
                        if ( (questionOldFormat.getVisited()) && (onEdit) )
                            answer = AppMIDlet.getInstance().getFileStores().loadAnswerById(i, imageQuestion.getIdNumber());
                        else
                            answer = new ImageAnswer();

                        imageQuestion.setAnswer((br.org.indt.ndg.lwuit.model.Answer)answer);
                        questions[j] = imageQuestion;
                    }
                }
            }
            category.setQuestions(questions);
            categories[i] = category;
        }
        survey.setCategories(categories);
        return  survey;
    }

    public void deleteResults(boolean[] selectedFlags){
        Vector filenamesToDelete = new Vector();
        FileSystem fs = AppMIDlet.getInstance().getFileSystem();

        // checkSelectedFiles
        for (int i=0; i < selectedFlags.length; i++){
            if (selectedFlags[i]) filenamesToDelete.addElement(fs.getResultFilename(i));
        }

        // deleteSelectedFiles
        String fName;

        Enumeration e = filenamesToDelete.elements();
        while (e.hasMoreElements()) {
            fName = (String) e.nextElement();
            fs.deleteFile(fName);
            fs.deleteSMSFile(SMSUtils.getSMSFileNameFromXMLFileName(fName));
        }

    }

}

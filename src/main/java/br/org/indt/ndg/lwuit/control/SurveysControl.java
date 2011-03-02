package br.org.indt.ndg.lwuit.control;


import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.structures.SurveyStructure;
import br.org.indt.ndg.lwuit.model.Answer;
import br.org.indt.ndg.lwuit.model.Category;
import br.org.indt.ndg.lwuit.model.ChoiceAnswer;
import br.org.indt.ndg.lwuit.model.ChoiceQuestion;
import br.org.indt.ndg.lwuit.model.DateAnswer;
import br.org.indt.ndg.lwuit.model.DateQuestion;
import br.org.indt.ndg.lwuit.model.DecimalAnswer;
import br.org.indt.ndg.lwuit.model.DescriptiveQuestion;
import br.org.indt.ndg.lwuit.model.ImageAnswer;
import br.org.indt.ndg.lwuit.model.ImageQuestion;
import br.org.indt.ndg.lwuit.model.IntegerAnswer;
import br.org.indt.ndg.lwuit.model.NDGAnswer;
import br.org.indt.ndg.lwuit.model.NDGQuestion;
import br.org.indt.ndg.lwuit.model.NumericQuestion;
import br.org.indt.ndg.lwuit.model.Question;
import br.org.indt.ndg.lwuit.model.Result;
import br.org.indt.ndg.lwuit.model.StringAnswer;
import br.org.indt.ndg.lwuit.model.Survey;
import br.org.indt.ndg.lwuit.model.TimeAnswer;
import br.org.indt.ndg.lwuit.model.TimeQuestion;
import br.org.indt.ndg.mobile.FileSystem;
import br.org.indt.ndg.mobile.Resources;
import com.nokia.mid.appl.cmd.Local;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author mluz, mturiel, amartini
 */
public class SurveysControl {

    private static SurveysControl instance;

    public static SurveysControl getInstance() {
        if (instance == null)
            instance = new SurveysControl();
        return instance;
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

    public Survey[] getAllSurveys() {
        Vector list = AppMIDlet.getInstance().getSurveyList().getList();
        int total = list.size();
        Survey[] v = new Survey[total+1];
        Survey checkForNewSurveyItem = new Survey();
        checkForNewSurveyItem.setName( "-> " +Local.getText(Local.QTJ_CHECK_NEW_SURVEYS) + " <-");
        v[0] = checkForNewSurveyItem;

        for (int i=0; i < total; i++ ) {
            Survey survey = new Survey();
            survey.setName((String)list.elementAt(i));
            v[i+1] = survey;
        }
        return v;
    }


    public Result[] getResultsFromOpenedSurvey() {
        Vector list = AppMIDlet.getInstance().getResultList().getList();
        int totalResultsFromNDG = list.size();
        Result[] results = new Result[totalResultsFromNDG+1];
        Result result = new Result();
        result.setName("-> " + Local.getText(Local.QTJ_CMD_NEW_SURVEY) +" <-");
        results[0] = result;
        for (int i=0; i < totalResultsFromNDG; i++ ) {
            result = new Result();
            result.setName((String)list.elementAt(i));
            results[i+1] = result;
        }
        return results;
    }

    public String getOpenedSurveyTitle() {
        return AppMIDlet.getInstance().getFileStores().getSurveyStructure().getTitle();
    }

    private String [] avaiableSurveyList;
    public void setAvaiableSurveyToDownload(String[] avaiableSurveyList)
    {
        this.avaiableSurveyList = avaiableSurveyList;
    }

    public String[] getAvailableSurveysToDownload() {
        return avaiableSurveyList;
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
    private Survey newModelSurvey = null;

    private int selectedCategory = 0;

    // Reset Question Given
    public void resetQuestion() {
        categoryIndex = 0;
        questionIndex = 0;
        newModelSurvey = null;
    }
    public void reset() {
        selectedCategory = 0;
        newModelSurvey = null;
    }

    public int getSelectedCategoryIndex(){
        return selectedCategory;
    }

    public Category getSelectedCategory(){
        return  newModelSurvey.getCategories()[selectedCategory];
    }

    public void setSelectedCategory(int selection) {
        if( selection < 0 ) {
            selectedCategory = newModelSurvey.getCategories().length - 1;
        } else if ( selection >= newModelSurvey.getCategories().length ) {
            selectedCategory = 0;
        } else {
            selectedCategory = selection;
        }
    }

    public Vector getQuestionsFlat() {
        if( newModelSurvey == null )
            newModelSurvey = getCurrentSurveyNewModel();

        Vector questions = new Vector();
        
        for( int i=0; i < newModelSurvey.getCategories().length; i++) {
            for (int j = 0; j < newModelSurvey.getCategories()[i].getQuestions().length; j++) {
                questions.addElement(newModelSurvey.getCategories()[i].getQuestions()[j]);
            }
        }
        return questions;
    }


    public Category getCategory( int categoryNo ) {
        if (newModelSurvey == null) {
            newModelSurvey = getCurrentSurveyNewModel();
        }
        Category category = null;
        try {
            category = newModelSurvey.getCategories()[categoryNo];
        } catch( ArrayIndexOutOfBoundsException ex ) {
        }
        return category;
    }

    public Vector getSurveyStatDescription(){
        if (newModelSurvey == null) {
            newModelSurvey = getCurrentSurveyNewModel();
        }
        Vector categoryList = new Vector();
        for (int i=0; i < newModelSurvey.getCategories().length; i++) {
            NDGQuestion[] questions  = newModelSurvey.getCategories()[i].getQuestions();
            int questionsCount = questions.length;
            String questionStr = "";
            //if(questionsCount > 0){
                 questionStr = (questionsCount > 1 ? " " + Resources.QUESTIONS : " "+ Resources.QUESTION);
                 String categoryText = (String) newModelSurvey.getCategories()[i].getName() + "\n" + questionsCount + questionStr;
//                if (survey.getVisitedValue(i) > 0) this.append(categoryText, Resources.question);
//                else this.append(categoryText, Resources.check);
                 categoryList.addElement(categoryText);
            //}
        }
        return categoryList;
    }


    public Category[] getCategoriesFromOpenedSurvey() {
        if( newModelSurvey == null )
            newModelSurvey = getCurrentSurveyNewModel();
        return newModelSurvey.getCategories();
    }

    public boolean hasMoreThenOneCategory() {
        return (getCategoriesFromOpenedSurvey().length > 1);
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
                    DescriptiveQuestion question = (DescriptiveQuestion)questionOldFormat;// new DescriptiveQuestion();
                    question.setName(question.getDescription());

                    StringAnswer answer = new StringAnswer();
                    if ((question.getVisited()) && (onEdit) && ((StringAnswer)currentAnswer).getValue() != null )
                        answer.setValue(((StringAnswer) currentAnswer).getValue());
                    else
                        answer.setValue("");
                    question.setAnswer(answer);
                }
                else if (questionOldFormat.getType().equals("_int")) {
                    NumericQuestion question = (NumericQuestion)questionOldFormat;// new NumericQuestion();
                    question.setName(questionOldFormat.getDescription());

                    IntegerAnswer answer = new IntegerAnswer();
                    if ((question.getVisited()) && (onEdit) && ((IntegerAnswer)currentAnswer).getValue() != null )
                        answer.setValue(((IntegerAnswer)currentAnswer).getValue());
                    else
                        answer.setValue("");
                    question.setAnswer(answer);
                }
                else if (questionOldFormat.getType().equals("_decimal")) {
                    NumericQuestion question = (NumericQuestion)questionOldFormat;
                    question.setName(question.getDescription());

                    DecimalAnswer answer = new DecimalAnswer();
                    if ((question.getVisited()) && (onEdit) && ((DecimalAnswer)currentAnswer).getValue() != null)
                        answer.setValue(((DecimalAnswer)currentAnswer).getValue());
                    else
                        answer.setValue("");
                    question.setAnswer(answer);
                }
                else if (questionOldFormat.getType().equals("_date")) {
                    DateQuestion question = (DateQuestion)questionOldFormat;
                    question.setName(question.getDescription());
                    
                    Answer answer = new Answer();
                    if ((question.getVisited()) && (onEdit)) {
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
                }
                else if (questionOldFormat.getType().equals("_choice")) {
                    ChoiceQuestion question = (ChoiceQuestion)questionOldFormat;
                    question.setName(question.getDescription());
                   
                    Vector vOthersText = new Vector();
                    if ((question.getVisited()) && (onEdit)) {
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
                    //question.setVisited(questionOldFormat.getVisited());

                    Answer answer = new Answer();
                    if (question.isExclusive()) {
                        if ((question.getVisited()) && (onEdit)) {
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
                        if ((question.getVisited()) && (onEdit)) {
                            Vector vIndexes = ((ChoiceAnswer) currentAnswer).getSelectedIndexes();
                            answer.setValue(vIndexes);
                        }
                        else {
                            Vector vIndexes = new Vector(0);
                            answer.setValue(vIndexes);
                        }
                    }
                    question.setAnswer(answer);
                } else if (questionOldFormat.getType().equals("_time")) {
                    TimeQuestion question = (TimeQuestion)questionOldFormat;
                    question.setName(questionOldFormat.getDescription());
                    
                    Answer answer = new Answer();
                    if ((question.getVisited()) && (onEdit)) {

                        long time = ((TimeAnswer) currentAnswer).getTime();
                        question.setAm_pm(((TimeAnswer) currentAnswer).getAmPm24());

                        Long timelong = new Long(time);
                        answer.setValue(timelong.toString());
                    }
                    else {
                        Date time = new Date();
                        Long timelong = new Long(time.getTime());
                        answer.setValue(timelong.toString());
                    }
                    question.setAnswer(answer);
                }
                else{
                    if(questionOldFormat.getType().equals("_img")){
                        ImageQuestion question = (ImageQuestion) questionOldFormat;
                        question.setName(questionOldFormat.getDescription());
                        question.setType("_img");

                        NDGAnswer answer;
                        if ( (question.getVisited()) && (onEdit) )
                            answer = AppMIDlet.getInstance().getFileStores().loadAnswerById(i, question.getIdNumber());
                        else
                            answer = new ImageAnswer();

                        question.setAnswer((br.org.indt.ndg.lwuit.model.Answer)answer);
                    }
                }
                questionOldFormat.setCategoryId(surveyOldFormat.getCatID(i));
                questionOldFormat.setQuestionId( String.valueOf(questionOldFormat.getIdNumber()));
                questionOldFormat.setCategoryName( category.getName() );
                questions[j] = questionOldFormat;
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
            fs.deleteDir( "b_" + fName );
        }
    }
}

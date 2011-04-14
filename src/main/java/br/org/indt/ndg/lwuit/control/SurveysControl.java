package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.lwuit.model.Category;
import br.org.indt.ndg.lwuit.model.CategoryAnswer;
import br.org.indt.ndg.lwuit.model.CategoryConditional;
import br.org.indt.ndg.lwuit.model.NDGAnswer;
import br.org.indt.ndg.lwuit.model.NDGQuestion;
import br.org.indt.ndg.lwuit.model.Survey;
import br.org.indt.ndg.mobile.FileSystem;
import br.org.indt.ndg.mobile.structures.ResultStructure;
import java.util.Enumeration;
import java.util.Vector;

/**
 *
 * @author mluz, mturiel, amartini
 */
public class SurveysControl {

    private static SurveysControl instance;
    private Survey currentSurvey = null;
    private ResultStructure currentResults = null;
    private String tmpOtherText;
    private int selectedCategory = 0;
    private int mSelectedSubCategory = 0;
    private String[] avaiableSurveyList;

    public static SurveysControl getInstance() {
        if (instance == null) {
            instance = new SurveysControl();
        }
        return instance;
    }

    public void setSurvey(Survey survey) {
        currentSurvey = survey;
    }

    public Survey getSurvey() {
        return currentSurvey;
    }

    public ResultStructure getResult() {
        return currentResults;
    }

    public void setResult(ResultStructure aResults) {
        currentResults = aResults;
    }

    public int getSurveyIdNumber() {
        return currentSurvey.getId();
    }

    public String getSurveyTitle() {
        return AppMIDlet.getInstance().getFileStores().getSurveyStructure().getDisplayableName();
    }

    public void setAvaiableSurveyToDownload(String[] avaiableSurveyList) {
        this.avaiableSurveyList = avaiableSurveyList;
    }

    public String[] getAvailableSurveysToDownload() {
        return avaiableSurveyList;
    }

    public void setItemOtherText(String _val) {
        tmpOtherText = _val;
    }

    public String getItemOtherText() {
        return tmpOtherText;
    }

    public int getSelectedSubCategoryIndex() {
        return mSelectedSubCategory;
    }

    public void setSelectedSubCategoryIndex( int aSelectSubCategory ) {
        mSelectedSubCategory = aSelectSubCategory;
    }

    public void reset() {
        selectedCategory = 0;
    }

    public int getSelectedCategoryIndex() {
        return selectedCategory;
    }

    public Category getSelectedCategory() {
        return (Category) currentSurvey.getCategories().elementAt(selectedCategory);
    }

    public void setSelectedCategory( int selection ) {
        if (selection < 0) {
            selectedCategory = currentSurvey.getCategories().size() - 1;
        } else if (selection >= currentSurvey.getCategories().size()) {
            selectedCategory = 0;
        } else {
            selectedCategory = selection;
        }
    }

    public Vector getQuestionsFlat() {
        Vector questions = new Vector();

        for (int i = 0; i < currentSurvey.getCategories().size(); i++) {
            Category category = (Category) currentSurvey.getCategories().elementAt(i);
            for (int j = 0; j < category.getQuestions().size(); j++) {
                questions.addElement(category.getQuestions().elementAt(j));
            }
        }
        return questions;
    }

    public void deleteResults(boolean[] selectedFlags) {
        Vector filenamesToDelete = new Vector();
        FileSystem fs = AppMIDlet.getInstance().getFileSystem();
        fs.useResults(FileSystem.USE_NOT_SENT_RESULTS);

        // checkSelectedFiles
        for (int i = 0; i < selectedFlags.length; i++) {
            if (selectedFlags[i]) {
                filenamesToDelete.addElement(fs.getResultFilename(i));
            }
        }

        // deleteSelectedFiles
        String fName;

        Enumeration e = filenamesToDelete.elements();
        while (e.hasMoreElements()) {
            fName = (String) e.nextElement();
            fs.deleteFile(fName);
            fs.deleteDir("b_" + fName);
        }
    }

    void prepareEmptyResults() {
        currentResults = new ResultStructure();
        Enumeration categoryIterator = currentSurvey.getCategories().elements();

        while (categoryIterator.hasMoreElements()) {
            CategoryAnswer categoriesAnswerContainer = new CategoryAnswer();
            Category surveyCategory = (Category) categoryIterator.nextElement();
            categoriesAnswerContainer.setName(surveyCategory.getName());
            categoriesAnswerContainer.setId(surveyCategory.getId());

            Enumeration questionIterator = surveyCategory.getQuestions().elements();
            while (questionIterator.hasMoreElements()) {
                NDGQuestion question = (NDGQuestion) questionIterator.nextElement();
                NDGAnswer answer = question.getAnswerModel();
                answer.setId(question.getIdNumber());
                answer.setType(question.getType());
                if (!(surveyCategory instanceof CategoryConditional)) {//this is simple category so we have to prepare container for answers
                    //there is another method to prepare container for ConditionalCategory
                    categoriesAnswerContainer.put(String.valueOf(answer.getId()), answer);
                }
            }
            currentResults.addAnswer(categoriesAnswerContainer);
        }
    }

    void prepareResultsForConditionaCategory(CategoryConditional aCategory, int aNewQuantity) {
        CategoryAnswer categoriesAnswerContainer = currentResults.getCategoryAnswers(aCategory.getId());
        for (int i = aCategory.getQuantity(); i < aNewQuantity; i++) {
            Enumeration questionIterator = aCategory.getQuestions().elements();
            while (questionIterator.hasMoreElements()) {
                NDGQuestion question = (NDGQuestion) questionIterator.nextElement();
                NDGAnswer answer = question.getAnswerModel();
                answer.setId(question.getIdNumber());
                answer.setType(question.getType());
                categoriesAnswerContainer.put(i, String.valueOf(answer.getId()), answer);
            }
        }
    }

    void removeResultsFromConditionaCategory(CategoryConditional aCategory) {
        CategoryAnswer categoriesAnswerContainer = currentResults.getCategoryAnswers(aCategory.getId());

        for (int i = categoriesAnswerContainer.getSubcategoriesCount() - 1; i >= aCategory.getQuantity(); i--) {
            categoriesAnswerContainer.remove(i);
        }
    }
}

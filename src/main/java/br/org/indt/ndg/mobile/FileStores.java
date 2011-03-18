package br.org.indt.ndg.mobile;

import br.org.indt.ndg.lwuit.control.SurveysControl;
import br.org.indt.ndg.lwuit.model.Category;
import br.org.indt.ndg.lwuit.model.CategoryAnswer;
import br.org.indt.ndg.lwuit.model.CategoryConditional;
import br.org.indt.ndg.lwuit.model.NDGAnswer;
import br.org.indt.ndg.lwuit.model.NDGQuestion;
import br.org.indt.ndg.lwuit.model.Survey;
import java.util.Hashtable;
import java.util.Vector;
import br.org.indt.ndg.mobile.structures.ResultStructure;
import br.org.indt.ndg.mobile.xmlhandle.Parser;
import br.org.indt.ndg.mobile.xmlhandle.ResultHandler;
import br.org.indt.ndg.mobile.xmlhandle.kParser;

public class FileStores {

    private Survey surveyStructure = null;
    private ResultStructure resultStructure = null;

    private Parser parser=null;
    private kParser kparser = null;

    public FileStores() {
    }

    public boolean getError() {
        if (parser!=null) return parser.getError();
        else return false;
    }

    public boolean getErrorkParser() {
        if (kparser!=null) return kparser.getError();
        else return false;
    }

    public Survey getSurveyStructure() {
        return surveyStructure;
    }

    public ResultStructure getResultStructure() {
        return resultStructure;
    }

    public void resetResultStructure()
    {
        resultStructure = null;
    }

    //reset widgets so they will be created newly again
    public void resetQuestions() {
        Survey currentSurvey = SurveysControl.getInstance().getSurvey();
        int numCats = currentSurvey.getCategories().size();
        NDGQuestion question;

        for (int i=0; i < numCats; i++ ) {
            if( currentSurvey.getCategories().elementAt(i) instanceof CategoryConditional ) {
                ((CategoryConditional)currentSurvey.getCategories().elementAt(i)).setQuantity(0);
            }
            for( int j=0; j < ((Category)currentSurvey.getCategories().elementAt(i)).getQuestions().size(); j++ ) {
                question = (NDGQuestion)((Category)currentSurvey.getCategories().elementAt(i)).getQuestions().elementAt(j);
                question.setIsNew(true);
                question.setFirstTime();
                question.setVisited(false);
            }
        }
    }

    public void parseSurveyFile() {
        surveyStructure =  new Survey();

        String dirName = AppMIDlet.getInstance().getFileSystem().getSurveyDirName();

        if (AppMIDlet.getInstance().isXformDir(dirName)) {
            surveyStructure.setTitle(AppMIDlet.getInstance().getFileSystem().getCurrentSurveyName());
        } else {
            kparser = new kParser();
            kparser.setSurveyStructure(surveyStructure);
            kparser.parserSurveyFile(Resources.ROOT_DIR + dirName + Resources.SURVEY_NAME);
        }
        SurveysControl.getInstance().setSurvey((Survey) surveyStructure);
    }

    public void parseResultFile() {
        resultStructure = new ResultStructure();

        ResultHandler handler = new ResultHandler();
        handler.setResultStructure(resultStructure);

        String dirName = AppMIDlet.getInstance().getFileSystem().getSurveyDirName();
        String fileName = AppMIDlet.getInstance().getFileSystem().getResultFilename();

        if (fileName!=null) {
            parser = new Parser(handler);
            parser.parseFile(Resources.ROOT_DIR + dirName + fileName);
        }
    }

    //Loads answers read from result.xml file from result structure to survey structure
    public void loadAnswers() {
        Vector questions;
        NDGQuestion question;
        int questionID;
        NDGAnswer currentAnswer;

        for (int i=0; i < surveyStructure.getCategories().size(); i++) {
            Category category = (Category) surveyStructure.getCategories().elementAt(i);
            if( category instanceof CategoryConditional ) {
                CategoryAnswer categoryAnswer = SurveysControl.getInstance().getResult().getCategoryAnswers( String.valueOf( i + 1 ) );
                ((CategoryConditional)category).setQuantity( categoryAnswer.getSubcategoriesCount() );
            } else if ( category instanceof Category ){
                questions = category.getQuestions();
                CategoryAnswer answers = resultStructure.getCategoryAnswers( String.valueOf( i + 1 ) );

                int questionsSize = questions.size();
                for (int j=0; j < questionsSize; j++) {
                    question = (NDGQuestion) questions.elementAt(j);

                    questionID = question.getIdNumber();

                    //set visited questions loaded from result.xml
                    Object tempanswer = answers.getSubCategoryAnswers(0).get(String.valueOf(questionID));
                    currentAnswer = (NDGAnswer) tempanswer;
                    question.setVisited(currentAnswer.getVisited());
                }
            }
        }
    }
}

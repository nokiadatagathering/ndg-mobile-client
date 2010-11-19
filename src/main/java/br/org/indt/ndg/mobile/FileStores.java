
package br.org.indt.ndg.mobile;

import br.org.indt.ndg.lwuit.model.NDGAnswer;
import br.org.indt.ndg.lwuit.model.NDGQuestion;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import br.org.indt.ndg.mobile.structures.ResultStructure;
import br.org.indt.ndg.mobile.structures.SurveyStructure;
import br.org.indt.ndg.mobile.xmlhandle.Parser;
import br.org.indt.ndg.mobile.xmlhandle.ResultHandler;
import br.org.indt.ndg.mobile.xmlhandle.kParser;

public class FileStores {
    
    private SurveyStructure surveyStructure = null;
    private ResultStructure resultStructure = null;
    
    private Parser parser=null;
    kParser kparser = null;
    
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
    
    public SurveyStructure getSurveyStructure() {
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
        int numCats = getSurveyStructure().getNumCategories();
        Enumeration e;
        NDGQuestion question;
        
        for (int j=0; j < numCats; j++ ) {
            e = getSurveyStructure().getQuestions(j).elements();
            while (e.hasMoreElements()) {
                question = (NDGQuestion) e.nextElement();
                question.setIsNew(true);
                question.setFirstTime();
                question.setVisited(false, j);
            }       
        }
        getSurveyStructure().initVisitedArray();
    }
    
    public void parseSurveyFile() {
        surveyStructure =  new SurveyStructure();
        
        String dirName = AppMIDlet.getInstance().getFileSystem().getSurveyDirName();
        
        kparser = new kParser();
        kparser.setSurveyStructure(surveyStructure);
        kparser.parserSurveyFile(Resources.ROOT_DIR + dirName + Resources.SURVEY_NAME);
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

    /**
     * TESTAR EXAUSTIVAMENTE
     * @param categoryIndex
     * @param id
     * @return the Answer or null
     */
    public NDGAnswer loadAnswerById(int categoryIndex, int id){
        if(resultStructure != null){
            Hashtable answers = resultStructure.getAnswers(categoryIndex);
            return (NDGAnswer) answers.get(String.valueOf(id));
        }
        else
            return null;
    }
    
    //Loads answers read from result.xml file from result structure to survey structure
    public void loadAnswers() {
        Vector questions;
        Hashtable answers;
        NDGQuestion question;
        String questionType;
        int questionID;
        NDGAnswer currentAnswer;

        for (int i=0; i < surveyStructure.getNumCategories(); i++) {

            questions = surveyStructure.getQuestions(i);
            answers = resultStructure.getAnswers(i);

            int questionsSize = questions.size();
            for (int j=0; j < questionsSize; j++) {
                question = (NDGQuestion) questions.elementAt(j);

                questionID = question.getIdNumber();
                questionType = question.getType();

                //set visited questions loaded from result.xml
                Object tempanswer = answers.get(String.valueOf(questionID));
                currentAnswer = (NDGAnswer) tempanswer;
                question.setVisited(currentAnswer.getVisited(), i);
            }
        }        
    }

}

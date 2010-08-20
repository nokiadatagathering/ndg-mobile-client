
package br.org.indt.ndg.mobile;

import br.org.indt.ndg.lwuit.model.NDGAnswer;
import br.org.indt.ndg.lwuit.model.NDGQuestion;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import br.org.indt.ndg.mobile.structures.ResultStructure;
import br.org.indt.ndg.mobile.structures.SurveyStructure;
import br.org.indt.ndg.mobile.structures.answer.Answer;
import br.org.indt.ndg.mobile.structures.answer.ChoiceAnswer;
import br.org.indt.ndg.mobile.structures.answer.DateAnswer;
import br.org.indt.ndg.mobile.structures.answer.DecimalAnswer;
import br.org.indt.ndg.mobile.structures.answer.IntegerAnswer;
import br.org.indt.ndg.mobile.structures.answer.StringAnswer;
import br.org.indt.ndg.mobile.structures.answer.TimeAnswer;
import br.org.indt.ndg.mobile.structures.question.Question;

import br.org.indt.ndg.mobile.structures.question.TypeChoice;
import br.org.indt.ndg.mobile.structures.question.TypeDate;
import br.org.indt.ndg.mobile.structures.question.TypeTextFieldDecimal;
import br.org.indt.ndg.mobile.structures.question.TypeTextFieldInteger;
import br.org.indt.ndg.mobile.structures.question.TypeTextFieldString;
import br.org.indt.ndg.mobile.structures.question.TypeTime;
import br.org.indt.ndg.mobile.util.Utils;
import br.org.indt.ndg.mobile.xmlhandle.Parser;
import br.org.indt.ndg.mobile.xmlhandle.ResultHandler;
import br.org.indt.ndg.mobile.xmlhandle.SurveyHandler;
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
                
                if (question.getType().equals("_str")) {
                    StringAnswer answer = (StringAnswer) currentAnswer;
                    ((TypeTextFieldString) questions.elementAt(j)).getWidget().setString(answer.getValue());
                } if (question.getType().equals("_date")) {
                    DateAnswer answer = (DateAnswer) currentAnswer;
                    ((TypeDate) questions.elementAt(j)).getWidget().setDate(new Date(answer.getDate()));
                }
                else if (question.getType().equals("_int")) {
                    IntegerAnswer answer = (IntegerAnswer) currentAnswer;                    
                    ((TypeTextFieldInteger) questions.elementAt(j)).getWidget().setString(String.valueOf(answer.getValue()));
                } 
                else if (question.getType().equals("_decimal")) {
                    DecimalAnswer answer = (DecimalAnswer) currentAnswer;                    
                    ((TypeTextFieldDecimal) questions.elementAt(j)).getWidget().setString(Utils.convertDoubleNumberFromSciNotation(String.valueOf(answer.getValue())));
                    
                } 
                else if (question.getType().equals("_choice")) {
                    ChoiceAnswer answer = (ChoiceAnswer) currentAnswer;              
                    Vector selectedIndexes = answer.getSelectedIndexes();
                    String selectedIndex, otherText;
                    for (int k=0; k < selectedIndexes.size(); k++) {
                        selectedIndex = (String) selectedIndexes.elementAt(k);                  
                        ((TypeChoice) questions.elementAt(j)).getWidget().setSelectedItem(selectedIndex);
                        otherText = answer.getOtherText(selectedIndex);                   
                        if (otherText != null)  ((TypeChoice) questions.elementAt(j)).getWidget().setOtherText(selectedIndex, otherText);
                    }      
                }
                else if (question.getType().equals("_time")) {
                    TimeAnswer answer = (TimeAnswer) currentAnswer;
                    TypeTime typeTime = (TypeTime) questions.elementAt(j);
                    
                    if(typeTime.getConvention().equals("12")){                       
                        typeTime.setHourConvention(answer.getConvention() == 1?"am":"pm");
                    }
                    typeTime.getWidget().setTime(new Date(answer.getTime()));
                }
            }
        }        
    }

    public void clearAnswers() {
        Vector questions;
        NDGQuestion question;

        for (int i=0; i < surveyStructure.getNumCategories(); i++) {

            questions = surveyStructure.getQuestions(i);

            for (int j=0; j < questions.size(); j++) {
                question = (NDGQuestion) questions.elementAt(j);

                if (question.getType().equals("_str")) {
                    ((TypeTextFieldString) questions.elementAt(j)).getWidget();
                } if (question.getType().equals("_date")) {
                    ((TypeDate) questions.elementAt(j)).getWidget();
                }
                else if (question.getType().equals("_int")) {
                    ((TypeTextFieldInteger) questions.elementAt(j)).getWidget();
                }
                else if (question.getType().equals("_decimal")) {
                    ((TypeTextFieldDecimal) questions.elementAt(j)).getWidget();

                }
                else if (question.getType().equals("_choice")) {
                    ((TypeChoice) questions.elementAt(j)).getWidget();
                }
                else if (question.getType().equals("_time")) {
                    ((TypeTime) questions.elementAt(j)).getWidget();
                }
            }
        }
    }
}

/*
*  Nokia Data Gathering
*
*  Copyright (C) 2011 Nokia Corporation
*
*  This program is free software; you can redistribute it and/or
*  modify it under the terms of the GNU Lesser General Public
*  License as published by the Free Software Foundation; either
*  version 2.1 of the License, or (at your option) any later version.
*
*  This program is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
*  Lesser General Public License for more details.
*
*  You should have received a copy of the GNU Lesser General Public License
*  along with this program.  If not, see <http://www.gnu.org/licenses/
*/

package br.org.indt.ndg.mobile;

import br.org.indt.ndg.lwuit.control.AES;
import br.org.indt.ndg.lwuit.control.ExitCommand;
import br.org.indt.ndg.lwuit.control.SurveysControl;
import br.org.indt.ndg.lwuit.model.Category;
import br.org.indt.ndg.lwuit.model.CategoryAnswer;
import br.org.indt.ndg.lwuit.model.CategoryConditional;
import br.org.indt.ndg.lwuit.model.NDGAnswer;
import br.org.indt.ndg.lwuit.model.NDGQuestion;
import br.org.indt.ndg.lwuit.model.Survey;
import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import java.util.Vector;
import br.org.indt.ndg.mobile.structures.ResultStructure;
import br.org.indt.ndg.mobile.xmlhandle.Parser;
import br.org.indt.ndg.mobile.xmlhandle.ResultHandler;
import br.org.indt.ndg.mobile.xmlhandle.kParser;
import com.nokia.xfolite.xml.dom.Document;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;

public class FileStores {

    private Survey surveyStructure = null;
    private ResultStructure resultStructure = null;
    private Document xformResult;

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

    public void createResultStructure(){
        if(resultStructure == null){
            resultStructure = new ResultStructure();
        }
    }

    public void resetResultStructure()
    {
        resultStructure = null;
        xformResult = null;
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

        if ( Utils.isXformDir(dirName) ) {
            surveyStructure.setTitle(AppMIDlet.getInstance().getFileSystem().getCurrentSurveyName());
        } else {
            kparser = new kParser();
            kparser.setSurveyStructure(surveyStructure);
            kparser.parserSurveyFile(AppMIDlet.getInstance().getRootDir() + dirName + NdgConsts.SURVEY_NAME);
        }
        SurveysControl.getInstance().setSurvey((Survey) surveyStructure);
    }

    public void loadXFormResult(){
        xformResult = null;

        String resultFileName = AppMIDlet.getInstance().getFileSystem().getResultFilename();
        String dirName = AppMIDlet.getInstance().getFileSystem().getSurveyDirName();
        String resultPath = AppMIDlet.getInstance().getRootDir() + dirName + resultFileName;
        FileConnection fc;
        try {
            fc = (FileConnection) Connector.open(resultPath, Connector.READ);

            boolean encryption = false;
            if(AppMIDlet.getInstance().getSettings() != null) {
                if( AppMIDlet.getInstance().getSettings().getStructure().isEncryptionConfigured() )
                    encryption = AppMIDlet.getInstance().getSettings().getStructure().getEncryption();
            }
            InputStream is = fc.openInputStream();

            if( encryption ) {
                AES encrypter = new AES();
                try {
                    is = encrypter.decryptInputStreamToInputStream( is );
                } catch (Exception e) {
                    GeneralAlert.getInstance().addCommand( ExitCommand.getInstance());
                    GeneralAlert.getInstance().show(Resources.ERROR_TITLE, Resources.WRONG_KEY, GeneralAlert.ERROR );
                }
            }

            KXmlParser parser = new KXmlParser();
            parser.setInput(is, "UTF-8");

            xformResult = new Document();
            xformResult.parse(parser);

        } catch (XmlPullParserException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Document getXFormResult(){
        return xformResult;
    }

    public void parseResultFile() {
        resultStructure = new ResultStructure();

        ResultHandler handler = new ResultHandler();
        handler.setResultStructure(resultStructure);

        String dirName = AppMIDlet.getInstance().getFileSystem().getSurveyDirName();
        String fileName = AppMIDlet.getInstance().getFileSystem().getResultFilename();

        if (fileName!=null) {
            parser = new Parser(handler);
            parser.parseFile(AppMIDlet.getInstance().getRootDir() + dirName + fileName);
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

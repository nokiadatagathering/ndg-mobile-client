package br.org.indt.ndg.mobile.xmlhandle;

import br.org.indt.ndg.lwuit.control.ExitCommand;
import br.org.indt.ndg.lwuit.model.Category;
import br.org.indt.ndg.lwuit.model.CategoryConditional;
import br.org.indt.ndg.lwuit.model.ChoiceQuestion;
import br.org.indt.ndg.lwuit.model.DateQuestion;
import br.org.indt.ndg.lwuit.model.DecimalQuestion;
import br.org.indt.ndg.lwuit.model.DescriptiveQuestion;
import br.org.indt.ndg.lwuit.model.ImageQuestion;
import br.org.indt.ndg.lwuit.model.IntegerQuestion;
import br.org.indt.ndg.lwuit.model.NumericQuestion;
import br.org.indt.ndg.lwuit.model.NDGQuestion;
import br.org.indt.ndg.lwuit.model.Survey;
import br.org.indt.ndg.lwuit.model.TimeQuestion;
import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.logging.Logger;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.structures.FileSystemSurveyStructure;

import java.io.InputStream;
import java.io.InputStreamReader;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import org.kxml2.io.*;
import org.xmlpull.v1.*;


import java.util.Vector;

public class kParser {
    private boolean error = false;
    private FileSystemSurveyStructure structure;

    private Survey mSurvey;
    private Category currentCategory;
    private NDGQuestion currentQuestion;
    private Vector questions=null;
    private String currentOther = "0";

    public boolean getError() {
        return error;
    }

    public void parserSurveyFileInfo(String filename) {
        try {
            FileConnection fc = (FileConnection) Connector.open(filename);
            InputStream is = fc.openInputStream();
             //Inicia o XMLParser
            KXmlParser parser = new KXmlParser();
            parser.setInput(new InputStreamReader(is, "UTF-8"));
            parser.nextTag();
 
            String name = null;
            int res = -1;
            if(parser.getName().equals("survey")){
                name = parser.getAttributeValue(parser.getNamespace(), "title");
            }else if(parser.getName().equals("xforms") || parser.getName().equals("html")) {
                name = "xform";
                boolean bVal = true;
                try{
                    while(bVal){
                        if(parser.getAttributeValue("", "name") != null){
                            name = parser.getAttributeValue("", "name");
                            bVal = false;
                        }
                        if(parser.next() == KXmlParser.END_DOCUMENT){
                            name = filename;
                            bVal = false;
                        }
                    }
                }catch(Exception ex){
                    name = filename;
                }
            }

            if(name != null){
                structure.addName(name);
            }

            is.close();
            fc.close();
        }
        catch(XmlPullParserException e) {
            Logger.getInstance().logException("XmlPullParserException[parserSurveyFileInfo]: " + e.getMessage());
            e.printStackTrace();
            error = true;
            AppMIDlet.getInstance().getFileSystem().setError(true);
        }
        catch(Exception e) {
            Logger.getInstance().logException("Exception[parserSurveyFileInfo]: " + e.getMessage());
            e.printStackTrace();
            error = true;
            GeneralAlert.getInstance().addCommand( ExitCommand.getInstance());
            GeneralAlert.getInstance().show(Resources.ERROR_TITLE, Resources.EPARSE_GENERAL, GeneralAlert.ERROR );
        }
    }

    public void parserSurveyFile(String filename) {
        try {
            FileConnection fc = (FileConnection) Connector.open(filename);
            InputStream is = fc.openInputStream();
             //Inicia o XMLParser
            KXmlParser parser = new KXmlParser();
            parser.setInput(new InputStreamReader(is, "UTF-8"));
            parser.nextTag();
            //Posiciona na tag <survey>
            parser.require(IXmlPullParser.START_TAG, null, "survey");

            getSurveyAttributes(parser);

            //Enquanto é diferente de END_TAG
            while (parser.nextTag () != IXmlPullParser.END_TAG)
            {
                //Posiciona na tag <category>
                parser.require(IXmlPullParser.START_TAG, null, "category");

                parserCategory(parser);

                parser.require(IXmlPullParser.END_TAG, null, "category");
                mSurvey.addCategory(currentCategory);
            }

            parser.require(IXmlPullParser.END_TAG, null, "survey");
            parser.next();

            parser.require(IXmlPullParser.END_DOCUMENT, null, null);
            is.close();
            fc.close();

            //survey.confirmChecksum(checksum, surveyChecksumBuf);
        }
        catch(XmlPullParserException e) {
            Logger.getInstance().logException("XmlPullParserException[parserSurveyFile]: " + e.getMessage());
        }
        catch(Exception e) {
            Logger.getInstance().logException("Exception[parserSurveyFile]: " + e.getMessage());
        }
    }

    private void parserCategory(KXmlParser parser) throws Exception {
        getCategoryAttributes(parser);

        //Enquanto é diferente de </question>
        questions = new Vector();
        while (parser.nextTag() != IXmlPullParser.END_TAG) {
            //Posiciona em uma tag "START". Ex: <question>
            parser.require(IXmlPullParser.START_TAG, null, "question");

            parserQuestion(parser);
            //Posiciona no fim da tag </category>
            parser.require(IXmlPullParser.END_TAG, null, "question");
        }
        currentCategory.setQuestions(questions);
    }

    private void parserQuestion(KXmlParser parser) throws Exception {
        getQuestionAttributes(parser);

        String elemName = "";
        Vector defaultAnswers = new Vector();
        int itemIndex = 0;
        while (parser.nextTag() != IXmlPullParser.END_TAG) {
            //Posiciona em uma tag "START". Ex: <description> <length> <item> <select> <SkipLogic>
            parser.require(IXmlPullParser.START_TAG, null, null);
            elemName = parser.getName();
            String text = "";
            String strIsDefault = "";


            if (elemName.equals("description")){
                text = parser.nextText();
                currentQuestion.setDescription(text);
            }
            else if (elemName.equals("length")) {
                text = parser.nextText();
                if (currentQuestion instanceof DescriptiveQuestion )
                    ((DescriptiveQuestion) currentQuestion).setLength(Integer.parseInt(text));
                else if (currentQuestion instanceof NumericQuestion )
                    ((NumericQuestion) currentQuestion).setLength(Integer.parseInt(text));

            }
            else if (elemName.equals("SkipLogic")) {
                String namespace = parser.getNamespace();
                String catTo = parser.getAttributeValue(namespace, "catTo");
                String operand = parser.getAttributeValue(namespace, "operand");
                String operator = parser.getAttributeValue(namespace, "operator");
                String skipTo = parser.getAttributeValue(namespace, "skipTo");

                ((ChoiceQuestion) currentQuestion).setCatTo(Integer.parseInt(catTo));
                ((ChoiceQuestion) currentQuestion).setChoiceItem(Integer.parseInt(operand));
                ((ChoiceQuestion) currentQuestion).setInverse(operator.equals("1"));
                ((ChoiceQuestion) currentQuestion).setSkipTo(Integer.parseInt(skipTo));
                ((ChoiceQuestion) currentQuestion).setSkipEnabled(true);

                text = parser.nextText();
            }
            else if (elemName.equals("select")) {
                text = parser.nextText();
                ((ChoiceQuestion) currentQuestion).setExclusive( "exclusive".equals(text) );
            }
            else if (elemName.equals("item")) {
                if (currentQuestion instanceof ChoiceQuestion) {
                    strIsDefault = parser.getAttributeValue(parser.getNamespace(), "def");
                    currentOther = parser.getAttributeValue(parser.getNamespace(), "otr");
                    text = parser.nextText();
                    ((ChoiceQuestion) currentQuestion).addChoice(text);
                    ((ChoiceQuestion) currentQuestion).addOther( currentOther );
                    ((ChoiceQuestion) currentQuestion).addOthersText( text );

                    if(strIsDefault != null && strIsDefault.compareTo("1") == 0) {
                        defaultAnswers.addElement(new Integer(itemIndex).toString());
                    }

                    itemIndex++;

                } else if (currentQuestion instanceof DescriptiveQuestion) {
                    currentOther = parser.getAttributeValue(parser.getNamespace(), "otr");
                    text = parser.nextText();
                    ((DescriptiveQuestion) currentQuestion).addChoice(text);
                }
            }
            //Posiciona no fim da tag <description> <length> <item> <select> <SkipLogic>
            parser.require(IXmlPullParser.END_TAG, null, elemName);
        }

        if (currentQuestion instanceof ChoiceQuestion && defaultAnswers.size() > 0 ){
            ChoiceQuestion chQuestion = (ChoiceQuestion) currentQuestion;
            chQuestion.setDefaultAnswers(defaultAnswers);
        }

        currentQuestion.setCategoryId(currentCategory.getId());
        currentQuestion.setCategoryName(currentCategory.getName());
        questions.addElement(currentQuestion);
    }

    private void getSurveyAttributes(KXmlParser parser) throws Exception {
        String namespace = parser.getNamespace();
        String id = parser.getAttributeValue(namespace, "id");
        String display = parser.getAttributeValue(namespace, "display");
        String title = parser.getAttributeValue(namespace, "title");

        mSurvey.setIdNumber(Integer.parseInt(id));
        mSurvey.setDisplayId(display);
        mSurvey.setTitle(title);
    }

    private void getCategoryAttributes(KXmlParser parser) throws Exception {
        String namespace = parser.getNamespace();
        String name = parser.getAttributeValue(namespace, "name");
        String id = parser.getAttributeValue(namespace, "id");
        String condition = parser.getAttributeValue(namespace, "condition");

        if( condition!= null && condition.trim().length() > 0 ) {
            currentCategory = new CategoryConditional( name, id, condition );
        } else {
            currentCategory = new Category( name, id );
        }
    }

    private void getQuestionAttributes(KXmlParser parser) throws Exception {
        String namespace = parser.getNamespace();

        String _type = parser.getAttributeValue(namespace, "type");
        String _id = parser.getAttributeValue(namespace, "id");

        if (_type.equals("_str")) currentQuestion = new DescriptiveQuestion();
        else if (_type.equals("_int")) currentQuestion = new IntegerQuestion( );
        else if (_type.equals("_decimal")) currentQuestion = new DecimalQuestion();
        else if (_type.equals("_choice")) currentQuestion = new ChoiceQuestion();
        else if (_type.equals("_date")) currentQuestion = new DateQuestion();
        else if (_type.equals("_time")) currentQuestion = new TimeQuestion();
        else if (_type.equals("_img")) currentQuestion = new ImageQuestion();

        currentQuestion.setType(_type);
        currentQuestion.setIdNumber(Integer.parseInt(_id));

        String strEditable = parser.getAttributeValue(namespace, "editable");
        if (strEditable != null)
            currentQuestion.setEdit(strEditable);

        if (_type.equals("_time")) {
            String convention = parser.getAttributeValue(namespace, "convention");
            ((TimeQuestion) currentQuestion).setConvention(convention);
        }

        if(_type.equals("_int") || _type.equals("_date") || _type.equals("_decimal")){
            String min = parser.getAttributeValue(namespace, "min");
            String max = parser.getAttributeValue(namespace, "max");
            if (_type.equals("_int")){
                ((NumericQuestion) currentQuestion).setLowConstraint(min);
                ((NumericQuestion) currentQuestion).setHighConstraint(max);
            }
            else if (_type.equals("_date")){
                ((DateQuestion)currentQuestion).setLowConstraint(min);
                ((DateQuestion) currentQuestion).setHighConstraint(max);
            }
            else if (_type.equals("_decimal")){
                ((NumericQuestion) currentQuestion).setLowConstraint(min);
                ((NumericQuestion) currentQuestion).setHighConstraint(max);
            }
        }
        if(_type.equals("_img")){
            String maxCount = parser.getAttributeValue(namespace, "maxCount");
            int count = 1;
            if ( maxCount != null ) {
                try {
                    count = Integer.parseInt(maxCount);
                } catch ( NumberFormatException nex) {
                    count = 1;
                }
            }
            ((ImageQuestion)currentQuestion).setMaxCount( count < 1 ? 1 : count );
        }
    }

    public void setFileSystemSurveyStructure(FileSystemSurveyStructure _structure) {
        structure = _structure;
    }

    public void setSurveyStructure(Survey aSurvey) {
        mSurvey = aSurvey;
    }
}



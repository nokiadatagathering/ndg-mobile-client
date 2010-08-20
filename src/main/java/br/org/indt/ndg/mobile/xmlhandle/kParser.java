package br.org.indt.ndg.mobile.xmlhandle;

import br.org.indt.ndg.lwuit.model.ImageQuestion;
import br.org.indt.ndg.lwuit.model.NDGQuestion;
import br.org.indt.ndg.lwuit.model.TimeQuestion;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.logging.Logger;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.structures.FileSystemSurveyStructure;
import br.org.indt.ndg.mobile.structures.SurveyStructure;
import br.org.indt.ndg.mobile.structures.question.Question;
import br.org.indt.ndg.mobile.structures.question.TypeChoice;
import br.org.indt.ndg.mobile.structures.question.TypeDate;
import br.org.indt.ndg.mobile.structures.question.TypeTextFieldDecimal;
import br.org.indt.ndg.mobile.structures.question.TypeTextFieldInteger;
import br.org.indt.ndg.mobile.structures.question.TypeTextFieldString;
import br.org.indt.ndg.mobile.structures.question.TypeTime;

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
    
    
    private SurveyStructure survey;
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
            //InputStreamSurveyReader issr = new InputStreamSurveyReader(is, "UTF-8");
            parser.setInput(new InputStreamReader(is, "UTF-8"));
            //parser.setInput(issr);
            
            parser.nextTag();
 
            //Posiciona na tag <survey>
            parser.require(XmlPullParser.START_TAG, null, "survey");
            
            structure.addName(parser.getAttributeValue(parser.getNamespace(), "title"));
            
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
            AppMIDlet.getInstance().getGeneralAlert().showErrorExit(Resources.EPARSE_GENERAL);
        }
    }
    
    public void parserSurveyFile(String filename) {
        try {
            FileConnection fc = (FileConnection) Connector.open(filename);
            InputStream is = fc.openInputStream();
             //Inicia o XMLParser
            KXmlParser parser = new KXmlParser();
            //InputStreamSurveyReader issr = new InputStreamSurveyReader(is, "UTF-8");
            parser.setInput(new InputStreamReader(is, "UTF-8"));
            //parser.setInput(issr);
            
            parser.nextTag();
 
            //Posiciona na tag <survey>
            parser.require(XmlPullParser.START_TAG, null, "survey");
            
            getSurveyAttributes(parser);
            
            //Enquanto é diferente de END_TAG
            while (parser.nextTag () != XmlPullParser.END_TAG)
            {
                //Posiciona na tag <category>
                parser.require(XmlPullParser.START_TAG, null, "category");

                parserCategory(parser);

                parser.require(XmlPullParser.END_TAG, null, "category");
            }
 
            parser.require(XmlPullParser.END_TAG, null, "survey");
            parser.next();

            parser.require(XmlPullParser.END_DOCUMENT, null, null);
            //issr.show();
            is.close();
            fc.close();
            
            survey.initVisitedArray();
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
        while (parser.nextTag() != XmlPullParser.END_TAG) {
            //Posiciona em uma tag "START". Ex: <question>
            parser.require(XmlPullParser.START_TAG, null, "question");
            
            parserQuestion(parser);

            //Posiciona no fim da tag </category>
            parser.require(XmlPullParser.END_TAG, null, "question");
        }
        survey.addCategory(questions);
    }
    
    private void parserQuestion(KXmlParser parser) throws Exception {
        getQuestionAttributes(parser);
        
        String elemName = "";
        while (parser.nextTag() != XmlPullParser.END_TAG) {
            //Posiciona em uma tag "START". Ex: <description> <length> <item> <select> <SkipLogic>
            parser.require(XmlPullParser.START_TAG, null, null);
            elemName = parser.getName();
            String text = "";


            if (elemName.equals("description")){
                text = parser.nextText();
                currentQuestion.setDescription(text);
            }
            else if (elemName.equals("length")) {
                text = parser.nextText();
                if (currentQuestion.getType().equals("_str"))
                    ((TypeTextFieldString) currentQuestion).setLength(Integer.parseInt(text));
                else if (currentQuestion.getType().equals("_int") )
                    ((TypeTextFieldInteger) currentQuestion).setLength(Integer.parseInt(text));
                else if (currentQuestion.getType().equals("_decimal") )
                    ((TypeTextFieldDecimal) currentQuestion).setLength(Integer.parseInt(text));
                
                
            }
            else if (elemName.equals("SkipLogic")) {
                String namespace = parser.getNamespace();
                String catTo = parser.getAttributeValue(namespace, "catTo");
                String operand = parser.getAttributeValue(namespace, "operand");
                String operator = parser.getAttributeValue(namespace, "operator");
                String skipTo = parser.getAttributeValue(namespace, "skipTo");

                ((TypeChoice) currentQuestion).setCatTo(catTo);
                ((TypeChoice) currentQuestion).setOperand(operand);
                ((TypeChoice) currentQuestion).setOperator(operator);  
                ((TypeChoice) currentQuestion).setSkipTo(skipTo);
                ((TypeChoice) currentQuestion).setSkipEnabled(true);  

                text = parser.nextText();
            }
            else if (elemName.equals("select")) {
                text = parser.nextText();
                ((TypeChoice) currentQuestion).setSelect(text);
                
            }
            else if (elemName.equals("item")) {
                if (currentQuestion instanceof TypeChoice) {
                    currentOther = parser.getAttributeValue(parser.getNamespace(), "otr");
                    text = parser.nextText();
                    ((TypeChoice) currentQuestion).addChoice(text, currentOther);
                    ((TypeChoice) currentQuestion).addChoiceOrdered(text);
                } else if (currentQuestion instanceof TypeTextFieldString) {
                    currentOther = parser.getAttributeValue(parser.getNamespace(), "otr");
                    text = parser.nextText();
                    ((TypeTextFieldString) currentQuestion).addChoice(text, currentOther);
                    ((TypeTextFieldString) currentQuestion).addChoiceOrdered(text);
                }

                
            }
            
            //Posiciona no fim da tag <description> <length> <item> <select> <SkipLogic>
            parser.require(XmlPullParser.END_TAG, null, elemName);
        }
        questions.addElement(currentQuestion);
    }
    
    private void getSurveyAttributes(KXmlParser parser) throws Exception {
        String namespace = parser.getNamespace();
        String id = parser.getAttributeValue(namespace, "id");
        String display = parser.getAttributeValue(namespace, "display");
        String deployed = parser.getAttributeValue(namespace, "deployed");
        String title = parser.getAttributeValue(namespace, "title");
        
        survey.setIdNumber(Integer.parseInt(id));
        survey.setDisplayId(display);
        survey.setTitle(title);
    }
    
    private void getCategoryAttributes(KXmlParser parser) throws Exception {
        String namespace = parser.getNamespace();
        questions = new Vector();
            
        String name = parser.getAttributeValue(namespace, "name");
        String id = parser.getAttributeValue(namespace, "id");

        survey.addCatName(name);
        survey.addCatId(id);            

    }
    
    private void getQuestionAttributes(KXmlParser parser) throws Exception {
        String namespace = parser.getNamespace();
        String field = parser.getAttributeValue(namespace, "field");
        String direction = parser.getAttributeValue(namespace, "direction");
        String editable = parser.getAttributeValue(namespace, "editable");

        String _type = parser.getAttributeValue(namespace, "type");
        String _id = parser.getAttributeValue(namespace, "id");

        if (_type.equals("_str")) currentQuestion = new TypeTextFieldString();
        else if (_type.equals("_int")) currentQuestion = new TypeTextFieldInteger();
        else if (_type.equals("_decimal")) currentQuestion = new TypeTextFieldDecimal();
        else if (_type.equals("_choice")) currentQuestion = new TypeChoice();
        else if (_type.equals("_date")) currentQuestion = new TypeDate();
        else if (_type.equals("_time")) currentQuestion = new TypeTime();
        else if (_type.equals("_img")) currentQuestion = new ImageQuestion();

        currentQuestion.setType(_type);
        currentQuestion.setIdNumber(Integer.parseInt(_id));

        String strEditable = parser.getAttributeValue(namespace, "editable");
        if (strEditable != null)
            currentQuestion.setEdit(strEditable);

        if (_type.equals("_time")) {
            String convention = parser.getAttributeValue(namespace, "convention");            
            ((TypeTime) currentQuestion).setConvention(convention);
        }

        if(_type.equals("_int") || _type.equals("_date") || _type.equals("_decimal")){
            String min = parser.getAttributeValue(namespace, "min");
            String max = parser.getAttributeValue(namespace, "max");
            if (_type.equals("_int")){
                ((TypeTextFieldInteger) currentQuestion).setLowConstraint(min);
                ((TypeTextFieldInteger) currentQuestion).setHighConstraint(max);
            }
            else if (_type.equals("_date")){
                ((TypeDate) currentQuestion).setLowConstraint(min);
                ((TypeDate) currentQuestion).setHighConstraint(max);
            }
            else if (_type.equals("_decimal")){
                ((TypeTextFieldDecimal) currentQuestion).setLowConstraint(min);
                ((TypeTextFieldDecimal) currentQuestion).setHighConstraint(max);
            }
        }
    }
    
    public void setFileSystemSurveyStructure(FileSystemSurveyStructure _structure) {
        structure = _structure;
    }
    
    public void setSurveyStructure(SurveyStructure survey) {
        this.survey = survey;
    }
}



package br.org.indt.ndg.mobile.xmlhandle;

import br.org.indt.ndg.lwuit.model.ChoiceQuestion;
import java.io.UnsupportedEncodingException;
import java.util.Stack;
import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import br.org.indt.ndg.mobile.AppMIDlet;

import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.logging.Logger;
import br.org.indt.ndg.mobile.structures.SurveyStructure;
import br.org.indt.ndg.mobile.structures.question.Question;
import br.org.indt.ndg.mobile.structures.question.TypeChoice;
import br.org.indt.ndg.mobile.structures.question.TypeDate;
import br.org.indt.ndg.mobile.structures.question.TypeTextFieldDecimal;
import br.org.indt.ndg.mobile.structures.question.TypeTextFieldInteger;
import br.org.indt.ndg.mobile.structures.question.TypeTextFieldString;
import br.org.indt.ndg.mobile.structures.question.TypeTime;
import com.twmacinta.util.MD5;
import java.io.DataOutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

public class SurveyHandler extends DefaultHandler {
    private final static String QUOTES = "\"";
    private final static char SPACE =  ' ';
    
    private SurveyStructure survey;
    private Stack tagStack = new Stack();
    private Question currentQuestion;
    private Vector questions=null;
    private String currentOther = "0";
    
    private StringBuffer surveyChecksumBuf;
    private String checksum;
    
    public SurveyHandler(){
        surveyChecksumBuf = new StringBuffer();      
    }
    
    public void setSurveyStructure(SurveyStructure survey) {
        this.survey = survey;
    }
    
    public void startDocument() throws SAXException {}
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {        
        if (qName.equals("survey")) {
            String id = attributes.getValue(attributes.getIndex("id"));
            String display = attributes.getValue(attributes.getIndex("display"));
            String deployed = attributes.getValue(attributes.getIndex("deployed"));
            String title = attributes.getValue(attributes.getIndex("title"));
            checksum = attributes.getValue(attributes.getIndex("checksum"));
            
            survey.setIdNumber(Integer.parseInt(id));
            survey.setDisplayId(display);            
            
            surveyChecksumBuf.append("<survey display=").append(QUOTES).append(display).append(QUOTES).append(SPACE);
            surveyChecksumBuf.append("id=").append(QUOTES).append(id).append(QUOTES).append(SPACE);
            surveyChecksumBuf.append("title=").append(QUOTES).append(title).append(QUOTES).append(SPACE);
            surveyChecksumBuf.append("deployed=").append(QUOTES).append(deployed).append(QUOTES).append(SPACE);
            surveyChecksumBuf.append("checksum=\"\"");//.append(QUOTES).append("").append(QUOTES);
            surveyChecksumBuf.append(">");
            surveyChecksumBuf.append('\n');
        }
        else if (qName.equals("category")) {
            questions = new Vector();
            
            String name = attributes.getValue(attributes.getIndex("name"));
            String id = attributes.getValue(attributes.getIndex("id"));
            
            survey.addCatName(name);
            survey.addCatId(id);            
            
            surveyChecksumBuf.append(SPACE).append(SPACE);
            surveyChecksumBuf.append("<category id=").append(QUOTES).append(id).append(QUOTES).append(SPACE);
            surveyChecksumBuf.append("name=").append(QUOTES).append(name).append(QUOTES);
            surveyChecksumBuf.append(">");
            surveyChecksumBuf.append('\n');     
        }
        else if (qName.equals("question")) {
            //String type = attributes.getValue(attributes.getIndex("tp"));
            //String id = attributes.getValue(attributes.getIndex("id"));
            String field = attributes.getValue(attributes.getIndex("field"));
            String direction = attributes.getValue(attributes.getIndex("direction"));
            String editable = attributes.getValue(attributes.getIndex("editable"));
            
            String _type = attributes.getValue(attributes.getIndex("type"));
            String _id = attributes.getValue(attributes.getIndex("id"));
            
            if (_type.equals("_str")) currentQuestion = new TypeTextFieldString();
            else if (_type.equals("_int")) currentQuestion = new TypeTextFieldInteger();
            else if (_type.equals("_decimal")) currentQuestion = new TypeTextFieldDecimal();
            else if (_type.equals("_choice")) currentQuestion = new TypeChoice();            
            else if (_type.equals("_date")) currentQuestion = new TypeDate();
            else if (_type.equals("_time")) currentQuestion = new TypeTime();
            
            currentQuestion.setType(_type);
            currentQuestion.setIdNumber(Integer.parseInt(_id));
            
            //check if "editable" attribute exists
            int edit_index = attributes.getIndex("editable");
            if (edit_index != -1) { currentQuestion.setEdit(attributes.getValue(edit_index)); }

            //check if "convention" attribute exists
            int conv_index = attributes.getIndex("convention");
            if (conv_index != -1) { currentQuestion.setEdit(attributes.getValue(conv_index)); }
            
            surveyChecksumBuf.append(SPACE).append(SPACE).append(SPACE).append(SPACE);
            surveyChecksumBuf.append("<question id=").append(QUOTES).append(_id).append(QUOTES).append(SPACE);
            surveyChecksumBuf.append("type=").append(QUOTES).append(_type).append(QUOTES).append(SPACE);
            surveyChecksumBuf.append("field=").append(QUOTES).append(field).append(QUOTES).append(SPACE);
            surveyChecksumBuf.append("direction=").append(QUOTES).append(direction).append(QUOTES).append(SPACE);
            surveyChecksumBuf.append("editable=").append(QUOTES).append(editable).append(QUOTES);


            if (_type.equals("_time")) {
                String convention = attributes.getValue(attributes.getIndex("convention"));                
                convention = attributes.getValue(attributes.getIndex("convention"));
                surveyChecksumBuf.append("convention=").append(QUOTES).append(convention).append(QUOTES);
                ((TypeTime) currentQuestion).setConvention(convention);
            }

            if(_type.equals("_int") || _type.equals("_date") || _type.equals("_decimal")){
                String min = attributes.getValue(attributes.getIndex("min"));
                String max = attributes.getValue(attributes.getIndex("max"));
                surveyChecksumBuf.append(SPACE).append("min=").append(QUOTES).append(min).append(QUOTES).append(SPACE);
                surveyChecksumBuf.append("max=").append(QUOTES).append(max).append(QUOTES);
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
            surveyChecksumBuf.append(">");
            surveyChecksumBuf.append('\n');
        }
        else if (qName.equals("item")) {
            currentOther = attributes.getValue(0);
            surveyChecksumBuf.append(SPACE).append(SPACE).append(SPACE).append(SPACE).append(SPACE).append(SPACE);
            surveyChecksumBuf.append("<item otr=").append(QUOTES).append(currentOther).append(QUOTES).append(">");
        }
        else if (qName.equals("SkipLogic")) {
            String catTo = attributes.getValue(attributes.getIndex("catTo"));
            String operand = attributes.getValue(attributes.getIndex("operand"));
            String operator = attributes.getValue(attributes.getIndex("operator"));
            String skipTo = attributes.getValue(attributes.getIndex("skipTo"));
            
            ((TypeChoice) currentQuestion).setCatTo(catTo);
            ((TypeChoice) currentQuestion).setOperand(operand);
            ((TypeChoice) currentQuestion).setOperator(operator);  
            ((TypeChoice) currentQuestion).setSkipTo(skipTo);
            ((TypeChoice) currentQuestion).setSkipEnabled(true);  
            
            surveyChecksumBuf.append(SPACE).append(SPACE).append(SPACE).append(SPACE).append(SPACE).append(SPACE);
            surveyChecksumBuf.append("<SkipLogic catTo=").append(QUOTES).append(catTo).append(QUOTES).append(SPACE);
            surveyChecksumBuf.append("operand=").append(QUOTES).append(operand).append(QUOTES).append(SPACE);
            surveyChecksumBuf.append("operator=").append(QUOTES).append(operator).append(QUOTES).append(SPACE);
            surveyChecksumBuf.append("skipTo=").append(QUOTES).append(skipTo).append(QUOTES);                
            surveyChecksumBuf.append("/>");
            surveyChecksumBuf.append('\n');            
        }
        
        tagStack.push(qName);
    }
    
    public void characters(char[] ch, int start, int length) throws SAXException {
        String chars = new String(ch, start, length).trim();
        
        if (chars.length() > 0) {
            
            String qName = (String)tagStack.peek();
            
            if (qName.equals("title")) survey.setTitle(chars);
            else if (qName.equals("description")){
                Logger.getInstance().log("start: " + start);
                Logger.getInstance().log("length: " + length);
                Logger.getInstance().log("chars: " + chars);
                currentQuestion.setDescription(chars);
                
                surveyChecksumBuf.append(SPACE).append(SPACE).append(SPACE).append(SPACE).append(SPACE).append(SPACE);
                surveyChecksumBuf.append("<description>").append(chars).append("</description>");
                surveyChecksumBuf.append('\n');
            }
            else if (qName.equals("length")) {
                if (currentQuestion.getType().equals("_str"))
                    ((TypeTextFieldString) currentQuestion).setLength(Integer.parseInt(chars));
                else if (currentQuestion.getType().equals("_int") )
                    ((TypeTextFieldInteger) currentQuestion).setLength(Integer.parseInt(chars));
                else if (currentQuestion.getType().equals("_decimal") )
                    ((TypeTextFieldDecimal) currentQuestion).setLength(Integer.parseInt(chars));
                
                surveyChecksumBuf.append(SPACE).append(SPACE).append(SPACE).append(SPACE).append(SPACE).append(SPACE);
                surveyChecksumBuf.append("<length>").append(chars).append("</length>");
                surveyChecksumBuf.append('\n');
            }
            else if (qName.equals("item")) {
                if (currentQuestion instanceof TypeChoice) {
                    ((TypeChoice) currentQuestion).addChoice(chars, currentOther);
                    ((TypeChoice) currentQuestion).addChoiceOrdered(chars);

                    surveyChecksumBuf.append(chars).append("</item>");
                    surveyChecksumBuf.append('\n');
                } else if (currentQuestion instanceof TypeTextFieldString) {
                    ((TypeTextFieldString) currentQuestion).addChoice(chars, currentOther);
                    ((TypeTextFieldString) currentQuestion).addChoiceOrdered(chars);

                    surveyChecksumBuf.append(chars).append("</item>");
                    surveyChecksumBuf.append('\n');
                }

            } 
            else if (qName.equals("select")) {
                ((TypeChoice) currentQuestion).setSelect(chars);
                
                surveyChecksumBuf.append(SPACE).append(SPACE).append(SPACE).append(SPACE).append(SPACE).append(SPACE);
                surveyChecksumBuf.append("<select>").append(chars).append("</select>");
                surveyChecksumBuf.append('\n');                
            } 
//            else if (qName.equals("low")) {
//                if (currentQuestion.getType().equals("_int"))
//                    ((TypeTextFieldInteger) currentQuestion).setLowConstraint(chars);
//                else if (currentQuestion.getType().equals("_date"))
//                    ((TypeDate) currentQuestion).setLowConstraint(chars);
//            } else if (qName.equals("high")) {
//                if (currentQuestion.getType().equals("_int"))
//                    ((TypeTextFieldInteger) currentQuestion).setHighConstraint(chars);
//                else if (currentQuestion.getType().equals("_date"))
//                    ((TypeDate) currentQuestion).setHighConstraint(chars);
//            }
        }
    }
    
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("category")){
            survey.addCategory(questions);
            
            surveyChecksumBuf.append(SPACE).append(SPACE);
            surveyChecksumBuf.append("</category>");
            surveyChecksumBuf.append('\n');
        }
        else if (qName.equals("question")){
            questions.addElement(currentQuestion);
            
            surveyChecksumBuf.append(SPACE).append(SPACE).append(SPACE).append(SPACE);
            surveyChecksumBuf.append("</question>");
            surveyChecksumBuf.append('\n');
        }
        else if(qName.equals("survey")){
            surveyChecksumBuf.append("</survey>");
        }
        
        tagStack.pop();
    }
    
    public void endDocument() throws SAXException {
        try {            
            survey.initVisitedArray();
        } catch (Exception ex) {
            Logger.getInstance().log(ex.getMessage());
        }
    }

}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.mobile.xmlhandle;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.logging.Logger;
import br.org.indt.ndg.mobile.sms.SMSReceiver;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Stack;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 *
 */
public class SMSProtocolSurveyHandler extends DefaultHandler{
    private final static String QUOTES = "\"";
    private final static char SPACE =  ' ';
    private static Hashtable codes;
    FileConnection conn;
    DataOutputStream out;
    
    private Stack tagStack = new Stack();
    public SMSProtocolSurveyHandler(String surveyDir){
        initTable();
        try {
            conn = (FileConnection) Connector.open(Resources.ROOT_DIR + surveyDir + "survey.xml");
            if(!conn.exists()){
                //conn.delete();
                conn.create();
            }
            //conn.create();
            out = conn.openDataOutputStream();
            String xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" + "\n";
            out.write(xmlHeader.getBytes("UTF-8"));            
        } catch (IOException ex) {            
            ex.printStackTrace();
        }
    }
    
    private void initTable(){
        codes = new Hashtable();
        codes.put("_dt", "_date");
        codes.put("_ch", "_choice");
        codes.put("_str", "_str");
        codes.put("_int", "_int");
        codes.put("_dc", "_decimal");
        codes.put("exc", "exclusive");
        codes.put("mul", "multiple");
        codes.put("_tm", "_time");
    }
    
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("sv")) {
            try {
                String id = attributes.getValue(attributes.getIndex("id"));
                String display = attributes.getValue(attributes.getIndex("_dp"));
                String deployed = attributes.getValue(attributes.getIndex("dpd"));
                String title = attributes.getValue(attributes.getIndex("ttl"));
                SMSReceiver.getInstance().setSurveyTitle(title);
                String checksum = attributes.getValue(attributes.getIndex("cks"));
                
                //System.out.println("Title: " + title);
                //System.out.println("Title 2: " + AppMIDlet.getInstance().u2x(title));
                
                StringBuffer surveyTag = new StringBuffer();
                surveyTag.append("<survey display=").append(QUOTES).append(display).append(QUOTES).append(SPACE);
                surveyTag.append("id=").append(QUOTES).append(id).append(QUOTES).append(SPACE);
                surveyTag.append("title=").append(QUOTES).append(AppMIDlet.getInstance().u2x(title)).append(QUOTES).append(SPACE);
                //surveyTag.append("title=").append(QUOTES).append(title).append(QUOTES).append(SPACE);
                surveyTag.append("deployed=").append(QUOTES).append(deployed).append(QUOTES).append(SPACE);
                surveyTag.append("checksum=").append(QUOTES).append(checksum).append(QUOTES);
                surveyTag.append(">");
                surveyTag.append('\n');
                
                out.write(surveyTag.toString().getBytes("UTF-8"));
                out.flush();                
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        else if (qName.equals("c")) {
            try {
                //questions = new Vector();
                String name = attributes.getValue(attributes.getIndex("nm"));
                String id = attributes.getValue(attributes.getIndex("id"));

                StringBuffer categoryTag = new StringBuffer();
                categoryTag.append(SPACE).append(SPACE);
                categoryTag.append("<category id=").append(QUOTES).append(id).append(QUOTES).append(SPACE);
                categoryTag.append("name=").append(QUOTES).append(AppMIDlet.getInstance().u2x(name)).append(QUOTES);
                categoryTag.append(">");
                categoryTag.append('\n');

                out.write(categoryTag.toString().getBytes("UTF-8"));
                out.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }      
        }
        else if (qName.equals("q")) {
            try {
                String type = attributes.getValue(attributes.getIndex("tp"));
                String id = attributes.getValue(attributes.getIndex("id"));
                String field = attributes.getValue(attributes.getIndex("fld"));
                String direction = attributes.getValue(attributes.getIndex("dtn"));
                String editable = attributes.getValue(attributes.getIndex("edb"));
                
                String questionType = (String) codes.get(type);

                StringBuffer questionTag = new StringBuffer();
                questionTag.append(SPACE).append(SPACE).append(SPACE).append(SPACE);
                questionTag.append("<question id=").append(QUOTES).append(id).append(QUOTES).append(SPACE);
                questionTag.append("type=").append(QUOTES).append(questionType).append(QUOTES).append(SPACE);
                questionTag.append("field=").append(QUOTES).append(field).append(QUOTES).append(SPACE);
                questionTag.append("direction=").append(QUOTES).append(direction).append(QUOTES).append(SPACE);
                questionTag.append("editable=").append(QUOTES).append(editable).append(QUOTES).append(SPACE);
                
                if(type.equals("_int") || type.equals("_dt") || type.equals("_dc")){
                    String min = attributes.getValue(attributes.getIndex("min"));
                    String max = attributes.getValue(attributes.getIndex("max"));
                    questionTag.append(SPACE).append("min=").append(QUOTES).append(min).append(QUOTES).append(SPACE);
                    questionTag.append("max=").append(QUOTES).append(max).append(QUOTES);
                }
                questionTag.append(">");
                questionTag.append('\n');

                out.write(questionTag.toString().getBytes("UTF-8"));
                out.flush();


            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        else if (qName.equals("i")) {
            try {
                String value = attributes.getValue(0);
                StringBuffer itemTag = new StringBuffer();
                itemTag.append(SPACE).append(SPACE).append(SPACE).append(SPACE).append(SPACE).append(SPACE);
                itemTag.append("<item otr=").append(QUOTES).append(AppMIDlet.getInstance().u2x(value)).append(QUOTES).append(">");

                out.write(itemTag.toString().getBytes("UTF-8"));
                out.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        //<sk catTo="1" operand="0" operator="1" skipTo="8"/>
        else if (qName.equals("sk")) {
            try {
                String catTo = attributes.getValue(attributes.getIndex("catTo"));
                String operand = attributes.getValue(attributes.getIndex("operand"));
                String operator = attributes.getValue(attributes.getIndex("operator"));
                String skipTo = attributes.getValue(attributes.getIndex("skipTo"));
                
                StringBuffer skipLogicTag = new StringBuffer();
                skipLogicTag.append(SPACE).append(SPACE).append(SPACE).append(SPACE).append(SPACE).append(SPACE);
                skipLogicTag.append("<SkipLogic catTo=").append(QUOTES).append(catTo).append(QUOTES).append(SPACE);
                skipLogicTag.append("operand=").append(QUOTES).append(operand).append(QUOTES).append(SPACE);
                skipLogicTag.append("operator=").append(QUOTES).append(operator).append(QUOTES).append(SPACE);
                skipLogicTag.append("skipTo=").append(QUOTES).append(skipTo).append(QUOTES);                
                skipLogicTag.append("/>");
                skipLogicTag.append('\n');

                out.write(skipLogicTag.toString().getBytes("UTF-8"));
                out.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
//        
        tagStack.push(qName);
    }
    
    public void characters(char[] ch, int start, int length) throws SAXException {
        String chars = new String(ch, start, length).trim();
        
        if (chars.length() > 0) {
            
            String qName = (String)tagStack.peek();
            
            if (qName.equals("d")){
                try {
                    StringBuffer descriptionTag = new StringBuffer();
                    descriptionTag.append(SPACE).append(SPACE).append(SPACE).append(SPACE).append(SPACE).append(SPACE);
                    descriptionTag.append("<description>").append(AppMIDlet.getInstance().u2x(chars)).append("</description>");
                    descriptionTag.append('\n');

                    out.write(descriptionTag.toString().getBytes("UTF-8"));
                    out.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            else if (qName.equals("l")) {
                try {
                    StringBuffer descriptionTag = new StringBuffer();
                    descriptionTag.append(SPACE).append(SPACE).append(SPACE).append(SPACE).append(SPACE).append(SPACE);
                    descriptionTag.append("<length>").append(chars).append("</length>");
                    descriptionTag.append('\n');

                    out.write(descriptionTag.toString().getBytes("UTF-8"));
                    out.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } 
            else if (qName.equals("s")) {
                String selectType = (String) codes.get(chars);
                try {
                    StringBuffer descriptionTag = new StringBuffer();
                    descriptionTag.append(SPACE).append(SPACE).append(SPACE).append(SPACE).append(SPACE).append(SPACE);
                    descriptionTag.append("<select>").append(selectType).append("</select>");
                    descriptionTag.append('\n');

                    out.write(descriptionTag.toString().getBytes("UTF-8"));
                    out.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } 
            else if (qName.equals("i")) {
               try {
                    StringBuffer itemTag = new StringBuffer();
                    //descriptionTag.append('\t').append('\t');
                    itemTag.append(chars).append("</item>");
                    itemTag.append('\n');

                    out.write(itemTag.toString().getBytes("UTF-8"));
                    out.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } 
        }
    }
    
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("sv")) {
            try {
                StringBuffer surveyTag = new StringBuffer();
                surveyTag.append("</survey>");
                out.write(surveyTag.toString().getBytes("UTF-8"));   
            }
            catch (IOException ex) {
                Logger.getInstance().log("Ex: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        else if(qName.equals("c")){
            try {
                StringBuffer categoryTag = new StringBuffer();
                categoryTag.append(SPACE).append(SPACE).append("</category>");
                categoryTag.append('\n');
                out.write(categoryTag.toString().getBytes("UTF-8"));   
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        else if(qName.equals("q")){
            try {
                StringBuffer questionTag = new StringBuffer();
                questionTag.append(SPACE).append(SPACE).append(SPACE).append(SPACE);
                questionTag.append("</question>");
                questionTag.append('\n');
                out.write(questionTag.toString().getBytes("UTF-8"));   
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        tagStack.pop();        
    }    
    
    public void endDocument() throws SAXException {
        try {
            out.close();
            conn.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}

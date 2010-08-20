package br.org.indt.ndg.mobile.xmlhandle;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.logging.Logger;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.sms.SMSReceiver;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import org.kxml2.io.*;
import org.xmlpull.v1.*;

public class kParserSMS {
    private boolean error = false;
    
    private final static String QUOTES = "\"";
    private final static char SPACE =  ' ';
    
    private String currentOther = "0";
    
    private StringBuffer surveyBuf;
    
    FileConnection conn;
    DataOutputStream out;
    private Hashtable codes;
    private Conversor conversor = new Conversor();
    
    public kParserSMS() {
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
 
            //Posiciona na tag <survey>
            parser.require(XmlPullParser.START_TAG, null, "survey");
            
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
    
    public void parserSurveyFile(String surveyDir, String survey) {
        surveyBuf = new StringBuffer();
        try {
            // Retrive InputStream from String
            ByteArrayInputStream is = new ByteArrayInputStream(survey.getBytes("UTF-8"));
            
            //Inicia o XMLParser
            KXmlParser parser = new KXmlParser();
            parser.setInput(new InputStreamReader(is, "UTF-8"));
            
            parser.nextTag();
 
            //Posiciona na tag <sv>
            parser.require(XmlPullParser.START_TAG, null, "sv");
            
            getSurveyAttributes(parser);
            
            //Enquanto é diferente de END_TAG
            while (parser.nextTag () != XmlPullParser.END_TAG)
            {
                //Posiciona na tag <c>
                parser.require(XmlPullParser.START_TAG, null, "c");

                parserCategory(parser);

                parser.require(XmlPullParser.END_TAG, null, "c");
            }
 
            parser.require(XmlPullParser.END_TAG, null, "sv");
            surveyBuf.append("</survey>");
            parser.next();

            parser.require(XmlPullParser.END_DOCUMENT, null, null);

            // Open new file to write
            try {
                conn = (FileConnection) Connector.open(Resources.ROOT_DIR + surveyDir + "survey.xml");
                if(!conn.exists()){
                    conn.create();
                }
                out = conn.openDataOutputStream();
                String xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" + "\n";
                out.write(xmlHeader.getBytes("UTF-8"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            
            //Write surveyBuf to file
            out.write(surveyBuf.toString().getBytes("UTF-8"));
        }
        catch(XmlPullParserException e) {
            Logger.getInstance().logException("XmlPullParserException[parserSurveyFile]: " + e.getMessage());
        }
        catch(Exception e) {
            Logger.getInstance().logException("Exception[parserSurveyFile]: " + e.getMessage());
        }
        finally {
            try {
                out.flush();
                out.close();
                conn.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void parserCategory(KXmlParser parser) throws Exception {
        getCategoryAttributes(parser);
        
        //Enquanto é diferente de </q>
        while (parser.nextTag() != XmlPullParser.END_TAG) {
            //Posiciona em uma tag "START". Ex: <q>
            parser.require(XmlPullParser.START_TAG, null, "q");
            
            parserQuestion(parser);

            //Posiciona no fim da tag </c>
            parser.require(XmlPullParser.END_TAG, null, "q");
        }
        surveyBuf.append(SPACE).append(SPACE);
        surveyBuf.append("</category>");
        surveyBuf.append('\n');
    }
    
    private void parserQuestion(KXmlParser parser) throws Exception {
        getQuestionAttributes(parser);
        
        String elemName = "";
        while (parser.nextTag() != XmlPullParser.END_TAG) {
            //Posiciona em uma tag "START". Ex: <description> <length> <item> <select> <SkipLogic>
            parser.require(XmlPullParser.START_TAG, null, null);
            elemName = parser.getName();
            String text = "";
            
            if (elemName.equals("d")){
                text = parser.nextText();
                
                surveyBuf.append(SPACE).append(SPACE).append(SPACE).append(SPACE).append(SPACE).append(SPACE);
                surveyBuf.append("<description>").append(conversor.uni2xmlEntity(text)).append("</description>");
                surveyBuf.append('\n');
            }
            else if (elemName.equals("l")) {
                text = parser.nextText();
                surveyBuf.append(SPACE).append(SPACE).append(SPACE).append(SPACE).append(SPACE).append(SPACE);
                surveyBuf.append("<length>").append(conversor.uni2xmlEntity(text)).append("</length>");
                surveyBuf.append('\n');
            }
            else if (elemName.equals("sk")) {
                String namespace = parser.getNamespace();
                String catTo = parser.getAttributeValue(namespace, "catTo");
                String operand = parser.getAttributeValue(namespace, "operand");
                String operator = parser.getAttributeValue(namespace, "operator");
                String skipTo = parser.getAttributeValue(namespace, "skipTo");

                text = parser.nextText();
                surveyBuf.append(SPACE).append(SPACE).append(SPACE).append(SPACE).append(SPACE).append(SPACE);
                surveyBuf.append("<SkipLogic catTo=").append(QUOTES).append(catTo).append(QUOTES).append(SPACE);
                surveyBuf.append("operand=").append(QUOTES).append(operand).append(QUOTES).append(SPACE);
                surveyBuf.append("operator=").append(QUOTES).append(operator).append(QUOTES).append(SPACE);
                surveyBuf.append("skipTo=").append(QUOTES).append(skipTo).append(QUOTES);                
                surveyBuf.append("/>");
                surveyBuf.append('\n');            
            }
            else if (elemName.equals("s")) {
                text = parser.nextText();
                surveyBuf.append(SPACE).append(SPACE).append(SPACE).append(SPACE).append(SPACE).append(SPACE);
                text = (String) codes.get(text);
                surveyBuf.append("<select>").append(conversor.uni2xmlEntity(text)).append("</select>");
                surveyBuf.append('\n');                
            }
            else if (elemName.equals("i")) {
                currentOther = parser.getAttributeValue(parser.getNamespace(), "_o");
                surveyBuf.append(SPACE).append(SPACE).append(SPACE).append(SPACE).append(SPACE).append(SPACE);
                surveyBuf.append("<item otr=").append(QUOTES).append(currentOther).append(QUOTES).append(">");

                text = parser.nextText();
                surveyBuf.append(conversor.uni2xmlEntity(text)).append("</item>");                
                surveyBuf.append('\n');
            }
            
            //Posiciona no fim da tag <description> <length> <item> <select> <SkipLogic>
            parser.require(XmlPullParser.END_TAG, null, elemName);
        }
        surveyBuf.append(SPACE).append(SPACE).append(SPACE).append(SPACE);
        surveyBuf.append("</question>");
        surveyBuf.append('\n');
    }
    
    private void getSurveyAttributes(KXmlParser parser) throws Exception {
        String namespace = parser.getNamespace();
        String id = parser.getAttributeValue(namespace, "id");
        String display = parser.getAttributeValue(namespace, "_dp");
        String deployed = parser.getAttributeValue(namespace, "dpd");
        String title = parser.getAttributeValue(namespace, "ttl");
        String checksum = parser.getAttributeValue(namespace, "cks");
        
        SMSReceiver.getInstance().setSurveyTitle(title);
        
        surveyBuf.append("<survey display=").append(QUOTES).append(display).append(QUOTES).append(SPACE);
        surveyBuf.append("id=").append(QUOTES).append(id).append(QUOTES).append(SPACE);
        surveyBuf.append("title=").append(QUOTES).append(conversor.uni2xmlEntity(title)).append(QUOTES).append(SPACE);
        surveyBuf.append("deployed=").append(QUOTES).append(deployed).append(QUOTES).append(SPACE);
        surveyBuf.append("checksum=").append(QUOTES).append(checksum).append(QUOTES);
        surveyBuf.append(">");
        surveyBuf.append('\n');
    }
    
    private void getCategoryAttributes(KXmlParser parser) throws Exception {
        String namespace = parser.getNamespace();
            
        String name = parser.getAttributeValue(namespace, "nm");
        String id = parser.getAttributeValue(namespace, "id");

        surveyBuf.append(SPACE).append(SPACE);
        surveyBuf.append("<category id=").append(QUOTES).append(id).append(QUOTES).append(SPACE);
        surveyBuf.append("name=").append(QUOTES).append(conversor.uni2xmlEntity(name)).append(QUOTES);
        surveyBuf.append(">");
        surveyBuf.append('\n');
    }
    
    private void getQuestionAttributes(KXmlParser parser) throws Exception {
        String namespace = parser.getNamespace();
        String field = parser.getAttributeValue(namespace, "fld");
        String direction = parser.getAttributeValue(namespace, "dtn");
        String editable = parser.getAttributeValue(namespace, "edb");

        String _type = parser.getAttributeValue(namespace, "tp");
        String _id = parser.getAttributeValue(namespace, "id");
        
        _type = (String) codes.get(_type);

        surveyBuf.append(SPACE).append(SPACE).append(SPACE).append(SPACE);
        surveyBuf.append("<question id=").append(QUOTES).append(_id).append(QUOTES).append(SPACE);
        surveyBuf.append("type=").append(QUOTES).append(_type).append(QUOTES).append(SPACE);
        surveyBuf.append("field=").append(QUOTES).append(field).append(QUOTES).append(SPACE);
        surveyBuf.append("direction=").append(QUOTES).append(direction).append(QUOTES).append(SPACE);
        surveyBuf.append("editable=").append(QUOTES).append(editable).append(QUOTES);
        

        if(_type.equals("_int") || _type.equals("_date") || _type.equals("_decimal")){
            String min = parser.getAttributeValue(namespace, "min");
            String max = parser.getAttributeValue(namespace, "max");
            surveyBuf.append(SPACE).append("min=").append(QUOTES).append(min).append(QUOTES).append(SPACE);
            surveyBuf.append("max=").append(QUOTES).append(max).append(QUOTES);
        }
        if(_type.equals("_time")) {
            String convention = parser.getAttributeValue(namespace, "convention");
            surveyBuf.append(SPACE).append("convention=").append(QUOTES).append(convention).append(QUOTES);
        }
        surveyBuf.append(">");
        surveyBuf.append('\n');
    }
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.mobile.download;

import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.xmlhandle.DoneParsingException;
import java.io.DataInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author MSS01
 */
public class SurveyHandler extends DefaultHandler {
    
    private static String SURVEY = "survey"; 
    private static String ID = "id";
    private static String TITLE = "title";
    private String surveyID;
    private String surveyTitle;

    public String getSurveyID() {
        return surveyID;
    }

    public String getSurveyTitle() {
        return surveyTitle;
    }
    
    
    /**
     * Start the Surveys List parse
     *
     * @param dis DataInputStream
     *
     * @throws SAXException - SAXException
     * @throws IOException - IOException
     * @throws ParserConfigurationException - ParserConfigurationException
     */
    public void parse(DataInputStream dis) throws SAXException, IOException, ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        factory.newSAXParser().parse(dis, this);
    }
    
    /**
     * Receives the element start notification. 
     * @param uri - The <code>URI</code> name.
     * @param localName - The local name
     * @param elementName - The of the element.
     * @param attributes - The <code>Atributes</code>.
     * @throws SAXException
     * @see org.xml.sax.helpers.DefaultHandler
     */
    
    public void startDocument() throws SAXException {

    }
    
    public final void startElement(final String uri, final String localName,
    final String elementName, final Attributes attributes) throws SAXException {
 
        if (elementName.equals(SURVEY)) {
    
            surveyID = attributes.getValue(ID);
            surveyTitle = attributes.getValue(TITLE);
    
            if (surveyID == null || "".equals(surveyID)) {
      
                throw new SAXException(Resources.EINVALID_SURVEYS);
            }
            throw new DoneParsingException();
        }
    }
    
    public void characters(char[] ch, int start, int length) throws SAXException {}
    
    public void endElement(String uri, String localName, String qName) throws SAXException {}
    
    public void endDocument() throws SAXException {
     
    }

}

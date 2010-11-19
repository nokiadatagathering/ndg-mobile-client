/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.mobile.download;

import br.org.indt.ndg.mobile.Resources;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author MSS01
 */
public class SurveysListHandler extends DefaultHandler {
    
    private static String SURVEY = "survey"; 
    private static String SURVEYS = "surveys";
    private static String ID = "id";
    private static String TITLE = "title";
    
    private boolean newFile = true;
    
    Vector surveysTitles;
    
    /**
     * Start the Surveys List parse
     *
     * @param dis DataInputStream
     *
     * @throws SAXException - SAXException
     * @throws IOException - IOException
     * @throws ParserConfigurationException - ParserConfigurationException
     */
    public String[] parse(DataInputStream dis) throws SAXException, IOException, ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
//        factory.setValidating(true);
        surveysTitles = new Vector();
        newFile = true;

        factory.newSAXParser().parse(dis, this);
        String[] result = new String[surveysTitles.size()];
        for (int i = 0; i < surveysTitles.size(); i++) {
            result[i] = (String)surveysTitles.elementAt(i);
            
        }
        return result;
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
    public final void startElement(final String uri, final String localName,
    final String elementName, final Attributes attributes) throws SAXException {
        if (newFile && !elementName.equals(SURVEYS)) {
            throw new SAXException(Resources.EINVALID_XML_FILE);
        }

        if (elementName.equals(SURVEY)) {
            String title = attributes.getValue(TITLE);
            if (title != null && !"".equals(title)) {
                surveysTitles.addElement(title);
            }
        } else if (elementName.equals(SURVEYS)) {
            newFile = false;
        }
    }
}

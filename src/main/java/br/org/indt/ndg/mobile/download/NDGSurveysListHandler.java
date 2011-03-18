package br.org.indt.ndg.mobile.download;

import br.org.indt.ndg.mobile.Resources;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class NDGSurveysListHandler extends SurveysListHandler {

    private static String SURVEY = "survey"; 
    private static String SURVEYS = "surveys";
    private static String TITLE = "title";

    public void startElement(final String uri, final String localName,
        final String elementName, final Attributes attributes) throws SAXException
    {
        if (newFile && !elementName.equals(SURVEYS)) {
            throw new SAXException(Resources.EINVALID_XML_FILE);
        }

        if (elementName.equals(SURVEY)) {
            String title = attributes.getValue(TITLE);
            if (title != null && !"".equals(title)) {
                m_surveysTitles.addElement(title);
            }
        } else if (elementName.equals(SURVEYS)) {
            newFile = false;
        }
    }
}

package br.org.indt.ndg.mobile.download;

import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.xmlhandle.DoneParsingException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class NDGSurveyHandler extends SurveyHandler {

    private final static String SURVEY = "survey";
    private final static String ID = "id";
    private final static String TITLE = "title";
    private String surveyID;
    private String surveyTitle;

    public String getSurveyID() {
        return surveyID;
    }

    public String getSurveyTitle() {
        return surveyTitle;
    }

    public void startElement(final String uri, final String localName,
        final String elementName, final Attributes attributes) throws SAXException
    {
         if (elementName.equals(SURVEY)) {
    
            surveyID = attributes.getValue(ID);
            surveyTitle = attributes.getValue(TITLE);
    
            if (surveyID == null || "".equals(surveyID)) {
      
                throw new SAXException(Resources.EINVALID_SURVEYS);
            }
            throw new DoneParsingException();
        }
    }

}

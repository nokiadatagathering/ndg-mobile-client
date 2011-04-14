package br.org.indt.ndg.mobile.download;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author MSS01
 */
public abstract class SurveysListHandler extends DefaultHandler {

    final protected Vector m_surveysTitles = new Vector();
    protected boolean newFile = true;

    public String[] parse(DataInputStream dis) throws SAXException, IOException, ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        newFile = true;

        factory.newSAXParser().parse(dis, this);
        String[] result = new String[m_surveysTitles.size()];
        for (int i = 0; i < m_surveysTitles.size(); i++) {
            result[i] = titlePrefix() + (String)m_surveysTitles.elementAt(i);
        }
        return result;
    }

    protected String titlePrefix() {
        return "[NDG]";
    }
}

/*
*  Nokia Data Gathering
*
*  Copyright (C) 2011 Nokia Corporation
*
*  This program is free software; you can redistribute it and/or
*  modify it under the terms of the GNU Lesser General Public
*  License as published by the Free Software Foundation; either
*  version 2.1 of the License, or (at your option) any later version.
*
*  This program is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
*  Lesser General Public License for more details.
*
*  You should have received a copy of the GNU Lesser General Public License
*  along with this program.  If not, see <http://www.gnu.org/licenses/
*/

package br.org.indt.ndg.mobile.download;

import br.org.indt.ndg.lwuit.model.XFormSurvey;
import java.util.Vector;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class XFormsSurveysListHandler extends SurveysListHandler {

    private String m_currentTag = null;
    private Vector m_surveys = new Vector(); //of XFormSurvey
    private XFormSurvey m_currentSurvey = null;

    private final static String NAME_TAG = "name";
    private final static String FORM_ID_TAG = "formID";
    private final static String DOWNLOAD_URL_TAG = "downloadUrl";
    private final static String MAJOR_VERSION_TAG = "majorMinorVersion";
    private final static String XFORM_TAG = "xform";


    public XFormSurvey[] getSurveysToDownload() {
        XFormSurvey[] surveysToDownload = new XFormSurvey[m_surveys.size()];
        m_surveys.copyInto(surveysToDownload);
        return surveysToDownload;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        m_currentTag = qName;
        if ( XFORM_TAG.equals(m_currentTag) ) {
            m_currentSurvey = new XFormSurvey();
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ( XFORM_TAG.equals(qName) ) {
            m_surveys.addElement(m_currentSurvey);
            m_currentSurvey = null;
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        String value = new String(ch, start, length).trim();
        if ( m_currentSurvey == null )
            return;

        if ( FORM_ID_TAG.equals(m_currentTag) ) {
            m_currentSurvey.setFormId(value);
        } else if (NAME_TAG.equals(m_currentTag)) {
            m_currentSurvey.setTitle(value);
            m_surveysTitles.addElement(value);
        } else if ( DOWNLOAD_URL_TAG.equals(m_currentTag) ) {
            m_currentSurvey.setDownloadUrl(value);
        } else if ( MAJOR_VERSION_TAG.equals(m_currentTag) ) {
            m_currentSurvey.setMajorVersion(value);
        }
        m_currentTag = "";
    }

    protected String titlePrefix() {
        return "[OpenRosa]";
    }
}

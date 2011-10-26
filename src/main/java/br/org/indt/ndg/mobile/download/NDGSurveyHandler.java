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

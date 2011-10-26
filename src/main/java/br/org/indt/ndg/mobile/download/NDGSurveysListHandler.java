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

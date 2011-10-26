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

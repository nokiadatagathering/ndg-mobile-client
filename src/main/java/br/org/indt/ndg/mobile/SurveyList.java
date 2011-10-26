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

package br.org.indt.ndg.mobile;

import java.util.Enumeration;
import java.util.Vector;

public class SurveyList {

    private FileSystem fs;
    private Vector surveyList;

    public SurveyList() {
        fs = AppMIDlet.getInstance().getFileSystem();
        surveyList = new Vector();
        Vector surveyNames = fs.SurveyNames();

        Enumeration e = surveyNames.elements();

        while (e.hasMoreElements()) {
            surveyList.addElement((String) e.nextElement());
        }
    }

    public Vector getList() {
        return surveyList;
    }

    public void setSurveyCurrentIndex( int index ) {
        fs.setSurveyCurrentIndex(index);
    }

    public void deleteSurvey() {
        String dirName = AppMIDlet.getInstance().getFileSystem().getSurveyDirName();
        fs.deleteSurveyDir(dirName);
    }
}

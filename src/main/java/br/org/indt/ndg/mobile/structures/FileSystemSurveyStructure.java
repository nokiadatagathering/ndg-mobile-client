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



package br.org.indt.ndg.mobile.structures;

import java.util.Vector;
import java.util.Enumeration;

public class FileSystemSurveyStructure {
    
    private Vector surveyDirNames;
    private Vector surveyNames;
    
    private int currentIndex;
    
    public FileSystemSurveyStructure() {
        surveyNames = new Vector();
        surveyDirNames = new Vector();
        
        currentIndex = 0;
    }
    
    public void setCurrentIndex(int _index) {
        currentIndex = _index;
    }
    
    public int getCurrentIndex() {
        return currentIndex; 
    }
    
    public void addFileName(String _file) {
        surveyDirNames.addElement(_file);
    }
    
    public void addName(String _name) {
        surveyNames.addElement(_name);
    }
    
    public Vector getNames() {
        return surveyNames;
    }
    
    public String getDirName() {
        return (String) surveyDirNames.elementAt(currentIndex);
    }
    
    public int getSurveysCount(){
        return surveyNames.size();
    }
    
    public void removeNameAndFileNameAtCurrentIndex() {
        surveyDirNames.removeElementAt(currentIndex);
        surveyNames.removeElementAt(currentIndex);
    }

    public String getCurrentName(){
        return (String)surveyNames.elementAt(currentIndex);
    }
}

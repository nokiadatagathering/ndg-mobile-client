

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
}

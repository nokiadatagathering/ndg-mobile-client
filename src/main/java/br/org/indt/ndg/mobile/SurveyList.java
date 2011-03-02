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

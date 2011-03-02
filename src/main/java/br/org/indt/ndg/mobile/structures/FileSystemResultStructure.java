package br.org.indt.ndg.mobile.structures;

import java.util.Vector;
import br.org.indt.ndg.mobile.Sorts;
import br.org.indt.ndg.mobile.XmlResultFile;

public class FileSystemResultStructure {
    
    private Vector xmlResultFile;
    
    private boolean isLocalFilename;
    private int currentIndex;
    
    public FileSystemResultStructure(){
        xmlResultFile = new Vector();
        
        setLocalFile(false);
    }
    
    private int LocateFileName(String _filename) {
        int nResult = -1;
        int len = xmlResultFile.size();
        for (int i = 0; i < len; i++)
        {
            XmlResultFile obj = (XmlResultFile) xmlResultFile.elementAt(i);
            if (_filename.equals(obj.getFileName()))
            {
                nResult = i;
                break;
            }
        }
        return nResult;
    }
    
    public void removeSelectedResult() {
        xmlResultFile.removeElementAt(currentIndex);
    }
    
    public void removeFile(String _filename) {
        removeDisplay(_filename);
    }
    
    public void removeDisplay(String _filename) {
        int index = LocateFileName(_filename);
        if (index != -1)
            xmlResultFile.removeElementAt(index);
    }
    
    public void reset() {
        xmlResultFile.removeAllElements();
    }
    
    public void sortDisplayNames() {
        Sorts sort = new Sorts();
        sort.qsort(xmlResultFile);
    }
    
    public Vector getXmlResultFile() {
        return xmlResultFile;
    }

    public String getFilename() {
        if (xmlResultFile.size() > 0) {
            XmlResultFile obj = (XmlResultFile) xmlResultFile.elementAt(currentIndex);
            return obj.getFileName();
        } else return null;
    }
    
    public String getFilename(int _index) {
        if (xmlResultFile.size() > 0) {
            XmlResultFile obj = (XmlResultFile) xmlResultFile.elementAt(_index);
            return obj.getFileName();
        } else return null;
    }

    public void addXmlResultFileObj(String _displayName, String _filename) {
        xmlResultFile.addElement(new XmlResultFile(_displayName, _filename));
        setCurrentIndex( xmlResultFile.size() - 1 );
    }

    public void setCurrentIndex(int _index) {
        currentIndex = _index;
    }
    
    public int getCurrentIndex() {
        return currentIndex;
    }
    
    public void setLocalFile(boolean _bool) { 
        isLocalFilename = _bool;
    }
    
    public boolean isLocalFile() {
        return isLocalFilename; 
    }
}

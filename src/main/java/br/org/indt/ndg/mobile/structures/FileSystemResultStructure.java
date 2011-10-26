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

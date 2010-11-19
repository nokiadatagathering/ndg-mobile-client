package br.org.indt.ndg.mobile;

import java.util.Vector;
import java.util.Enumeration;


public class ResultList {
    
    private FileSystem fs;
    private Vector xmlResultFile;
    final ResultList list;
    public Vector displayName;

    public ResultList() {
        list = this;
        fs = AppMIDlet.getInstance().getFileSystem();
        xmlResultFile = fs.getXmlResultFile();
        displayName = new Vector();
        Enumeration e = xmlResultFile.elements();
        while (e.hasMoreElements()) {
             displayName.addElement(((XmlResultFile) e.nextElement()).getDisplayName());
        }
    }

    public Vector getList()
    {
        return displayName;
    } 
}

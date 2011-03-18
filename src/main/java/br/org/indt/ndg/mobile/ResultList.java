package br.org.indt.ndg.mobile;

import br.org.indt.ndg.lwuit.model.Result;
import java.util.Vector;
import java.util.Enumeration;


public class ResultList {
    private FileSystem fs;
    private Vector xmlResultFile;
    public Vector displayName;

    public ResultList() {
        fs = AppMIDlet.getInstance().getFileSystem();
        xmlResultFile = fs.getXmlResultFile();
        displayName = new Vector();
        Enumeration e = xmlResultFile.elements();
        while (e.hasMoreElements()) {
             displayName.addElement( new Result(((XmlResultFile) e.nextElement()).getDisplayName()));
        }
    }

    public Vector getList() {
        return displayName;
    }
}

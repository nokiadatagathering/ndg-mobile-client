/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.FileSystem;
import br.org.indt.ndg.mobile.XmlResultFile;
import br.org.indt.ndg.lwuit.model.Result;
import java.util.Vector;

/**
 *
 * @author Alexandre Martini
 */
public class ResultControl {
    private static ResultControl instance = new ResultControl();

    private ResultControl(){}

    public static ResultControl getInstance() {
        return instance;
    }
    private FileSystem fs;
    private Vector xmlResultFile;

    public Result[] getResults(){
        fs = AppMIDlet.getInstance().getFileSystem();
        xmlResultFile = fs.getXmlResultFile();

        Result r;
        int totalResultsFromNDG = xmlResultFile.size();
        Result[] results = new Result[totalResultsFromNDG];
        for(int i = 0; i < totalResultsFromNDG; i++){
            r = new Result();
            r.setName(((XmlResultFile) xmlResultFile.elementAt(i)).getDisplayName());
            results[i] = r;
        }

        return results;
    }

    public Result[] getSentResults() {
        fs = AppMIDlet.getInstance().getFileSystem();
        xmlResultFile = fs.getXmlSentFile();

        Result r;
        XmlResultFile xmlResultFileObj;
        int totalResultsFromNDG = xmlResultFile.size();
        Result[] results = new Result[totalResultsFromNDG];
        for(int i = 0; i < totalResultsFromNDG; i++){
            xmlResultFileObj = (XmlResultFile) xmlResultFile.elementAt(i);
            r = new Result();
            r.setName(xmlResultFileObj.getDisplayName());
            r.setPhisicallyFileName( xmlResultFileObj.getFileName());
            results[i] = r;
        }
        return results;
    }

}

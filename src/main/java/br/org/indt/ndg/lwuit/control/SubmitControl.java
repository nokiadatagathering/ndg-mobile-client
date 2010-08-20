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
public class SubmitControl {
    private static SubmitControl instance = new SubmitControl();

    private SubmitControl(){}

    public static SubmitControl getInstance() {
        return instance;
    }
    private FileSystem fs;
    private Vector xmlResultFile;

    public Result[] getResults(){
        fs = AppMIDlet.getInstance().getFileSystem();
        xmlResultFile = fs.getXmlResultFile();

        //Enumeration e = xmlResultFile.elements();
        Result r;
        int totalResultsFromNDG = xmlResultFile.size();
        Result[] results = new Result[totalResultsFromNDG];
        for(int i = 0; i < totalResultsFromNDG; i++){
            r = new Result();
            r.setName(((XmlResultFile) xmlResultFile.elementAt(i)).getDisplayName());
            results[i] = r;
            //this.append(((XmlResultFile) e.nextElement()).getDisplayName(), null);
        }

        return results;
    }

}

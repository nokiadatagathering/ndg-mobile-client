/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.model;

import br.org.indt.ndg.mobile.multimedia.Base64Coder;
import java.io.IOException;
import java.io.PrintStream;

/**
 *
 * @author alexandre martini
 */
public class ImageQuestion extends Question{
    
    //IMPLEMENT
    public String getDisplayValue() throws Exception{
        return "getDisplayvalue";
    }

    public void save(PrintStream _output) {
        byte[] imageData = (byte[]) this.getAnswer().getValue();
        if (imageData!=null){
            try {
                _output.print("<img_data>");
                _output.write(new String(Base64Coder.encode(imageData)).getBytes("UTF-8"));
                _output.println("</img_data>");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public boolean passConstraints() {
        return true;
    }
}

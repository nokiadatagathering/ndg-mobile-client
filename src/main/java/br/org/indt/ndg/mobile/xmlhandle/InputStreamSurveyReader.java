/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.mobile.xmlhandle;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author mluz
 */
public class InputStreamSurveyReader extends InputStreamReader {

    private StringBuffer sb;


    public InputStreamSurveyReader(InputStream is, String enc) throws UnsupportedEncodingException, IOException  {
        super(is, enc);
        //sb = new StringBuffer();
    }

    public int read() throws IOException {
        int result = super.read();
        //sb.append((char)result);
        return result;
    }

    public int read(char[] cbuf, int off, int len) throws IOException {
        int result = super.read(cbuf, off, len);
        //sb.append(cbuf);
        return result;
    }

    public void show() {
        System.out.println(sb.toString());
    }
}

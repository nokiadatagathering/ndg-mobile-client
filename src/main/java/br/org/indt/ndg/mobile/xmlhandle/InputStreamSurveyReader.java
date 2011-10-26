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

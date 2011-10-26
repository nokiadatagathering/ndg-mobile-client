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

package br.org.indt.ndg.lwuit.model;

import br.org.indt.ndg.mobile.Utils;
import java.io.PrintStream;

public class DescriptiveAnswer extends NDGAnswer {

    public DescriptiveAnswer() {
        super();
        setValue("");
    }

    public void save( PrintStream _output ){
        String value = (String)getValue();
        if ( value!=null ){
            String temp;
            if(value.length() == 0) {
                temp = " ";
            }
            else {
                temp = Utils.u2x(value);
            }
        _output.print("<str>");
        _output.print(temp);
        _output.println("</str>");
        }
    }
}

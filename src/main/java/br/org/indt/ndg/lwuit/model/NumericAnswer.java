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

import java.io.PrintStream;

public abstract class NumericAnswer extends NDGAnswer {

    public NumericAnswer() {
        setValue("");
    }

    public void save( PrintStream _output ){
        String value = getValueString();

        if ( value!=null ) {
            _output.print("<" + getElementName() + ">");
            _output.print(value);
            _output.println("</" + getElementName() + ">");
        }
    }

    public abstract String getValueString();

    public abstract String getElementName();
}

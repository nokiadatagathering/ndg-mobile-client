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
import java.util.Date;

public class DateAnswer extends NDGAnswer {

    public DateAnswer() {
        super();
        Date date = new Date();
        Long datelong = new Long(date.getTime());
        setValue( String.valueOf( datelong.longValue() ) );
    }

    public void setDate(long _date) {
        setValue( String.valueOf( _date) );
    }

    public long getDate() {
        return Long.parseLong( (String)getValue() );
    }

    public void save( PrintStream _output ){
        String value = (String)getValue();
        if (value!=null) {
            _output.print("<date>");
            _output.print( value );
            _output.println("</date>");
        }
    }
}

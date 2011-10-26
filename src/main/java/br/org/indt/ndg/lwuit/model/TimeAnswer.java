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
import java.util.Calendar;
import java.util.Date;

public class TimeAnswer extends NDGAnswer {
    private long convention = 0;

    public TimeAnswer() {
        super();
        Date time = new Date();
        Long timelong = new Long(time.getTime());
        setValue( String.valueOf( timelong.longValue() ) );
    }

    public void setTime(long _time) {
        setValue( String.valueOf( _time ) );
    }

    public long getTime() {
        return Long.parseLong( (String)getValue());
    }

    /**
     * @return the convention
     */
    public long getAmPm24() {
        return convention;
    }

    /**
     * @param convention the convention to set
     */
    public void setAmPm24(long convention) {
        this.convention = convention;
    }

    public String getConvetionString() {
        if ( convention == TimeQuestion.AM ) {
            return "am";
        } else if ( convention == TimeQuestion.PM ) {
            return "pm";
        } else {
            return "24";
        }
    }

    public void save( PrintStream _output ){
        String value = (String)getValue();
        String value1 = convertLongToTimeString( getTime() );
        if (value!=null) {
            _output.print("<time>");
            _output.print(value1);
            _output.println("</time>");
        }
    }

     private String convertLongToTimeString(long _time) {
        String result = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(_time));
        String strHour = "" + cal.get(Calendar.HOUR);
        String strHourofDay = "" + cal.get(Calendar.HOUR_OF_DAY);
        String strMin = "" + cal.get(Calendar.MINUTE);

        if (this.convention == 1 || this.convention == 2) { // Se AM ou PM.
            if (cal.get(Calendar.HOUR) == 0) {
                result = "" + "12";
                if (strMin.length() == 1)
                    result += ":" + "0" + (cal.get(Calendar.MINUTE));
                else
                    result += ":" + (cal.get(Calendar.MINUTE));
            } else {
                if (strHour.length() == 1)
                    result = "0" + cal.get(Calendar.HOUR);
                else
                    result = "" + cal.get(Calendar.HOUR);
                    if (strMin.length() == 1)
                        result += ":" + "0" + cal.get(Calendar.MINUTE);
                    else
                        result += ":" + cal.get(Calendar.MINUTE);
            }
        } else { // Se 24 hours
                if (strHourofDay.length() == 1)
                    result = "0" + cal.get(Calendar.HOUR_OF_DAY);
                else
                    result = "" + cal.get(Calendar.HOUR_OF_DAY);
                    if (strMin.length() == 1)
                        result += ":" + "0" + cal.get(Calendar.MINUTE);
                    else
                        result += ":" + cal.get(Calendar.MINUTE);
        }

        return result;
    }
}

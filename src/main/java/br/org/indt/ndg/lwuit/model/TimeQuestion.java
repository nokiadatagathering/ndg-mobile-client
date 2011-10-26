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

import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import br.org.indt.ndg.mobile.NdgConsts;
import br.org.indt.ndg.mobile.Resources;
import java.util.Calendar;
import java.util.Date;

public class TimeQuestion extends NDGQuestion {

    private static final int MIDNIGHT = 24;
    public static final int AM = 1;
    public static final int PM = 2;

    private long low = NdgConsts.NOENTRY; // definição dos limites MIN e MAX.
    private long high = NdgConsts.NOENTRY;
    private long convention = NdgConsts.NOENTRY;
    private long am_pm = -1;

    private long parseDateLow(String _date) {  //timer format is hours:min ex: 01:30

        int hour = Integer.parseInt(_date.substring(0, 2));
        int minute = Integer.parseInt(_date.substring(3, 5)) - 1; //subtract 1 because range is 0-11
        //int year = Integer.parseInt(_date.substring(6));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, MIDNIGHT);
        calendar.set(Calendar.HOUR, hour);      //day before at midnight so date given is inclusive
        calendar.set(Calendar.MINUTE, minute);

        return calendar.getTime().getTime();
    }

    private long parseTimeHigh(String _time) {  //date format is day/month/year ex: 24/12/2007

        int hour = Integer.parseInt(_time.substring(0, 2));
        int minute = Integer.parseInt(_time.substring(3, 5)) - 1; //subtract 1 because range is 0-11

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.AM_PM, Calendar.PM);
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        calendar.set(Calendar.DAY_OF_MONTH, 31);      //day before at midnight so date given is inclusive
        calendar.set(Calendar.MONTH, 12);
        calendar.set(Calendar.YEAR, 2010);

        return calendar.getTime().getTime();
    }

    public void setHighConstraint(String _date) {
        try {
            high = parseTimeHigh(_date);
        }
        catch(RuntimeException re){
            high = NdgConsts.NOENTRY;
        }
    }

    public void setLowConstraint(String _date) {
        try {
            low = parseDateLow(_date);
        }
        catch(RuntimeException re){
            low = NdgConsts.NOENTRY;
        }
    }

    public void setConvention(String _time) {
        try {
            convention = Integer.parseInt(_time);
        }
        catch(Exception re){
            convention = NdgConsts.NOENTRY;
        }
    }

    public void setConvention(long _convention) {
        convention = _convention;
    }

    public long getConvention() {
        return convention;
    }

    private boolean passLowConstraint( long value ) {
       if (low == NdgConsts.NOENTRY) return true;
       else {
           if (value >= low) return true;
           else {
               GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
               GeneralAlert.getInstance().show(Resources.DATE, Resources.VALUE_GREATER + convertLongToTimeString(low), GeneralAlert.WARNING);
               return false;
           }
       }
    }

    private boolean passHighConstraint( long value ) {
        if (high == NdgConsts.NOENTRY) return true;
        else {
            if (value <= high) return true;
            else {
                GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
                GeneralAlert.getInstance().show(Resources.DATE, Resources.VALUE_LOWER + convertLongToTimeString(high), GeneralAlert.WARNING);
                return false;
            }
        }
    }

    public boolean passConstraints( long aTimeAnswer ) {
        if ( passLowConstraint( aTimeAnswer ) )
            if ( passHighConstraint( aTimeAnswer ) ) return true;
            else return false;
        else return false;
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

    /**
     * @return the am_pm
     */
    public long getAm_pm() {
        
        return am_pm;
    }

    /**
     * @param am_pm the am_pm to set
     */
    public void setAm_pm(long am_pm) {
        this.am_pm = am_pm;
    }

    public NDGAnswer getAnswerModel() {
        return new TimeAnswer();
    }
}

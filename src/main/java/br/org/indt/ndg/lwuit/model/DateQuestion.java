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

/**
 *
 * @author mluz
 */
public class DateQuestion extends NDGQuestion {

    private static final int MIDNIGHT = 24;
    private long low = NdgConsts.NOENTRY;
    private long high = NdgConsts.NOENTRY;

    private long parseDateLow(String _date) {  //date format is day/month/year ex: 24/12/2007

        int day = Integer.parseInt(_date.substring(0, 2));
        int month = Integer.parseInt(_date.substring(3, 5)) - 1; //subtract 1 because range is 0-11
        int year = Integer.parseInt(_date.substring(6));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, MIDNIGHT);
        calendar.set(Calendar.DAY_OF_MONTH, day);      //day before at midnight so date given is inclusive
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);

        return calendar.getTime().getTime();
    }

    private long parseDateHigh(String _date) {  //date format is day/month/year ex: 24/12/2007

        int day = Integer.parseInt(_date.substring(0, 2));
        int month = Integer.parseInt(_date.substring(3, 5)) - 1; //subtract 1 because range is 0-11
        int year = Integer.parseInt(_date.substring(6));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.AM_PM, Calendar.PM);
        calendar.set(Calendar.HOUR, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        calendar.set(Calendar.DAY_OF_MONTH, day);      //day before at midnight so date given is inclusive
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);

        return calendar.getTime().getTime();
    }

    public void setHighConstraint(String _date) {
        try{
            high = parseDateHigh(_date);
        }
        catch(RuntimeException re){
            high = NdgConsts.NOENTRY;
        }
    }

    public void setLowConstraint(String _date) {
        try{
            low = parseDateLow(_date);
        }
        catch(RuntimeException re){
            low = NdgConsts.NOENTRY;
        }
    }

    private boolean passLowConstraint( long value ) {
       if (low == NdgConsts.NOENTRY) return true;
       else {
           if (value >= low) return true;
           else {
               GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
               GeneralAlert.getInstance().show(Resources.DATE, Resources.VALUE_GREATER + convertLongToDateString(low), GeneralAlert.WARNING);
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
                GeneralAlert.getInstance().show(Resources.DATE, Resources.VALUE_LOWER + convertLongToDateString(high), GeneralAlert.WARNING);
                return false;
            }
        }
    }

    public boolean passConstraints( long dateAnswer ) {
        if (passLowConstraint( dateAnswer ))
            if (passHighConstraint( dateAnswer )) return true;
            else return false;
        else return false;
    }

    private String convertLongToDateString(long _date) {
        String result = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(_date));
        result = "" + cal.get(Calendar.DAY_OF_MONTH);
        result += "/" + (cal.get(Calendar.MONTH) + 1);
        result += "/" + cal.get(Calendar.YEAR);
        return result;
    }

    public NDGAnswer getAnswerModel() {
        return new DateAnswer();
    }
}

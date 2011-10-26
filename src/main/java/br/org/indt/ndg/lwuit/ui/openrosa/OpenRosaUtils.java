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

package br.org.indt.ndg.lwuit.ui.openrosa;

import br.org.indt.ndg.mobile.AppMIDlet;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author damian.janicki
 */
public class OpenRosaUtils {

    /**
     *
     * @param date
     * @return date string in formate choosed by user (DD/MM/YYYY)
     */
    public static String getUserFormatDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String retString = "";
        if(AppMIDlet.getInstance().getSettings().getStructure().getDateFormatId() == 2){
            retString = String.valueOf(month)+ "/" + String.valueOf(day) + "/" + String.valueOf(year);
        }else{
            retString = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
        }

        return retString;
    }

    /**
     *
     * @param date
     * @return string in XML format (YYYY-MM-DD)
     */
    public static String getStringFromDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return String.valueOf(year)+ "-" + String.valueOf(month)+ "-" + String.valueOf(day);
    }

    /**
     *
     * @param dateString - string with date in XML format (YYYY-MM-DD)
     * @return Date
     */
    public static Date getDateFromString(String dateString){
        String separator = null;
        if(dateString.indexOf("-") > 0){//XML date format
            separator = "-";
        }else if(dateString.indexOf("/") > 0){ //ODK builder format DD/MM/YYYY
            separator = "/";
        }else {
//            throw new UnsupportedDateFormatException();
            return null;
        }

        String fieldOne = "", fieldTwo = "", fieldThree = "";
        Date date = null;
        try {
            int firstSeparatorIndex = dateString.indexOf(separator);
            int secondSeparatorIndex = dateString.indexOf(separator, firstSeparatorIndex + 1);

            fieldOne = dateString.substring(0, firstSeparatorIndex);
            if (secondSeparatorIndex >= 0 && (firstSeparatorIndex + 1 < dateString.length())) {
                fieldTwo = dateString.substring(firstSeparatorIndex + 1, secondSeparatorIndex);
                fieldThree = dateString.substring(secondSeparatorIndex + 1, dateString.length());
            }

            int year;
            int month;
            int day;

            if(separator.equals("/")){
                month = Integer.parseInt(fieldOne);
                day= Integer.parseInt(fieldTwo);
                year = Integer.parseInt(fieldThree);
            }else{
                year = Integer.parseInt(fieldOne);
                month = Integer.parseInt(fieldTwo);
                day = Integer.parseInt(fieldThree);
            }

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month - 1);
            calendar.set(Calendar.DAY_OF_MONTH, day );

            date = calendar.getTime();

        } catch (Exception ex) {
        }
        return date;
    }
}

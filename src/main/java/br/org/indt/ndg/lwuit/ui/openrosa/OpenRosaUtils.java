package br.org.indt.ndg.lwuit.ui.openrosa;

import br.org.indt.ndg.mobile.AppMIDlet;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author damian.janicki
 */
public class OpenRosaUtils {

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

    public static String getStringFromDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return String.valueOf(year)+ "-" + String.valueOf(month)+ "-" + String.valueOf(day);
    }

    public static Date getDateFromString(String dateString) {

        String separator = "-";
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
            int year = Integer.parseInt(fieldOne);
            int month = Integer.parseInt(fieldTwo);
            int day = Integer.parseInt(fieldThree);

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

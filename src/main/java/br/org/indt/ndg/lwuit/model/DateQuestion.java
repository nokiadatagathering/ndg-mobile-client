/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.model;

import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import br.org.indt.ndg.mobile.Resources;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author mluz
 */
public class DateQuestion extends Question {

    private static final int MIDNIGHT = 24;
    private long low = Resources.NOENTRY;
    private long high = Resources.NOENTRY;

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
            high = Resources.NOENTRY;
        }
    }

    public void setLowConstraint(String _date) {
        try{
            low = parseDateLow(_date);
        }
        catch(RuntimeException re){
            low = Resources.NOENTRY;
        }
    }

    private boolean passLowConstraint() {
       if (low == Resources.NOENTRY) return true;
       else {
           long value = Long.parseLong((String) this.getAnswer().getValue());
           if (value >= low) return true;
           else {
               GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
               GeneralAlert.getInstance().show(Resources.DATE, Resources.VALUE_GREATER + convertLongToDateString(low), GeneralAlert.WARNING);
               return false;
           }
       }
    }

    private boolean passHighConstraint() {
        if (high == Resources.NOENTRY) return true;
        else {
            long value = Long.parseLong((String) this.getAnswer().getValue());
            if (value <= high) return true;
            else {
                GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
                GeneralAlert.getInstance().show(Resources.DATE, Resources.VALUE_LOWER + convertLongToDateString(high), GeneralAlert.WARNING);
                return false;
            }
        }
    }

    public boolean passConstraints() {
        if (passLowConstraint())
            if (passHighConstraint()) return true;
            else return false;
        else return false;
    }

    public void save(PrintStream _output){
        String value = (String)this.getAnswer().getValue();
        if (value!=null) {
             _output.print("<date>");
            _output.print(value);
            _output.println("</date>");
        }
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

}

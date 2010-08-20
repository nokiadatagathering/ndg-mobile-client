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

public class TimeQuestion extends Question {

    private static final int MIDNIGHT = 24;
    private long low = Resources.NOENTRY; // definição dos limites MIN e MAX.
    private long high = Resources.NOENTRY;

    private long convention = Resources.NOENTRY;
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
        try{
            high = parseTimeHigh(_date);
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

    public void setConvention(long _convention) {
        this.convention = _convention;
    }

    public long getConvention() {
        return this.convention;
    }

    private boolean passLowConstraint() {
       if (low == Resources.NOENTRY) return true;
       else {
           long value = Long.parseLong((String) this.getAnswer().getValue());
           if (value >= low) return true;
           else {
               GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
               GeneralAlert.getInstance().show(Resources.DATE, Resources.VALUE_GREATER + convertLongToTimeString(low), GeneralAlert.WARNING);
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
                GeneralAlert.getInstance().show(Resources.DATE, Resources.VALUE_LOWER + convertLongToTimeString(high), GeneralAlert.WARNING);
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
        String value1 = convertLongToTimeString(Long.parseLong((String) this.getAnswer().getValue()));
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

}

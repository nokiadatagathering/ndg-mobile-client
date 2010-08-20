package br.org.indt.ndg.mobile.structures.question;

import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.lwuit.extended.TimeField;
import java.io.PrintStream;
import java.util.Date;
import java.util.Calendar;

public class TypeTime extends Question {

    private TimeField dateWidget = null;
    private static final int MIDNIGHT = 24;
    private long low = Resources.NOENTRY;
    private long high = Resources.NOENTRY;
    private String strLow = "";
    private String strHigh = "";

    private String strConvention = "";
    private long convention = Resources.NOENTRY;
    private String hourConvention = "";

    public TypeTime() {
    }

    public TimeField getWidget() {
        if (isNew()) {
            Date currentDate = new Date();
            dateWidget = new TimeField(TimeField.HHMM);
            dateWidget.setTime(currentDate);

            setIsNew(false);
        }

        return dateWidget;
    }

    public void save(PrintStream _output, StringBuffer bfSMS){
        if (dateWidget!=null) {
            _output.print("<time>");
            _output.print(dateWidget.getTime().getTime());
            _output.println("</time>");
            bfSMS.append(dateWidget.getTime().getTime());
        }
    }

    private long parseTime(String _time) {  //time format is hh:mm ex: 10:45

        int hour = Integer.parseInt(_time.substring(0, 2));
        int minute = Integer.parseInt(_time.substring(3, 5)) - 1; //subtract 1 because range is 0-11

        Calendar calendar = Calendar.getInstance();
        //calendar.set(Calendar.HOUR_OF_DAY, MIDNIGHT);
        calendar.set(Calendar.HOUR, hour);      //day before at midnight so date given is inclusive
        calendar.set(Calendar.MINUTE, minute);
System.out.println(this.getClass().getName()+": #" );
        return calendar.getTime().getTime();
    }

    public String getLowConstraint(){
        return strLow;
    }

    public String getHighConstraint(){
        return strHigh;
    }

    public String getConvention(){
        return strConvention;
    }

    public void setConvention(String _time) {
       
        try{
            strConvention = _time;
            convention = parseTime(_time);
        }
        catch(RuntimeException re){
            convention = Resources.NOENTRY;
        }
    }
    
    public void setHighConstraint(String _date) {
        try{
            strHigh = _date;
            high = parseTime(_date);
        }
        catch(RuntimeException re){
            high = Resources.NOENTRY;
        }
    }

    public void setLowConstraint(String _date) {
        try{
            strLow = _date;
            low = parseTime(_date);
        }
        catch(RuntimeException re){
            low = Resources.NOENTRY;
        }
    }

    private boolean passLowConstraint() {
       if (low == Resources.NOENTRY) return true;
       else {
           long value = dateWidget.getTime().getTime();
           if (value >= low) return true;
           else return false;
       }
    }

    private boolean passHighConstraint() {
        if (high == Resources.NOENTRY) return true;
        else {
            long value = dateWidget.getTime().getTime();
            if (value <= high) return true;
            else return false;
        }
    }

    public boolean passConstraints() {
        if (passLowConstraint())
            if (passHighConstraint()) return true;
            else return false;
        else return false;
    }

    public String getDisplayValue() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR);
        int minute = cal.get(Calendar.MINUTE);
        String strMin = String.valueOf(minute);
        if(minute < 10)
            strMin = "0" + minute;
        cal.setTime(new Date());
        String time = dateWidget.getTime().toString().substring(0, 10) + " " + hour + ":" + strMin;        
        return time;
    }

    /**
     * @return the hourConvention
     */
    public String getHourConvention() {
        return hourConvention;
    }

    /**
     * @param hourConvention the hourConvention to set
     */
    public void setHourConvention(String hourConvention) {
        this.hourConvention = hourConvention;
    }
}

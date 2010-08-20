package br.org.indt.ndg.mobile.structures.question;

import br.org.indt.ndg.mobile.Resources;
import javax.microedition.lcdui.DateField;
import java.io.PrintStream;
import java.util.Date;
import java.util.Calendar;

public class TypeDate extends Question {
    
    private DateField dateWidget=null;
    private static final int MIDNIGHT = 24;
    private long low = Resources.NOENTRY;
    private long high = Resources.NOENTRY;   
    private String strLow = "";
    private String strHigh = "";

    public TypeDate() {
    }
    
    public DateField getWidget() {
        if (isNew()) {
            Date currentDate = new Date();
            dateWidget = new DateField("", DateField.DATE);
            dateWidget.setDate(currentDate);
            
            setIsNew(false);
        }
        
        return dateWidget;
    }

    public void save(PrintStream _output, StringBuffer bfSMS){
        if (dateWidget!=null) {
             _output.print("<date>");
            _output.print(dateWidget.getDate().getTime());
            _output.println("</date>");
            bfSMS.append(dateWidget.getDate().getTime());
        }
    }
    
    private long parseDate(String _date) {  //date format is day/month/year ex: 24/12/2007
        
        int day = Integer.parseInt(_date.substring(0, 2));        
        int month = Integer.parseInt(_date.substring(3, 5)) - 1; //subtract 1 because range is 0-11   
        int year = Integer.parseInt(_date.substring(6));
        
        Calendar calendar = Calendar.getInstance();     
        calendar.set(Calendar.HOUR_OF_DAY, MIDNIGHT);      
        calendar.set(Calendar.DAY_OF_MONTH, day-1);      //day before at midnight so date given is inclusive
        calendar.set(Calendar.MONTH, month);    
        calendar.set(Calendar.YEAR, year);

        return calendar.getTime().getTime();
    }

    public String getLowConstraint(){
        return strLow;
    }

    public String getHighConstraint(){
        return strHigh;
    }
    
    public void setHighConstraint(String _date) {
        try{
            strHigh = _date;
            high = parseDate(_date);
        }
        catch(RuntimeException re){
            high = Resources.NOENTRY;
        }
    }
    
    public void setLowConstraint(String _date) {
        try{
            strLow = _date;
            low = parseDate(_date);
        }
        catch(RuntimeException re){
            low = Resources.NOENTRY;
        }
    }
    
    private boolean passLowConstraint() {
       if (low == Resources.NOENTRY) return true;
       else {
           long value = dateWidget.getDate().getTime();
           if (value >= low) return true;
           else return false;
       }
    }
    
    private boolean passHighConstraint() {
        if (high == Resources.NOENTRY) return true;
        else {
            long value = dateWidget.getDate().getTime();
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
    
    public String getDisplayValue(){
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        String strMin = String.valueOf(minute);
        if(minute < 10)
            strMin = "0" + minute;
        cal.setTime(new Date());
        String date = dateWidget.getDate().toString().substring(0, 10) + " " + hour + ":" + strMin;
        return date;
    }
}

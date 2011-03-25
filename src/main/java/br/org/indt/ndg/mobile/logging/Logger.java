/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.mobile.logging;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

public class Logger {
    private static Logger instance = new Logger();
    private FileConnection logFile = null;
    private OutputStream output;
    private String message;
    
    
    private Logger(){
        
    }
    
    public static Logger getInstance(){
        return instance;
    }

    public String lastMessage(){
        return message;
    }
    private void openLogFile() {
        if (logFile == null) { 
            try {
                Calendar cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                String strDay = (day < 10 ? "0" + Integer.toString(day) : Integer.toString(day));
                int month = cal.get(Calendar.MONTH) + 1;
                String strMonth = (month < 10 ? "0" + Integer.toString(month) : Integer.toString(month));
                int year = cal.get(Calendar.YEAR);
                logFile = (FileConnection) Connector.open(Resources.ROOT_DIR + "logging_" + year + strMonth + strDay + ".txt");
                if(!logFile.exists()){                
                    logFile.create();
                }
                output = logFile.openOutputStream(logFile.fileSize());
            } catch (IOException ex) {
                getInstance().message = ex.getMessage();
                AppMIDlet.getInstance().setDisplayable(LoggerScreen.class);
                ex.printStackTrace();
            }  
        }
    }
    
    public void log(double n){
        log(String.valueOf(n), false);
    }
    
    public void log(String s){
        log(s, false);
    }
    public void log(String a, String b){
        log(a);
        log(b);
        log("\r\n");
    }
    public void logException(String s){
        logException(s, false);
    }
    
    private void log(String s, boolean close){
        boolean logSupport = AppMIDlet.getInstance().getSettings().getStructure().getLogSupport();
        
        if (logSupport)
        {
            openLogFile();
            try {
                output.write( (s + "\r\n").getBytes("UTF-8"));
                output.flush();

                if (close){
                    output.close();
                    logFile.close();
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void logException(String s, boolean close){
        openLogFile();
        try {
            output.write( (s + "\r\n").getBytes("UTF-8"));
            output.flush();

            if (close){
                output.close();
                logFile.close();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void emul(String message, Object o){
        System.out.println(message + o);
    }
    public void emul(String message, double n){
        System.out.println(message + n);
    }
    public void emul(String message){
        System.out.println(message);
    }

}

package br.org.indt.ndg.mobile;

import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import br.org.indt.ndg.lwuit.control.Display;
//import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Graphics;

public class SplashScreen extends Canvas {
    private Display     display;
    private Displayable next;
    private Timer       timer = new Timer();
    private int countDown = Resources.splashCountdown;
    public static final int thousand = 1000;
    private boolean cancelled = false;
    private Displayable noSurveyScreen = null;
    
    public SplashScreen(Display display, Displayable next) {
        this.display = display;
        this.next = next;
        
        //setSplashCountdown(AppMIDlet.getInstance().getSettings().getStructure().getSplashTime());
        
        this.setFullScreenMode(true);
        
        //display.setCurrent(this);
        //serviceRepaints();
    }
    
    public void setSplashCountdown(int _countdown) {
        countDown = _countdown;
    }
    
    protected void keyPressed(int keyCode){   
        //dismiss();
    }
    
    protected void paint(Graphics g){
        g.drawImage(Resources.splash, 0, 0, Graphics.TOP | Graphics.LEFT);
    }
    
    protected void pointerPressed( int x, int y ){        
        //dismiss();
    }
    
    protected void showNotify(){

        //if(!cancelled)
        //    timer.schedule(new CountDown(), /*countDown*thousand*/3000);
    }
    
    private void dismiss(){
        if(!cancelled){
            cancelled = true;
            timer.cancel();
                
            display.setCurrent(next);            
             
            if(AppMIDlet.getInstance().getFileSystem().getSurveysCount() == 0){
                try {
                    Thread.sleep(1500);                
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                showNoSurveyAlert();
            }
        }
    }
    
    private void showNoSurveyAlert(){ 
//        Alert noSurveysAlert = new Alert(Resources.NO_SURVEYS, Resources.THERE_ARE_NO_SURVEYS, null, AlertType.WARNING);
//        noSurveysAlert.setTimeout(3000);          
//        display.setCurrent(noSurveysAlert, next);
        //option 1
        //AppMIDlet.getInstance().getGeneralAlert().showAlert(Resources.NO_SURVEYS, Resources.THERE_ARE_NO_SURVEYS);
        //option 2
        noSurveyScreen = new NoSurveyScreen();
        display.setCurrent(noSurveyScreen);
    }

    public Displayable getNoSurveyScreen() {
        return noSurveyScreen;
    }
    
    private class CountDown extends TimerTask {
        public void run(){   
            if(!cancelled)
                dismiss();
        }
    }
}
class NoSurveyScreen extends Form implements CommandListener{    
    private Displayable current;
        
    public NoSurveyScreen(){
        super(Resources.NO_SURVEYS);        
        Display display = Display.getDisplay(AppMIDlet.getInstance());
        current = display.getCurrent();
        append(Resources.THERE_ARE_NO_SURVEYS + "!");
        addCommand(Resources.CMD_OK);
        setCommandListener(this);
    }

    public void commandAction(Command arg0, Displayable arg1) {
        if(arg0 == Resources.CMD_OK){
            AppMIDlet instance = AppMIDlet.getInstance();
            instance.setDisplayable(current);
        }
    }
    
}

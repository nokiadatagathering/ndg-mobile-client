package br.org.indt.ndg.mobile;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Form;

public class AlertSave extends Form implements CommandListener {

    private ChoiceGroup cg;
    
    public AlertSave() {
        super(Resources.SAVE_MODIFICATIONS);
        
        cg = new ChoiceGroup("", Choice.EXCLUSIVE);
        cg.append(Resources.YES, null);
        cg.append(Resources.NO, null);
        cg.setSelectedIndex(0, true);  //set YES to default
        
        this.append(cg);
        
        addCommand(Resources.CMD_OK);
        addCommand(Resources.CMD_CANCEL);
        
        this.setCommandListener(this);
    }
    
    public void commandAction(Command c, Displayable d) {
        if (c == Resources.CMD_OK) {
            if (cg.getSelectedIndex()==0) 
                AppMIDlet.getInstance().getFileSystem().saveResult(); //AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getCategoryList());
            else {
            AppMIDlet.getInstance().getFileStores().resetQuestions();  //need to reset questions - user may have loaded result file
            AppMIDlet.getInstance().setResultList(new ResultList());
            AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getResultList());
            }            
        }
        else if (c == Resources.CMD_CANCEL){
            AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getCategoryList());
        }
    }
    
}

package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.DeleteSurveyCommand;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

public class SurveyListContextMenu extends ContextMenu {

    public SurveyListContextMenu(int index, int size){
        super(index, size);
    }

    protected void buildOptions(){
        Command[] options  = new Command[] { DeleteSurveyCommand.getInstance().getCommand() };
        buildOptions(options);
    }

    protected void buildCommands(){
        String[] commands = new String[] {Resources.NEWUI_CANCEL, Resources.NEWUI_SELECT};
        buildCommands(commands);
    }

    protected void action(Command cmd){
        if (cmd == DeleteSurveyCommand.getInstance().getCommand()) {
            DeleteSurveyCommand.getInstance().execute(new Integer(indexList));
        }
    }

    public void show( int leftMargin, int topMargin ) {
        super.show(getHorizontalMargin(), topMargin );
    }

}

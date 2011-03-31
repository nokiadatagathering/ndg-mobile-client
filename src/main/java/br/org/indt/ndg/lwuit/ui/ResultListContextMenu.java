package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.DeleteResultNowCommand;
import br.org.indt.ndg.lwuit.control.OpenResultCommand;
import br.org.indt.ndg.lwuit.control.SendResultNowCommand;
import br.org.indt.ndg.lwuit.control.ViewResultCommand;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;


/**
 *
 * @author kgomes
 */
public class ResultListContextMenu extends ContextMenu{

    public ResultListContextMenu(int index, int size){
        super(index, size);
    }

    protected void buildOptions(){
        Command[] options  = new Command[] {SendResultNowCommand.getInstance().getCommand(),
                                            OpenResultCommand.getInstance().getCommand(),
                                            ViewResultCommand.getInstance().getCommand(),
                                            DeleteResultNowCommand.getInstance().getCommand()};
        buildOptions(options);
    }

    protected void buildCommands(){
        String[] commands = new String[] {Resources.NEWUI_CANCEL, Resources.NEWUI_SELECT};
        buildCommands(commands);
    }

    protected void action(Command cmd){
        if (cmd == OpenResultCommand.getInstance().getCommand()) {
            OpenResultCommand.getInstance().execute(new Integer(indexList));
        } else if (cmd == ViewResultCommand.getInstance().getCommand()) {
            ViewResultCommand.getInstance().execute(new Integer(indexList));
        } else if (cmd == SendResultNowCommand.getInstance().getCommand()) {
            boolean[] listFlags = new boolean[sizeList];
            listFlags[indexList] = true;
            SendResultNowCommand.getInstance().execute(listFlags);
        } else if (cmd == DeleteResultNowCommand.getInstance().getCommand()) {
            boolean[] listFlags = new boolean[sizeList];
            listFlags[indexList] = true;
            DeleteResultNowCommand.getInstance().execute(listFlags);
        }
    }

    public void show() {
        super.show(getHorizontalMargin(), getVerticalMargin()/2);
    }
}

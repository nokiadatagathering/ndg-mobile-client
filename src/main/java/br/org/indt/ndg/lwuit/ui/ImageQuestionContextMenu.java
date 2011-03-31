package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.OpenFileBrowserCommand;
import br.org.indt.ndg.lwuit.control.RemovePhotoCommand;
import br.org.indt.ndg.lwuit.control.ShowPhotoCommand;
import br.org.indt.ndg.lwuit.control.TakePhotoCommand;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;

public class ImageQuestionContextMenu extends ContextMenu{

    public ImageQuestionContextMenu(int index, int size){
        super(index, size);
    }

    protected void buildOptions(){
        Command[] options = null;
        if ( sizeList == 2) {
            options  = new Command[] { TakePhotoCommand.getInstance().getCommand(),
                                       OpenFileBrowserCommand.getInstance().getCommand()
                                       };
        } else {
            options  = new Command[] { TakePhotoCommand.getInstance().getCommand(),
                                       OpenFileBrowserCommand.getInstance().getCommand(),
                                       ShowPhotoCommand.getInstance().getCommand(),
                                       RemovePhotoCommand.getInstance().getCommand()
                                     };
        }
        buildOptions(options);
    }

    protected void buildCommands(){
        String[] commands = new String[] {Resources.NEWUI_CANCEL, Resources.NEWUI_SELECT};
        buildCommands(commands);
    }

    protected void action(Command cmd){
        if (cmd == TakePhotoCommand.getInstance().getCommand()) {
            TakePhotoCommand.getInstance().execute(null);
        } else if (cmd ==  OpenFileBrowserCommand.getInstance().getCommand()) {
            OpenFileBrowserCommand.getInstance().execute(null);
        } else if ( cmd == ShowPhotoCommand.getInstance().getCommand() ) {
            ShowPhotoCommand.getInstance().execute(null);
        } else if ( cmd == RemovePhotoCommand.getInstance().getCommand() ) {
            RemovePhotoCommand.getInstance().execute(null);
        }
    }

    public void show() {
        super.show(getHorizontalMargin()/2, getVerticalMargin()/2);
    }
}

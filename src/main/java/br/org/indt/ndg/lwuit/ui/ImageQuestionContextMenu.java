package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.OpenFileBrowserCommand;
import br.org.indt.ndg.lwuit.control.RemovePhotoCommand;
import br.org.indt.ndg.lwuit.control.ShowPhotoCommand;
import br.org.indt.ndg.lwuit.control.TakePhotoCommand;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Display;
import com.sun.lwuit.List;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.list.DefaultListModel;

public class ImageQuestionContextMenu extends ContextMenu{

    public ImageQuestionContextMenu(int index, int size){
        super(index, size);
    }

    protected void buildMenu() {
        buildOptions();
        buildCommands();
    }

    private void buildOptions(){
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

        optionsModel = new DefaultListModel(options);
        optionsList.setSelectedIndex(0);
        optionsModel.setSelectedIndex(0);
        optionsList.setModel(optionsModel);
        optionsList.setMinElementHeight( this.sizeList );

        optionsList.setListCellRenderer(new MenuCellRenderer());

        optionsList.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                menuDialog.dispose();
                List list = (List)evt.getSource();
                Command cmd = (Command)list.getSelectedItem();
                action(cmd);
            }
        });
        menuDialog.setAutoDispose(true);
        menuDialog.setScrollable(false);
        menuDialog.addComponent( BorderLayout.CENTER, optionsList);
    }

    private void buildCommands(){
        String[] commands = new String[] {Resources.NEWUI_CANCEL, Resources.NEWUI_SELECT};
        for(int i=0; i<commands.length; i++){
            Command cmd = new Command(commands[i]);
            menuDialog.addCommand(cmd);
        }
        /** Commands Listeners **/
        menuDialog.addCommandListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {

                Command cmd = evt.getCommand();
                if(cmd.getCommandName().equals(Resources.NEWUI_CANCEL)){
                    menuDialog.dispose();
                } else {
                    action((Command)optionsList.getSelectedItem());
                }
                
            }
        });
    }

    private void action(Command cmd){
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
        int fontHigh = NDGStyleToolbox.getInstance().menuStyle.selectedFont.getHeight();//this font is used in cell

        MenuCellRenderer rendererItem = (MenuCellRenderer)optionsList.getRenderer();
        int itemTopMargin = rendererItem.getStyle().getMargin(Component.TOP);
        int itemBottonMargin = rendererItem.getStyle().getMargin(Component.BOTTOM);
        int itemTopPadding = rendererItem.getStyle().getPadding(Component.TOP);
        int itemBottonPadding = rendererItem.getStyle().getPadding(Component.BOTTOM);

        int marginH = calculateMarginH() - optionsList.size() * ( fontHigh
                                                                + itemTopMargin
                                                                + itemBottonMargin
                                                                + itemTopPadding
                                                                + itemBottonPadding
                                                                + optionsList.getItemGap() )
                                                                - 2 * optionsList.getBorderGap();
        int marginW = calculateMarginW() ;
        menuDialog.show( marginH/2, marginH/2, marginW/2, marginW/2, true );
    }
}

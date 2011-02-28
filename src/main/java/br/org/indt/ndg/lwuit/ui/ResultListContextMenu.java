package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.DeleteResultNowCommand;
import br.org.indt.ndg.lwuit.control.OpenResultCommand;
import br.org.indt.ndg.lwuit.control.SendResultNowCommand;
import br.org.indt.ndg.lwuit.control.ViewResultCommand;
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


/**
 *
 * @author kgomes
 */
public class ResultListContextMenu extends ContextMenu{

    public ResultListContextMenu(int index, int size){
        super(index, size);
    }

    protected void buildMenu() {
        /** Options **/
        buildOptions();

        /** Commands **/
        buildCommands();

    }

    private void buildOptions(){

        Command[] options  = new Command[] {SendResultNowCommand.getInstance().getCommand(),
                                            OpenResultCommand.getInstance().getCommand(),
                                            ViewResultCommand.getInstance().getCommand(),
                                            DeleteResultNowCommand.getInstance().getCommand()};

        optionsModel = new DefaultListModel(options);
        optionsList.setModel(optionsModel);

        optionsList.setListCellRenderer(new MenuCellRenderer());

        optionsList.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                menuDialog.dispose();
                List list = (List)evt.getSource();
                Command cmd = (Command)list.getSelectedItem();
                action(cmd);
            }
            
        });

        menuDialog.addComponent(BorderLayout.CENTER, optionsList);
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
        
        menuDialog.addGameKeyListener(Display.GAME_LEFT, new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                menuDialog.dispose();
            }
        });
    }

    private void action(Command cmd){
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
        menuDialog.show( marginH/2, marginH/2,calculateMarginW(), 0, true );
    }
}

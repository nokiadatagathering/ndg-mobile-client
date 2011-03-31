package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.List;
import com.sun.lwuit.animations.CommonTransitions;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.list.DefaultListModel;
import com.sun.lwuit.list.ListModel;
import com.sun.lwuit.plaf.Border;

public abstract class ContextMenu {

    protected Dialog menuDialog = null;
    protected ListModel optionsModel = null;
    protected List optionsList = null;
    protected int indexList;
    protected int sizeList;

    protected int displayWidth, displayHeight;

    public ContextMenu(int index, int size){
        displayWidth = Display.getInstance().getDisplayWidth();
        displayHeight = Display.getInstance().getDisplayHeight();
        indexList = index;
        sizeList = size;

        menuDialog = new Dialog();
        menuDialog.setDialogStyle( NDGStyleToolbox.getInstance().menuStyle.getBaseStyle() );
        menuDialog.getDialogStyle().setBgColor( NDGStyleToolbox.getInstance().menuStyle.bgUnselectedColor );
        menuDialog.setAutoDispose(true);
        menuDialog.setScrollable(true);
        menuDialog.setLayout(new BorderLayout());

        optionsList = new List();
        optionsList.getStyle().setBorder(Border.createEmpty());
        optionsList.setFixedSelection(List.FIXED_NONE_CYCLIC);

        setTransitions();
        buildMenu();
    }

    public void show( int leftMargin, int topMargin ) {
        leftMargin += 5; // offset to avoid automatic selection of item that shows under the pointer
        int rightMargin = getHorizontalMargin() - leftMargin;
        int bottomMargin = getVerticalMargin() - topMargin;
        if ( bottomMargin < 0 ) { // check if not out off screen
            bottomMargin = 0;
            topMargin = getVerticalMargin();
        }
        if ( rightMargin < 0 ) { // check if not out off screen
            rightMargin = 0;
            leftMargin = getHorizontalMargin();
        }
        menuDialog.show( topMargin, bottomMargin,leftMargin, rightMargin, true );
    }

    public Dialog getMenuDialog(){
        return this.menuDialog;
    }

    public void setIndexList(int index) {
        this.indexList = index;
    }

    public void setSizeList(int size) {
        this.sizeList = size;
    }

    protected void buildMenu() {
        buildOptions();
        buildCommands();
    }

    protected abstract void buildOptions();

    protected abstract void buildCommands();

    protected abstract void action(Command command);

    protected final void buildCommands(String[] commands) {
        for ( int i=0; i<commands.length; i++ ) {
            Command cmd = new Command(commands[i]);
            menuDialog.addCommand(cmd);
        }

        menuDialog.addCommandListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Command cmd = evt.getCommand();
                menuDialog.dispose();
                if(cmd.getCommandName().equals(Resources.NEWUI_CANCEL)){
                    // do nothing
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

    protected final void buildOptions(Command[] options) {
        optionsModel = new DefaultListModel(options);
        optionsList.setModel(optionsModel);
        optionsList.setListCellRenderer(new MenuCellRenderer());
        optionsList.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if ( evt.getSource() instanceof List ) {
                    menuDialog.dispose();
                    List list = (List)evt.getSource();
                    Command cmd = (Command)list.getSelectedItem();
                    action(cmd);
                }
            }

        });
        menuDialog.addComponent(BorderLayout.CENTER, optionsList);
    }

    protected void setTransitions(){
        CommonTransitions ct = CommonTransitions.createSlide(CommonTransitions.SLIDE_HORIZONTAL, true, 600, false);
        CommonTransitions ct2 = CommonTransitions.createSlide(CommonTransitions.SLIDE_HORIZONTAL, false, 600, false);
        menuDialog.setTransitionInAnimator(ct);
        menuDialog.setTransitionOutAnimator(ct2);
    }

    /**
     * Calculates available hotizontal space that should be distributed for top and bottom margins
     * @return  hotizontal margin
     */
    protected int getHorizontalMargin() {
         return displayWidth - getMenuWidth();
    }

    /**
     * Calculates available vertical space that should be distributed for top and bottom margins
     * @return  vertical margin
     */
    protected int getVerticalMargin() {
        return displayHeight - getMenuHeight() - Display.getInstance().getCurrent().getSoftButton(0).getPreferredH();
    }

    protected int getMenuHeight() {
        int fontHigh = NDGStyleToolbox.getInstance().menuStyle.selectedFont.getHeight(); //this font is used in cell
        MenuCellRenderer rendererItem = (MenuCellRenderer)optionsList.getRenderer();
        int height = menuDialog.getStyle().getMargin( Component.BOTTOM )
                + menuDialog.getStyle().getMargin( Component.TOP )
                + menuDialog.getStyle().getPadding( Component.BOTTOM )
                + menuDialog.getStyle().getPadding( Component.TOP )
                + optionsList.getStyle().getMargin( Component.BOTTOM )
                + optionsList.getStyle().getMargin( Component.TOP )
                + optionsList.getStyle().getPadding( Component.BOTTOM )
                + optionsList.getStyle().getPadding( Component.TOP )
                + optionsList.size() * ( fontHigh
                                        + rendererItem.getStyle().getPadding(Component.BOTTOM)
                                        + rendererItem.getStyle().getPadding(Component.TOP)
                                        + rendererItem.getStyle().getMargin(Component.BOTTOM)
                                        + rendererItem.getStyle().getMargin(Component.TOP)
                                        + 2 * rendererItem.getStyle().getBorder().getThickness()
                                        + optionsList.getItemGap() )
                + menuDialog.getBottomGap()
                + 2 * optionsList.getBorderGap()
                + optionsList.getBottomGap()
                + 15; // magic number, could not find all height influencing factors
        return height;
    }

    protected int getMenuWidth() {
        String longestDesc = "";
        for ( int i = 0; i< optionsList.size(); i++ ) {
            String description = ((Command)optionsList.getModel().getItemAt(i)).getCommandName();
            longestDesc = description.length() < longestDesc.length() ? longestDesc : description;
        }
        MenuCellRenderer rendererItem = (MenuCellRenderer)optionsList.getRenderer();
        return optionsList.getStyle().getMargin( Component.LEFT )
                               + optionsList.getStyle().getMargin( Component.RIGHT )
                               + optionsList.getStyle().getPadding( Component.LEFT )
                               + optionsList.getStyle().getPadding( Component.RIGHT )
                               + menuDialog.getStyle().getMargin( Component.LEFT )
                               + menuDialog.getStyle().getMargin( Component.RIGHT )
                               + menuDialog.getStyle().getPadding( Component.LEFT )
                               + menuDialog.getStyle().getPadding( Component.RIGHT )
                               + NDGStyleToolbox.getInstance().menuStyle.selectedFont.stringWidth( longestDesc )
                               + rendererItem.getStyle().getMargin( Component.LEFT )
                               + rendererItem.getStyle().getMargin( Component.RIGHT )
                               + rendererItem.getStyle().getPadding( Component.LEFT )
                               + rendererItem.getStyle().getPadding( Component.RIGHT )
                               + 2 * optionsList.getBorderGap()
                               + optionsList.getSideGap()
                               + 3; // magic number, could not find all height influencing factors
    }
}

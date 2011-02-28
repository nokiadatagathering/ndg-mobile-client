package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.List;
import com.sun.lwuit.animations.CommonTransitions;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.list.ListModel;
import com.sun.lwuit.plaf.Border;

/**
 *
 * @author kgomes
 */
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

        /** Menu Dialog **/
        menuDialog = new Dialog();
        menuDialog.setDialogStyle( NDGStyleToolbox.getInstance().menuStyle.getBaseStyle() );
        menuDialog.getDialogStyle().setBgColor( NDGStyleToolbox.getInstance().menuStyle.bgUnselectedColor );
        menuDialog.setLayout(new BorderLayout());

        /** Options List **/
        optionsList = new List();
        optionsList.getStyle().setBorder(Border.createEmpty());
        optionsList.setFixedSelection(List.FIXED_NONE_CYCLIC);

        setTransitions();
        buildMenu();

    }

    protected abstract void buildMenu();

    protected void setTransitions(){
        CommonTransitions ct = CommonTransitions.createSlide(CommonTransitions.SLIDE_HORIZONTAL, true, 600, true);
        CommonTransitions ct2 = CommonTransitions.createSlide(CommonTransitions.SLIDE_HORIZONTAL, false, 600, true);
        menuDialog.setTransitionInAnimator(ct);
        menuDialog.setTransitionOutAnimator(ct2);
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

    public void show(int y, int height, int width) {
        if(sizeList>0){
            menuDialog.show(y,displayHeight - (y + height),displayWidth - width,0,true);
        }
    }

    protected int calculateMarginW() {
         String longestDesc = "";

         for ( int i = 0; i< optionsList.size(); i++ ) {
             String description = ((Command)optionsList.getModel().getItemAt(i)).getCommandName();
             longestDesc = description.length() < longestDesc.length() ? longestDesc : description;
         }
         MenuCellRenderer rendererItem = (MenuCellRenderer)optionsList.getRenderer();
         int itemLeftMargin = rendererItem.getStyle().getMargin( Component.LEFT );
         int itemRightMargin = rendererItem.getStyle().getMargin( Component.RIGHT );
         int itemLeftPadding = rendererItem.getStyle().getPadding( Component.LEFT );
         int itemRightPadding = rendererItem.getStyle().getPadding( Component.RIGHT );

         return  displayWidth  - optionsList.getStyle().getMargin( Component.LEFT )
                               - optionsList.getStyle().getMargin( Component.RIGHT )
                               - optionsList.getStyle().getPadding( Component.LEFT )
                               - optionsList.getStyle().getPadding( Component.RIGHT )
                               - menuDialog.getStyle().getMargin( Component.LEFT )
                               - menuDialog.getStyle().getMargin( Component.RIGHT )
                               - menuDialog.getStyle().getPadding( Component.LEFT )
                               - menuDialog.getStyle().getPadding( Component.RIGHT )
                               - NDGStyleToolbox.getInstance().menuStyle.selectedFont.stringWidth( longestDesc )
                               - itemLeftMargin
                               - itemRightMargin
                               - itemLeftPadding
                               - itemRightPadding
                               - 2 * optionsList.getBorderGap()
                               - optionsList.getSideGap()
                               - 3;
    }

    protected int calculateMarginH() {
        return  displayHeight - menuDialog.getStyle().getMargin( Component.TOP )
                              - menuDialog.getStyle().getMargin( Component.BOTTOM )
                              - menuDialog.getStyle().getPadding( Component.BOTTOM )
                              - menuDialog.getStyle().getPadding( Component.TOP )
                              - optionsList.getStyle().getMargin( Component.TOP )
                              - optionsList.getStyle().getMargin( Component.BOTTOM )
                              - optionsList.getStyle().getPadding( Component.TOP )
                              - optionsList.getStyle().getPadding( Component.BOTTOM )
                              - Display.getInstance().getCurrent().getSoftButton(0).getPreferredH();
    }
}

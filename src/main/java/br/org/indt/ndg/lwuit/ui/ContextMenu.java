/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui;

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

}

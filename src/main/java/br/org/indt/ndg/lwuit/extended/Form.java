/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.extended;

import com.sun.lwuit.Command;
import com.sun.lwuit.Dialog;

/**
 *
 * @author mluz
 */
public class Form extends com.sun.lwuit.Form {

    protected Command showMenuDialog(Dialog menu) {
        int minMargin = 30;
        int width = 224; // 320px width screen
        int width2 = 180; // 240px width screen
        int formWidth = Form.this.getWidth();
        int formHeight = Form.this.getHeight();

        int marginLeft = (formWidth > (width + minMargin)) ? (formWidth - width) /2 : (formWidth - width2)/2;
        int marginRight = marginLeft;

        //int calcHeight = 31 * getCommandCount()

        //int height = 59; // for 4 commands height
        //int height = 60 + 59; // for 3 cmds height

        //int height = 116 + 59;
        int sun = 0;
        int commandCount = getCommandCount();
        if (commandCount == 3)
            sun = 60;
        if (commandCount == 4)
            sun = 31;
        int height = 59 + sun;
        int height2 = 59 + 80 + sun;

        int top = (formHeight == 240) ? height : height2;

        return menu.show(top,0, marginLeft, marginRight, true);
    }


}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui;

import com.sun.lwuit.Component;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.animations.CommonTransitions;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.plaf.UIManager;

/**
 *
 * @author mluz
 */
public class WaitingScreen {

    private static WaitingScreen instance;
    private Dialog dialog;

    public static void show(String status) {
        UIManager.getInstance().getLookAndFeel().setDefaultDialogTransitionIn(null);
        UIManager.getInstance().getLookAndFeel().setDefaultDialogTransitionOut(null);
        instance = new WaitingScreen(status);
        instance.showModeless();
    }

    public static boolean isShowed() {
        if (instance != null)
            return instance.dialog.isVisible();
        else
            return false;
    }

    public static void dispose() {
        instance.dialog.dispose();
        UIManager.getInstance().getLookAndFeel().setDefaultDialogTransitionIn(CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, false, 500));
        UIManager.getInstance().getLookAndFeel().setDefaultDialogTransitionOut(CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, true, 500));
    }

    private void showModeless() {
        dialog.showPacked( BorderLayout.CENTER, false);
    }


    private WaitingScreen(String status) {
        dialog = new Dialog();
        dialog.setLayout(new BorderLayout());
        Image image = Screen.getRes().getAnimation("wait");

        Label label = new Label(image);
        label.setAlignment(Component.CENTER);

        TextArea msg = new TextArea();
        msg.setText(status);
        msg.setStyle(UIManager.getInstance().getComponentStyle("Label"));
        msg.setEditable(false);

        dialog.addComponent(BorderLayout.NORTH ,label);
        dialog.addComponent(BorderLayout.CENTER, msg);
    }
}

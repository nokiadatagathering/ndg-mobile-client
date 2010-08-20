/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui;

import com.sun.lwuit.Component;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.animations.CommonTransitions;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.layouts.BoxLayout;
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
        dialog.setPreferredSize(new Dimension(50,50));
        dialog.show(73,98,53,53,true, false);
    }


    public WaitingScreen(String status) {
        dialog = new Dialog();
        dialog.getStyle().setPadding(Component.TOP, 20);
        dialog.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        Image image = Screen.getRes().getAnimation("wait");
        Label l = new Label(image);
        l.setAlignment(Component.CENTER);
        l.setText("  "+status);
        dialog.addComponent(l);
    }

}

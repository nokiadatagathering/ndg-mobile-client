/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.SurveysControl;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Component;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.layouts.BoxLayout;

/**
 *
 * @author mturiel, kgomes
 */
public class WaitingForm extends Screen {
    private String status;
    private String title1;
    private String title2;

    public WaitingForm()
    {
    }

    public WaitingForm( String _title )
    {
        this.title1 = _title;
    }

    protected void loadData() {
        status = Resources.PROCESSING;
        title1 = SurveysControl.getInstance().getOpenedSurveyTitle();
        title2 = Resources.NEWUI_NOKIA_DATA_GATHERING;
    }

    protected void customize() {
        createScreen();
        setTitle(title1, title2);
        form.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        form.addComponent(new Label(" "));
        form.addComponent(new Label(" "));
        Image image = Screen.getRes().getImage("wait");
        Label l = new Label(image);
        l.setAlignment(Component.CENTER);
        l.setText("  "+status);
        form.addComponent(l);
    }

}

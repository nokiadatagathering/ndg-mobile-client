/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.SurveysControl;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;
import com.sun.lwuit.Container;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Font;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Label;
import com.sun.lwuit.Painter;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.plaf.Style;
import com.sun.lwuit.plaf.UIManager;

/**
 *
 * @author mturiel
 */
public class DetailsForm extends Screen implements ActionListener {

    private static DetailsForm df;
    private Dialog dialog;
    private Style dialogStyle = UIManager.getInstance().getComponentStyle("Dialog");
    private TitlePainter tp = new TitlePainter();

    private static String title;
    private static String label;
    private static String otrText;
    private Command cmdOk;

    private DescriptiveField tfDesc;

    protected void loadData() {
        title = Resources.MORE_DETAILS;
        cmdOk = new Command(br.org.indt.ndg.mobile.Resources.OK);
    }

    protected void customize() {
        dialog = new Dialog();
        dialog.getTitleStyle().setBgPainter(tp);
        dialog.setTitle(" ");
        dialog.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        Container c = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        Label l1 = new Label(label);
        c.addComponent(l1);
        tfDesc = new DescriptiveField(50);
        tfDesc.setText(otrText);
        tfDesc.setInputMode("Abc");
        tfDesc.setEditable(true);
        c.addComponent(tfDesc);
        dialog.addComponent(c);
        dialog.addCommand(cmdOk);
        dialog.setCommandListener(this);
    }

    public void actionPerformed(ActionEvent evt) {
        Object cmd = evt.getSource();

        if (cmd == cmdOk) {
            SurveysControl.getInstance().setItemOtherText(tfDesc.getText());
            otrText = tfDesc.getText();
            dialog.dispose();
        }
    }

    public static void show(String _label, String _otrText) {
        label = _label;
        otrText = _otrText;
        df = new DetailsForm();
        df.loadData();
        df.customize();
        df.showDialog();
    }

    public static void dispose() {
        df.dialog.dispose();
    }

    private void showDialog() {
        dialog.show(53, 53, 10, 10, true);
    }

    class TitlePainter implements Painter {

        private Style dialogTitleStyle = UIManager.getInstance().getComponentStyle("DialogTitle");

        public void paint(Graphics g, Rectangle rect) {
            int width = rect.getSize().getWidth();
            int height = rect.getSize().getHeight();

            int bgColor = dialogStyle.getBgColor();
            g.setColor(bgColor);
            g.fillRect(rect.getX(), rect.getY(), width, height);

            int endColor = dialogTitleStyle.getBgColor();
            int startColor = dialogTitleStyle.getBgSelectionColor();
            g.fillLinearGradient(startColor, endColor, rect.getX()+1, rect.getY()+1, width-3, height, false);


            // curve left side
            g.fillRect(rect.getX()+1, rect.getY()+1, 1, 1);
            //g.fillRect(rect.getX()+1, rect.getY()+1, 1, 2);

            // curve right side
            g.fillRect(rect.getX()+width-2, rect.getY()+1, 1, 1);
            //g.fillRect(rect.getX()+width-2, rect.getY()+1, 1, 2);

            int tintColor = UIManager.getInstance().getLookAndFeel().getDefaultFormTintColor();
            g.setColor(tintColor);
            g.fillRect(rect.getX(), rect.getY(), 1, 1, (byte) ((tintColor >> 24) & 0xff));
            g.fillRect(rect.getX()+width-1, rect.getY(), 1, 1, (byte) ((tintColor >> 24) & 0xff));

            Font titleFont = Screen.getRes().getFont("NokiaSansWideBold15");
            g.setFont(titleFont);
            g.setColor(dialogTitleStyle.getFgColor());
            g.drawString(title, rect.getX()+10, rect.getY()+5);

        }
    }

}

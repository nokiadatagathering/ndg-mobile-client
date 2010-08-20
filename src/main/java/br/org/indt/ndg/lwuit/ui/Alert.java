/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.SurveysControl;
import com.sun.lwuit.Command;
import com.sun.lwuit.Container;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Font;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.Painter;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.FlowLayout;
import com.sun.lwuit.plaf.Border;
import com.sun.lwuit.plaf.Style;
import com.sun.lwuit.plaf.UIManager;

/**
 *
 * @author mluz
 */
public class Alert {

    private static Alert alert;
    private Dialog dialog;
    private Style dialogStyle = UIManager.getInstance().getComponentStyle("Dialog");
    private TitlePainter tp = new TitlePainter();
    private Font msgFont = Screen.getRes().getFont("NokiaSansWide15");

    public static final int ERROR = 1001;
    public static final int CONFIRMATION = 1002;
    public static final int INFO = 1003;
    public static final int WARNING = 1004;
    public static final int ALARM = 1005;

    private String title;
    private String text;
    private int alertType;
    private Command[] cmds;

    public void loadData() {
        title = SurveysControl.getInstance().getTitleCurrentAlert();
        text = SurveysControl.getInstance().getTextCurrentAlert();
        alertType = SurveysControl.getInstance().getTypeCurrentAlert();
        cmds = SurveysControl.getInstance().getCommandsCurrentAlert();
    }

    private Image getIcon(int alertType) {
        Image img = null;
        switch (alertType) {
            case ERROR:         img = Screen.getRes().getImage("error"); break;
            case CONFIRMATION:  img = Screen.getRes().getImage("confirmation"); break;
            case INFO:  img = Screen.getRes().getImage("info"); break;
            case WARNING:  img = Screen.getRes().getImage("warning"); break;
            case ALARM:  img = Screen.getRes().getImage("warning"); break;
        }
        return img;
    }

    public void customize() {
        dialog = new Dialog();
        dialog.getTitleStyle().setBgPainter(tp);
        dialog.setTitle(" ");
        dialog.setLayout(new BorderLayout());
        Image img = getIcon(alertType);
        Container c = new Container(new FlowLayout());
        c.addComponent(new Label(img));
        dialog.addComponent(BorderLayout.WEST, c);
        TextArea msg = new TextArea(5,20);
        msg.setText(text);
        msg.setStyle(UIManager.getInstance().getComponentStyle("Label"));
        msg.getStyle().setFont(msgFont);
        int rows = msg.getLines();
        msg.setRows(rows);
        msg.setEditable(false);
        if (rows < 4) { // less than 4 lines needs a space label on top
            Image dialogspace = Screen.getRes().getImage("dialogspace");
            Label l = new Label(dialogspace);
            dialog.addComponent(BorderLayout.NORTH, l);
        }

        dialog.addComponent(BorderLayout.CENTER, msg);

        for (int i=0; i<cmds.length; i++) {
            dialog.addCommand(cmds[i]);
        }

        dialog.setScrollable(false);
        
    }

    public static void show() {
        UIManager.getInstance().getLookAndFeel().setReverseSoftButtons(false);
        alert = new Alert();
        alert.loadData();
        alert.customize();
        alert.showDialog();
        UIManager.getInstance().getLookAndFeel().setReverseSoftButtons(true);
    }

    public static void dispose() {
        alert.dialog.dispose();
    }

    private void showDialog() {
        dialog.show(53, 26, 53, 53, true);
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.CommandControl;
import com.sun.lwuit.Command;
import com.sun.lwuit.Container;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Font;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.Painter;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.FlowLayout;
import com.sun.lwuit.plaf.Style;
import com.sun.lwuit.plaf.UIManager;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author mturiel
 */
public class GeneralAlert extends Screen implements ActionListener {

    public static final int DIALOG_OK = 0;
    CommandControl cmdOk = new OkCommandControl();

    public static final int DIALOG_YES_NO = 1;
    CommandControl cmdYes = new YesCommandControl();
    CommandControl cmdNo = new NoCommandControl();

    public static final int RESULT_YES = 100;
    public static final int RESULT_NO = 101;

    private static GeneralAlert instance;
    private Dialog dialog;
    private Style dialogStyle = UIManager.getInstance().getComponentStyle("Dialog");
    private TitlePainter tp = new TitlePainter();

    private static String title;
    private static String label;
    private Hashtable hCommands = new Hashtable();

    private int resultCmdIndex;

    public static final int ERROR = 1001;
    public static final int CONFIRMATION = 1002;
    public static final int INFO = 1003;
    public static final int WARNING = 1004;
    public static final int ALARM = 1005;
    private int alertType;

    public static GeneralAlert getInstance() {
        if (instance == null)
            instance = new GeneralAlert();
        return instance;
    }

    public void addCommand(int _cmd, boolean bFirst) {
        if (bFirst) {
            hCommands.clear();
        }
        if (_cmd == DIALOG_OK) {
            hCommands.put(cmdOk, "");
        }
        else if (_cmd == DIALOG_YES_NO) {
            hCommands.put(cmdNo, "");
            hCommands.put(cmdYes, "");
        }
    }

    public void addCommand(CommandControl cmd, Object param) {
        hCommands.put(cmd, param);
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

    protected void loadData() {
    }

    protected void customize() {
        createScreen();
        dialog = new Dialog();
        dialog.getTitleStyle().setBgPainter(tp);
        dialog.setTitle(" ");

        dialog.setLayout(new BorderLayout());
        Image img = getIcon(alertType);
        Container c = new Container(new FlowLayout());
        c.addComponent(new Label(img));
        dialog.addComponent(BorderLayout.WEST, c);
        TextArea msg = new TextArea(5,22);
        msg.setText(label);
        msg.setStyle(UIManager.getInstance().getComponentStyle("Label"));
        //msg.getStyle().setFont(msgFont);
        int rows = msg.getLines();
        msg.setRows(rows);
        msg.setEditable(false);
        if (rows < 4) { // less than 4 lines needs a space label on top
            Image dialogspace = Screen.getRes().getImage("dialogspace");
            Label l = new Label(dialogspace);
            dialog.addComponent(BorderLayout.NORTH, l);
        }

        dialog.addComponent(BorderLayout.CENTER, msg);

//        dialog.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
//        Container c = new Container(new BoxLayout(BoxLayout.Y_AXIS));
//        Image img = getIcon(alertType);
//        dialog.addComponent(new Label(img));
//        dialog.addComponent(new Label(label));
//        dialog.addComponent(c);

        Enumeration commands = hCommands.keys();
        while (commands.hasMoreElements()) {
            dialog.addCommand(((CommandControl) commands.nextElement()).getCommand());
        }

        dialog.setCommandListener(this);
    }

    public void actionPerformed(ActionEvent evt) {
        Object cmd = evt.getSource();

        int nCmdIndex = 1;
        CommandControl cmdControl;
        Enumeration commands = hCommands.keys();
        while (commands.hasMoreElements()) {
            nCmdIndex++;
            cmdControl = (CommandControl) commands.nextElement(); /*CommandControl*/
            if (cmd == cmdControl.getCommand()) {
                // Set resultCmdIndexs
                if (cmdControl instanceof YesCommandControl) {
                    resultCmdIndex = RESULT_YES;
                }
                else if (cmdControl instanceof NoCommandControl) {
                    resultCmdIndex = RESULT_NO;
                }
                else {
                    resultCmdIndex = nCmdIndex;
                }
                cmdControl.execute(hCommands.get(cmdControl)/*param*/);
                dialog.dispose();
            }
        }
    }

    public int show(String _title, String _label, int _alertType) {
        UIManager.getInstance().getLookAndFeel().setReverseSoftButtons(false);
        title = _title;
        label = _label;
        alertType = _alertType;
        instance.loadData();
        instance.customize();
        instance.showDialog();
        UIManager.getInstance().getLookAndFeel().setReverseSoftButtons(true);
        return resultCmdIndex;
    }

    public static void dispose() {
        instance.dialog.dispose();
    }

    private void showDialog() {
        dialog.show(53, 26, 53, 38, true);
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

    class OkCommandControl extends CommandControl {

        protected Command createCommand() {
            return new Command(br.org.indt.ndg.mobile.Resources.OK);
        }

        protected void doAction(Object parameter) {
            
        }

    }

    class YesCommandControl extends CommandControl {

        protected Command createCommand() {
            return new Command(br.org.indt.ndg.mobile.Resources.YES);
        }

        protected void doAction(Object parameter) {

        }

    }

    class NoCommandControl extends CommandControl {

        protected Command createCommand() {
            return new Command(br.org.indt.ndg.mobile.Resources.NO);
        }

        protected void doAction(Object parameter) {

        }

    }

}

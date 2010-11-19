/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.CommandControl;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.error.NetworkErrCode;
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
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.layouts.FlowLayout;
import com.sun.lwuit.plaf.Style;
import com.sun.lwuit.plaf.UIManager;
import java.util.Enumeration;
import java.util.Vector;

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
    private Vector hCommands = new Vector();

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
            hCommands.removeAllElements();
        }
        if (_cmd == DIALOG_OK) {
            hCommands.addElement(cmdOk);
        }
        else if (_cmd == DIALOG_YES_NO) {
            hCommands.addElement(cmdYes);
            hCommands.addElement(cmdNo);
        }
    }

    public void addCommand(CommandControl cmd) {
        hCommands.removeAllElements();
        hCommands.addElement(cmd);
    }

    private Image getIcon(int alertType) {
        Image img = null;
        switch (alertType) {
            case ERROR:         img = Screen.getRes().getImage("error"); break;
            case CONFIRMATION:  img = Screen.getRes().getImage("confirmation"); break;
            case INFO:          img = Screen.getRes().getImage("info"); break;
            case WARNING:       img = Screen.getRes().getImage("warning"); break;
            case ALARM:         img = Screen.getRes().getImage("warning"); break;
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
        dialog.setScrollable(false);

        Container c = new Container(new FlowLayout());
        Image img = getIcon(alertType);
        Label imgLabel= new Label(img);
        imgLabel.setAlignment( Label.LEFT );
        c.addComponent(new Label(img));
        dialog.addComponent(BorderLayout.NORTH, c);
        c.setScrollable(false);

        Container c2 = new Container( new BoxLayout(BoxLayout.Y_AXIS));
        TextArea msg = new TextArea(2,12);
        msg.setText(label);
        msg.setStyle(UIManager.getInstance().getComponentStyle("Label"));
        msg.setEditable(false);

        int rows = msg.getLines();
        if( rows < 4)
        {
           c2.addComponent( new Label( Screen.getRes().getImage("dialogspace")));
        }
        msg.setRows(rows);
        c2.addComponent(msg);
        dialog.addComponent(BorderLayout.CENTER,c2);

        for ( int i = 0; i< hCommands.size(); i++ ) {
            dialog.addCommand( ((CommandControl)hCommands.elementAt(i)).getCommand());
        }

        dialog.setCommandListener(this);
    }

    public void actionPerformed(ActionEvent evt) {
        Object cmd = evt.getSource();

        int nCmdIndex = 1;
        CommandControl cmdControl;
        for (int i = 0; i< hCommands.size(); i++ ) {
            nCmdIndex++;
            cmdControl = (CommandControl) hCommands.elementAt(i);
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
                cmdControl.execute("");
                dialog.dispose();
            }
        }
    }
    
    public int show( Exception ex ) {
        return show(Resources.ERROR_TITLE, (ex.getMessage() == null) ? ex.toString() : ex.getMessage() ,GeneralAlert.ERROR );
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

    public int showCodedAlert(String _title, String _label, int _alertType) {
        return show( _title, NetworkErrCode.codeToString(_label), _alertType );
    }

    public static void dispose() {
        instance.dialog.dispose();
    }

    private void showDialog() {
        dialog.showPacked(BorderLayout.CENTER, true);
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
            g.drawString(title, rect.getX()+5, rect.getY()+3);

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

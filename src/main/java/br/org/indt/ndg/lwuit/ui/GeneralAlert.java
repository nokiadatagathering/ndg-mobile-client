package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.CommandControl;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.error.NetworkErrCode;
import com.sun.lwuit.Command;
import com.sun.lwuit.Container;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
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
import com.sun.lwuit.plaf.UIManager;
import java.util.Vector;

/**
 *
 * @author mturiel
 */
public class GeneralAlert extends Screen implements ActionListener {

    public static final int DIALOG_OK = 0;
    public static final int DIALOG_YES_NO = 1;
    public static final int RESULT_YES = 100;
    public static final int RESULT_NO = 101;
    public static final int ERROR = 1001;
    public static final int CONFIRMATION = 1002;
    public static final int INFO = 1003;
    public static final int WARNING = 1004;
    public static final int ALARM = 1005;

    CommandControl cmdOk = new OkCommandControl();
    CommandControl cmdYes = new YesCommandControl();
    CommandControl cmdNo = new NoCommandControl();

    private static GeneralAlert instance;
    private Dialog dialog;
    private TitlePainter tp = new TitlePainter();

    private static String title;
    private static String label;
    private Vector hCommands = new Vector();

    private int resultCmdIndex;


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
        dialog = new Dialog();
        dialog.getTitleStyle().setBgPainter(tp);
        dialog.setDialogStyle( NDGStyleToolbox.getInstance().menuStyle.getBaseStyle() );
        dialog.getDialogStyle().setBgColor( NDGStyleToolbox.getInstance().menuStyle.bgUnselectedColor );
        dialog.setTitle(" ");
        dialog.setLayout(new BorderLayout());

        Container c = new Container(new FlowLayout());
        Image img = getIcon(alertType);
        Label imgLabel= new Label(img);
        imgLabel.setAlignment( Label.LEFT );
        c.addComponent( imgLabel );
        dialog.addComponent(BorderLayout.NORTH, c);
        c.setScrollable(false);

        Container c2 = new Container( new BoxLayout(BoxLayout.Y_AXIS));

        int displayW = Display.getInstance().getDisplayWidth();
        int displayH = Display.getInstance().getDisplayHeight();

        TextArea msg = new TextArea( );
        msg.getSelectedStyle().setFgColor( NDGStyleToolbox.getInstance().menuStyle.unselectedFontColor );
        msg.getSelectedStyle().setFont( NDGStyleToolbox.getInstance().menuStyle.unselectedFont );
        msg.getSelectedStyle().setBgTransparency(0x00);
        int textWidth = msg.getSelectedStyle().getFont().stringWidth(label) + 5;
        int lineHeight = msg.getSelectedStyle().getFont().getHeight() + msg.getRowsGap();
        msg.setText(label);
        msg.setIsScrollVisible(false);
        msg.setEditable(false);

        if (textWidth >= displayW)
        {
            msg.setPreferredW( textWidth / ( textWidth/displayW + 1) + 2 );//to fill equally to all lines
            msg.setRows(textWidth / msg.getPreferredW() + 2);
            int preferredH = lineHeight * msg.getRows() + 2;
            if(preferredH > (displayH * 0.5) ) {
                msg.setIsScrollVisible(true);
                preferredH = (int) (  displayH * 0.5);
            }
            msg.setPreferredH(preferredH);
            msg.setGrowByContent(true);
        }
        else
        {
            msg.setGrowByContent(false);
            msg.setRows(1);
            msg.setPreferredW( msg.getSelectedStyle().getFont().stringWidth( label ) + 5 );
        }
        // WARN: the following line is important!
        // Setting it to false caused hard to track OutOfMemoryException
        dialog.setScrollable(true);

        c2.addComponent(msg);
        dialog.addComponent(BorderLayout.CENTER,c2);

        for ( int i = 0; i< hCommands.size(); i++ ) {
            dialog.addCommand( ((CommandControl)hCommands.elementAt(i)).getCommand());
        }

        dialog.addCommandListener(this);
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
        Container cont1 = dialog.getContentPane();
        int hi = 0;
        int wi = cont1.getPreferredW() + 2*5;
        int wi2 = dialog.getTitleStyle().getFont().stringWidth( title ) + 2*5 + TitlePainter.TITLE_LEFT_MARGIN;
        wi = Math.max(wi, wi2);

        for( int i = 0 ; i< dialog.getComponentCount() ; i ++ ){
            hi += dialog.getComponentAt(i).getPreferredH();
        }

        int disH = Display.getInstance().getDisplayHeight();
        int disW = Display.getInstance().getDisplayWidth();

        int H_Margin = hi < disH ? (disH - hi)/2 : 0;
        int V_Margin =  wi < disW ? (disW - wi)/2 : 0;
        dialog.show( H_Margin, H_Margin, V_Margin, V_Margin, true);
    }

    class TitlePainter implements Painter {
        static final public int TITLE_LEFT_MARGIN = 10;

        public void paint(Graphics g, Rectangle rect) {
            int width = rect.getSize().getWidth();
            int height = rect.getSize().getHeight();

            int bgColor = NDGStyleToolbox.getInstance().dialogTitleStyle.bgUnselectedColor;
            g.setColor(bgColor);
            g.fillRect(rect.getX(), rect.getY(), width, height);

            int endColor = NDGStyleToolbox.getInstance().dialogTitleStyle.bgSelectedEndColor;
            int startColor = NDGStyleToolbox.getInstance().dialogTitleStyle.bgSelectedStartColor;
            g.fillLinearGradient(startColor, endColor, rect.getX()+1, rect.getY()+1, width-2, height, false);

            // curve left side
            g.fillRect(rect.getX()+1, rect.getY()+1, 1, 1);

            // curve right side
            g.fillRect(rect.getX()+width-2, rect.getY()+1, 1, 1);

            int tintColor = UIManager.getInstance().getLookAndFeel().getDefaultFormTintColor();
            g.setColor(tintColor);
            g.fillRect(rect.getX(), rect.getY(), 1, 1, (byte) ((tintColor >> 24) & 0xff));
            g.fillRect(rect.getX()+width-1, rect.getY(), 1, 1, (byte) ((tintColor >> 24) & 0xff));

            g.setFont( NDGStyleToolbox.getInstance().dialogTitleStyle.unselectedFont );
            g.setColor( NDGStyleToolbox.getInstance().dialogTitleStyle.unselectedFontColor );
            g.drawString(title, rect.getX() + TITLE_LEFT_MARGIN, rect.getY()+5);
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

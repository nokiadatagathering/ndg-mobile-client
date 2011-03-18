package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.SurveysControl;
import br.org.indt.ndg.lwuit.extended.DescriptiveField;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;
import com.sun.lwuit.Container;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Painter;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.impl.midp.VirtualKeyboard;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.plaf.UIManager;

public class QuantityForm extends Screen implements ActionListener {

    private static QuantityForm df;
    private Dialog dialog;
    private TitlePainter tp = new TitlePainter();

    private static String title;
    private static String label;
    private static String otrText;
    private Command cmdOk;

    private DescriptiveField tfDesc;

    protected void loadData() {
        title = "Condition";//TODO localize
        cmdOk = new Command(br.org.indt.ndg.mobile.Resources.OK);
    }

    protected void customize() {
        dialog = new Dialog();
        dialog.setDialogStyle(NDGStyleToolbox.getInstance().menuStyle.getBaseStyle());
        dialog.setMenuCellRenderer(new MenuCellRenderer());
        dialog.getTitleStyle().setBgPainter(tp);
        dialog.setTitle(" ");
        dialog.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        Container c = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        c.setIsScrollVisible(false);

        c.addComponent( createWrappedText( label ) );
        tfDesc = new DescriptiveField(50);
        tfDesc.setText(otrText);
        tfDesc.setInputMode("123");
        tfDesc.setInputModeOrder(new String[]{"123"});
        tfDesc.setEditable(true);
        tfDesc.setFocus(true);
        tfDesc.requestFocus();
        c.addComponent(tfDesc);
        dialog.addComponent(c);
        dialog.addCommand(cmdOk);
        dialog.addCommandListener(this);


        if(Display.getInstance().isTouchScreenDevice()) {
            VirtualKeyboard vkExtendedOk= new VirtualKeyboard() {
                //trigger cmdOK when 'OK' button is pressed on virtual keyboard
                protected void actionCommand(Command cmd) {
                    super.actionCommand(cmd);
                    if (cmd.getId() == OK) {
                        okPressed();
                    }
                }
            };

			//TODO add 'Clear' button to virtual keyboard if possible
            VirtualKeyboard.bindVirtualKeyboard(tfDesc, vkExtendedOk);
        }
    }

    public void actionPerformed(ActionEvent evt) {
        Object cmd = evt.getSource();

        if (cmd == cmdOk) {
            okPressed();
        }
    }

    public static void show(String _label, String _otrText) {
        label = _label;
        otrText = _otrText;
        df = new QuantityForm();
        df.loadData();
        df.customize();
        df.showDialog();
    }

    public static void dispose() {
        df.dialog.dispose();
    }

    private void showDialog() {
        Container cont1 = dialog.getContentPane();
        int hi = 0;
        int wi = cont1.getPreferredW();

        for( int i = 0 ; i< dialog.getComponentCount() ; i ++ ){
            hi += dialog.getComponentAt(i).getPreferredH();
        }

        int disH = Display.getInstance().getDisplayHeight();
        int disW = Display.getInstance().getDisplayWidth();

        int H_Margin = hi < disH ? (disH - hi)/2 : 0;
        int V_Margin =  wi < disW ? (disW - wi)/2 : 0;
        dialog.show( H_Margin, H_Margin, V_Margin, V_Margin, true);
    }

    private void okPressed() {
            SurveysControl.getInstance().setItemOtherText(tfDesc.getText());
            otrText = tfDesc.getText();
            dialog.dispose();
    }

    private TextArea createWrappedText( String aText ) {
        TextArea questionName = new TextArea();
        questionName.setEditable(false);
        questionName.setFocusable(false);
        questionName.setColumns(20);
        questionName.setRows(1);
        questionName.setGrowByContent(false);
        questionName.setText(aText);

        int pw = Display.getInstance().getDisplayWidth();
        int w = questionName.getStyle().getFont().stringWidth(aText);
        if ( w >= pw ) {
            questionName.setGrowByContent(true);
            questionName.setRows(2);
        } else {
            questionName.setGrowByContent(false);
            questionName.setRows(1);
        }

        return questionName;
    }

    class TitlePainter implements Painter {

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
            g.drawString(title, rect.getX()+10, rect.getY()+5);
        }
    }
}

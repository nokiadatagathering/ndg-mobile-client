package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.BackUISettingsCommand;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Component;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import br.org.indt.ndg.lwuit.extended.RadioButton;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import com.sun.lwuit.Container;
import com.sun.lwuit.Font;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Label;
import com.sun.lwuit.Painter;
import com.sun.lwuit.events.FocusListener;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.plaf.Border;
import com.sun.lwuit.plaf.UIManager;

public class UISettingsFontSize extends Screen implements ActionListener {

    private RadioButton defaultFont;
    private RadioButton smallFont;
    private RadioButton mediumFont;
    private RadioButton largeFont;

    private static Painter focusBGPainter = new FocusBGPainter();

    protected void loadData() {
    }

    protected void customize() {
        setTitle(Resources.NEWUI_NOKIA_DATA_GATHERING, Resources.STYLES);
        form.removeAll();
        form.removeAllCommands();

        try{
            form.removeCommandListener(this);
        } catch (NullPointerException npe ) {
            //during first initialisation remove throws exception.
            //this ensure that we have registered listener once
        }
        form.addCommandListener(this);

        Label label = new Label(Resources.AVAILABLE_FONT_SIZE);
        label.getStyle().setFont( NDGStyleToolbox.fontSmall );
        label.setFocusable(false);
        form.addComponent(label);
        Container container = new Container( new BoxLayout( BoxLayout.Y_AXIS ) );
        container.getStyle().setBorder(Border.createRoundBorder(8, 8, UIManager.getInstance().getComponentStyle("RadioButton").getFgColor()));

        defaultFont = new RadioButton( Resources.DEFAULT );
        defaultFont.setSelected(false);
        defaultFont.getUnselectedStyle().setFont( NDGStyleToolbox.getFont( NDGStyleToolbox.FONTSANS, Font.SIZE_MEDIUM ) );
        defaultFont.getSelectedStyle().setFont( NDGStyleToolbox.getFont( NDGStyleToolbox.FONTSANS, Font.SIZE_MEDIUM ));
        defaultFont.removeFocusListener( defaultFont );
        defaultFont.addFocusListener( new RadioButtonFocusListener() );
        defaultFont.addActionListener(this);

        smallFont = new RadioButton( Resources.SMALL );
        smallFont.getUnselectedStyle().setFont( NDGStyleToolbox.getFont( NDGStyleToolbox.FONTSANS, Font.SIZE_SMALL ) );
        smallFont.getSelectedStyle().setFont( NDGStyleToolbox.getFont( NDGStyleToolbox.FONTSANS, Font.SIZE_SMALL ) );
        smallFont.removeFocusListener( smallFont );
        smallFont.addFocusListener( new RadioButtonFocusListener() );
        smallFont.addActionListener(this);

        mediumFont = new RadioButton( Resources.MEDIUM );
        mediumFont.getUnselectedStyle().setFont( NDGStyleToolbox.getFont( NDGStyleToolbox.FONTSANS, Font.SIZE_MEDIUM ) );
        mediumFont.getSelectedStyle().setFont( NDGStyleToolbox.getFont( NDGStyleToolbox.FONTSANS, Font.SIZE_MEDIUM ) );
        mediumFont.removeFocusListener( mediumFont );
        mediumFont.addFocusListener( new RadioButtonFocusListener() );
        mediumFont.addActionListener(this);

        largeFont = new RadioButton( Resources.LARGE );
        largeFont.getUnselectedStyle().setFont( NDGStyleToolbox.getFont( NDGStyleToolbox.FONTSANS, Font.SIZE_LARGE ) );
        largeFont.getSelectedStyle().setFont( NDGStyleToolbox.getFont( NDGStyleToolbox.FONTSANS, Font.SIZE_LARGE ) );
        largeFont.removeFocusListener( largeFont );
        largeFont.addFocusListener( new RadioButtonFocusListener() );
        largeFont.addActionListener(this);

        container.addComponent(defaultFont);
        container.addComponent(smallFont);
        container.addComponent(mediumFont);
        container.addComponent(largeFont);
        switch( NDGStyleToolbox.getInstance().fontSizeSetting ) {
            case NDGStyleToolbox.SMALL:
                smallFont.setSelected(true);
                break;
            case NDGStyleToolbox.MEDIUM:
                mediumFont.setSelected(true);
                break;
            case NDGStyleToolbox.LARGE:
                largeFont.setSelected(true);
                break;
            case NDGStyleToolbox.DEFAULT:
            default:
                defaultFont.setSelected(true);
                break;
        }
        form.addComponent(container);
        form.addCommand( BackUISettingsCommand.getInstance().getCommand() );
    }

    public void actionPerformed(ActionEvent evt) {
        Object cmd = evt.getSource();
        if ( cmd == BackUISettingsCommand.getInstance().getCommand() ) {
            BackUISettingsCommand.getInstance().execute(null);
        } else if( cmd instanceof RadioButton ) {
            Font newFont = null;
            if( cmd == defaultFont ) {
                smallFont.setSelected(false);
                mediumFont.setSelected(false);
                largeFont.setSelected(false);
                NDGStyleToolbox.getInstance().fontSizeSetting = NDGStyleToolbox.DEFAULT;
                NDGStyleToolbox.getInstance().saveSettings();
                NDGStyleToolbox.getInstance().loadSettings();
                form.show();
                return;
            } else if( cmd == smallFont ) {
                defaultFont.setSelected(false);
                mediumFont.setSelected(false);
                largeFont.setSelected(false);
                newFont = smallFont.getStyle().getFont();
                NDGStyleToolbox.getInstance().fontSizeSetting = NDGStyleToolbox.SMALL;
            } else if( cmd == mediumFont ) {
                defaultFont.setSelected(false);
                smallFont.setSelected(false);
                largeFont.setSelected(false);
                newFont = mediumFont.getStyle().getFont();
                NDGStyleToolbox.getInstance().fontSizeSetting = NDGStyleToolbox.MEDIUM;
            } else if( cmd == largeFont ) {
                defaultFont.setSelected(false);
                smallFont.setSelected(false);
                mediumFont.setSelected(false);
                newFont = largeFont.getStyle().getFont();
                NDGStyleToolbox.getInstance().fontSizeSetting = NDGStyleToolbox.LARGE;
            }
            NDGStyleToolbox.getInstance().listStyle.selectedFont =
                NDGStyleToolbox.getInstance().listStyle.unselectedFont =
                    NDGStyleToolbox.getInstance().listStyle.secondarySelectedFont =
                        NDGStyleToolbox.getInstance().listStyle.secondaryUnselectedFont =
                            NDGStyleToolbox.getInstance().menuStyle.selectedFont =
                                NDGStyleToolbox.getInstance().menuStyle.unselectedFont =
                                    NDGStyleToolbox.getInstance().dialogTitleStyle.selectedFont =
                                        NDGStyleToolbox.getInstance().dialogTitleStyle.unselectedFont =
                                            NDGStyleToolbox.getInstance().fontMedium =
                                                NDGStyleToolbox.getInstance().fontMediumBold =
                                                    NDGStyleToolbox.getInstance().fontSmall =
                                                        newFont;
            NDGStyleToolbox.getInstance().saveSettings();
            form.show();
        }
    }

    static class RadioButtonFocusListener implements FocusListener {

        public void focusGained(Component cmpnt) {
            cmpnt.getStyle().setBgPainter(focusBGPainter);
        }

        public void focusLost(Component cmpnt) {
        }
    }

    static class FocusBGPainter implements Painter {

        public void paint(Graphics g, Rectangle rect) {
            int width = rect.getSize().getWidth();
            int height = rect.getSize().getHeight();

            int endColor = NDGStyleToolbox.getInstance().listStyle.bgSelectedEndColor;
            int startColor = NDGStyleToolbox.getInstance().listStyle.bgSelectedStartColor;
            g.fillLinearGradient(startColor, endColor, rect.getX(), rect.getY(), width, height, false);

            int borderColor = NDGStyleToolbox.getInstance().listStyle.bgUnselectedColor;
            g.setColor(borderColor);
            g.fillRect(rect.getX(), rect.getY(), 1, 1);
            g.fillRect(rect.getX()+width-1, rect.getY(), 1, 1);
            g.fillRect(rect.getX(), rect.getY()+height-1, 1, 1);
            g.fillRect(rect.getX()+width-1, rect.getY()+height-1, 1, 1);
        }
    }
}


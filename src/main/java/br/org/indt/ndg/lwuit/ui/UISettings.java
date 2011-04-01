package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.SaveCustomUISettingsCommand;
import br.org.indt.ndg.lwuit.control.BackUISettingsCommand;
import br.org.indt.ndg.lwuit.control.Event;
import br.org.indt.ndg.lwuit.control.RestoreToCurrentUISettingCommand;
import br.org.indt.ndg.lwuit.control.RestoreToDefaultUISettingCommand;
import br.org.indt.ndg.lwuit.extended.NDGIntegerSpinner;
import br.org.indt.ndg.lwuit.ui.renderers.SimpleListCellRenderer;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import br.org.indt.ndg.lwuit.ui.style.PreviewStyleContainer;
import br.org.indt.ndg.lwuit.ui.style.StyleUtils;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.lwuit.extended.ComboBox;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.Label;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.DataChangedListener;
import com.sun.lwuit.events.FocusListener;
import com.sun.lwuit.events.SelectionListener;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.list.DefaultListModel;
import com.sun.lwuit.plaf.Border;
import com.sun.lwuit.plaf.Style;



public class UISettings extends Screen implements ActionListener, FocusListener, DataChangedListener {
    final private int LIST = 0;
    final private int MENU = 1;
    final private int DIALOGTITLE = 2;

    final private int BG_SELECTED = 0;
    final private int BG_UNSLECTED = 1;
    final private int FG_SELECTED = 2;
    final private int FG_UNSELECTED = 3;

    final private int RED = 0;
    final private int GREEN = 1;
    final private int BLUE = 2;

    final private int MAX_COLOR = 255;
    final private int MIN_COLOR = 0;

    private String title2 = Resources.UI_CUSTOMIZE;
    private String title1 = Resources.NEWUI_NOKIA_DATA_GATHERING;

    private Object[] customizedObjects = new Object[]{ Resources.LIST,
                                                       Resources.MENU,
                                                       Resources.DIALOG_TITLE };
    private Object[] customizedElements = new Object[]{ Resources.BG_SELECTED,
                                                        Resources.BG_UNSELECTED,
                                                        Resources.FONT_SELECTED,
                                                        Resources.FONT_UNSELECTED };
    private ComboBox comboElements;
    private ComboBox comboObjects;
    private NDGIntegerSpinner redSpinner;
    private NDGIntegerSpinner greenSpinner;
    private NDGIntegerSpinner blueSpinner;

    private Container listSettings;

    private Label selectedLabel;
    private Label unselectedLabel;
    private Style selectedStyle;
    private Style unselectedStyle;

    private PreviewStyleContainer styleCache;

    protected void loadData() {
        setTitle(title1, title2);
        form.removeAll();
        form.removeAllCommands();

        try{
            form.removeCommandListener(this);
        } catch (NullPointerException npe ) {
            //during first initialisation remove throws exception.
            //this ensure that we have registered listener once
        }

        styleCache =  new PreviewStyleContainer();
        styleCache.init();

        initUIComponents();

        form.addCommand( BackUISettingsCommand.getInstance().getCommand() );
        form.addCommand( RestoreToCurrentUISettingCommand.getInstance().getCommand() );
        form.addCommand( RestoreToDefaultUISettingCommand.getInstance().getCommand() );
        form.addCommand( SaveCustomUISettingsCommand.getInstance().getCommand() );

        form.addCommandListener(this);

        // fix: screen was not showing first element when revisiting this view
        comboObjects.requestFocus();
        form.scrollComponentToVisible(comboObjects);
    }

    protected void customize() {
        refreshPreviewStyle();
    }

    private void initUIComponents() {
        listSettings = new Container();
        listSettings.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        
        Label l1 = new Label( Resources.OBJECT );
        l1.setFocusable(false);

        Label l2 = new Label( Resources.ELEMENT );
        l2.setFocusable(false);

        comboObjects = new ComboBox();
        comboObjects.addFocusListener(this);
        comboObjects.setModel( new DefaultListModel( customizedObjects ) );
        comboObjects.setListCellRenderer( new SimpleListCellRenderer() );
        comboObjects.setSelectedIndex(0);
        comboObjects.addSelectionListener(  new ComboObjectsSelectionListener() );
        comboObjects.setNextFocusLeft(comboObjects);
        comboObjects.setNextFocusRight(comboObjects);

        comboElements = new ComboBox();
        comboElements.addFocusListener(this);
        comboElements.setModel( new DefaultListModel( customizedElements ) );
        comboElements.setListCellRenderer( new SimpleListCellRenderer() );
        comboElements.setSelectedIndex(0);
        comboElements.addSelectionListener( new ComboElementsSelectionListener() );
        comboElements.setNextFocusLeft(comboElements);
        comboElements.setNextFocusRight(comboElements);

        redSpinner = new NDGIntegerSpinner( MIN_COLOR, MAX_COLOR );
        redSpinner.addDataChangeObserver(this);
        greenSpinner = new NDGIntegerSpinner( MIN_COLOR, MAX_COLOR );
        greenSpinner.addDataChangeObserver(this);
        blueSpinner = new NDGIntegerSpinner( MIN_COLOR, MAX_COLOR );
        blueSpinner.addDataChangeObserver(this);

        Container previewMainContainer = new Container( new BorderLayout() );
        Container previewContainer = new Container( new BoxLayout(BoxLayout.X_AXIS));
        selectedLabel = new Label( Resources.SELECTED );
        selectedLabel.setPreferredW( Display.getInstance().getDisplayWidth()/2 );
        selectedLabel.setAlignment(Container.CENTER);
        selectedLabel.getStyle().setBgTransparency(0xff);
        selectedLabel.setFocusable(false);

        unselectedLabel = new Label( Resources.UNSELECTED );
        unselectedLabel.setPreferredW( Display.getInstance().getDisplayWidth()/2 );
        unselectedLabel.setAlignment(Container.CENTER);
        selectedLabel.getStyle().setBgTransparency( 0xff );
        unselectedLabel.setFocusable(false);

        previewContainer.addComponent(selectedLabel);
        previewContainer.addComponent(unselectedLabel);
        Label preview = new Label( Resources.PREVIEW );
        preview.setAlignment(Component.CENTER);
        preview.setPreferredW( Display.getInstance().getDisplayWidth() );
        previewMainContainer.addComponent(BorderLayout.NORTH, preview );
        previewMainContainer.addComponent(BorderLayout.CENTER, previewContainer);

        // FIX for problems with greedy focus on spinners
        redSpinner.setNextFocusUp(comboElements);
        blueSpinner.setNextFocusDown(comboObjects);

        listSettings.addComponent(l1);
        listSettings.addComponent(comboObjects);
        listSettings.addComponent(l2);
        listSettings.addComponent(comboElements);
        listSettings.addComponent(previewMainContainer);
        listSettings.addComponent(redSpinner);
        listSettings.addComponent(greenSpinner);
        listSettings.addComponent(blueSpinner);
        form.addComponent(listSettings);

        registerEvent(new RepaintSpinners(), ON_SHOW);
    }

    public void focusGained(Component cmpnt){
        cmpnt.getStyle().setBorder(Border.createBevelLowered( NDGStyleToolbox.getInstance().focusGainColor,
                                                              NDGStyleToolbox.getInstance().focusGainColor,
                                                              NDGStyleToolbox.getInstance().focusGainColor,
                                                              NDGStyleToolbox.getInstance().focusGainColor ), false);
        cmpnt.refreshTheme();
    }

    public void focusLost(Component cmpnt) {
        cmpnt.getStyle().setBorder(Border.createBevelLowered( NDGStyleToolbox.getInstance().focusLostColor,
                                                              NDGStyleToolbox.getInstance().focusLostColor,
                                                              NDGStyleToolbox.getInstance().focusLostColor,
                                                              NDGStyleToolbox.getInstance().focusLostColor ), false);
        cmpnt.refreshTheme();
    }

    public void actionPerformed(ActionEvent evt) {
        Object cmd = evt.getSource();

        if ( cmd == SaveCustomUISettingsCommand.getInstance().getCommand() ) {
            SaveCustomUISettingsCommand.getInstance().execute( styleCache );
            evt.consume();
        } else if ( cmd == BackUISettingsCommand.getInstance().getCommand() ) {
            BackUISettingsCommand.getInstance().execute( null );
            evt.consume();
        } else if ( cmd == RestoreToCurrentUISettingCommand.getInstance().getCommand() ) {
            RestoreToCurrentUISettingCommand.getInstance().execute( null );
            evt.consume();
        } else if ( cmd == RestoreToDefaultUISettingCommand.getInstance().getCommand() ) {
            RestoreToDefaultUISettingCommand.getInstance().execute( null );
        }
    }

    private void updateStyle() {
      int currentRedLevel = redSpinner.getValue();
      int currentGreenLevel = greenSpinner.getValue();
      int currentBlueLevel = blueSpinner.getValue();

      switch( comboElements.getSelectedIndex() ) {
            case BG_SELECTED:
                selectedLabel.getStyle().setBgColor( StyleUtils.mixRGB( currentRedLevel, currentGreenLevel, currentBlueLevel ) );
                selectedLabel.repaint();
                break;
            case BG_UNSLECTED:
                unselectedLabel.getStyle().setBgColor( StyleUtils.mixRGB( currentRedLevel, currentGreenLevel, currentBlueLevel ) );
                unselectedLabel.repaint();
                break;
            case FG_SELECTED:
                selectedLabel.getStyle().setFgColor( StyleUtils.mixRGB( currentRedLevel, currentGreenLevel, currentBlueLevel ) );
                selectedLabel.repaint();
                break;
            case FG_UNSELECTED:
                unselectedLabel.getStyle().setFgColor( StyleUtils.mixRGB( currentRedLevel, currentGreenLevel, currentBlueLevel ) );
                unselectedLabel.repaint();
                break;
        }
    }

    private void refreshPreviewStyle() {
        unselectedLabel.repaint();
        selectedLabel.repaint();
    }

    private void LoadValues() {
        int color = 0;
        switch( comboElements.getSelectedIndex() ) {
            case BG_SELECTED:
                color = selectedLabel.getStyle().getBgColor();
                break;
            case BG_UNSLECTED:
                color = unselectedLabel.getStyle().getBgColor();
                break;
            case FG_SELECTED:
                color = selectedLabel.getStyle().getFgColor();
                break;
            case FG_UNSELECTED:
                color = unselectedLabel.getStyle().getFgColor();
                break;
        }
        int [] rgb = new int[3];
        StyleUtils.getRGB(color, rgb);

        redSpinner.setValue(rgb[RED]);
        greenSpinner.setValue(rgb[GREEN]);
        blueSpinner.setValue(rgb[BLUE]);
        refreshPreviewStyle();
    }

    public void dataChanged(int i, int i1) {
        updateStyle();
    }

    class ComboObjectsSelectionListener implements SelectionListener {

        public void selectionChanged(int oldValue , int newValue) {
            switch( newValue ) {
                case LIST:
                    selectedStyle = styleCache.listSelectedStyle;
                    unselectedStyle = styleCache.listUnselectedStyle;
                    break;
                case MENU:
                    selectedStyle = styleCache.menuSelectedStyle;
                    unselectedStyle = styleCache.menuUnselectedStyle;
                    break;
                case DIALOGTITLE:
                    selectedStyle = styleCache.dialogTitleSelectedStyle;
                    unselectedStyle = styleCache.dialogTitleUnselectedStyle;
                    break;
            }
            selectedLabel.setUnselectedStyle(selectedStyle);
            unselectedLabel.setUnselectedStyle(unselectedStyle);
            LoadValues();
        }
    }

    class ComboElementsSelectionListener implements SelectionListener {

        public void selectionChanged(int oldValue , int newValue) {
            LoadValues();
        }
    }

    class RepaintSpinners extends Event {

        public RepaintSpinners() {
        }

        protected void doAction(Object parameter) {
            redSpinner.repaint();
            greenSpinner.repaint();
            blueSpinner.repaint();
        }
    }
}

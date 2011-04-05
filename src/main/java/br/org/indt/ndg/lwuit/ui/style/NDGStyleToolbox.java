package br.org.indt.ndg.lwuit.ui.style;

import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import br.org.indt.ndg.lwuit.ui.Screen;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.xmlhandle.Parser;
import com.sun.lwuit.Font;
import com.sun.lwuit.plaf.Style;
import com.sun.lwuit.plaf.UIManager;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class NDGStyleToolbox {
    public static final int DEFAULT = 0;
    public static final int SMALL = 1;
    public static final int MEDIUM = 2;
    public static final int LARGE = 3;

    public static final String FONTSANS = "NokiaSansWide";
    public static final String FONTSANSBOLD = "NokiaSansWideBold";
    public static final int MAX_DEFINED_FONT_SIZE = 30;
    public static final int MIN_DEFINED_FONT_SIZE = 11;

    public static NDGStyleToolbox instance;

    public ListStyleProxy listStyle;
    public MenuStyleProxy menuStyle;
    public DialogTitleStyleProxy dialogTitleStyle;
    public int fontSizeSetting;

    public int focusGainColor;
    public int focusLostColor;

    public Style unselectedStyle = new Style();
    public Style selectedStyle = new Style();

    static public Font fontMediumBold;
    static public Font fontMedium;
    static public Font fontSmall;

    static public int smallSize;
    static public int mediumSize;
    static public int largeSize;

    private NDGStyleToolbox() {
        smallSize = Font.createSystemFont( Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL ).getHeight();
        mediumSize = Font.createSystemFont( Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM ).getHeight();
        largeSize = Font.createSystemFont( Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE ).getHeight();

        listStyle = new ListStyleProxy();
        menuStyle = new MenuStyleProxy();
        dialogTitleStyle = new DialogTitleStyleProxy();
        initFonts();

        focusGainColor = UIManager.getInstance().getComponentStyle("").getFgColor();
        focusLostColor = UIManager.getInstance().getComponentStyle("Form").getFgColor();

        fontSizeSetting = DEFAULT;
    }

    public void initFonts() {
        fontMedium = getFont(FONTSANS, mediumSize);
        fontSmall = getFont(FONTSANS, smallSize);
        fontMediumBold = getFont(FONTSANSBOLD, mediumSize);
        listStyle.updateFonts();
        menuStyle.updateFonts();
        dialogTitleStyle.updateFonts();
    }

    public static NDGStyleToolbox getInstance() {
        if( instance == null ) {
            instance = new NDGStyleToolbox();
        }
        return instance;
    }

    public void reset() {
        instance = new NDGStyleToolbox();
    }

    public void saveSettings() {
        String filename = Resources.ROOT_DIR + Resources.STYLE_FILE;

        try {
            FileConnection connection = (FileConnection) Connector.open(filename);
            if(!connection.exists()) connection.create();
            else {
                connection.delete();
                connection.create();
            }

            OutputStream out = connection.openOutputStream();
            PrintStream output = new PrintStream(out);

            output.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            output.print("<settings fontSize=\"" + fontSizeSetting +"\">");
            output.println("<style name=\"" + "list" + "\">" );
            listStyle.writeSettings(output);
            output.println("</style>");

            output.println("<style name=\"" + "menu" + "\">" );
            menuStyle.writeSettings(output);
            output.println("</style>");

            output.println("<style name=\"" + "dialogTitle" + "\">" );
            dialogTitleStyle.writeSettings(output);
            output.println("</style>");

            output.println("</settings>");

            output.close();
            out.close();
            connection.close();

        } catch(IOException e) {
            GeneralAlert.getInstance().addCommand( GeneralAlert.DIALOG_OK, true);
            GeneralAlert.getInstance().show(e);
        }
    }

    public void loadSettings() {
        try {
            StyleHandler sh = new StyleHandler();
            sh.setStyleStructure(this);
            Parser parser = new Parser(sh);
            parser.parseFileNoClose(Resources.ROOT_DIR + Resources.STYLE_FILE);
            applayFontSetting();
        } catch (SAXException ex) {
            GeneralAlert.getInstance().addCommand( GeneralAlert.DIALOG_OK, true );
            GeneralAlert.getInstance().show( Resources.LOADING_STYLE,
                                             Resources.LOADING_STYLE_ERROR,
                                             GeneralAlert.WARNING );
        } catch (IOException ex) {
            //there is no style config file
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        }
    }

    static public Font getFont( String aBaseName, int size ) {
        Font font = null;
        for( int i = size; size <= MAX_DEFINED_FONT_SIZE; i++) {
            font = Screen.getRes().getFont( aBaseName + i );
            if( font != null ){
                return font;
            }
        }
        for( int i = size; size >= MIN_DEFINED_FONT_SIZE ; i--) {
            font = Screen.getRes().getFont( aBaseName + i );
            if( font != null ){
                return font;
            }
        }
        if( font == null ) {
            font = Font.createSystemFont( Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM );
        }
        return font;
    }

    private void applayFontSetting() {
        Font newFont = null;
        switch( fontSizeSetting ) {
            case SMALL:
                newFont = getFont( FONTSANS, smallSize );
                break;
            case MEDIUM:
                newFont = getFont( FONTSANS, mediumSize );
                break;
            case LARGE:
                newFont = getFont( FONTSANS, largeSize );
                break;
            case DEFAULT:
            default:
                initFonts();
                return;
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
    }
}

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

    public int questionPreviewColor;
    public int answerPreviewColor;

    public Style unselectedStyle = new Style();
    public Style selectedStyle = new Style();

    static public Font fontMediumBold;
    static public Font fontMedium;
    static public Font fontSmall;

    static private int smallSize;
    static private int mediumSize;
    static private int largeSize;

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
        questionPreviewColor = UIManager.getInstance().getComponentStyle("PreviewQuestion").getFgColor();
        answerPreviewColor = UIManager.getInstance().getComponentStyle("PreviewAnswer").getFgColor();

        fontSizeSetting = DEFAULT;
    }

    public final void initFonts() {
        fontMedium = getFont( FONTSANS, Font.SIZE_MEDIUM );
        fontSmall = getFont( FONTSANS, Font.SIZE_SMALL );
        fontMediumBold = getFont( FONTSANSBOLD, Font.SIZE_MEDIUM );
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

    public static Font getFont( String aBaseName, int sizeEnum ) {
        int size = size2Height( sizeEnum );
        Font font = null;

        if( Screen.getFontRes() != null ) {
            for( int i = size; i <= MAX_DEFINED_FONT_SIZE; i++) {
                font = Screen.getFontRes().getFont( aBaseName + i );
                if( font != null ){
                    return font;
                }
            }
            for( int i = size; i >= MIN_DEFINED_FONT_SIZE ; i--) {
                font = Screen.getFontRes().getFont( aBaseName + i );
                if( font != null ){
                    return font;
                }
            }
        }
        if( font == null ) {
            font = Font.createSystemFont( Font.FACE_SYSTEM, getStyle( aBaseName ), sizeEnum );
        }
        return font;
    }

    private static int size2Height( int sizeEnum ) {
        switch( sizeEnum ) {
            case Font.SIZE_SMALL:
                return smallSize;
            case Font.SIZE_MEDIUM:
                return mediumSize;
            case Font.SIZE_LARGE:
                return largeSize;
            default:
                return mediumSize;
        }
    }

    private static int getStyle( String aFontName ) {
        if ( aFontName.equals( FONTSANS) ) {
            return Font.STYLE_PLAIN;
        } else if ( aFontName.equals( FONTSANSBOLD ) ) {
            return Font.STYLE_BOLD;
        } else {
            return Font.STYLE_PLAIN;
        }
    }

    private void applayFontSetting() {
        Font newFont = null;
        switch( fontSizeSetting ) {
            case SMALL:
                newFont = getFont( FONTSANS, Font.SIZE_SMALL );
                break;
            case MEDIUM:
                newFont = getFont( FONTSANS, Font.SIZE_MEDIUM );
                break;
            case LARGE:
                newFont = getFont( FONTSANS, Font.SIZE_LARGE );
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
                                        NDGStyleToolbox.fontMedium =
                                            NDGStyleToolbox.fontMediumBold =
                                                NDGStyleToolbox.fontSmall =
                                                    newFont;
    }
}

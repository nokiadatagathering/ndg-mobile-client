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

    public static final String FONTSANS = "NokiaSansWide";
    public static final String FONTSANSBOLD = "NokiaSansWideBold";
    public static final int MAX_DEFINED_FONT_SIZE = 30;
    public static final int MIN_DEFINED_FONT_SIZE = 11;
    public static NDGStyleToolbox instance;

    public ListStyleProxy listStyle;
    public MenuStyleProxy menuStyle;
    public DialogTitleStyleProxy dialogTitleStyle;
    public int focusGainColor;
    public int focusLostColor;

    public Style unselectedStyle = new Style();
    public Style selectedStyle = new Style();

    static public Font fontMediumBold;
    static public Font fontMedium;
    static public Font fontSmall;

    static public int smallSize;
    static public int mediumSize;

    private NDGStyleToolbox() {
        mediumSize = Font.createSystemFont( Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM ).getHeight();
        smallSize = Font.createSystemFont( Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL ).getHeight();

        listStyle = new ListStyleProxy();
        listStyle.updateFonts();
        menuStyle = new MenuStyleProxy();
        menuStyle.updateFonts();
        dialogTitleStyle = new DialogTitleStyleProxy();
        dialogTitleStyle.updateFonts();

        fontMedium = getFont( FONTSANS , mediumSize );
        fontSmall = getFont( FONTSANS , smallSize );
        fontMediumBold = getFont( FONTSANSBOLD, mediumSize );

        focusGainColor = UIManager.getInstance().getComponentStyle("").getFgColor();
        focusLostColor = UIManager.getInstance().getComponentStyle("Form").getFgColor();
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
            output.println("<settings>");
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
}

package br.org.indt.ndg.lwuit.ui.style;

import br.org.indt.ndg.lwuit.ui.GeneralAlert;
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

    public static NDGStyleToolbox instance;

    public ListStyleProxy listStyle;
    public MenuStyleProxy menuStyle;
    public DialogTitleStyleProxy dialogTitleStyle;
    public int focusGainColor;
    public int focusLostColor;

    public Style unselectedStyle = new Style();
    public Style selectedStyle = new Style();

    static public Font fontLargeBold = Font.createSystemFont( Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE );
    static public Font fontLarge = Font.createSystemFont( Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE );
    static public Font fontMediumBold = Font.createSystemFont( Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM );
    static public Font fontMedium = Font.createSystemFont( Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM );
    static public Font fontMediumItalic = Font.createSystemFont( Font.FACE_SYSTEM, Font.STYLE_ITALIC, Font.SIZE_MEDIUM );
    static public Font fontSmallBold = Font.createSystemFont( Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL );
    static public Font fontSmall = Font.createSystemFont( Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL );


    private NDGStyleToolbox() {
        listStyle = new ListStyleProxy();
        menuStyle = new MenuStyleProxy();
        dialogTitleStyle = new DialogTitleStyleProxy();
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
}

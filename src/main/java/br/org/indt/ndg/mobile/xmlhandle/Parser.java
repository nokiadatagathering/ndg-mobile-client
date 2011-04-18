package br.org.indt.ndg.mobile.xmlhandle;

import br.org.indt.ndg.lwuit.control.AES;
import br.org.indt.ndg.lwuit.control.ExitCommand;
import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.logging.Logger;
import java.io.IOException;
import org.xml.sax.SAXException;

public class Parser {

    private DefaultHandler handler;
    private boolean error = false;
    private boolean encryption = false;

    public Parser(DefaultHandler _handler) {
        this.handler = _handler;
    }

    public boolean getError() {
        return error;
    }

    public void parseInputStream(InputStream is) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            try {
                saxParser.parse(is, handler);
            } catch (DoneParsingException e) {

            } catch (SAXParseException e) {
                Logger.getInstance().logException("SAXParseException on parsing: " + e.getMessage());
                e.printStackTrace();
                error = true;
                AppMIDlet.getInstance().getFileSystem().setError(true);
            }

        } catch(Exception e) {
            Logger.getInstance().logException("Exception on parsing: " + e.getMessage());
            e.printStackTrace();
            error = true;
            GeneralAlert.getInstance().addCommand( ExitCommand.getInstance());
            GeneralAlert.getInstance().show(Resources.ERROR_TITLE, Resources.EPARSE_GENERAL, GeneralAlert.ERROR );
        }
    }

    public void parseFile(String filename) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            FileConnection fc = (FileConnection) Connector.open(filename);
            InputStream is = fc.openInputStream();
            if(AppMIDlet.getInstance().getSettings() != null) {
                if( AppMIDlet.getInstance().getSettings().getStructure().isEncryptionConfigured() )
                    encryption = AppMIDlet.getInstance().getSettings().getStructure().getEncryption();
            }

            if(isResult(filename) && encryption) {
                AES encrypter = new AES();
                try {
                    is = encrypter.decryptInputStreamToInputStream(is);
                } catch (Exception e) {
                    GeneralAlert.getInstance().addCommand( ExitCommand.getInstance());
                    GeneralAlert.getInstance().show(Resources.ERROR_TITLE, Resources.WRONG_KEY, GeneralAlert.ERROR );
                }
            }

            try {
                saxParser.parse(is, handler);
            } catch (DoneParsingException e) {
                // do nothing
            } catch (SAXParseException e) {
                Logger.getInstance().logException("SAXParseException on parsing: " + e.getMessage());
                e.printStackTrace();
                error = true;
                AppMIDlet.getInstance().getFileSystem().setError(true);
            }catch(Exception e){
                e.printStackTrace();
            }
            finally {
                if( is != null ) is.close();
                if( fc != null ) fc.close();
            }
        } catch(Exception e) {
            Logger.getInstance().logException("Exception on parsing: " + e.getMessage());
            e.printStackTrace();
            error = true;
            GeneralAlert.getInstance().addCommand( ExitCommand.getInstance());
            GeneralAlert.getInstance().show(Resources.ERROR_TITLE, Resources.EPARSE_GENERAL, GeneralAlert.ERROR );
        }
    }

    private boolean isResult(String filename)
    {
        int i = filename.indexOf("r_");
        if(i != -1) {
            return true;
        } else {
            return false;
        }
    }

     public void parseFileNoClose(String filename) throws SAXException, IOException, ParserConfigurationException {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            FileConnection fc = (FileConnection) Connector.open(filename);

            InputStream is = fc.openInputStream();

            try {
                saxParser.parse(is, handler);
            } catch (DoneParsingException e) {

            } catch (SAXParseException e) {
                Logger.getInstance().logException("SAXParseException on parsing: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
            is.close();
            fc.close();
    }
}
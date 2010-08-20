package br.org.indt.ndg.mobile.xmlhandle;

import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Alert;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.logging.Logger;
import java.io.ByteArrayInputStream;

public class Parser {    
    private DefaultHandler handler;    
    private boolean error = false;
    
    public Parser(DefaultHandler _handler) {
        this.handler = _handler;
    }
    
    public boolean getError() {
        return error;
    }
    
    public void parseSMSProtocolSurvey(String survey){
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            ByteArrayInputStream is = new ByteArrayInputStream(survey.getBytes("UTF-8"));            
            
            saxParser.parse(is, handler);
        } catch (Exception e) {
            Logger.getInstance().log("Exception on parsing: " + e.getMessage());
            AppMIDlet.getInstance().getGeneralAlert().showErrorExit(Resources.EPARSE_GENERAL);
        }
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
                //AppMIDlet.getInstance().getGeneralAlert().showText(Resources.EPARSE_SAX);
            } 
            
        } catch(Exception e) {
            Logger.getInstance().logException("Exception on parsing: " + e.getMessage());
            e.printStackTrace();
            error = true;
            AppMIDlet.getInstance().getGeneralAlert().showErrorExit(Resources.EPARSE_GENERAL);
        }        
    }
    
    public void parseFile(String filename) {
        try {
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
                error = true;
                AppMIDlet.getInstance().getFileSystem().setError(true);
                //AppMIDlet.getInstance().getGeneralAlert().showText(Resources.EPARSE_SAX);
            } 
            is.close();
            fc.close();
            
        } catch(Exception e) {
            Logger.getInstance().logException("Exception on parsing: " + e.getMessage());
            e.printStackTrace();
            error = true;
            AppMIDlet.getInstance().getGeneralAlert().showErrorExit(Resources.EPARSE_GENERAL);
        }
    }
}
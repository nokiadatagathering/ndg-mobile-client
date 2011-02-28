
package br.org.indt.ndg.mobile.settings;

import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SettingsHandler extends DefaultHandler {

    private SettingsStructure structure;
    private Stack tagStack = new Stack();

    public SettingsHandler() { }

    public void setSettingsStructure(SettingsStructure _structure) {
        this.structure = _structure;
    }

    public void startDocument() throws SAXException { }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("settings")) {
            structure.setSplashTime(Integer.parseInt(attributes.getValue(attributes.getIndex("splash"))));
            structure.setLanguage(attributes.getValue(attributes.getIndex("language")));
            String isRegisteredFlag = attributes.getValue(attributes.getIndex("registered"));
            if (isRegisteredFlag != null) {
                structure.setRegisteredFlag(Integer.parseInt(isRegisteredFlag));
            }
            else {
                structure.setRegisteredFlag(0);
            }
        }
        else if (qName.equals("server")) {
            if (attributes.getValue(attributes.getIndex("compression")).equals("on"))
                structure.setServerCompression(true);
            else
                structure.setServerCompression(false);
        }
        else if (qName.equals("gps")) {
            if (attributes.getValue(attributes.getIndex("configured")).equals("yes"))
                structure.setGpsConfigured(true);
            else
                structure.setGpsConfigured(false);
        }
        else if (qName.equals("geotagging")) {
            if (attributes.getValue(attributes.getIndex("configured")).equals("yes"))
                structure.setGeoTaggingConfigured(true);
            else
                structure.setGeoTaggingConfigured(false);
        }
        else if(qName.equals("log")){
            String logSupport = attributes.getValue(attributes.getIndex("active"));

            if(logSupport.equalsIgnoreCase("YES")) {
                structure.setLogSupport(true);
            }
        }
        else if(qName.equals("version")) {
            String appVersion = attributes.getValue(attributes.getIndex("application"));
            if (appVersion == null) {
                appVersion = "-1";
            }
            structure.setAppVersion(appVersion);
        }
        else if (qName.equals("photoResolution")) {
            String config = attributes.getValue(attributes.getIndex("configId"));
            try {
                int configInt = Integer.parseInt(config);
                structure.setPhotoResolutionId(configInt);
            } catch ( Exception ex ){
                structure.setPhotoResolutionId(0);
            }
        }
        else if (qName.equals("style")) {
            String config = attributes.getValue(attributes.getIndex("id"));
            try {
                int configInt = Integer.parseInt(config);
                structure.setStyleId(configInt);
            } catch ( Exception ex ){
                structure.setStyleId(0);
            }
        }

        tagStack.push(qName);
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
         String chars = new String(ch, start, length).trim();

         if (chars.length() > 0) {

            String qName = (String)tagStack.peek();

            if (qName.equals("url_compress")) structure.setServerUrl_Compress(chars);
            else if (qName.equals("url_normal")) structure.setServerUrl_Normal(chars);
            else if (qName.equals("url_receive_survey")) structure.setReceiveSurveyURL(chars);
            else if (qName.equals("url_update_check")) structure.setUpdateCheckURL(chars);
            else if (qName.equals("url_register_imei")) structure.setRegisterIMEIUrl(chars);
         }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        tagStack.pop();
    }

    public void endDocument() throws SAXException {  }
}

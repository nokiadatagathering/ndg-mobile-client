
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
            String agree_flag = attributes.getValue(attributes.getIndex("agree"));
            if (agree_flag != null) {
                structure.setAgreeFlag(Integer.parseInt(agree_flag));
            }
            else {
                structure.setAgreeFlag(1);
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
        
        else if(qName.equals("sms")){
            String country_code = attributes.getValue(attributes.getIndex("country_code"));
            structure.setCountryCode(country_code);
            String area_code = attributes.getValue(attributes.getIndex("area_code"));
            structure.setAreaCode(area_code);
            String phone_number = attributes.getValue(attributes.getIndex("phone_number"));
            structure.setPhoneNumber(phone_number);
            String sendingPort = attributes.getValue(attributes.getIndex("sendingPort"));
            structure.setSendingPort(sendingPort);
            String receivingPort = attributes.getValue(attributes.getIndex("receivingPort"));
            structure.setReceivingPort(receivingPort);
            String charCount = attributes.getValue(attributes.getIndex("number_of_char_per_sms"));
            structure.setSMSLength(Integer.parseInt(charCount));
        }
        else if(qName.equals("transport")){
            String gprsSupport = attributes.getValue(attributes.getIndex("gprs"));
            String smsSupport = attributes.getValue(attributes.getIndex("sms"));
            
            if(gprsSupport.equalsIgnoreCase("YES")){
                //SubmitServer.GPRS_SUPPORT = true;
                structure.setGPRSSupport(true);
            }
            if(smsSupport.equalsIgnoreCase("YES")){
                //SubmitServer.SMS_SUPPORT = true;
                structure.setSMSSupport(true);
            }
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

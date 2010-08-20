package br.org.indt.ndg.mobile.settings;

import java.io.PrintStream;

import br.org.indt.ndg.mobile.Resources;

public class SettingsStructure {
    
    private String deviceName = null;
    private String deviceAddress = null;
    private String deviceUrl = null;
    private boolean autoConnect = false;
    
    private String server_normal_url = null;
    private String server_compress_url = null;
    private String receive_survey_url;
    private boolean compress_state = false;
    
    private String font_size = new String("medium");
    private int splash_time = Resources.splashCountdown; //just set default
    private int agree_flag = 1; //just set default
    
    private boolean gps_configured = false;
    
    private boolean display_always = false;
    private int display_brightness = 0;
    
    private int result_range;
    
    private boolean gprsSupport = false;
    private boolean smsSupport = false;
    
    private String receivingPort;
    private int smsLength;    
    private String sendingPort;
    private String country_code;
    private String area_code;
    private String phone_number;
    
    private boolean logSupport = false;
    private String language = "";
    private String appVersion = "";
    private String update_app_url = "";
    private String update_check_url = "";
    private String register_imei_url = "";

    public SettingsStructure() {
        result_range = 1000;
    }
    
    public void writeServerSettings(PrintStream _out) {
        _out.print("<server compression=\"");
        if (compress_state) _out.println("on\">");
        else _out.println("off\">");
        
        _out.println("<url_compress>" + server_compress_url + "</url_compress>");
        _out.println("<url_normal>" + server_normal_url + "</url_normal>");
        _out.println("<url_receive_survey>" + receive_survey_url + "</url_receive_survey>");
        _out.println("<url_update_check>" + update_check_url + "</url_update_check>");
        _out.println("<url_register_imei>" + register_imei_url + "</url_register_imei>");

        _out.println("</server>");
    }
    
    public void writeGpsSettings(PrintStream _out) {
        _out.print("<gps configured=\"");
        if (gps_configured) _out.println("yes\"/>");
        else _out.println("no\"/>");
    }
    
    void writeSmsSettings(PrintStream _out) {
        //<sms sendTo_phone="559281396411" receivingPort="50001" sendingPort="50000" number_of_char_per_sms="100"/>
        _out.print("<sms country_code=\"" + country_code + "\"" + " ");
        _out.print("area_code=\"" + area_code + "\"" + " ");
        _out.print("phone_number=\"" + phone_number + "\"" + " ");
        _out.print("receivingPort=\"" + receivingPort + "\"" + " ");       
        _out.print("sendingPort=\"" + sendingPort + "\"" + " ");       
        _out.println("number_of_char_per_sms=\"" + String.valueOf(smsLength) + "\"" + "/>");       
    }
    
    void writeTransportSettings(PrintStream _out) {
        //<transport gprs="no" sms="yes"/>
        String gprs_support = "no";
        String sms_support = "no";
        
        if(gprsSupport)
            gprs_support = "yes";
        _out.print("<transport gprs=\"" + gprs_support + "\"" + " ");
        if(smsSupport)
            sms_support = "yes";
        _out.println("sms=\"" + sms_support + "\"" + "/>");
    }
    
    void writeLogSettings(PrintStream _out) {
        String strLogSupport = logSupport ? "yes" : "no";
        _out.println("<log active=\"" + strLogSupport + "\"" + "/>");
    }
    
    void writeVersionSettings(PrintStream _out) {
        _out.println("<version application=\"" + appVersion + "\"/>");
    }

    void setGPRSSupport(boolean _gprsSupport) {
        gprsSupport = _gprsSupport;
    }
    public boolean getGPRSSupport(){
        return gprsSupport;
    }

    void setReceivingPort(String _receivingPort) {
        receivingPort = _receivingPort;
    }
    public String getReceivingPort(){
        return receivingPort;
    }

    void setSMSLength(int _smsLength) {
        smsLength = _smsLength;
    }
    public int getSMSLength(){
        return smsLength;
    }

    void setSMSSupport(boolean _smsSupport) {
        smsSupport = _smsSupport;
    }
    public boolean getSMSSupport(){
        return smsSupport;
    }

    void setCountryCode(String _strVal) {
        country_code = _strVal;
    }
    public String getCountryCode(){
        return country_code;
    }
    
    void setAreaCode(String _strVal) {
        area_code = _strVal;
    }
    public String getAreaCode(){
        return area_code;
    }
    
    void setPhoneNumber(String _strVal) {
        phone_number = _strVal;
    }
    public String getPhoneNumber(){
        return phone_number;
    }

    void setSendingPort(String _sendingPort) {
        sendingPort = _sendingPort;
    }
    public String getSendingPort(){
        return sendingPort;
    }
    
    void setLogSupport(boolean _logSupport) {
        logSupport = _logSupport;
    }
    public boolean getLogSupport(){
        return logSupport;
    }
    
    void setLanguage(String _lang) {
        language = _lang;
    }
    
    public String getLanguage() {
        return language;
    }
    
    void setAppVersion(String _ver) {
        appVersion = _ver;
    }
    
    public String getAppVersion() {
        return appVersion;
    }
    
    void setUpdateAppURL(String _url) {
        update_app_url = _url;
    }
    
    public String getUpdateAppURL() {
        return update_app_url;
    }

    void setUpdateCheckURL(String _url) {
        update_check_url = _url;
    }

    public String getUpdateCheckURL() {
        return update_check_url;
    }
        
    public void setServerCompression(boolean _state) { compress_state = _state; }
    public boolean getServerCompression() { return compress_state; }
    
    public void setServerUrl_Compress(String _url) { server_compress_url = _url; }
    public void setServerUrl_Normal(String _url) { server_normal_url = _url; }
    public String getServerUrl() {
        if (compress_state) return server_compress_url;
        else return server_normal_url;
    }
    public void setReceiveSurveyURL(String url){
        receive_survey_url = url;
    }
    public String getReceiveSurveyURL(){
        return receive_survey_url;
    }

    public String getRegisterIMEIUrl() {
        return register_imei_url;
    }

    public void setRegisterIMEIUrl(String url) {
        this.register_imei_url = url;
    }
    
    public void setAgreeFlag(int _flag) { agree_flag = _flag; }
    public int getAgreeFlag() { return agree_flag; }

    public void setSplashTime(int _time) { splash_time = _time; }
    public int getSplashTime() { return splash_time; }
    
    public void setGpsConfigured(boolean _state) { gps_configured = _state; }
    public boolean getGpsConfigured() { return gps_configured; }
    
    public void setResultListRange(int _range) { result_range = 1000/*_range*/; }
    
    //TODO It is hardcoded as many modifications must be done to simply remove the number of items to be displayed;
    //In fact, the behaviour is: ilimited number of items should be displayed: not 10, 100 or 500
    public int getResultListRange() { return 1000;/*result_range;*/ }

    

    

}

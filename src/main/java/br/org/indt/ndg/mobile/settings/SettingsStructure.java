package br.org.indt.ndg.mobile.settings;

import java.io.PrintStream;

import br.org.indt.ndg.mobile.Resources;

public class SettingsStructure {
    
   
    private String server_normal_url = null;
    private String server_compress_url = null;
    private String receive_survey_url;
    private boolean compress_state = false;
    
    private int splash_time = Resources.splashCountdown; //just set default
    private int agree_flag = 1; //just set default
    
    private boolean gps_configured = false;
    

    private boolean categoriesEnabled = false;
    
    
    private boolean logSupport = false;
    private String language = "";
    private String appVersion = "";
    private String update_app_url = "";
    private String update_check_url = "";
    private String register_imei_url = "";

    public SettingsStructure() {
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

    public void writeCategoryEnableSettings(PrintStream _out) {
        _out.print("<categoryView enabled=\"");
        if (categoriesEnabled) _out.println("yes\"/>");
        else _out.println("no\"/>");
    }
       
    void writeLogSettings(PrintStream _out) {
        String strLogSupport = logSupport ? "yes" : "no";
        _out.println("<log active=\"" + strLogSupport + "\"" + "/>");
    }
    
    void writeVersionSettings(PrintStream _out) {
        _out.println("<version application=\"" + appVersion + "\"/>");
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


    public boolean getCategoriesEnabled() {
        return categoriesEnabled;
    }

    public void setCategoriesEnabled(boolean enabled) {
        categoriesEnabled = enabled;
    }

}

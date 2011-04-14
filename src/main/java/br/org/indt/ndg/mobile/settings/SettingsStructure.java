package br.org.indt.ndg.mobile.settings;

import br.org.indt.ndg.mobile.AppMIDlet;
import java.io.PrintStream;

import br.org.indt.ndg.mobile.Utils;
import br.org.indt.ndg.mobile.settings.PhotoSettings.PhotoResolution;

/**
 * READ FIRST!
 * To add a new setting You need to perform 5 steps:
 * 1) Implement Setter and Getter for new setting
 * 2) Add default setting value constant
 * 3) Apply the constant to initial value of new setting
 * 4) Add setting default value to createDefaultSettings method
 * 5) Update SettingsHandler
 * @author tomasz.baniak
 */

public class SettingsStructure {

    /* Default values */
    private static final boolean DEFAULT_USE_COMPRESSION = true;
    private static final int DEFAULT_SPLASH_TIME = 8000;
    private static final int DEFAULT_IS_REGISTERED = 0;
    private static final boolean DEFAULT_GPS = true;
    private static final boolean DEFAULT_GEO_TAGGING = true;
    private static final int DEFAULT_PHOTO_RESULUTION_ID = 0;
    private static final int DEFAULT_STYLE_ID = 0;
    private static final boolean DEFAULT_LOG_SUPPORT = false;

    private String server_normal_url;
    private String server_compress_url;
    private String server_results_openrosa_url;
    private String receive_survey_url;
    private String update_check_url;
    private String register_imei_url;
    private boolean compress_state = DEFAULT_USE_COMPRESSION;

    private int splash_time = DEFAULT_SPLASH_TIME;
    private int isRegistered_flag = DEFAULT_IS_REGISTERED;

    private boolean gps_configured = DEFAULT_GPS;
    private boolean geoTagging_configured = DEFAULT_GEO_TAGGING;

    private int selectedResolution = DEFAULT_PHOTO_RESULUTION_ID;
    private int selectedStyle = DEFAULT_STYLE_ID;

    private boolean logSupport = DEFAULT_LOG_SUPPORT;
    private String language;
    private String appVersion;


    public SettingsStructure() {
        initializeDefaultRuntimeSettings();
    }

    private void initializeDefaultRuntimeSettings() {
        String defaultServerUrl = AppMIDlet.getInstance().getDefaultServerUrl();
        String defaultAppLanguage = AppMIDlet.getInstance().getDefaultAppLanguage();
        String[] defaultServelts = AppMIDlet.getInstance().getDefaultServlets();

        server_normal_url = defaultServerUrl + defaultServelts[0] + defaultServelts[1];
        server_compress_url = defaultServerUrl + defaultServelts[0] + defaultServelts[1];
        server_results_openrosa_url = defaultServerUrl + defaultServelts[0] + defaultServelts[5];
        receive_survey_url = defaultServerUrl + defaultServelts[0] + defaultServelts[2];
        update_check_url = defaultServerUrl + defaultServelts[0] + defaultServelts[3];
        register_imei_url = defaultServerUrl + defaultServelts[0] + defaultServelts[4];
        language = defaultAppLanguage;
        appVersion = AppMIDlet.getInstance().getAppVersion();
    }

    public void createDefaultSettings(PrintStream _out) {
        String defaultServerUrl = AppMIDlet.getInstance().getDefaultServerUrl();
        String defaultAppLanguage = AppMIDlet.getInstance().getDefaultAppLanguage();
        String[] defaultServelts = AppMIDlet.getInstance().getDefaultServlets();

        // Reset to default values
        setLanguage(defaultAppLanguage);
        setRegisteredFlag(DEFAULT_IS_REGISTERED);
        setSplashTime(DEFAULT_SPLASH_TIME);
        setGpsConfigured(DEFAULT_GPS);
        setGeoTaggingConfigured(DEFAULT_GEO_TAGGING);
        setPhotoResolutionId(DEFAULT_PHOTO_RESULUTION_ID);
        setStyleId(DEFAULT_STYLE_ID);
        setLogSupport(DEFAULT_LOG_SUPPORT);
        setServerCompression(DEFAULT_USE_COMPRESSION);
        setServerUrl_Compress(defaultServerUrl + defaultServelts[0] + defaultServelts[1]);
        setServerUrl_Normal(defaultServerUrl + defaultServelts[0] + defaultServelts[1]);
        setServerUrl_ResultsOpenRosa(defaultServerUrl + defaultServelts[0] + defaultServelts[5]);
        setReceiveSurveyURL(defaultServerUrl + defaultServelts[0] + defaultServelts[2]);
        setUpdateCheckURL(defaultServerUrl + defaultServelts[0] + defaultServelts[3]);
        setRegisterIMEIUrl(defaultServerUrl + defaultServelts[0] + defaultServelts[4]);
        setAppVersion(AppMIDlet.getInstance().getAppVersion());

        saveSettings(_out);
    }

    public void saveSettings(PrintStream _out) {
        _out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

        _out.print("<settings");
        _out.print(" registered=\"" + getRegisteredFlag() + "\"");
        _out.print(" splash=\"" + getSplashTime() + "\"");
        _out.println(" language=\"" + getLanguage() + "\">");

        writeGpsSettings(_out);
        writeGeoTaggingSettings(_out);
        writePhotoResolutionSettings(_out);
        writeStyleSettings(_out);
        writeLogSettings(_out);
        writeServerSettings(_out);
        writeVersionSettings(_out);

        _out.println("</settings>");
    }

    public void writeServerSettings(PrintStream _out) {
        _out.print("<server compression=\"");
        if (compress_state) _out.println("on\">");
        else _out.println("off\">");

        _out.println("<url_compress>" + server_compress_url + "</url_compress>");
        _out.println("<url_normal>" + server_normal_url + "</url_normal>");
        _out.println("<url_results_openrosa>" + server_results_openrosa_url + "</url_results_openrosa>");
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

    public void writeGeoTaggingSettings(PrintStream _out) {
        _out.print("<geotagging configured=\"");
        if (geoTagging_configured) _out.println("yes\"/>");
        else _out.println("no\"/>");
    }

    void writeLogSettings(PrintStream _out) {
        String strLogSupport = logSupport ? "yes" : "no";
        _out.println("<log active=\"" + strLogSupport + "\"" + "/>");
    }

    void writeVersionSettings(PrintStream _out) {
        _out.println("<version application=\"" + appVersion + "\"/>");
    }

    void writePhotoResolutionSettings(PrintStream output) {
        output.print("<photoResolution configId=\"");
        output.print( String.valueOf(selectedResolution) );
        output.println( "\"/>" );
    }

    void writeStyleSettings(PrintStream output) {
        output.print("<style id=\"");
        output.print( String.valueOf(selectedStyle) );
        output.println( "\"/>" );
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

    void setUpdateCheckURL(String _url) {
        update_check_url = _url;
    }
    public String getUpdateCheckURL() {
        return update_check_url;
    }

    public void setServerCompression(boolean _state) {
        compress_state = _state;
    }
    public boolean getServerCompression() {
        return compress_state;
    }

    public void setServerUrl_Compress(String _url) {
        server_compress_url = _url;
    }
    public void setServerUrl_Normal(String _url) {
        server_normal_url = _url;
    }

    public void setServerUrl_ResultsOpenRosa(String _url) {
        server_results_openrosa_url = _url;
    }

    public String getServerUrl( int surveyFormat ) {
        String result = null;
        switch (surveyFormat) {
            case Utils.NDG_FORMAT:
                if ( compress_state )
                    result = server_compress_url;
                else
                    result = server_normal_url;
                break;
            case Utils.OPEN_ROSA_FORMAT:
                result = server_results_openrosa_url;
                break;
            default:
                throw new RuntimeException("Unsupported Survey Format");
        }
        return result;
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

    public void setRegisteredFlag(int _flag) {
        isRegistered_flag = _flag;
    }
    public int getRegisteredFlag() {
        return isRegistered_flag;
    }

    public void setSplashTime(int _time) {
        splash_time = _time;
    }

    public int getSplashTime() {
        return splash_time;
    }

    public void setGpsConfigured(boolean _state) {
        gps_configured = _state;
    }
    public boolean getGpsConfigured() {
        return gps_configured;
    }

    public void setGeoTaggingConfigured(boolean _state) {
        geoTagging_configured = _state;
    }
    public boolean getGeoTaggingConfigured() {
        return geoTagging_configured;
    }

    public void setPhotoResolutionId(int _resConf ) {
        selectedResolution = _resConf;
    }
    public int getPhotoResolutionId() {
        return selectedResolution;
    }

    public void setStyleId(int styleId ) {
        selectedStyle = styleId;
    }
    public int getStyleId() {
        return selectedStyle;
    }

    public PhotoResolution getPhotoResolution() {
        return PhotoSettings.getInstance().getPhotoResolution( selectedResolution );
    }

    public String[] getResolutionList() {
        return PhotoSettings.getInstance().getResolutionList();
    }
}

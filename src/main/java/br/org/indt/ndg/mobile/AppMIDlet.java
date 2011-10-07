package br.org.indt.ndg.mobile;

import br.org.indt.ndg.lwuit.control.SurveysControl;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import br.org.indt.ndg.lwuit.ui.OpenRosaInterviewForm;
import br.org.indt.ndg.lwuit.ui.NDGLookAndFeel;
import br.org.indt.ndg.lwuit.ui.RegisterIMEI;
import br.org.indt.ndg.lwuit.ui.Screen;
import java.io.IOException;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.location.Location;
import br.org.indt.ndg.mobile.settings.Settings;
import br.org.indt.ndg.mobile.settings.LocationHandler;
import br.org.indt.ndg.mobile.logging.Logger;
import br.org.indt.ndg.lwuit.ui.SplashScreen;
import br.org.indt.ndg.lwuit.ui.camera.ICameraManager;
import br.org.indt.ndg.lwuit.ui.style.StyleConst;
import br.org.indt.ndg.mobile.settings.IMEIHandler;
import br.org.indt.ndg.mobile.submit.SubmitServer;
import com.sun.lwuit.Display;
import com.sun.lwuit.TextField;
import com.sun.lwuit.impl.midp.VKBImplementationFactory;
import com.sun.lwuit.plaf.UIManager;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;
import javax.microedition.location.Coordinates;
import javax.microedition.location.LocationProvider;

public class AppMIDlet extends MIDlet {

    private static AppMIDlet instance = null;

    private SurveyList surveyList = null;
    private ResultList resultList = null;
    private FileSystem fileSystem = null;
    private FileStores fileStores = null;
    private Resources resources = null;

    private Settings settings = null;

    private LocationHandler locationHandler = null;
    private SubmitServer submitServer;

    private long timeTracker = 0;

    private ICameraManager currentCameraManager;

    private String imei = "999966669999";

    private byte[] key = null;

    private String rootDir = null;

    public AppMIDlet() throws Exception {
        instance = this;
    }

    public static AppMIDlet getInstance() {
        return instance;
    }

    protected void startApp() throws MIDletStateChangeException {
        init(true);
    }

    protected void pauseApp() {}

    public void destroyApp(boolean unconditional) throws MIDletStateChangeException {
        if ( locationHandler != null )
            locationHandler.disconnect();
        locationHandler = null;
        surveyList = null;
        resultList = null;
        resources = null;
        fileSystem = null;
        fileStores = null;
        settings = null;
        notifyDestroyed();
    }

    public void init(boolean showSplashScreen) {
        VKBImplementationFactory.init(); // virtual keyboard will be shown on touch devices
        Display.init(this);
        if (showSplashScreen) {
            new SplashScreen().show();
        }
        settings = new Settings();
        Localization.initLocalizationSupport();
        resources = new Resources();
        locationHandler = new LocationHandler();

        initLWUIT();

        setIMEI();

        fileSystem = new FileSystem(AppMIDlet.getInstance().getRootDir());
        fileStores = new FileStores();
        registerApp();
    }

    public void initLWUIT() {
        // This is the charset used to all fonts in NDG.res LWUIT Resource File
        // When creating a new font in a resource file, this charset must be used
        // jABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghiklmnopqrstuvwxyz0123456789.,;:!@/\*()[]{}|#$%^&<>?'"+- _`~¡¿¤§=£¥€ÀÁÂÃÄĄÅÇĆÈÉÊĘÌÍŁÑŃŚŠÒÓÔÕÖÙÚÜŻŹŽàáâãäąåçćèéêęìíłñńśšòóôõöùúüżźž÷
        Hashtable i18n = new Hashtable();
        i18n.put("menu", Resources.NEWUI_OPTIONS);
        i18n.put("select", Resources.NEWUI_SELECT);
        i18n.put("cancel", Resources.NEWUI_CANCEL);
        TextField.setReplaceMenuDefault(false); //this is sucks, but does not work inside the NumericField and DescritionField constructor
        TextField.setQwertyAutoDetect(true);//this is sucks, but does not work inside the NumericField and DescritionField constructor
        try {
            com.sun.lwuit.util.Resources res = com.sun.lwuit.util.Resources.open("/br/org/indt/ndg/lwuit/ui/res/NDG.res");
            Screen.setRes(res);
            if( !Utils.isS40() ) {
                Screen.setFontRes( createResourceFromFile() );
            }
            NDGLookAndFeel ndgLF = new NDGLookAndFeel();
            // checkbox
            ndgLF.setCheckBoxImages(res.getImage("checked"), res.getImage("unchecked"));
            ndgLF.setRadioButtonImages(res.getImage("radioon"), res.getImage("radiooff"));
            UIManager.getInstance().setLookAndFeel(ndgLF);
            NDGLookAndFeel.setDefaultFormTransitionInForward();
            NDGLookAndFeel.setDefaultDialogTransitionInAndOut();

            switch( AppMIDlet.getInstance().getSettings().getStructure().getStyleId() ) {
                case StyleConst.DEFAULT:
                    UIManager.getInstance().setThemeProps(res.getTheme("SurveyList"));
                    NDGStyleToolbox.getInstance().reset();
                    break;
                case StyleConst.HIGHCONTRAST:
                    UIManager.getInstance().setThemeProps(res.getTheme("HighContrast"));
                    NDGStyleToolbox.getInstance().reset();
                    break;
                case StyleConst.CUSTOM:
                    UIManager.getInstance().setThemeProps(res.getTheme("SurveyList"));
                    NDGStyleToolbox.getInstance().reset();
                    NDGStyleToolbox.getInstance().loadSettings();
                    break;
                default:
                    UIManager.getInstance().setThemeProps(res.getTheme("SurveyList"));
                    NDGStyleToolbox.getInstance().reset();
                    break;
            }

            if(Display.getInstance().isTouchScreenDevice()) {
                UIManager.getInstance().addThemeProps(res.getTheme("TouchScreen"));
            }

            UIManager.getInstance().getLookAndFeel().setReverseSoftButtons(true);
            UIManager.getInstance().setResourceBundle(i18n);
            if(Display.getInstance().isTouchScreenDevice())
                UIManager.getInstance().getLookAndFeel().setTactileTouchDuration(20);

        } catch (java.io.IOException e) {
            Logger.getInstance().log(e.getMessage());
        }
    }

    private com.sun.lwuit.util.Resources createResourceFromFile(){
        FileConnection conn = null;
        InputStream stream = null;
        com.sun.lwuit.util.Resources fontRes = null;

        String langLocale = getSettings().getStructure().getLanguage().substring(0, 2);
        String resFile = AppMIDlet.getInstance().getRootDir() + NdgConsts.LANGUAGE_DIR + NdgConsts.FONTS_FILE_NAME + langLocale + NdgConsts.RES_EXTENSION;

        try {
            conn = (FileConnection) Connector.open( resFile );
            if(conn.exists()){
                fontRes = com.sun.lwuit.util.Resources.open(conn.openInputStream());
            }else{
                fontRes = com.sun.lwuit.util.Resources.open("/br/org/indt/ndg/lwuit/ui/res/fonts.res");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally{
            try{
                if(stream != null){
                    stream.close();
                }
                if(conn != null){
                    conn.close();
                }
            }catch(IOException ex){
            }
        }
        return fontRes;
    }

    public void registerApp() {
        // check if application is registered
        IMEIHandler im = new IMEIHandler();
        if(!im.isIMEIRegistered()){
            Screen.show(RegisterIMEI.class,true);
        } else {
            showEncryptionScreen();
        }
    }

    public void showEncryptionScreen() {
        if( !getSettings().getStructure().isEncryptionConfigured( ) )
            setDisplayable( br.org.indt.ndg.lwuit.ui.EncryptionConfigScreen.class);
        else {
            if(getSettings().getStructure().getEncryption())
                setDisplayable( br.org.indt.ndg.lwuit.ui.EncryptionKeyScreen.class);
            else
                continueAppLoading();
        }
    }

    public void continueAppLoading() {
        //check if location is available, if not turn off gps
        if(getSettings().getStructure().getGpsConfigured()){
            int status = locationHandler.connect();
            if (status == LocationProvider.AVAILABLE ||
                status == LocationProvider.TEMPORARILY_UNAVAILABLE) {
            } else {
                getSettings().getStructure().setGpsConfigured(false);
            }
        }
        setSurveyList(new SurveyList());

        //check for errors first before loading spash screen
        if (!fileSystem.getError() && !resources.getError() && !fileStores.getErrorkParser()) {

            setDisplayable( br.org.indt.ndg.lwuit.ui.SurveyList.class);
        }
    }

    public void setDisplayable( Class c ) {
        Screen.show( c, true);
    }

    public void showInterview() {
        String dirName = AppMIDlet.getInstance().getFileSystem().getSurveyDirName();
        if ( Utils.isNdgDir(dirName) ) {
            SurveysControl.getInstance().setSurveyChanged(false);
            AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.CategoryList.class);
        } else if ( Utils.isXformDir(dirName) ) {
            AppMIDlet.getInstance().setDisplayable(OpenRosaInterviewForm.class);
        }
    }


    public LocationHandler getLocationHandler() {
        return this.locationHandler;
    }

    public Location getLocation() {
        if ( locationHandler == null )
            return null;
        else
            return locationHandler.getLocation();
    }

    public Coordinates getCoordinates() {
        if ( getLocation() == null )
            return null;
        else
            return getLocation().getQualifiedCoordinates();
    }

    public boolean locationObtained(){
        return locationHandler.locationObtained();
    }

    public void setIMEI() {
        imei = System.getProperty("com.nokia.mid.imei"); // is null on emulator
        //imei = "9999";
    }

    public String getIMEI() {
        return imei;
    }

    public String getAppVersion() {
        return getAppProperty("MIDlet-Version");
    }

    public String getDefaultServerUrl() {
        return getAppProperty("server-url");
    }

    public String getAppMsisdn() {
        return getAppProperty("app-msisdn");
    }

    public String[] getDefaultServlets() {
        return new String[] {
            getAppProperty("server-servlet_Context"),
            getAppProperty("server-servlet_PostResults"),
            getAppProperty("server-servlet_ReceiveSurveys"),
            getAppProperty("server-servlet_CheckForUpdate"),
            getAppProperty("server-servlet_RegisterIMEI"),
            getAppProperty("server-servlet_PostResultsOpenRosa"),
            getAppProperty("server-servlet_Localization"),
            getAppProperty("server-servlet_LanguageList")
        };
    }

    public void setTimeTracker(long _time) {
        timeTracker = _time;
    }

    public long getTimeTracker() {
        return timeTracker;
    }

    public void setSurveyList(SurveyList _list) {
        surveyList = _list;
    }

    public SurveyList getSurveyList() {
        return surveyList;
    }

    public void setResultList(ResultList _list) {
        resultList = _list;
    }

    public ResultList getResultList() {
        return resultList;
    }

    public void setSubmitServer( SubmitServer _submitServer ) {
       this.submitServer = _submitServer;
    }

    public SubmitServer getSubmitServer() {
        return submitServer;
    }

    public FileStores getFileStores() {
        return fileStores;
    }

    public void setFileSystem(FileSystem _fs) {
        fileSystem = _fs;
    }

    public FileSystem getFileSystem() {
        return fileSystem;
    }

    public void setSettings(Settings _settings) {
        settings = _settings;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setKey(byte[] keyByte) {
        key = keyByte;
    }

    public byte[] getKey() {
        return key;
    }

    public void setCurrentCameraManager(ICameraManager manager){
        currentCameraManager = manager;
    }

    public ICameraManager getCurrentCameraManager(){
        return currentCameraManager;
    }

    public String getRootDir(){
        if(rootDir == null){
            setRootDir();
        }
        return rootDir;
    }

    private void setRootDir() {
        boolean sunWTKEmulator = false;
        Enumeration e = FileSystemRegistry.listRoots();
        while(e.hasMoreElements()){
            String drive = (String) e.nextElement();
            if (drive.equals("root1/")){
                sunWTKEmulator = true;
                break;
            }
        }

        String cardDir = "";
        String phoneDir = "";
        if(sunWTKEmulator){
            cardDir = "file:///root1/ndg/";
            phoneDir = "file:///root1/ndg/";
        }
        else{
            cardDir = System.getProperty("fileconn.dir.memorycard") + "ndg/";
            phoneDir = System.getProperty("fileconn.dir.photos") + "ndg/";
        }

        FileConnection fc;
        try {
            fc = (FileConnection) Connector.open(cardDir);
            if (!fc.exists()){
                fc.mkdir();
            }
            rootDir = cardDir;
            fc.close();
        }
        catch (IOException ioe) {
           try {
                fc = (FileConnection) Connector.open(phoneDir);
                if (!fc.exists())
                    fc.mkdir();
                rootDir = phoneDir;
                fc.close();
            }
            catch (IOException ioe2) {
            }
        }
    }


}
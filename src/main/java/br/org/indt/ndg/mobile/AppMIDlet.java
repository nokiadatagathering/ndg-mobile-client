package br.org.indt.ndg.mobile;

import br.org.indt.ndg.lwuit.ui.NDGLookAndFeel;
import br.org.indt.ndg.lwuit.ui.Screen;
import java.io.IOException;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.location.Location;
import br.org.indt.ndg.mobile.settings.Settings;
import br.org.indt.ndg.mobile.settings.LocationHandler;
import br.org.indt.ndg.mobile.xmlhandle.Conversor;
import br.org.indt.ndg.mobile.logging.Logger;
import br.org.indt.ndg.mobile.settings.IMEIHandler;
import br.org.indt.ndg.lwuit.ui.RegisterIMEI;
import br.org.indt.ndg.lwuit.ui.SplashScreen;
import br.org.indt.ndg.mobile.submit.SubmitServer;
import com.sun.lwuit.Display;
import com.sun.lwuit.TextField;
import com.sun.lwuit.animations.CommonTransitions;
import com.sun.lwuit.plaf.UIManager;
import java.io.DataOutputStream;
import java.util.Hashtable;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.location.LocationProvider;

public class AppMIDlet extends MIDlet {
    
    private static AppMIDlet instance = null;
    
    private SurveyList surveyList = null;
    private ResultList resultList = null;
    private FileSystem fileSystem = null;
    private FileStores fileStores = null;
    private Resources resources = null; 

    private Settings settings = null;
    private Conversor unicode = null;
    
    private LocationHandler locationHandler = null;
    private SubmitServer submitServer;
    
    private long timeTracker = 0;
    
    private String imei = "9999";

    
    public AppMIDlet() throws Exception {
        instance = this;
    }
    
    public LocationHandler getLocationHandler() {
        return this.locationHandler;
    }
    
    public Location getLocation() {
        if (locationHandler == null) return null;
        else return locationHandler.getLocation();
    }
    
    public static AppMIDlet getInstance() {
        return instance;
    }
    
    public void setIMEI() {
        imei = System.getProperty("com.nokia.mid.imei");
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
       
    public String getDefaultAppLanguage() {
        return getAppProperty("app-language");
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
            getAppProperty("server-servlet_RegisterIMEI")
        };
    }
    
    public void setTimeTracker(long _time) {
        timeTracker = _time;
    }

    public long getTimeTracker() {
        return timeTracker;
    }
    
    public SurveyList getSurveyList() {
        return surveyList;
    }
    
    public void setSurveyList(SurveyList _list) {
        surveyList = _list;
    }

    public void setSubmitServer( SubmitServer _submitServer ) {
       this.submitServer = _submitServer;
    }

    public SubmitServer getSubmitServer()
    {
        return submitServer;
    }
   
    public ResultList getResultList() {
        return resultList;
    }
    
    public void setResultList(ResultList _list) {
        resultList = _list;
    }
    
    public FileSystem getFileSystem() {
        return fileSystem;
    }
    
    public void setFileSystem(FileSystem _fs) {
        fileSystem = _fs;
    }
    
    public void setSettings(Settings _settings) {
        settings = _settings;
    }
    
    public Settings getSettings() {
        return settings;
    }
    
    public FileStores getFileStores() {
        return fileStores;
    }
    
    public String x2u(String _value) {
        return unicode.xml2uni(_value);
    }
    
    public String u2x(String _value) {
        return unicode.uni2xml(_value);
    }
    
    private void writeIMEIToFileSystem(){
        try {
            FileConnection con = (FileConnection) Connector.open(Resources.ROOT_DIR + "imei");
            if (!con.exists()){
                con.create();
                DataOutputStream out = con.openDataOutputStream();
                try{
                    out.write(imei.getBytes("UTF-8"));
                    out.flush();
                }
                catch(Exception e){
                    Logger.getInstance().emul("", "IMEI is null when running in emulator. Relax.");
                }
                out.close();
                con.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }            
    }
    
    public void init(boolean showSplashScreen) {
        Display.init(this);
        resources = new Resources();
        locationHandler = new LocationHandler();
        
        if (showSplashScreen) {
            Screen.show(SplashScreen.class,true);
        }

        this.setIMEI();

        writeIMEIToFileSystem();

        unicode = new Conversor();
        fileSystem = new FileSystem(Resources.ROOT_DIR);  
        fileStores = new FileStores();

        initLWUIT();
        
        registerApp();
    }
    

    public void initLWUIT() {

        // This is the charset used to all fonts in NDG.res LWUIT Resource File
        // When creating a new font in a resource file, this charset must be used
        // ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789.,;:!@/\*()[]{}|#$%^&<>?'"+- _`~¡¿¤§=£¥€ÀÁÂÃÇÈÉÊÌÍÑÒÓÔÕÙÚÜàáâãçèéêìíñòóôõ÷ùúü
        Hashtable i18n = new Hashtable();
        //com.sun.lwuit.Display.init(this);//moved to app start to speed up resource loading
        i18n.put("menu", Resources.NEWUI_OPTIONS);
        i18n.put("select", Resources.NEWUI_SELECT);
        i18n.put("cancel", Resources.NEWUI_CANCEL);
        TextField.setReplaceMenuDefault(false); //this is sucks, but does not work inside the NumericField and DescritionField constructor
        TextField.setQwertyAutoDetect(true);//this is sucks, but does not work inside the NumericField and DescritionField constructor
        try {
            com.sun.lwuit.util.Resources res = com.sun.lwuit.util.Resources.open("/br/org/indt/ndg/lwuit/ui/res/NDG.res");
            NDGLookAndFeel ndgLF = new NDGLookAndFeel();
            //Dialog
            ndgLF.setDefaultDialogTransitionIn(CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, false, 500));
            ndgLF.setDefaultDialogTransitionOut(CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, true, 500));
            //Form
            ndgLF.setDefaultFormTransitionIn(CommonTransitions.createSlide(CommonTransitions.SLIDE_HORIZONTAL, false, 500));
            // checkbox
            ndgLF.setCheckBoxImages(res.getImage("checked"), res.getImage("unchecked"));
            ndgLF.setRadioButtonImages(res.getImage("radioon"), res.getImage("radiooff"));
            //Style style = UIManager.getInstance().getComponentStyle("Dialog");
            //style.setBorder(Border.createEmpty());
            //style = UIManager.getInstance().getComponentStyle("DialogTitle");
            //style.setBorder(Border.createEmpty());
            //style = UIManager.getInstance().getComponentStyle("DialogBody");
            //style.setBorder(Border.createEmpty());
            UIManager.getInstance().setLookAndFeel(ndgLF);
            UIManager.getInstance().setThemeProps(res.getTheme("SurveyList"));
            UIManager.getInstance().getLookAndFeel().setReverseSoftButtons(true);
            UIManager.getInstance().setResourceBundle(i18n);
            Screen.setRes(res);
        } catch (java.io.IOException e) {
            Logger.getInstance().log(e.getMessage());
        }
    }
       
    public void destroy() {
        if (locationHandler != null) locationHandler.disconnect();
        locationHandler = null;
        surveyList = null;
        resultList = null;
        resources = null;
        fileSystem = null;
        fileStores = null;
        unicode = null;
        settings = null;
    }
    
    protected void startApp() throws MIDletStateChangeException {
        init(true);          
    }

    public void setDisplayable( Class c ) {
        Screen.show( c, true);
    }

    protected void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) throws MIDletStateChangeException {
        destroy();
        notifyDestroyed();
    }

    public void registerApp() {
        // check if application is registered
        IMEIHandler im = new IMEIHandler();
        if(!im.isIMEIRegistered() && !settings.getStructure().getRegisterIMEIUrl().equals("registered")){
            Screen.show(RegisterIMEI.class,true);
        } else {
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

    public Class getInterviewForm() {
        if( getSettings().getStructure().getCategoriesEnabled() ) {
            return br.org.indt.ndg.lwuit.ui.CategoryList.class;
        } else {
            return br.org.indt.ndg.lwuit.ui.InterviewForm2.class;
        }
    }
}
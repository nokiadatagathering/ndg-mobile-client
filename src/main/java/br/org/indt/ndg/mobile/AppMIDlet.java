package br.org.indt.ndg.mobile;

import br.org.indt.ndg.lwuit.ui.NDGLookAndFeel;
import br.org.indt.ndg.lwuit.ui.Screen;
import java.io.IOException;
import javax.microedition.lcdui.Alert;
import br.org.indt.ndg.lwuit.control.Display;
//import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import javax.microedition.location.Location;

import br.org.indt.ndg.mobile.error.GeneralAlert;
import br.org.indt.ndg.mobile.settings.Settings;
import br.org.indt.ndg.mobile.settings.SimpleLocation;

import br.org.indt.ndg.mobile.sms.SMSReceiver;
import br.org.indt.ndg.mobile.xmlhandle.Conversor;
import br.org.indt.ndg.mobile.logging.Logger;
import br.org.indt.ndg.mobile.settings.IMEIHandler;
import br.org.indt.ndg.lwuit.ui.RegisterIMEI;
import com.sun.lwuit.TextField;
import com.sun.lwuit.animations.CommonTransitions;
import com.sun.lwuit.plaf.UIManager;
import java.io.DataOutputStream;
import java.util.Hashtable;
import javax.microedition.io.Connector;
import javax.microedition.io.PushRegistry;
import javax.microedition.io.file.FileConnection;

public class AppMIDlet extends MIDlet {
    
    private static AppMIDlet instance = null;
    
    private SurveyList surveyList = null;
    private ResultList resultList = null;
    private ResultView resultView = null;
    private SubmitList submitList = null;
    private FileSystem fileSystem = null;
    private FileStores fileStores = null;
    private Resources resources = null; 
    
    private CategoryList categoryList = null;
    private QuestionList questionList = null;
    
    private Displayable previousScreen = null;
    private Displayable currentScreen = null;
    private GeneralAlert generalAlert = null;
    private AgreementScreen agreementScreen = null;
    private Settings settings = null;
    private Conversor unicode = null;
    
    private SimpleLocation simpleLocation = null;
    
    private long timeTracker = 0;
    
    private String imei = "9999";
    private boolean initializedManually = false;

    private SplashScreen splashScreen = null;
    
    public AppMIDlet() throws Exception {
        instance = this;
    }
    
    public void setSimpleLocation(SimpleLocation _simplelocation) {
        this.simpleLocation = _simplelocation;
    }
    
    public SimpleLocation getSimpleLocation() {
        return this.simpleLocation;
    }
    
    public Location getLocation() {
        if (simpleLocation == null) return null;
        else return simpleLocation.getLocation();
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
    
    public String[] getDefaultSmsNumbers() {
        return new String[] {
            getAppProperty("sms-country_code"),
            getAppProperty("sms-area_code"),
            getAppProperty("sms-phone_number")
        };
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
    
    public CategoryList getCategoryList() {
        return categoryList;
    }
    
    public void setCategoryList(CategoryList _list) {
        categoryList = _list;
    }
    
    public SubmitList getSubmitList() {
        return submitList;
    }
    
    public void setSubmitList(SubmitList _list) {
        submitList = _list;
    }
    
    public ResultView getResultView() {
        return resultView;
    }
    
    public void setResultView(ResultView _resultView) {
        resultView = _resultView;
    }
    
    public ResultList getResultList() {
        return resultList;
    }
    
    public void setResultList(ResultList _list) {
        resultList = _list;
    }
    
    public QuestionList getQuestionList() {
        return  questionList;
    }
    
    public void setQuestionList(QuestionList _list) {
        questionList = _list;
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
    
    public Displayable getPreviousScreen() {
        return previousScreen;
    }
    
    public GeneralAlert getGeneralAlert() {
        return generalAlert;
    }

    public AgreementScreen getAgreementScreen() {
        return agreementScreen;
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
        resources = new Resources();
        
        if (showSplashScreen) {
            SplashScreen ss = new SplashScreen(Display.getDisplay(this), getSurveyList());
            setDisplayable(ss);
            ss.serviceRepaints();
        }

        this.setIMEI();
        initInitializedMannuallyFlag();

        writeIMEIToFileSystem();

        unicode = new Conversor();

        generalAlert = new GeneralAlert(Display.getDisplay(instance));

        fileSystem = new FileSystem(Resources.ROOT_DIR);  

        fileStores = new FileStores();

        initLWUIT();

        int agree_flag = getSettings().getStructure().getAgreeFlag();
        if (agree_flag == 1) {
            // Show Agreement Screen
            agreementScreen = new AgreementScreen();
            setDisplayable(agreementScreen);
        }
        else {
            registerApp();
        }  
    }

    public void initLWUIT() {

        // This is the charset used to all fonts in NDG.res LWUIT Resource File
        // When creating a new font in a resource file, this charset must be used
        // ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789.,;:!@/\*()[]{}|#$%^&<>?'"+- _`~¡¿¤§=£¥€ÀÁÂÃÇÈÉÊÌÍÑÒÓÔÕÙÚÜàáâãçèéêìíñòóôõ÷ùúü
        Hashtable i18n = new Hashtable();
        com.sun.lwuit.Display.init(this);
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

    public boolean isInitializedManually(){
        return initializedManually;
    }
    public void setInitializedManually(boolean _bVal){
        initializedManually = _bVal;
    }
    private void initInitializedMannuallyFlag() {
        String[] connections = PushRegistry.listConnections(true);
        if ((connections == null) || (connections.length == 0)) {
            initializedManually = true;
        }
        initializedManually = false;
    }
        
    public void destroy() {
        if (simpleLocation != null) simpleLocation.close();
        simpleLocation = null;
        surveyList = null;
        resultList = null;
        resultView = null;
        categoryList = null;
        questionList = null;
        submitList = null;
        resources = null;
        fileSystem = null;
        fileStores = null;
        generalAlert = null;
        unicode = null;
        settings = null;
        SMSReceiver.getInstance().end();
    }
    
    protected void startApp() throws MIDletStateChangeException {
        init(true);          
        //new SMSSender().testCompression();
    }
    
    public void setDisplayable(Displayable d) {
        previousScreen = currentScreen;
        currentScreen = d;
        Display.getDisplay(instance).setCurrent(d);
    }  
    
    public void setDisplayable(Alert a, Displayable d) {
        previousScreen = currentScreen;
        currentScreen = d;
        Display.getDisplay(instance).setCurrent(a,d);
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
        //if(true){
        if(!im.isIMEIRegistered() && !settings.getStructure().getRegisterIMEIUrl().equals("registered")){
            Screen.show(RegisterIMEI.class,true);
        }
        else {
            continueAppLoading();
        }
    }

    public void continueAppLoading() {
        //check if gps is already configured
        try {
            if (AppMIDlet.getInstance().getSettings().getStructure().getGpsConfigured())
                setSimpleLocation(new SimpleLocation(false));
        } catch (Exception e) {
            AppMIDlet.getInstance().getGeneralAlert().showError(e);
        }

        setSurveyList(new SurveyList());

        SMSReceiver.getInstance().init();

        //check for errors first before loading spash screen
        if (!fileSystem.getError() && !resources.getError() && !fileStores.getErrorkParser()) {

            setDisplayable(getSurveyList());
        }
    }

    public SplashScreen getSplashScreen() {
        return splashScreen;
    }
}
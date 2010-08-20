package br.org.indt.ndg.mobile;

import java.io.IOException;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Image;

import com.nokia.mid.appl.cmd.Local;

public class Resources {          
    /** Internationalized Resources */


    //****************************************************************************
    // TO BE INTERNATIONALIZED
    public static final String NEWUI_NOKIA_DATA_GATHERING = "Nokia Data Gathering";
    //public static final String NEWUI_TITLE_SURVEY_LIST = "List of Surveys";
    //public static final String NEWUI_TITLE_CATEGORIES = "Categories";
    //public static final String NEWUI_TITLE_RESULTS = "Results";
    public static final String NEWUI_OPTIONS = Local.getText(Local.QTJ_CMD_OPTIONS);
    public static final String NEWUI_SELECT = Local.getText(Local.QTJ_CMD_SELECT);
    public static final String NEWUI_CANCEL = Local.getText(Local.QTJ_CMD_CANCEL);
    public static final String NEWUI_BACK = Local.getText(Local.QTJ_CMD_BACK);
    public static final String NEWUI_NEXT = Local.getText(Local.QTJ_CMD_NEXT);
    public static final String NEWUI_NEW_RESULT = Local.getText(Local.QTJ_CMD_NEW_SURVEY);
    public static final String NEWUI_OPEN_RESULT = Local.getText(Local.QTJ_OPEN_RESULT);
    public static final String NEWUI_VIEW_RESULT = Local.getText(Local.QTJ_CMD_VIEW);
    public static final String NEWUI_SEND_RESULTS = Local.getText(Local.QTJ_CMD_SEND);
    public static final String NEWUI_DELETE_RESULTS = Local.getText(Local.QTJ_CMD_DELETE);
    public static final String NEWUI_VIEW_SENT_RESULTS = Local.getText(Local.QTJ_CMD_VIEWSENT);
    public static final String NEWUI_DELETE_CURRENT_RESULT = Local.getText(Local.QTJ_CMD_DELETE);
    public static final String NEW_IU_OK = Local.getText(Local.QTJ_CMD_OK);
    //****************************************************************************
    
    public static final String DELETE_SURVEY = Local.getText(Local.QTJ_CMD_DELETE_SURVEY);
    //public static final String DELETE_SURVEY = Local.getText(Local.QTJ_CHECK_NEW_SURVEYS);
    public static final String CHECK_NEW_SURVEYS = Local.getText(Local.QTJ_CHECK_NEW_SURVEYS);
    public static final String DOWNLOAD = Local.getText(Local.QTJ_CMD_DOWNLOAD);
    public static final String SETTINGS = Local.getText(Local.QTJ_SETTINGS);
    public static final String GPS = Local.getText(Local.QTJ_GPS);
    public static final String SERVER = Local.getText(Local.QTJ_SERVER);
    public static final String DISPLAY = Local.getText(Local.QTJ_DISPLAY);
    public static final String RANGE_TITLE = Local.getText(Local.QTJ_RESULTS_LIST_RANGE_TITLE);   
    //public static final String NO_SURVEY_IN_SERVER = Local.getText(Local.QTJ_NO_SURVEY_IN_SERVER);
    
    public static final Command CMD_BACK = new Command(Local.getText(Local.QTJ_CMD_BACK), Command.BACK, 1);
    public static final Command CMD_SEND = new Command(Local.getText(Local.QTJ_CMD_SEND), Command.ITEM, 1);
    public static final Command CMD_OPEN_RESULT = new Command(Local.getText(Local.QTJ_OPEN_RESULT), Command.ITEM, 1);
    public static final Command CMD_DELETE = new Command(Local.getText(Local.QTJ_CMD_DELETE), Command.ITEM, 1);
    public static final Command CMD_SAVE = new Command(Local.getText(Local.QTJ_CMD_SAVE), Command.ITEM, 1);
    public static final Command CMD_EXIT = new Command(Local.getText(Local.QTJ_CMD_EXIT), Command.EXIT, 1);
    public static final Command CMD_EXIT_OPTIONS = new Command(Local.getText(Local.QTJ_CMD_EXIT), Command.ITEM, 10);
    public static final Command CMD_NEW_RESULT = new Command(Local.getText(Local.QTJ_CMD_NEW_SURVEY), Command.ITEM, 1);
    public static final Command CMD_RESULTS = new Command(Local.getText(Local.QTJ_CMD_RESULTS), Command.ITEM, 1);
    public static final Command CMD_NEXT = new Command(Local.getText(Local.QTJ_CMD_NEXT), Command.ITEM, 1);
    public static final Command CMD_START = new Command(Local.getText(Local.QTJ_CMD_START), Command.ITEM, 1);
    public static final Command CMD_VIEW = new Command(Local.getText(Local.QTJ_CMD_VIEW), Command.ITEM, 1);
    public static final Command CMD_VIEWSENT = new Command(Local.getText(Local.QTJ_CMD_VIEWSENT), Command.ITEM, 1);
    public static final Command CMD_MARKALL = new Command(Local.getText(Local.QTJ_CMD_MARKALL), Command.ITEM, 1);
    public static final Command CMD_UNMARKALL = new Command(Local.getText(Local.QTJ_CMD_UNMARKALL), Command.ITEM, 1);
    public static final Command CMD_CANCEL = new Command(Local.getText(Local.QTJ_CMD_CANCEL), Command.CANCEL, 1);
    public static final Command CMD_MOVETOUNSENT = new Command(Local.getText(Local.QTJ_CMD_MOVETOUNSENT), Command.ITEM, 1);
    public static final Command CMD_QUESTIONS = new Command(Local.getText(Local.QTJ_CMD_QUESTIONS), Command.BACK, 1);
    public static final Command CMD_CATEGORIES = new Command(Local.getText(Local.QTJ_CMD_CATEGORIES), Command.BACK, 1);
    public static final Command CMD_RESULTADOS = new Command(Local.getText(Local.QTJ_CMD_RESULTADOS), Command.BACK, 1);
    public static final Command CMD_SURVEYS = new Command(Local.getText(Local.QTJ_CMD_SURVEYS), Command.BACK, 1);
    public static final Command CMD_OK = new Command(Local.getText(Local.QTJ_CMD_OK), Command.ITEM, 1);
    public static final Command CMD_SELECT = new Command(Local.getText(Local.QTJ_MORE_RESULTS), Command.ITEM, 1);  
    public static final Command CMD_SAVE_SETTINGS = new Command(Local.getText(Local.QTJ_CMD_SAVE_SETTINGS), Command.ITEM, 1);
    public static final Command CMD_SETTINGS = new Command(Local.getText(Local.QTJ_CMD_SETTINGS), Command.ITEM, 1);
    public static final Command CMD_GPSTEST = new Command(Local.getText(Local.QTJ_CMD_GPSTEST), Command.ITEM, 1);
    public static final Command CMD_OPEN_SURVEY = new Command(Local.getText(Local.QTJ_CMD_OPEN), Command.ITEM, 1);
    //public static final Command CMD_OPEN_SURVEY = new Command("Open", Command.ITEM, 1);
    public static final Command CMD_GPS = new Command(GPS, Command.ITEM, 1);
    public static final Command CMD_DOWNLOAD = new Command(DOWNLOAD, Command.ITEM, 1);
    public static final Command CMD_CHECK_NEW_SURVEYS = new Command(CHECK_NEW_SURVEYS, Command.ITEM, 1);
    public static final Command CMD_DELETE_SURVEY = new Command(DELETE_SURVEY, Command.ITEM, 1);
    public static final Command CMD_YES = new Command(Local.getText(Local.QTJ_YES), Command.OK, 1);
    public static final Command CMD_NO = new Command(Local.getText(Local.QTJ_NO), Command.CANCEL, 1);
    public static final Command CMD_CHECK_UPDATE = new Command(Local.getText(Local.QTJ_CHECK_FOR_UPDATE), Command.ITEM, 1);
    public static final Command CMD_VIEW_GPS_DETAILS = new Command(Local.getText(Local.QTJ_CMD_VIEW_GPS_DETAILS), Command.ITEM, 1);
    public static final Command CMD_ACCEPT = new Command(Local.getText(Local.QTJ_CMD_ACCEPT), Command.OK, 1);
    public static final Command CMD_DECLINE = new Command(Local.getText(Local.QTJ_CMD_DECLINE), Command.CANCEL, 1);
    public static final Command CMD_TEST_CONN= new Command(Local.getText(Local.QTJ_CMD_TEST_CONNECTION), Command.ITEM, 1);
    public static final Command CMD_HIDE= new Command(Local.getText(Local.QTJ_CMD_HIDE), Command.ITEM, 1);
    
    public static final String QUESTION_LIST_TITLE = Local.getText(Local.QTJ_QUESTION_LIST_TITLE);
    public static final String RESULTS_LIST_TITLE = Local.getText(Local.QTJ_RESULTS_LIST_TITLE);
    public static final String SUBMIT_LIST_TITLE = Local.getText(Local.QTJ_SUBMIT_LIST_TITLE);
    public static final String SENT_LIST_TITLE = Local.getText(Local.QTJ_SENT_LIST_TITLE);
    public static final String CATEGORY_LIST_TITLE = Local.getText(Local.QTJ_CATEGORY_LIST_TITLE);
    public static final String SURVEY_LIST_TITLE = Local.getText(Local.QTJ_SURVEY_LIST_TITLE);
    public static final String QUESTIONS = Local.getText(Local.QTJ_QUESTIONS);
    public static final String QUESTION = Local.getText(Local.QTJ_QUESTION);
    
    
    
    public static final String RANGE = Local.getText(Local.QTJ_RANGE);
    public static final String LARGE = Local.getText(Local.QTJ_LARGE);
    public static final String MEDIUM = Local.getText(Local.QTJ_MEDIUM);
    public static final String SMALL = Local.getText(Local.QTJ_SMALL);
    public static final String FONT = Local.getText(Local.QTJ_FONT);
    public static final String GPSCONFIG = Local.getText(Local.QTJ_GPS_LOCATION);
    public static final String COMPRESSION = Local.getText(Local.QTJ_COMPRESSION);
    public static final String OFF = Local.getText(Local.QTJ_OFF);
    public static final String ON = Local.getText(Local.QTJ_ON);
    public static final String OK = Local.getText(Local.QTJ_CMD_OK);
    
    public static final String SUBMIT_SERVER = Local.getText(Local.QTJ_SUBMIT_SERVER);
    public static final String SUBMIT_FILE = Local.getText(Local.QTJ_SUBMIT_FILE);
    
    //these strings are for custom widgets bool1 & bool2
    public static final String YES = Local.getText(Local.QTJ_YES);
    public static final String NO = Local.getText(Local.QTJ_NO);
    public static final String ALWAYS = Local.getText(Local.QTJ_ALWAYS);
    public static final String SOMETIMES = Local.getText(Local.QTJ_SOMETIMES);
    public static final String NEVER = Local.getText(Local.QTJ_NEVER);
    public static final String EXIT = Local.getText(Local.QTJ_CMD_EXIT);
    
    public static final String MALE = Local.getText(Local.QTJ_MALE);
    public static final String FEMALE = Local.getText(Local.QTJ_FEMALE);
    
    public static final String APPNAME = Local.getText(Local.QTJ_APPNAME);
    public static final String CATEGORY = Local.getText(Local.QTJ_CATEGORY);
    
    public static final String LOADING_SURVEYS = Local.getText(Local.QTJ_LOADING_SURVEY);
    public static final String PROCESSING = Local.getText(Local.QTJ_PROCESSING);
    public static final String WAIT = Local.getText(Local.QTJ_WAIT);
    public static final String LOADING_RESULTS = Local.getText(Local.QTJ_LOADING_RESULTS);
    public static final String SAVING_RESULT = Local.getText(Local.QTJ_SAVING_RESULT);
    public static final String DOWNLOAD_SURVEYS = Local.getText(Local.QTJ_DOWNLOAD_NEW_SURVEYS);
    public static final String CONNECTING = Local.getText(Local.QTJ_CONNECTING);
    public static final String DOWNLOADING_NEW_SURVEYS = Local.getText(Local.QTJ_DOWNLOADING_NEW_SURVEYS);
    
    public static final String DELETE_CONFIRMATION = Local.getText(Local.QTJ_DELETE_CONFIRMATION);
    public static final String DELETE_RESULTS_CONFIRMATION = Local.getText(Local.QTJ_DELETE_RESULTS_CONFIRMATION);
    public static final String DELETE_RESULT_CONFIRMATION = Local.getText(Local.QTJ_DELETE_RESULT_CONFIRMATION);
    public static final String DELETE_SURVEY_CONFIRMATION = Local.getText(Local.QTJ_DELETE_SURVEY_CONFIRMATION);
    public static final String NO_RESULTS_SELECTED = Local.getText(Local.QTJ_NO_RESULTS_SELECTED);
    public static final String SELECT_RESULTS_DELETE = Local.getText(Local.QTJ_SELECT_RESULTS_DELETE);
    public static final String IMPOSSIBLE_CREATE_ROOTDIR = Local.getText(Local.QTJ_IMPOSSIBLE_CREATE_ROOTDIR);
    public static final String NO_SURVEYS = Local.getText(Local.QTJ_NO_SURVEYS);
    public static final String THERE_ARE_NO_SURVEYS = Local.getText(Local.QTJ_THERE_ARE_NO_SURVEYS);
    public static final String ERROR_TITLE = Local.getText(Local.QTJ_ERROR_TITLE);
    public static final String GPS_LOCAL = Local.getText(Local.QTJ_GPS_LOCAL);
    public static final String CONNECTED = Local.getText(Local.QTJ_CONNECTED);
    public static final String LATITUDE = Local.getText(Local.QTJ_LATITUDE);
    public static final String LONGITUDE = Local.getText(Local.QTJ_LONGITUDE);
    public static final String ALTITUDE = Local.getText(Local.QTJ_ALTITUDE);
    public static final String HORIZONTAL_ACCU = Local.getText(Local.QTJ_HORIZONTAL_ACCU);
    public static final String VERTICAL_ACCU = Local.getText(Local.QTJ_VERTICAL_ACCU);
    public static final String NETWORK_FAILURE = Local.getText(Local.QTJ_NETWORK_FAILURE);
    public static final String TRY_AGAIN_LATER = Local.getText(Local.QTJ_TRY_AGAIN_LATER);
    public static final String INTEGER = Local.getText(Local.QTJ_INTEGER);
    public static final String VALUE_GREATER = Local.getText(Local.QTJ_VALUE_GREATER);
    public static final String VALUE_LOWER = Local.getText(Local.QTJ_VALUE_LOWER);
    public static final String NO_DETAILS = Local.getText(Local.QTJ_NO_DETAILS);
    public static final String NO_DETAILS_ALTERNATIVE = Local.getText(Local.QTJ_NO_DETAILS_ALTERNATIVE);
    public static final String NO_TRANSPORT = Local.getText(Local.QTJ_NO_TRANSPORT);
    public static final String NO_TRANSPORT_SELECTED = Local.getText(Local.QTJ_NO_TRANSPORT_SELECTED);
    public static final String SURVEY_NOT_IN_SERVER = Local.getText(Local.QTJ_SURVEY_NOT_IN_SERVER);
    public static final String MORE_DETAILS = Local.getText(Local.QTJ_MORE_DETAILS);
    public static final String EINVALID_SURVEY = Local.getText(Local.QTJ_EINVALID_SURVEY);
    public static final String END_OF_SURVEY = Local.getText(Local.QTJ_END_OF_SURVEY);
    public static final String PRESS_OK_TO_SAVE = Local.getText(Local.QTJ_PRESS_OK_TO_SAVE);
    public static final String WARNING = Local.getText(Local.QTJ_WARNING);
    public static final String NEW_SURVEY_RECEIVED = Local.getText(Local.QTJ_NEW_SURVEY_RECEIVED);
    public static final String AGREEMENT_TEXT = Local.getText(Local.QTJ_AGREEMENT_TEXT);
    public static final String SURVEY_NOT_DOWNLOADED = Local.getText(Local.QTJ_SURVEY_NOT_DOWNLOADED);
    public static final String TESTING_CONNECTION = Local.getText(Local.QTJ_TESTING_CONNECTION);
    public static final String CONNECTION_OK = Local.getText(Local.QTJ_CONNECTION_OK);
    public static final String CONNECTION_FAILED = Local.getText(Local.QTJ_CONNECTION_FAILED);
    public static final String GPRS_LABEL = Local.getText(Local.QTJ_GPRS_LABEL);
    public static final String SMS_LABEL = Local.getText(Local.QTJ_SMS_LABEL);
    public static final String CONNECTION_WAIT_FOR_ACK = Local.getText(Local.QTJ_CONNECTION_WAIT_FOR_ACK);
    public static final String CHECK_FOR_UPDATE_TITLE = Local.getText(Local.QTJ_CHECK_FOR_UPDATE_TITLE);
    public static final String NDG_UPDATED = Local.getText(Local.QTJ_NDG_UPDATED);
    public static final String NDG_NOT_UPDATED = Local.getText(Local.QTJ_NDG_NOT_UPDATED);
    public static final String DECIMAL = Local.getText(Local.QTJ_DECIMAL);
    public static final String DATE = Local.getText(Local.QTJ_DATE);
    public static final String NEW_INTERVIEW = Local.getText(Local.QTJ_NEW_INTERVIEW);
    public static final String EDITING = Local.getText(Local.QTJ_EDITING);
    public static final String TRY_AGAIN = Local.getText(Local.QTJ_TRY_AGAIN);
    public static final String REGISTERING_APP = Local.getText(Local.QTJ_REGISTERING_APP);
    public static final String REGISTRATION_DONE = Local.getText(Local.QTJ_REGISTRATION_DONE);
    public static final String APP_REGISTERED = Local.getText(Local.QTJ_APP_REGISTERED);
    public static final String APP_REGISTRATION_FAILED = Local.getText(Local.QTJ_APP_REGISTRATION_FAILED);
   
            
    public static final int NOENTRY = -99999;
    
    /** General */   
    //
        
    //deploy wtk
    public static String ROOT_DIR_PHONE;
    public static String ROOT_DIR_CARD;
    public static String ROOT_DIR;
    
    //public static final String URL_SERVLET_RESULTS = "http://200.141.73.235:8180/mobisus/PostResults";
    //public static final String URL_SERVLET_RESULTS = "http://localhost:8080/mobisus/PostResults";
    
    //public static final String ENCODE_TYPE = "UTF-8";
    
    public static final String SETTINGS_FILE = "settings.xml";
    public static final String SURVEY_NAME = "survey.xml";
    public static final String NEW_SURVEYS_LIST = "new_surveys_list.xml";
    public static final String TMP_DOWNLOAD_SURVEY = "tmp_download_survey.xml";
    public static final String XML_TAG_END_SURVEY = "</survey>";
    public static final String SURVEY = "survey";
    public static final String NDG_PROPERTIES_FILE = "ndg.properties";
    
    /** Messages */
    public static final String MSG_SERVER_CANT_WRITE_RESULTS = Local.getText(Local.QTJ_MSG_SERVER_CANT_WRITE_RESULTS);
    //public static final String CONT_WITHOUT_SAVE = Local.getText(Local.CONT_WITHOUT_SAVE);
    public static final String SAVE_MODIFICATIONS = Local.getText(Local.QTJ_SAVE_MODIFICATIONS);
    public static final String NOT_VISITED = Local.getText(Local.QTJ_NOT_VISITED);
    
    /*Error Messages*/
    public static final String ELOAD_SURVEY = Local.getText(Local.QTJ_ELOAD_SURVEY);
    public static final String ELOAD_SURVEYS = Local.getText(Local.QTJ_ELOAD_SURVEYS);
    public static final String ELOAD_RESULTS = Local.getText(Local.QTJ_ELOAD_RESULTS);
    public static final String EDELETE_RESULT = Local.getText(Local.QTJ_EDELETE_RESULT);
    public static final String ELEAVE_MIDLET = Local.getText(Local.QTJ_ELEAVE_MIDLET);
    public static final String ELOAD_IMAGES = Local.getText(Local.QTJ_ELOAD_IMAGES);
    public static final String ESET_ROOT = Local.getText(Local.QTJ_ESET_ROOT);
    public static final String EPARSE_SAX = Local.getText(Local.QTJ_EPARSE_SAX);
    public static final String EPARSE_SURVEY = Local.getText(Local.QTJ_EPARSE_SURVEY);
    public static final String EPARSE_RESULT = Local.getText(Local.QTJ_EPARSE_RESULT);
    public static final String EPARSE_GENERAL = Local.getText(Local.QTJ_EPARSE_GENERAL);
    public static final String EWRITE_RESULT = Local.getText(Local.QTJ_EWRITE_RESULT);
    public static final String ECREATE_RESULT = Local.getText(Local.QTJ_ECREATE_RESULT);
    public static final String ESEND_RESULT1 = Local.getText(Local.QTJ_ESEND_RESULT1);
    public static final String ESEND_RESULT2 = Local.getText(Local.QTJ_ESEND_RESULT2);
    public static final String ESEND_RESULT3 = Local.getText(Local.QTJ_ESEND_RESULT3);
    public static final String ERENAME = Local.getText(Local.QTJ_ERENAME);
    public static final String EWEBSERVER_ERROR = Local.getText(Local.QTJ_EWEBSERVER_ERROR);
    public static final String EDOWNLOAD_FAILED_ERROR_CODE = Local.getText(Local.QTJ_EDOWNLOAD_FAILED_ERROR_CODE);
    public static final String EDOWNLOAD_FAILED_HTTP_CODE = Local.getText(Local.QTJ_EDOWNLOAD_FAILED_HTTP_CODE);
    public static final String EDOWNLOAD_FAILED_INVALID_MIME_TYPE = Local.getText(Local.QTJ_EDOWNLOAD_FAILED_INVALID_MIME_TYPE);
    public static final String EDOWNLOAD_FAILED_INVALID_DATA = Local.getText(Local.QTJ_EDOWNLOAD_FAILED_INVALID_DATA);
    public static final String EDOWNLOAD_INCOMPLETED = Local.getText(Local.QTJ_EDOWNLOAD_INCOMPLETED);
    public static final String EDOWNLOAD_ACK_ERROR = Local.getText(Local.QTJ_EDOWNLOAD_ACK_ERROR);
    public static final String EINVALID_SURVEYS = Local.getText(Local.QTJ_EINVALID_SURVEYS);
    public static final String EINVALID_XML_FILE = Local.getText(Local.QTJ_EINVALID_XML_FILE);
    
    public static Image question = null;
    public static Image check = null;
    public static Image splash = null;
    public static Image pendingACK = null;
    
    public static int splashCountdown = 8000;
    
    private boolean error = false;
    
    public Resources() {        
        try {
            question = Image.createImage("/resources/images/redquestion.png");
            check = Image.createImage("/resources/images/greencheck.png");
            pendingACK = Image.createImage("/resources/images/hourglass.png");
            
            splash = Image.createImage("/resources/images/splashEN.jpg");
//            if (Local.phoneLang.substring(0,2).equals("pt"))
//                splash = Image.createImage("/resources/images/splashPT.jpg");
//            else if (Local.phoneLang.substring(0,2).equals("es"))
//                splash = Image.createImage("/resources/images/splashES.jpg");
//            else 
//                splash = Image.createImage("/resources/images/splashEN.jpg");
        } catch (IOException ex) {
            error = true;
            AppMIDlet.getInstance().getGeneralAlert().showErrorExit(Resources.ELOAD_IMAGES);
        }
    }
    
    public boolean getError() {
        return error;
    }
    
}

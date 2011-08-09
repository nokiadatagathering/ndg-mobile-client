package br.org.indt.ndg.mobile;

import br.org.indt.ndg.lwuit.control.ExitCommand;
import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import java.io.IOException;
import com.sun.lwuit.Image;

public class Resources {

    public static final String INFO = "Info";

    /** Internationalized Resources */
    //****************************************************************************
    public static final String SAVE_SURVEY_QUESTION = Localization.getMessage("QTJ_SAVE_SURVEY_QUESTION");
    public static final String ADD_LOCATION_FAILURE = Localization.getMessage("QTJ_ADD_LOCATION_FAILURE");
    public static final String LOCATION_OUT_OF_DATE = Localization.getMessage("QTJ_LOCATION_OUT_OF_DATE");
    public static final String LOCATION_OUT_OF_DATE_WARN = Localization.getMessage("QTJ_LOCATION_OUT_OF_DATE_WARN");
    public static final String NEWUI_NOKIA_DATA_GATHERING = Localization.getMessage("QTJ_NEWUI_NOKIA_DATA_GATHERING");
    public static final String DATE_FORMAT = Localization.getMessage("QTJ_DATE_FORMAT");
    public static final String AVAILABLE_DATE_FORMAT = Localization.getMessage("QTJ_AVAILABLE_DATE_FORMAT");
    public static final String OR_FORM_LOADING_FAILURE = Localization.getMessage("QTJ_OR_FORM_LOADING_FAILURE");

    public static final String OR_VALID_INPUT_FROM = Localization.getMessage("QTJ_OR_VALID_INPUT_FROM");
    public static final String TO = Localization.getMessage("QTJ_TO");
    public static final String OR_INVALID_INPUT = Localization.getMessage("QTJ_OR_INVALID_INPUT");
    public static final String SEND_ERRORS = Localization.getMessage("QTJ_SEND_ERRORS");
    public static final String RESULT_NOT_SENT = Localization.getMessage("QTJ_RESULT_NOT_SENT");
    public static final String REMOVE_CATEGORIES = Localization.getMessage("QTJ_REMOVE_CATEGORIES");
    public static final String CATEGORIES_LIMIT = Localization.getMessage("QTJ_CATEGORIES_LIMIT");
    public static final String ADD_CATEGORIES = Localization.getMessage("QTJ_ADD_CATEGORIES");
    public static final String ADD_ADDITIONAL_COPIES = Localization.getMessage("QTJ_ADD_ADDITIONAL_COPIES");
    public static final String FAIL_IMAGE_SAVE = Localization.getMessage("QTJ_FAIL_IMAGE_SAVE");
    public static final String OUT_OF_MEMORY = Localization.getMessage("QTJ_OUT_OF_MEMORY");

    public static final String NOT_ENOUGH_MEMORY = Localization.getMessage("QTJ_NOT_ENOUGH_MEMORY");
    public static final String CATEGORY_DISABLE = Localization.getMessage("QTJ_CATEGORY_DISABLE");
    public static final String SUB_CATEGORY = Localization.getMessage("QTJ_SUB_CATEGORY");
    public static final String AVAILABLE_STYLES = Localization.getMessage("QTJ_AVAILABLE_STYLES");
    public static final String AVAILABLE_FONT_SIZE = Localization.getMessage("QTJ_AVAILABLE_FONT_SIZE");
    public static final String UNSUPPORTED_TYPE = Localization.getMessage("QTJ_UNSUPPORTED_TYPE");
    public static final String SURVEY_LOCALIZED = Localization.getMessage("QTJ_SURVEY_LOCALIZED");
    public static final String CORRUPTED_SURVEY = Localization.getMessage("QTJ_CORRUPTED_SURVEY");
    public static final String ONE_SURVEY_CORRUPTED = Localization.getMessage("QTJ_ONE_SURVEY_CORRUPTED");
    public static final String CONDITION = Localization.getMessage("QTJ_CONDITION");

    //public static final String NEWUI_TITLE_SURVEY_LIST = "List of Surveys";
    //public static final String NEWUI_TITLE_CATEGORIES = "Categories";
    //public static final String NEWUI_TITLE_RESULTS = "Results";
    public static final String NEWUI_OPTIONS = Localization.getMessage("QTJ_CMD_OPTIONS");
    public static final String NEWUI_SELECT = Localization.getMessage("QTJ_CMD_SELECT");
    public static final String NEWUI_CANCEL = Localization.getMessage("QTJ_CMD_CANCEL");
    public static final String NEWUI_BACK = Localization.getMessage("QTJ_CMD_BACK");
    public static final String NEWUI_NEXT = Localization.getMessage("QTJ_CMD_NEXT");
    public static final String NEWUI_NEW_RESULT = Localization.getMessage("QTJ_CMD_NEW_SURVEY");
    public static final String NEWUI_OPEN_RESULT = Localization.getMessage("QTJ_OPEN_RESULT");
    public static final String NEWUI_VIEW_RESULT = Localization.getMessage("QTJ_CMD_VIEW");
    public static final String NEWUI_SEND_RESULTS = Localization.getMessage("QTJ_CMD_SEND");
    public static final String NEWUI_DELETE_RESULTS = Localization.getMessage("QTJ_CMD_DELETE");
    public static final String NEWUI_VIEW_SENT_RESULTS = Localization.getMessage("QTJ_CMD_VIEWSENT");
    public static final String NEWUI_DELETE_CURRENT_RESULT = Localization.getMessage("QTJ_CMD_DELETE");
    public static final String NEW_IU_OK = Localization.getMessage("QTJ_CMD_OK");
    //****************************************************************************
    public static final String DELETE_SURVEY = Localization.getMessage("QTJ_CMD_DELETE_SURVEY");
    public static final String CHECK_NEW_SURVEYS = Localization.getMessage("QTJ_CHECK_NEW_SURVEYS");
    public static final String DOWNLOAD = Localization.getMessage("QTJ_CMD_DOWNLOAD");
    public static final String SETTINGS = Localization.getMessage("QTJ_SETTINGS");
    public static final String GPS = Localization.getMessage("QTJ_GPS");
    //public static final String NO_SURVEY_IN_SERVER = Localization.getMessage("QTJ_NO_SURVEY_IN_SERVER");
    public static final String CMD_DELETE = Localization.getMessage("QTJ_CMD_DELETE");
    public static final String CMD_SAVE = Localization.getMessage("QTJ_CMD_SAVE");
    public static final String CMD_VIEW = Localization.getMessage("QTJ_CMD_VIEW");
    public static final String RESULTS_LIST_TITLE = Localization.getMessage("QTJ_RESULTS_LIST_TITLE");
    public static final String SENT_LIST_TITLE = Localization.getMessage("QTJ_SENT_LIST_TITLE");
    public static final String CATEGORY_LIST_TITLE = Localization.getMessage("QTJ_CATEGORY_LIST_TITLE");
    public static final String SURVEY_LIST_TITLE = Localization.getMessage("QTJ_SURVEY_LIST_TITLE");
    public static final String QUESTIONS = Localization.getMessage("QTJ_QUESTIONS");
    public static final String QUESTION = Localization.getMessage("QTJ_QUESTION");
    public static final String LARGE = Localization.getMessage("QTJ_LARGE");
    public static final String MEDIUM = Localization.getMessage("QTJ_MEDIUM");
    public static final String SMALL = Localization.getMessage("QTJ_SMALL");
    public static final String GPSCONFIG = Localization.getMessage("QTJ_GPS_LOCATION");
    public static final String OFF = Localization.getMessage("QTJ_OFF");
    public static final String ON = Localization.getMessage("QTJ_ON");
    public static final String OK = Localization.getMessage("QTJ_CMD_OK");
    public static final String SUBMIT_SERVER = Localization.getMessage("QTJ_SUBMIT_SERVER");
    //these strings are for custom widgets bool1 & bool2
    public static final String YES = Localization.getMessage("QTJ_YES");
    public static final String NO = Localization.getMessage("QTJ_NO");
    public static final String EXIT = Localization.getMessage("QTJ_CMD_EXIT");
    public static final String LOADING_SURVEYS = Localization.getMessage("QTJ_LOADING_SURVEY");
    public static final String PROCESSING = Localization.getMessage("QTJ_PROCESSING");
    public static final String LOADING_RESULTS = Localization.getMessage("QTJ_LOADING_RESULTS");
    public static final String SAVING_RESULT = Localization.getMessage("QTJ_SAVING_RESULT");
    public static final String DOWNLOAD_SURVEYS = Localization.getMessage("QTJ_DOWNLOAD_NEW_SURVEYS");
    public static final String CONNECTING = Localization.getMessage("QTJ_CONNECTING");
    public static final String DOWNLOADING_NEW_SURVEYS = Localization.getMessage("QTJ_DOWNLOADING_NEW_SURVEYS");
    public static final String DELETE_CONFIRMATION = Localization.getMessage("QTJ_DELETE_CONFIRMATION");
    public static final String DELETE_RESULTS_CONFIRMATION = Localization.getMessage("QTJ_DELETE_RESULTS_CONFIRMATION");
    public static final String DELETE_RESULT_CONFIRMATION = Localization.getMessage("QTJ_DELETE_RESULT_CONFIRMATION");
    public static final String DELETE_SURVEY_CONFIRMATION = Localization.getMessage("QTJ_DELETE_SURVEY_CONFIRMATION");
    public static final String IMPOSSIBLE_CREATE_ROOTDIR = Localization.getMessage("QTJ_IMPOSSIBLE_CREATE_ROOTDIR");
    public static final String ERROR_TITLE = Localization.getMessage("QTJ_ERROR_TITLE");
    public static final String GPS_LOCAL = Localization.getMessage("QTJ_GPS_LOCAL");
    public static final String CONNECTED = Localization.getMessage("QTJ_CONNECTED");
    public static final String LATITUDE = Localization.getMessage("QTJ_LATITUDE");
    public static final String LONGITUDE = Localization.getMessage("QTJ_LONGITUDE");
    public static final String ALTITUDE = Localization.getMessage("QTJ_ALTITUDE");
    public static final String HORIZONTAL_ACCU = Localization.getMessage("QTJ_HORIZONTAL_ACCU");
    public static final String VERTICAL_ACCU = Localization.getMessage("QTJ_VERTICAL_ACCU");
    public static final String NETWORK_FAILURE = Localization.getMessage("QTJ_NETWORK_FAILURE");
    public static final String TRY_AGAIN_LATER = Localization.getMessage("QTJ_TRY_AGAIN_LATER");
    public static final String INTEGER = Localization.getMessage("QTJ_INTEGER");
    public static final String VALUE_GREATER = Localization.getMessage("QTJ_VALUE_GREATER");
    public static final String VALUE_LOWER = Localization.getMessage("QTJ_VALUE_LOWER");
    public static final String SURVEY_NOT_IN_SERVER = Localization.getMessage("QTJ_SURVEY_NOT_IN_SERVER");
    public static final String MORE_DETAILS = Localization.getMessage("QTJ_MORE_DETAILS");
    public static final String EINVALID_SURVEY = Localization.getMessage("QTJ_EINVALID_SURVEY");
    public static final String WARNING = Localization.getMessage("QTJ_WARNING");
    public static final String SURVEY_NOT_DOWNLOADED = Localization.getMessage("QTJ_SURVEY_NOT_DOWNLOADED");
    public static final String TESTING_CONNECTION = Localization.getMessage("QTJ_TESTING_CONNECTION");
    public static final String CONNECTION_OK = Localization.getMessage("QTJ_CONNECTION_OK");
    public static final String CONNECTION_FAILED = Localization.getMessage("QTJ_CONNECTION_FAILED");
    public static final String GPRS_LABEL = Localization.getMessage("QTJ_GPRS_LABEL");
    public static final String CONNECTION_WAIT_FOR_ACK = Localization.getMessage("QTJ_CONNECTION_WAIT_FOR_ACK");
    public static final String CHECK_FOR_UPDATE_TITLE = Localization.getMessage("QTJ_CHECK_FOR_UPDATE_TITLE");
    public static final String NDG_UPDATED = Localization.getMessage("QTJ_NDG_UPDATED");
    public static final String NDG_NOT_UPDATED = Localization.getMessage("QTJ_NDG_NOT_UPDATED");
    public static final String DECIMAL = Localization.getMessage("QTJ_DECIMAL");
    public static final String DATE = Localization.getMessage("QTJ_DATE");
    public static final String NEW_INTERVIEW = Localization.getMessage("QTJ_NEW_INTERVIEW");
    public static final String EDITING = Localization.getMessage("QTJ_EDITING");
    public static final String TRY_AGAIN = Localization.getMessage("QTJ_TRY_AGAIN");
    public static final String REGISTERING_APP = Localization.getMessage("QTJ_REGISTERING_APP");
    public static final String REGISTRATION_DONE = Localization.getMessage("QTJ_REGISTRATION_DONE");
    public static final String APP_REGISTERED = Localization.getMessage("QTJ_APP_REGISTERED");
    public static final String CHECK_NETWORK = Localization.getMessage("QTJ_NDG_CHECK_NTWRK");
    public static final String CHECK_SERVER = Localization.getMessage("QTJ_NDG_CHECK_NTWRK");
    public static final String NO_ROUTE_TO_HOST = Localization.getMessage("QTJ_NDG_NO_ROUTE");
    public static final String SOFTWARE_CONN_ABORT = Localization.getMessage("QTJ_NDG_SOFTWARE_ABORT");
    public static final String CONNECTION_REFUSED = Localization.getMessage("QTJ_NDG_CONNECTION_REFUSED");
    public static final String PERMISSION_DENIED = Localization.getMessage("QTJ_NDG_PERMISSION_DENIED");
    public static final String NETWORK_DOWN = Localization.getMessage("QTJ_NDG_NETWORK_DOWN");
    public static final String NETWORK_UNREACHABLE = Localization.getMessage("QTJ_NDG_NETWORK_UNREACHABLE");
    public static final String CONNECTION_TIMEOUT = Localization.getMessage("QTJ_NDG_CONNECTION_TIMEOUT");
    public static final String NETWORK_UNAVAILABLE = Localization.getMessage("QTJ_NDG_NETWORK_UNAVAILABLE");
    public static final String HOST_NOT_FOUND = Localization.getMessage("QTJ_NDG_HOST_NOT_FOUND");
    public static final String HTTP_NOT_FOUND = Localization.getMessage("QTJ_HTTP_NOT_FOUND");
    public static final String HTTP_FORBIDDEN = Localization.getMessage("QTJ_HTTP_FORBIDDEN");
    public static final String HTTP_BAD_REQUEST = Localization.getMessage("QTJ_HTTP_BAD_REQUEST");
    public static final String HTTP_UNAUTHORIZED = Localization.getMessage("QTJ_HTTP_UNAUTHORIZED");
    public static final String HTTP_INTERNAL_ERROR = Localization.getMessage("QTJ_HTTP_INTERNAL_ERROR");
    public static final String HTTP_OVERLOADED = Localization.getMessage("QTJ_HTTP_OVERLOADED");
    public static final String FAILED_REASON = Localization.getMessage("QTJ_FAILED");
    public static final String NO_DNS = Localization.getMessage("QTJ_NDG_NO_DNS");
    public static final String NO_LOCATION = Localization.getMessage("QTJ_NDG_NO_LOCATION");
    public static final String IMEI_ALREADY_EXISTS = Localization.getMessage("QTJ_IMEI_ALREADY_EXISTS");
    public static final String MSISDN_NOT_FOUND = Localization.getMessage("QTJ_MSISDN_NOT_FOUND");
    public static final String REGISTRATION_FAILURE = Localization.getMessage("QTJ_REGISTRATION_FAILURE");
    public static final String MSISDN_ALREADY_REGISTERED = Localization.getMessage("QTJ_MSISDN_ALREADY_REGISTERED");
    public static final String WIRELESS_INTERFACE_ERROR = Localization.getMessage("QTJ_WIRELESS_INTERFACE_ERROR");
    public static final String THERE_ARE_NO_NEW_SURVEYS = Localization.getMessage("QTJ_THERE_ARE_NO_NEW_SURVEYS");
    public static final String NOTPROPERINTEGER = Localization.getMessage("QTJ_NOTPROPERINTEGER");
    public static final String NOTPROPERDECIMAL = Localization.getMessage("QTJ_NOTPROPRDECIMAL");
    public static final String RESTORE_DEFAULT = Localization.getMessage("QTJ_RESTORE_DEFAULT");
    public static final String RELOAD = Localization.getMessage("QTJ_RELOAD");
    public static final String LOADING_STYLE = Localization.getMessage("QTJ_LOADING_STYLE");
    public static final String LOADING_STYLE_ERROR = Localization.getMessage("QTJ_LOADING_STYLE_ERROR");
    public static final String UI_CUSTOMIZE = Localization.getMessage("QTJ_UI_CUSTOMIZE");
    public static final String SELECTED = Localization.getMessage("QTJ_SELECTED");
    public static final String UNSELECTED = Localization.getMessage("QTJ_UNSELECTED");
    public static final String PREVIEW = Localization.getMessage("QTJ_PREVIEW");
    public static final String ELEMENT = Localization.getMessage("QTJ_ELEMENT");
    public static final String OBJECT = Localization.getMessage("QTJ_OBJECT");
    public static final String LIST = Localization.getMessage("QTJ_LIST");
    public static final String MENU = Localization.getMessage("QTJ_MENU");
    public static final String DIALOG_TITLE = Localization.getMessage("QTJ_DIALOG_TITLE");
    public static final String BG_SELECTED = Localization.getMessage("QTJ_BG_SELECTED");
    public static final String BG_UNSELECTED = Localization.getMessage("QTJ_BG_UNSELECTED");
    public static final String FONT_SELECTED = Localization.getMessage("QTJ_FONT_SELECTED");
    public static final String FONT_UNSELECTED = Localization.getMessage("QTJ_FONT_UNSELECTED");
    public static final String ACCESS_DENIED = Localization.getMessage("QTJ_ACCESS_DENIED");
    public static final String LOAD_FROM_FILE = Localization.getMessage("QTJ_LOAD_FROM_FILE");
    public static final String MEMORY_OUT = Localization.getMessage("QTJ_MEMORY_OUT");
    public static final String DEFAULT_PHOTO_DIR = Localization.getMessage("QTJ_DEFAULT_PHOTO_DIR");
    public static final String CAPTURE_PHOTO = Localization.getMessage("QTJ_CAPTURE_PHOTO");
    public static final String DELETE_PHOTO = Localization.getMessage("QTJ_DELETE_PHOTO");
    public static final String PHOTO_RESOLUTION = Localization.getMessage("QTJ_PHOTO_RESOLUTION");
    public static final String SHOW_PHOTO = Localization.getMessage("QTJ_SHOW_PHOTO");
    public static final String TAKE_PHOTO = Localization.getMessage("QTJ_TAKE_PHOTO");
    public static final String JUST_SAVE = Localization.getMessage("QTJ_JUST_SAVE");
    public static final String UI_PREFERENCES = Localization.getMessage("QTJ_UI_PREFERENCES");
    public static final String GEO_TAGGING_CONF = Localization.getMessage("QTJ_GEO_TAGGING_CONF");
    public static final String MAX_IMG_NO = Localization.getMessage("QTJ_MAX_IMG_NO");
    public static final String RESOLUTIONS = Localization.getMessage("QTJ_RESOLUTIONS");
    public static final String DEFAULT = Localization.getMessage("QTJ_DEFAULT");
    public static final String HIGH_CONTRAST = Localization.getMessage("QTJ_HIGH_CONTRAST");
    public static final String CUSTOM = Localization.getMessage("QTJ_CUSTOM");
    public static final String STYLES = Localization.getMessage("QTJ_STYLES");
    public static final String TIME_FORMAT_ERROR = Localization.getMessage("QTJ_TIME_FORMAT_ERROR");
    public static final String DATE_FORMAT_ERROR = Localization.getMessage("QTJ_DATE_FORMAT_ERROR");
    public static final String DISCARD_CHANGES = Localization.getMessage("QTJ_DISCARD_CHANGES");
    public static final String ENCRYPTION = Localization.getMessage("QTJ_ENCRYPTION");
    public static final String ENCRYPTION_ENABLE = Localization.getMessage("QTJ_ENCRYPTION_ENABLE");
    public static final String ENCRYPTION_WITH_PASSWORD = Localization.getMessage("QTJ_ENCRYPTION_WITH_PASSWORD");
    public static final String ENCRYPTION_PASSWORD = Localization.getMessage("QTJ_ENCRYPTION_PASSWORD");
    public static final String DECRYPTION_FAILED = Localization.getMessage("QTJ_DECRYPTION_FAILED");
    public static final String EMPTY_KEY = Localization.getMessage("QTJ_EMPTY_KEY");
    public static final String WRONG_KEY = Localization.getMessage("QTJ_WRONG_KEY");

    
    /** General */
    /** Messages */
    public static final String MSG_SERVER_CANT_WRITE_RESULTS = Localization.getMessage("QTJ_MSG_SERVER_CANT_WRITE_RESULTS");
    //public static final String CONT_WITHOUT_SAVE = Localization.getMessage("CONT_WITHOUT_SAVE");
    /*Error Messages*/
    public static final String ELOAD_SURVEYS = Localization.getMessage("QTJ_ELOAD_SURVEYS");
    public static final String ELOAD_RESULTS = Localization.getMessage("QTJ_ELOAD_RESULTS");
    public static final String EDELETE_RESULT = Localization.getMessage("QTJ_EDELETE_RESULT");
    public static final String ELEAVE_MIDLET = Localization.getMessage("QTJ_ELEAVE_MIDLET");
    public static final String ELOAD_IMAGES = Localization.getMessage("QTJ_ELOAD_IMAGES");
    public static final String EPARSE_SAX = Localization.getMessage("QTJ_EPARSE_SAX");
    public static final String EPARSE_SURVEY = Localization.getMessage("QTJ_EPARSE_SURVEY");
    public static final String EPARSE_RESULT = Localization.getMessage("QTJ_EPARSE_RESULT");
    public static final String EPARSE_GENERAL = Localization.getMessage("QTJ_EPARSE_GENERAL");
    public static final String EWRITE_RESULT = Localization.getMessage("QTJ_EWRITE_RESULT");
    public static final String ECREATE_RESULT = Localization.getMessage("QTJ_ECREATE_RESULT");
    public static final String ERENAME = Localization.getMessage("QTJ_ERENAME");
    public static final String EWEBSERVER_ERROR = Localization.getMessage("QTJ_EWEBSERVER_ERROR");
    public static final String EDOWNLOAD_FAILED_ERROR_CODE = Localization.getMessage("QTJ_EDOWNLOAD_FAILED_ERROR_CODE");
    public static final String EDOWNLOAD_FAILED_INVALID_MIME_TYPE = Localization.getMessage("QTJ_EDOWNLOAD_FAILED_INVALID_MIME_TYPE");
    public static final String EDOWNLOAD_FAILED_INVALID_DATA = Localization.getMessage("QTJ_EDOWNLOAD_FAILED_INVALID_DATA");
    public static final String EDOWNLOAD_INCOMPLETED = Localization.getMessage("QTJ_EDOWNLOAD_INCOMPLETED");
    public static final String EDOWNLOAD_ACK_ERROR = Localization.getMessage("QTJ_EDOWNLOAD_ACK_ERROR");
    public static final String EINVALID_SURVEYS = Localization.getMessage("QTJ_EINVALID_SURVEYS");
    public static final String EINVALID_XML_FILE = Localization.getMessage("QTJ_EINVALID_XML_FILE");
    public static final String EFAILED_LOAD_IMAGE_LIMITED_DEVICE_RESOURCES = Localization.getMessage("QTJ_EFAILED_LOAD_IMAGE_LIMITED_DEVICE_RESOURCES");
    public static final String SHOW_CHOICES = Localization.getMessage("QTJ_SHOW_CHOICES");
    public static final String NATIVE_RESOLUTION = Localization.getMessage("QTJ_NATIVE_RESOLUTION");
    public static final String CMD_UI_COLORS =  Localization.getMessage("QTJ_CMD_COLORS");
    public static final String CMD_FONT_SIZES = Localization.getMessage("QTJ_CMD_SIZES");
    public static final String CMD_CLEAR = Localization.getMessage("QTJ_CMD_CLEAR");

    public static final String CMD_HIDE = Localization.getMessage("QTJ_CMD_HIDE");
    public static final String CMD_RESULTS = Localization.getMessage("QTJ_CMD_RESULTS");
    public static final String CMD_MARKALL = Localization.getMessage("QTJ_CMD_MARKALL");
    public static final String CMD_VIEW_GPS_DETAILS = Localization.getMessage("QTJ_CMD_VIEW_GPS_DETAILS");
    public static final String CMD_TEST_CONNECTION = Localization.getMessage("QTJ_CMD_TEST_CONNECTION");
    public static final String CMD_UNMARKALL = Localization.getMessage("QTJ_CMD_UNMARKALL");
    public static final String CMD_OPEN = Localization.getMessage("QTJ_CMD_OPEN");
    public static final String CMD_MOVETOUNSENT = Localization.getMessage("QTJ_CMD_MOVETOUNSENT");
    public static final String CHECK_FOR_UPDATE = Localization.getMessage("QTJ_CHECK_FOR_UPDATE");

    public static final String RESTART = Localization.getMessage("QTJ_RESTART");
    public static final String MSG_RESTART_NEEDED = Localization.getMessage("QTJ_MSG_RESTART_NEEDED");
    public static final String MSG_NEW_LANGUAGE = Localization.getMessage("QTJ_MSG_NEW_LANGUAGE");
    public static final String LANGUAGE = Localization.getMessage("QTJ_LANGUAGE");
    public static final String DOWNLOAD_LOCALE_FAILED = Localization.getMessage("QTJ_DOWNLOAD_LOCALE_FAILED");
    public static final String DOWNLOAD_LANG_LIST_FAILED = Localization.getMessage("QTJ_DOWNLOAD_LANG_LIST_FAILED");
    public static final String CHECK = Localization.getMessage("QTJ_CHECK");
    public static final String WARNING_TITLE = Localization.getMessage("QTJ_WARNING_TITLE");
    public static final String NEW_LANGUAGES = Localization.getMessage("QTJ_NEW_LANGUAGES");
    public static final String NO_NEW_LANGUAGE = Localization.getMessage("QTJ_NO_NEW_LANGUAGE");
    public static final String UPDATE = Localization.getMessage("QTJ_UPDATE");
    public static final String UPDATE_SUCCESS = Localization.getMessage("QTJ_UPDATE_SUCCESS");

    
    private boolean error = false;
    public static Image check;
    public static Image question;
    public static Image logo;

    public Resources() {
        try {
            question = Image.createImage("/resources/images/redquestion.png");
            check = Image.createImage("/resources/images/greencheck.png");
            logo = Image.createImage("/resources/images/logo02_48x48.png");
        } catch (IOException ex) {
            error = true;
            GeneralAlert.getInstance().addCommand(ExitCommand.getInstance());
            GeneralAlert.getInstance().show(Resources.ERROR_TITLE, Resources.ELOAD_IMAGES, GeneralAlert.ERROR);
        }
    }

    public boolean getError() {
        return error;
    }
}

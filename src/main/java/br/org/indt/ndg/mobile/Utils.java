package br.org.indt.ndg.mobile;

public class Utils {

    public static final int INVALID_FORMAT = 0;
    public static final int NDG_FORMAT = 1;
    public static final int OPEN_ROSA_FORMAT = 2;

    public static int resolveSurveyFormatFromDirName( String dirName ) {
        int result = INVALID_FORMAT;
        if (  dirName.startsWith("survey")) {
            result = NDG_FORMAT;
        } else if (  dirName.startsWith("xforms")) {
            result = OPEN_ROSA_FORMAT;
        }
        return result;
    }

}

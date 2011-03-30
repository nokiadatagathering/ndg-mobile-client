package br.org.indt.ndg.mobile.settings;


public class PhotoSettings {
    private static final int Xres = 0;
    private static final int Yres = 1;

    private static final int[][] photoResolutionValues = new int[][]{
                                                        {320,240},
                                                        {640,480},
                                                        {1024,768},
                                                        {1280,960}};
    private static final String[] photoResolutionTexts = new String[] {
                                                        "320x240 (0.1M)",
                                                        "640x480 (0.3M)",
                                                        "1024x768 (0.8M)",
                                                        "1280x960 (1.2M)"};

    public static int getX( int selectedResolution ) {
        if ( selectedResolution < 0 || selectedResolution > photoResolutionValues.length ) {
            return photoResolutionValues[0][Xres];
        } else {
            return photoResolutionValues[selectedResolution][Xres];
        }
    }

    public static int getY(int selectedResolution) {
        if ( selectedResolution < 0 || selectedResolution > photoResolutionValues.length ) {
            return photoResolutionValues[0][Yres];
        } else {
            return photoResolutionValues[selectedResolution][Yres];
        }
    }

    public static String[] getResolutionList() {
        return photoResolutionTexts;
    }

}

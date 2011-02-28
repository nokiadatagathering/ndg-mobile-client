package br.org.indt.ndg.mobile.settings;


public class PhotoSettings {
    final int Xres = 0;
    final int Yres = 1;

    final private int[][] photoResolution = new int[][]{{320,240},
                                                        {640,480},
                                                        {1024,768},
                                                        {1280,960}};

    public int getX( int selectedResolution ) {
        if( selectedResolution <0 || selectedResolution > photoResolution.length ) {
            return 320;
        } else {
            return photoResolution[selectedResolution][Xres];
        }
    }

    int getY(int selectedResolution) {
        if( selectedResolution <0 || selectedResolution > photoResolution.length ) {
            return 240;
        } else {
            return photoResolution[selectedResolution][Yres];
        }
    }

    public String[] getResolutionList() {
        String[] output = new String[photoResolution.length];
        for ( int i=0; i< photoResolution.length; i++ ) {
            output[i] = String.valueOf(photoResolution[i][Xres])
                      + "x"
                      + String.valueOf(photoResolution[i][Yres]);
        }
        return output;
    }
}

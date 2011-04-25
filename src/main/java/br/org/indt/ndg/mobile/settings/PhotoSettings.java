package br.org.indt.ndg.mobile.settings;

import br.org.indt.ndg.mobile.Resources;
import java.util.Enumeration;
import java.util.Vector;


public class PhotoSettings {

    private static PhotoSettings mInstance = new PhotoSettings();
    private final Vector mPhotoResolutionValues = new Vector(); /*<PhotoResolution>*/

    private PhotoSettings() {
        loadDefaultResolutions();
        addNativeResolution();
    }

    public static PhotoSettings getInstance() {
        return mInstance;
    }

    public PhotoResolution getPhotoResolution( int selectedResolution ) {
        if ( selectedResolution < 0 || selectedResolution > mPhotoResolutionValues.size() ) {
            return (PhotoResolution) mPhotoResolutionValues.elementAt(0);
        } else {
            return (PhotoResolution) mPhotoResolutionValues.elementAt(selectedResolution);
        }
    }

    public String[] getResolutionList() {
        int count = mPhotoResolutionValues.size();
        String[] resolutions = new String[count];
        for (int i = 0; i < count; i++) {
            resolutions[i] = mPhotoResolutionValues.elementAt(i).toString();
        }
        return resolutions;
    }

    private void loadDefaultResolutions() {
        mPhotoResolutionValues.addElement(new PhotoResolution(320, 240, false));
        mPhotoResolutionValues.addElement(new PhotoResolution(640, 480, false));
        mPhotoResolutionValues.addElement(new PhotoResolution(1024, 768, false));
        mPhotoResolutionValues.addElement(new PhotoResolution(1280, 960, false));
    }

    private void addNativeResolution() {
        String resolutionString = System.getProperty("camera.resolutions");
        if ( resolutionString != null ) {
            int startIndex = resolutionString.lastIndexOf(':');
            int splitIndex = resolutionString.indexOf('x', startIndex);
            if ( startIndex > 0 && splitIndex > 0 && startIndex < splitIndex ) {
                try {
                    String tempx = resolutionString.substring(startIndex+1, splitIndex);
                    String tempy = resolutionString.substring(splitIndex+1, resolutionString.length());
                    int x = Integer.valueOf(tempx).intValue();
                    int y = Integer.valueOf(tempy).intValue();
                    insertSorted(new PhotoResolution(x, y, true));
                } catch ( Exception ex ) { //in case resolution string format was different than assumed
                    // ignore and proceed as native not available
                }
            }
        }
    }

    private void insertSorted( PhotoResolution newResolution ) {
        Enumeration resolutions =  mPhotoResolutionValues.elements();
        while( resolutions.hasMoreElements() ) {
            PhotoResolution resolution = (PhotoResolution) resolutions.nextElement();
            if ( resolution.getMegapixel() > newResolution.getMegapixel() ) {
                mPhotoResolutionValues.insertElementAt(newResolution, mPhotoResolutionValues.indexOf(resolution));
                break;
            }
            if ( !resolutions.hasMoreElements() ) {
                mPhotoResolutionValues.addElement(newResolution);
                break;
            }
        }
    }

    public static class PhotoResolution {

        private int mX = 0;
        private int mY = 0;
        private boolean mIsNative = false;

        public PhotoResolution( int x, int y, boolean isNative ) {
            mX = x;
            mY = y;
            mIsNative = isNative;
        }

        public int getWidth() {
            return mX;
        }

        public int getHeight() {
            return mY;
        }

        public double getMegapixel() {
            // convert resolution to megapixel with one decimal position
            int temp = getWidth()*getHeight();
            double megaPixel = getWidth()*getHeight();
            temp = (temp / 10000)%10;
            if ( temp < 5 ) { // round down
                temp = (int)((int)(megaPixel/100000))*100000;
            } else { // round up
                temp = (int)((int)(megaPixel/100000+0.5))*100000;
            }
            megaPixel = (double)((double)temp/1000000);
            return megaPixel;
        }

        public boolean isNative() {
            return mIsNative;
        }

        public String toString() {
            double megaPixel = getMegapixel();
            String nativeIndicator = (mIsNative ? "(" + Resources.NATIVE_RESOLUTION + ")": "");
            return getWidth() + "x" + getHeight() + " (" + megaPixel + "M)" + nativeIndicator;
        }
    }
}

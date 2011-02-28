package br.org.indt.ndg.lwuit.ui.style;


public class StyleUtils {

    public static void getRGB( int color, int[] rgb ) {
        final int maskRed = 0xff0000;
        final int maskGreen = 0xff00;
        final int maskBlue = 0xff;
        rgb[0] = (color & maskRed)>>16;
        rgb[1] = (color & maskGreen)>>8;
        rgb[2] = (color & maskBlue);
    }

    public static int mixRGB(  int red, int green, int blue ) {
        int color = 0;
        color = red << 16;
        color = color | ( green << 8 );
        color = color | blue;
        return color;
    }
}

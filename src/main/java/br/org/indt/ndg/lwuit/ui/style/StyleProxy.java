package br.org.indt.ndg.lwuit.ui.style;

import com.sun.lwuit.Font;
import java.io.PrintStream;


abstract public class StyleProxy {
    public int bgUnselectedColor;
    public int bgSelectedStartColor;
    public int bgSelectedEndColor;
    public int selectedFontColor;
    public int unselectedFontColor;
    public Font selectedFont;
    public Font unselectedFont;



    public void writeSettings( PrintStream _out ) {
        _out.print("<bgUnselectedColor>");
        _out.print( String.valueOf( bgUnselectedColor ) );
        _out.println("</bgUnselectedColor>");
        _out.print("<bgSelectedStartColor>");
        _out.print( String.valueOf( bgSelectedStartColor ) );
        _out.println("</bgSelectedStartColor>");
        _out.print("<bgSelectedEndColor>");
        _out.print( String.valueOf( bgSelectedEndColor ) );
        _out.println("</bgSelectedEndColor>");
        _out.print("<selectedFontColor>");
        _out.print( String.valueOf( selectedFontColor ) );
        _out.println("</selectedFontColor>");
        _out.print("<unselectedFontColor>");
        _out.print( String.valueOf( unselectedFontColor ) );
        _out.println("</unselectedFontColor>");
    }
}

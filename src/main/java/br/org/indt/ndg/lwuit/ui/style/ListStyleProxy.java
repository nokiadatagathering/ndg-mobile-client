package br.org.indt.ndg.lwuit.ui.style;

import com.sun.lwuit.Font;
import com.sun.lwuit.plaf.Style;
import com.sun.lwuit.plaf.UIManager;


public class ListStyleProxy extends StyleProxy {
    private Style baseStyle;
    private Style selectedStyle;
    public Font secondarySelectedFont;
    public Font secondaryUnselectedFont;

    public ListStyleProxy() {
        baseStyle = UIManager.getInstance().getComponentStyle( "Container" );
        selectedStyle = UIManager.getInstance().getComponentSelectedStyle( "Container" );

        bgUnselectedColor = baseStyle.getBgColor();

        bgSelectedEndColor = UIManager.getInstance().getComponentStyle("").getFgColor();
        bgSelectedStartColor = selectedStyle.getBgColor();

        selectedFontColor = selectedStyle.getFgColor();
        unselectedFontColor = baseStyle.getFgColor();

        selectedFont = selectedStyle.getFont();
        unselectedFont = baseStyle.getFont();

        secondaryUnselectedFont = UIManager.getInstance().getComponentStyle( "Container2" ).getFont();
        secondarySelectedFont = UIManager.getInstance().getComponentSelectedStyle( "Container2" ).getFont();
    }

    public Style getBaseStyle() {
        return baseStyle;
    }

    public Style getSelectedStyle() {
        return selectedStyle;
    }

    public void updateFonts() {
        super.updateFonts();
        if( secondarySelectedFont.getStyle() == Font.STYLE_PLAIN ) {
            secondarySelectedFont = NDGStyleToolbox.getFont( NDGStyleToolbox.FONTSANS , NDGStyleToolbox.smallSize );
        } else if ( secondarySelectedFont.getStyle() == Font.STYLE_BOLD ) {
            secondarySelectedFont = NDGStyleToolbox.getFont( NDGStyleToolbox.FONTSANSBOLD , NDGStyleToolbox.smallSize );
        } else if ( secondarySelectedFont.getStyle() == Font.STYLE_ITALIC ) {
            secondarySelectedFont = NDGStyleToolbox.getFont( NDGStyleToolbox.FONTSANSBOLD , NDGStyleToolbox.smallSize );
        }

        if( secondaryUnselectedFont.getStyle() == Font.STYLE_PLAIN ) {
            secondaryUnselectedFont = NDGStyleToolbox.getFont( NDGStyleToolbox.FONTSANS , NDGStyleToolbox.smallSize );
        } else if ( secondaryUnselectedFont.getStyle() == Font.STYLE_BOLD ) {
            secondaryUnselectedFont = NDGStyleToolbox.getFont( NDGStyleToolbox.FONTSANSBOLD , NDGStyleToolbox.smallSize );
        } else if ( secondaryUnselectedFont.getStyle() == Font.STYLE_ITALIC ) {
            secondaryUnselectedFont = NDGStyleToolbox.getFont( NDGStyleToolbox.FONTSANSBOLD , NDGStyleToolbox.smallSize );
        }
    }
}

package br.org.indt.ndg.lwuit.ui.style;

import com.sun.lwuit.plaf.Style;
import com.sun.lwuit.plaf.UIManager;


public class MenuStyleProxy extends StyleProxy {
    private Style baseStyle;
    private Style selectedStyle;

    public MenuStyleProxy() {
        baseStyle = UIManager.getInstance().getComponentStyle( "Menu" );
        selectedStyle = UIManager.getInstance().getComponentSelectedStyle("Menu" );

        bgUnselectedColor = baseStyle.getBgColor();

        bgSelectedEndColor = UIManager.getInstance().getComponentStyle("").getFgColor();
        bgSelectedStartColor = selectedStyle.getBgColor();

        selectedFontColor = selectedStyle.getFgColor();
        unselectedFontColor = baseStyle.getFgColor();

        selectedFont = selectedStyle.getFont();
        unselectedFont = baseStyle.getFont();
    }

    public Style getBaseStyle() {
        return baseStyle;
    }

    public Style getSelectedStyle() {
        return selectedStyle;
    }
}

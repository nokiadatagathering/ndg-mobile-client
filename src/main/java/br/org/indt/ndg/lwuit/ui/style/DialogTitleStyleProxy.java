package br.org.indt.ndg.lwuit.ui.style;

import com.sun.lwuit.plaf.Style;
import com.sun.lwuit.plaf.UIManager;


public class DialogTitleStyleProxy extends StyleProxy {
    private Style baseStyle;
    private Style selectedStyle;

    public DialogTitleStyleProxy() {
        baseStyle = UIManager.getInstance().getComponentStyle( "DialogTitle" );
        selectedStyle = UIManager.getInstance().getComponentSelectedStyle( "DialogTitle" );

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
        return baseStyle;
    }
}

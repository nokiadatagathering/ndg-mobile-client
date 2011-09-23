package br.org.indt.ndg.lwuit.ui.renderers;

import br.org.indt.ndg.lwuit.model.RadioButtonGroupItem;
import br.org.indt.ndg.lwuit.ui.Screen;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import br.org.indt.ndg.mobile.Utils;
import com.sun.lwuit.Component;
import com.sun.lwuit.Font;
import com.sun.lwuit.List;
import com.sun.lwuit.RadioButton;
import com.sun.lwuit.layouts.BorderLayout;

/**
 *
 * @author damian.janicki
 */
public class RadioButtonListCellRenderer extends DefaultNDGListCellRenderer{


    private RadioButton rendererRadioButton = new RadioButton();

    public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
        if ( list.size() == 0 || value == null)
            return this;
        removeAll();
        RadioButtonGroupItem item = (RadioButtonGroupItem)value;

        Font font = null;
        if(!Utils.isS40()){
            font = Screen.getFontRes().getFont( item.getValue() );
        }

        if (isSelected) {
            rendererRadioButton.setFocus(true);
            if(font == null){
                font = NDGStyleToolbox.getInstance().listStyle.selectedFont;
            }
            rendererRadioButton.getStyle().setFont(font);
            rendererRadioButton.getStyle().setFgColor( NDGStyleToolbox.getInstance().listStyle.selectedFontColor );
            getStyle().setBgPainter(m_focusBGPainter);
        } else {
            rendererRadioButton.setFocus(false);
            if(font == null){
                font = NDGStyleToolbox.getInstance().listStyle.unselectedFont;
            }
            rendererRadioButton.getStyle().setFont(font);
            rendererRadioButton.getStyle().setFgColor( NDGStyleToolbox.getInstance().listStyle.unselectedFontColor );
            getStyle().setBgPainter(m_bgPainter);
        }

        rendererRadioButton.setText(item.getDisplayName());
        rendererRadioButton.setSelected(item.isChecked());
        addComponent(BorderLayout.CENTER, rendererRadioButton);

        return this;
    }

}

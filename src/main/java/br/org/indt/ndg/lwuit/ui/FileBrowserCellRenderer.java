package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import com.sun.lwuit.Component;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.layouts.BorderLayout;

/**
 *
 * @author mluz
 */
class FileBrowserCellRenderer extends DefaultNDGListCellRenderer {

    protected final Label m_rendererLabel = new Label();

    public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
        removeAll();
        if (value != null) {
            String text = value.toString();
            m_rendererLabel.setText(text);
            m_rendererLabel.setTickerEnabled(isSelected);
            if (isSelected) {
                m_rendererLabel.setFocus(true);
                m_rendererLabel.getStyle().setFont(NDGStyleToolbox.getInstance().listStyle.selectedFont);
                m_rendererLabel.getStyle().setFgColor(NDGStyleToolbox.getInstance().listStyle.selectedFontColor);
                getStyle().setBgPainter(m_focusBGPainter);
            } else {
                m_rendererLabel.setFocus(false);
                m_rendererLabel.getStyle().setFont(NDGStyleToolbox.getInstance().listStyle.unselectedFont);
                m_rendererLabel.getStyle().setFgColor(NDGStyleToolbox.getInstance().listStyle.unselectedFontColor);
                getStyle().setBgPainter(m_bgPainter);
            }
        }
        addComponent(BorderLayout.CENTER, m_rendererLabel);
        return this;
    }

}
package br.org.indt.ndg.lwuit.ui;


import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import com.sun.lwuit.Component;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.list.ListCellRenderer;

/**
 *
 * @author mluz
 */
class FileBrowserCellRenderer extends DefaultNDGListCellRenderer {

    public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
        String text = value.toString();
        Label label = new Label();
        label.setText(text);
        label.setTickerEnabled(isSelected);
        if (isSelected) {
            label.setFocus(true);
            label.getStyle().setFont(NDGStyleToolbox.getInstance().listStyle.selectedFont);
            label.getStyle().setFgColor(NDGStyleToolbox.getInstance().listStyle.selectedFontColor);
            label.getStyle().setBgPainter(focusBGPainter);
        } else {
            label.setFocus(false);
            label.getStyle().setFont(NDGStyleToolbox.getInstance().listStyle.unselectedFont);
            label.getStyle().setFgColor(NDGStyleToolbox.getInstance().listStyle.unselectedFontColor);
            label.getStyle().setBgPainter(bgPainter);
        }
        return label;
    }

        public Component getListFocusComponent(List list) {
        return null;
    }

}
package br.org.indt.ndg.lwuit.ui.renderers;

public class FileBrowserCellRenderer extends SimpleListCellRenderer {

    protected String getText(Object value) {
        String result = "";
        if (value != null)
            result = value.toString();
        return result;
    }
}
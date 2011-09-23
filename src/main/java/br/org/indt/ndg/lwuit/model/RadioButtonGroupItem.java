package br.org.indt.ndg.lwuit.model;

/**
 *
 * @author damian.janicki
 */
public class RadioButtonGroupItem {
    private String displayName;
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    private boolean checked = false;

    public RadioButtonGroupItem(String name, String value) {
        this.displayName = name;
        this.value = value;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean isChecked) {
        this.checked = isChecked;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String name) {
        this.displayName = name;
    }
}

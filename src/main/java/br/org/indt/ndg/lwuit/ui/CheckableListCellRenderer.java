package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.model.DisplayableModel;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import com.sun.lwuit.CheckBox;
import com.sun.lwuit.Component;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.layouts.BorderLayout;
import java.util.Vector;

/**
 *
 * @author Alexandre Martini
 */
public class CheckableListCellRenderer extends DefaultNDGListCellRenderer{

    protected Label label;
    protected Vector checkboxes = new Vector();
    protected DisplayableModel disp;
    protected int realSize;
    static final int TOUCH_SCREEN_VERTICAL_PADDING = 10;

    public CheckableListCellRenderer(int realSize) {
        super();
        this.realSize = realSize;
    }

    public CheckableListCellRenderer() {
        super();
        this.realSize = 0;
    }

    public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
        if ( list.size() == 0 ) return this;
        
        disp = (DisplayableModel) value;
        label = getCheckBox(disp, index);
        label.setVisible(true);
        
        if (isSelected) {
            setFocus(true);
            label.setFocus(true);
            label.getStyle().setFont(NDGStyleToolbox.getInstance().listStyle.selectedFont);
            label.getStyle().setFgColor( NDGStyleToolbox.getInstance().listStyle.selectedFontColor );
            getStyle().setBgPainter(focusBGPainter);
        } else {
            setFocus(false);
            label.setFocus(false);
            label.getStyle().setFont(NDGStyleToolbox.getInstance().listStyle.unselectedFont);
            label.getStyle().setFgColor( NDGStyleToolbox.getInstance().listStyle.unselectedFontColor );
            getStyle().setBgPainter(bgPainter);
        }

        addComponent(BorderLayout.CENTER, label);

        return this;
    }

    public Component getListFocusComponent(List list) {
        if (label != null) {
            label.setText(" ");
        }
        this.setFocus(true);
        return this;
    }

    /**
     *
     * @return an array with the status of each list entry
     */
    public boolean[] getSelectedFlags() {
        if (checkboxes.size() < realSize) {
            for (int i = 0; i < realSize; i++) {
                getCheckBox(disp, i);
            }
        }
        int size = checkboxes.size();
        boolean[] selected = new boolean[size];
        for(int i = 0; i < size; i++){
            CheckBox c = (CheckBox) checkboxes.elementAt(i);
            if(c.isSelected())
                selected[i] = true;
            else
                selected[i] = false;
        }
        return selected;

    }

    public void markAll() {
        if (checkboxes.size() < realSize) {
            for (int i = 0; i < realSize; i++) {
                getCheckBox(disp, i);
            }
        }
        int size = checkboxes.size();
        for(int i = 0; i < size; i++){
            CheckBox c = (CheckBox) checkboxes.elementAt(i);
            c.setSelected(true);
        }
    }

    public void unmarkAll(){
        int size = checkboxes.size();
        for(int i = 0; i < size; i++){
            CheckBox c = (CheckBox) checkboxes.elementAt(i);
            c.setSelected(false);
        }
    }

    public void updateCheckbox(int index) {
        if(checkboxes.size()>0){
            CheckBox c = (CheckBox) checkboxes.elementAt(index);
            c.setSelected(!c.isSelected());
        }
    }

    protected CheckBox getCheckBox(DisplayableModel disp, int index){
        CheckBox c, cBox;
        try{
            c = (CheckBox) checkboxes.elementAt(index);
            cBox = new CheckBox(disp.getDisplayableName());
            cBox.setSelected(c.isSelected());
            return cBox;
        }
        catch(ArrayIndexOutOfBoundsException outExc){
            c = new CheckBox(disp.getDisplayableName());
            checkboxes.addElement(c);
            return c;
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.extended;

import com.sun.lwuit.ButtonGroup;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.plaf.Border;
import com.sun.lwuit.plaf.Style;
import com.sun.lwuit.plaf.UIManager;

/**
 *
 * @author mluz
 */
public class ChoiceGroup extends Container implements ActionListener {
    
    public static int EXCLUSIVE = 2;
    public static int MULTIPLE = 1;
    private int type;
    private boolean[] marks; // for multiple choice only
    private String[] texts;
    private Component[] choices;
    private int selectedIndex; // for exclusive choice only
    private ButtonGroup radioGroup; // for exclusive choice only
    private ChoiceGroupListener cgListener;

    public void setCgListener(ChoiceGroupListener cgListener) {
        this.cgListener = cgListener;
    }

    private ChoiceGroup() {
        super(new BoxLayout(BoxLayout.Y_AXIS));
        getStyle().setBgTransparency(255); // no transparency = solid
        getStyle().setBorder(Border.createRoundBorder(8, 8, UIManager.getInstance().getComponentStyle("CheckBox").getFgColor()));
        Style tfStyle = UIManager.getInstance().getComponentStyle("TextField");
        getStyle().setMargin(tfStyle.getMargin(Component.TOP), tfStyle.getMargin(Component.BOTTOM), tfStyle.getMargin(Component.LEFT), tfStyle.getMargin(Component.RIGHT));
        getStyle().setPadding(tfStyle.getPadding(Component.TOP), tfStyle.getPadding(Component.BOTTOM), tfStyle.getPadding(Component.LEFT), tfStyle.getPadding(Component.RIGHT));
    }

    /**
     * Constructor for multiple choice
     **/
    public ChoiceGroup(String[] texts, boolean[] marks) {
        this();
        this.type = MULTIPLE;
        this.texts = texts;
        this.marks = marks;
        initChoiceGroup();
    }

    /**
     * Constructor for multiple choice
     **/
    public ChoiceGroup(String[] texts, int selectedIndex) {
        this();
        type = EXCLUSIVE;
        this.texts = texts;
        this.selectedIndex = selectedIndex;
        radioGroup = new ButtonGroup();
        initChoiceGroup();
    }

    private void initChoiceGroup() {
        if (type == MULTIPLE) {
            choices = new Component[texts.length];
            for (int i=0; i<texts.length; i++) {
                choices[i] = new br.org.indt.ndg.lwuit.extended.CheckBox(texts[i]);
                addComponent(choices[i]);
                ((br.org.indt.ndg.lwuit.extended.CheckBox)choices[i]).setSelected(marks[i]);
                ((br.org.indt.ndg.lwuit.extended.CheckBox)choices[i]).addActionListener(this);
            }
        } else if (type == EXCLUSIVE) {
            choices = new Component[texts.length];
            for (int i=0; i<texts.length; i++) {
                choices[i] = new br.org.indt.ndg.lwuit.extended.RadioButton(texts[i]);
                addComponent(choices[i]);
                if (i == selectedIndex)
                    ((br.org.indt.ndg.lwuit.extended.RadioButton)choices[i]).setSelected(true);
                ((br.org.indt.ndg.lwuit.extended.RadioButton)choices[i]).addActionListener(this);
                radioGroup.add(((br.org.indt.ndg.lwuit.extended.RadioButton)choices[i]));
                ((RadioButton)choices[i]).addActionListener(this);
            }
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (type == MULTIPLE) {
            CheckBox cb = (CheckBox)ae.getSource();
            for (int i=0; i<choices.length; i++) {
                if (cb == choices[i]) {
                    marks[i] = cb.isSelected();
                    if ((cgListener != null)&&(!ae.isConsumed())) {
                        ae.consume();
                        cgListener.itemChoosed(i);
                    }
                    break;
                }
            }
        } else if (type == EXCLUSIVE) {
            if (ae.getSource() instanceof RadioButton) {
                if ((cgListener != null)&&(!ae.isConsumed())) {
                    ae.consume();
                    cgListener.itemChoosed(radioGroup.getSelectedIndex());
                }
            }
        }
    }

    public boolean[] getMarks() {
        if (type == MULTIPLE)
            return marks;
        else
            return null;
    }

    public int getSelectedIndex() {
        if (type == EXCLUSIVE)
            return radioGroup.getSelectedIndex();
        else
            return -1;
    }

    public void requestFocus() {
        if ((choices != null) && (choices.length >0)) {
            choices[0].requestFocus();
        }
    }

    public int size() {
        if (choices != null) 
            return choices.length;
        else
            return -1;
    }

    public void setItemFocused(int i) {
        choices[i].requestFocus();
    }
}

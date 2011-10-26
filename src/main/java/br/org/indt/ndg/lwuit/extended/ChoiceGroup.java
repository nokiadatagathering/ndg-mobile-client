/*
*  Nokia Data Gathering
*
*  Copyright (C) 2011 Nokia Corporation
*
*  This program is free software; you can redistribute it and/or
*  modify it under the terms of the GNU Lesser General Public
*  License as published by the Free Software Foundation; either
*  version 2.1 of the License, or (at your option) any later version.
*
*  This program is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
*  Lesser General Public License for more details.
*
*  You should have received a copy of the GNU Lesser General Public License
*  along with this program.  If not, see <http://www.gnu.org/licenses/
*/

package br.org.indt.ndg.lwuit.extended;

import com.sun.lwuit.ButtonGroup;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.FocusListener;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.plaf.Border;
import com.sun.lwuit.plaf.Style;
import com.sun.lwuit.plaf.UIManager;

/**
 *
 * @author mluz
 */
public class ChoiceGroup extends Container implements ActionListener, FocusListener {
    
    public static int EXCLUSIVE = 2;
    public static int MULTIPLE = 1;
    protected int type;
    private boolean[] marks; // for multiple choice only
    protected String[] texts;
    protected final Component[] choices;
    protected int selectedIndex; // for exclusive choice only
    protected ButtonGroup radioGroup; // for exclusive choice only
    private ChoiceGroupListener cgListener;
    private ChoiceGroupSelectionListener selectionListener;

    public void setCgListener(ChoiceGroupListener cgListener) {
        this.cgListener = cgListener;
    }

    public void setSelectionListener(ChoiceGroupSelectionListener listener){
        selectionListener = listener;
    }

    protected ChoiceGroup( int textSize ) {
        super(new BoxLayout(BoxLayout.Y_AXIS));
        this.choices = new Component[textSize];
        getStyle().setBgTransparency(255); // no transparency = solid
        getStyle().setBorder(Border.createRoundBorder(8, 8, UIManager.getInstance().getComponentStyle("RadioButton").getFgColor()));
        Style tfStyle = getStyle();
        getStyle().setMargin(tfStyle.getMargin(Component.TOP), tfStyle.getMargin(Component.BOTTOM), tfStyle.getMargin(Component.LEFT), tfStyle.getMargin(Component.RIGHT));
        getStyle().setPadding(tfStyle.getPadding(Component.TOP), tfStyle.getPadding(Component.BOTTOM), tfStyle.getPadding(Component.LEFT), tfStyle.getPadding(Component.RIGHT));
        super.setFocusable(false);
    }

    /**
     * Constructor for multiple choice
     **/
    public ChoiceGroup(String[] texts, boolean[] marks) {
        this(texts.length);
        this.texts = texts;
        this.type = MULTIPLE;
        this.marks = marks;
        initChoiceGroup();
    }

    /**
     * Constructor for multiple choice
     **/
    public ChoiceGroup(String[] texts, int selectedIndex) {
        this(texts.length);
        this.texts = texts;
        this.type = EXCLUSIVE;
        this.selectedIndex = selectedIndex;
        this.radioGroup = new ButtonGroup();
        initChoiceGroup();
    }

    private void initChoiceGroup() {
        if (type == MULTIPLE) {
            CheckBox checkBox = null;
            for (int i=0; i<texts.length; i++) {
                checkBox = new CheckBox(texts[i]);
                
                choices[i] = checkBox;
                checkBox.setSelected(marks[i]);
                checkBox.addActionListener(this);
                checkBox.addFocusListener(this);
                addComponent(checkBox);
            }
        } else if (type == EXCLUSIVE) {
            RadioButton radioButton = null;
            for (int i=0; i<texts.length; i++) {
                radioButton = new RadioButton(texts[i]);

                choices[i] = radioButton;
                if (i == selectedIndex)
                    radioButton.setSelected(true);
                radioButton.addActionListener(this);
                radioButton.addFocusListener(this);
                radioGroup.add(radioButton);
                addComponent(radioButton);
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

    public void setSelectedIndex(int index) {
        if (type == EXCLUSIVE)
            radioGroup.setSelected(index);
    }

    public void requestFocus() {
        if ( choices.length > 0 ) {
            getFirstChoiceComponent().requestFocus();
        }
    }

    public int size() {
        return choices.length;
    }

    public void setItemFocused(int i) {
        choices[i].requestFocus();
    }

    public void setVisible( boolean visible ) {
        for (int i = 0; i < choices.length; i++) {
            choices[i].setVisible(visible);
        }
        super.setVisible(visible);
    }

    public void setFocusable( boolean focusable ) {
        if ( choices == null ) // do not remove, is invoked in super constructor
            return;
        for ( int i = 0; i < choices.length; i++ ) {
            choices[i].setFocusable(focusable);
        }
        // container shall stay always NOT focusable
    }

    /**
     * Removes focus transition on key up/down on first/last group element
     * Cannot be undone
     */
    public void blockLosingFocusUp() {
        getFirstChoiceComponent().setNextFocusUp(getFirstChoiceComponent());
    }
    public void blockLosingFocusDown() {
        getLastChoiceComponent().setNextFocusDown(getLastChoiceComponent());
    }

    private Component getFirstChoiceComponent() {
        return choices[0];
    }

    private Component getLastChoiceComponent() {
        return choices[choices.length-1];
    }

    public void focusGained(Component cmpnt) {
        for (int idx = 0; idx < choices.length; idx++) {
            if(selectionListener != null && choices[idx] == cmpnt){
                selectionListener.itemSelected(idx);
                return;
            }
        }
    }

    public void focusLost(Component cmpnt) {
        //do nothing
    }

    public int getFocusedIndex(){
        for (int idx = 0; idx < choices.length; idx++) {
            if(choices[idx].hasFocus()){
                return idx;
            }
        }
        return -1;
    }
}

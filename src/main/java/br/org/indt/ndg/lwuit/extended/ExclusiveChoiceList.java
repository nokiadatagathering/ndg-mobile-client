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

import br.org.indt.ndg.lwuit.ui.InterviewForm.RadioChoiceItem;
import com.sun.lwuit.List;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.list.ListModel;
import java.util.Enumeration;
import java.util.Vector;

public class ExclusiveChoiceList extends List implements PointerListener, ActionListener {

    private final Vector mListeners = new Vector(); /*<ExclusiveChoiceListListener>*/

    public ExclusiveChoiceList(ListModel model) {
        super(model);
        addActionListener(this);
    }

    public void actionPerformed(ActionEvent evt) {
        setHandlesInput(true); // prevents of loosing focus
    }

    public void pointerPressed(int x, int y) {
        super.pointerPressed(x,y);
    }

    /**
     * Assumptions when listener should be informed:
     * - it MUST have details
     * - if item has been just selected and details are empty
     * - if item is selected and right 1/5 od component is pointed
     */
    public void pointerReleased(int x, int y) {
        RadioChoiceItem item = (RadioChoiceItem)getModel().getItemAt(getModel().getSelectedIndex());
        // check if More Details should be requested
        if ( item != null ) {
            if ( item.hasMoreDetails() && x > (int)(0.8 * getWidth()) && item.isChecked() ) { // handle only details
                fireDetailsRequested();
            } else { // handle only selection
                super.pointerReleased(x, y);
            }
        } else {
            super.pointerReleased(x, y);
        }
    }

    private void fireDetailsRequested() {
        Enumeration listeners = mListeners.elements();
        while ( listeners.hasMoreElements() ) {
            ExclusiveChoiceListListener listener = (ExclusiveChoiceListListener)listeners.nextElement();
            listener.detailsRequested();
        }
    }

    public void addExclusiveChoiceListListener( ExclusiveChoiceListListener listener ) {
        mListeners.addElement(listener);
    }

    public void removeExclusiveChoiceListListener( ExclusiveChoiceListListener listener ) {
        mListeners.removeElement(listener);
    }

    public void removeAllPointerListeners() {
        mListeners.removeAllElements();
    }

    public static interface ExclusiveChoiceListListener {
        public void detailsRequested();
    }

};

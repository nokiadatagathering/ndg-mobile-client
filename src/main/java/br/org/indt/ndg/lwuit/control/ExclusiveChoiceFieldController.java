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

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.extended.ExclusiveChoiceList;
import br.org.indt.ndg.lwuit.ui.renderers.RadioButtonRenderer;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.list.ListModel;
import java.util.Enumeration;
import java.util.Vector;

public class ExclusiveChoiceFieldController {

    public static final int FINALIZE = 1;
    public static final int MORE_DETAILS = 2;

    private static ExclusiveChoiceFieldController mInstance = new ExclusiveChoiceFieldController();

    private String mQuestionText;
    private int mMaxQuestionLength;
    private ListModel mDataModel;
    private ExclusiveChoiceList mList;
    private Vector mActionListners = new Vector(); /*<ActionListener>*/

    private ExclusiveChoiceFieldController(){}

    public static ExclusiveChoiceFieldController getInstance(){
        return mInstance;
    }

    public void setData( ListModel dataModel, String question, int maxQuestionLength ) {
        mQuestionText = question;
        mMaxQuestionLength = maxQuestionLength;
        mDataModel = dataModel;
        mList = new ExclusiveChoiceList(mDataModel);
        mList.setListCellRenderer(new RadioButtonRenderer());
    }

    public void setData(ExclusiveChoiceList list, ListModel dataModel, String question, int maxQuestionLength){
        mQuestionText = question;
        mMaxQuestionLength = maxQuestionLength;
        mDataModel = dataModel;
        mList = list;
    }

    public String getQuestion() {
        return mQuestionText;
    }

    public int getMaxQuestionLength() {
        return mMaxQuestionLength;
    }

    public ListModel getModel() {
        return mDataModel;
    }

    public void addActionListener( ActionListener listener ) {
        mActionListners.addElement(listener);
    }

    public ExclusiveChoiceList getListForModel() {
        return mList;
    }

    public void removeActionListener( ActionListener listener ) {
        mActionListners.removeElement(listener);
    }

    public void removeAllActionListeners() {
        mActionListners.removeAllElements();
    }

    public void finalizeSelection() {
        Enumeration actionListeners = mActionListners.elements();
        while ( actionListeners.hasMoreElements() ) {
            ActionListener listener = (ActionListener)actionListeners.nextElement();
            ActionEvent evt = new ActionEvent(this, FINALIZE);
            listener.actionPerformed(evt);
        }
    }

    public void handleMoreDetails() {
        Enumeration actionListeners = mActionListners.elements();
        while ( actionListeners.hasMoreElements() ) {
            ActionListener listener = (ActionListener)actionListeners.nextElement();
            ActionEvent evt = new ActionEvent(this, MORE_DETAILS);
            listener.actionPerformed(evt);
        }
    }

}

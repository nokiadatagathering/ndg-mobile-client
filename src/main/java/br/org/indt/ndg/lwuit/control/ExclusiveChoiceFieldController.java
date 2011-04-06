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

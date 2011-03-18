package br.org.indt.ndg.lwuit.model;

import java.util.Vector;

/**
 *
 * @author mluz, amartini, mturiel
 */
public class Category {
    private String mName;
    private String mId;
    private Vector mQuestions;

    public Category( String aName, String aId ) {
        mName = aName;
        mId = aId;
    }

    public Vector getQuestions() {
        return mQuestions;
    }

    public void setQuestions(Vector questions) {
        mQuestions = questions;
    }

    public boolean isFullFilled() {
        for ( int i = 0; i< mQuestions.size(); i++ ) {
            if( ((NDGQuestion)mQuestions.elementAt(i)).getVisited() == true )
                return true;
        }
        return false;
    }

    public String getName() {
        return mName;
    }

    public String getId() {
        return mId;
    }
}

package br.org.indt.ndg.lwuit.model;

import java.util.Vector;

/**
 *
 * @author mluz
 */
public class Survey implements DisplayableItem{

    private String mDisplay;
    private String mTitle;
    private int mId;
    private Vector mCategories = new Vector();

    public Vector getCategories() {
        return mCategories;
    }

    public void addCategory( Category category ) {
        mCategories.addElement(category);
    }

    public String getDisplayableName() {
        return mTitle;
    }

    public void setIdNumber(int aId) {
        mId = aId;
    }

    public void setDisplayId(String aDisplay) {
        mDisplay = aDisplay;
    }

    public void setTitle(String aTitle) {
        mTitle = aTitle;
    }

    public int getId() {
        return mId;
    }
}

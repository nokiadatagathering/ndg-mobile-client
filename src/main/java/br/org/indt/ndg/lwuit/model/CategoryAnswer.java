package br.org.indt.ndg.lwuit.model;

import java.util.Hashtable;
import java.util.Vector;


public class CategoryAnswer {
    private String mName;
    private String mId;
    private Vector mSubCategory;

    public CategoryAnswer() {
        mSubCategory = new Vector();
    }

    public void setName( String aName ) {
        mName = aName;
    }

    public String getName( ) {
        return mName;
    }

    public void setId( String aId ) {
        mId = aId;
    }

    public String getId() {
        return mId;
    }

    public int getSubcategoriesCount() {
        return mSubCategory.size();
    }

    public Hashtable getSubCategoryAnswers( int index ) {
        return (Hashtable)mSubCategory.elementAt(index);
    }

    public void put(String aId, NDGAnswer aAnswer) {
        if ( mSubCategory.size() == 0 ) {
            mSubCategory.addElement( new Hashtable() );
        }
        ((Hashtable)mSubCategory.elementAt(0)).put( aId, aAnswer );
    }

    public void put(int aIndex, String aId, NDGAnswer aAnswer) {
        if( mSubCategory.size() <= aIndex ) {
            mSubCategory.addElement(new Hashtable());
        }
        ((Hashtable)mSubCategory.elementAt(aIndex)).put( aId, aAnswer );
    }

    public void remove( int aIndex ) {
        if(  aIndex < mSubCategory.size() ) {
            mSubCategory.removeElementAt( aIndex );
        }
    }
}

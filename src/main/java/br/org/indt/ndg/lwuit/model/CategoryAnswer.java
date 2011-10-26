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

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

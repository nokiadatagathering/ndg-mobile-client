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

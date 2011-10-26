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


public class CategoryConditional extends Category{

    private String mConditionQuestion;
    private int mQuantity;

    public CategoryConditional( String aName, String aId, String aConditionQuestion ) {
        super( aName, aId );
        mConditionQuestion = aConditionQuestion;
        mQuantity = 0;
    }

    public String getConditionQuestion() {
        return mConditionQuestion;
    }

    public void setQuantity( int aQuanity ) {
        mQuantity = aQuanity;
    }

    public int getQuantity() {
        return mQuantity;
    }
}

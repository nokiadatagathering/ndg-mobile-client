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

public abstract class NumericQuestion extends NDGQuestion {
    private int mLength;

    public int getLength() {
        return mLength;
    }

    public void setLength(int length) {
        mLength = length;
    }

    public boolean passConstraints( String input ) {
        if ( passLowConstraint( input ) )
            if ( passHighConstraint( input ) )
                return true;
            else
                return false;
        else
            return false;
    }

    abstract public void setHighConstraint( String _high );
    abstract public void setLowConstraint( String _low );
    abstract public String getType();

    abstract protected boolean passLowConstraint( String aAnswer );
    abstract protected boolean passHighConstraint( String aAnswer );
}

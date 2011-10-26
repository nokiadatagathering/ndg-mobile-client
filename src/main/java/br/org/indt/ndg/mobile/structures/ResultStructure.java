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

package br.org.indt.ndg.mobile.structures;

import br.org.indt.ndg.lwuit.model.CategoryAnswer;
import java.util.Vector;
import java.util.Hashtable;

public class ResultStructure {

    private String latitude = null;
    private String longitude = null;
    public String getLongitude(){ return longitude; }
    public void setLongitude(String _longitude) { longitude = _longitude; }
    public String getLatitude(){ return latitude; }
    public void setLatitude(String _latitude) { latitude = _latitude; }

    private Vector/*<CategoryAnwser>*/ answers = new Vector();

    public boolean isLocationValid(){
        if(latitude == null && longitude == null){
            return false;
        }else{
            return true;
        }
    }

    public void resetLocation(){
        longitude = null;
        latitude = null;
    }

    public void resetAnswers(){
        answers.removeAllElements();
    }

    public void addAnswer( CategoryAnswer _answer) {
        answers.addElement(_answer);
    }

    public Hashtable getAnswers( int _category ) {
        return (Hashtable) answers.elementAt(_category );
    }

    public CategoryAnswer getCategoryAnswers( String _categoryId ) {
        return (CategoryAnswer) answers.elementAt( Integer.parseInt(_categoryId) - 1 );
    }

    public Vector/*<CategoryAnwser>*/ getAllAnwsers() {
        return answers;
    }
}
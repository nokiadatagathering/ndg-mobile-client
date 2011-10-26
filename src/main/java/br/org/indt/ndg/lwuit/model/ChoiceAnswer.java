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

import java.io.PrintStream;
import java.util.Vector;
import java.util.Hashtable;

public class ChoiceAnswer extends NDGAnswer {
    private Vector selectedIndexes = new Vector();
    private Hashtable selectedOthers = new Hashtable();


    public ChoiceAnswer() {
    }

    public Vector getSelectedIndexes() {
        return selectedIndexes;
    }

    public void setSelectedIndex( Vector aSelectedIndexes ) {
        selectedIndexes = aSelectedIndexes;
    }

    public void addSelectedIndex(String _index) {
        selectedIndexes.addElement(_index);
    }

    public String getOtherText(String _index) {
        return (String) selectedOthers.get(_index);
    }

    public void setOtherText( Hashtable aOtherText ) {
        selectedOthers = aOtherText;
    }

    public void addOtherText(String _index, String _text) {
        selectedOthers.put(_index, _text);
    }

    public void save( PrintStream _output ){
        if ( !selectedIndexes.isEmpty() ) {
            if ( selectedOthers.isEmpty() ) {//item
                for( int i = 0; i< selectedIndexes.size(); i++ ) {
                    _output.print("<item>");
                    _output.print( (String)selectedIndexes.elementAt(i) );
                    _output.println("</item>");
                }
            } else { //other
                for( int i =0; i< selectedIndexes.size(); i++ ) {
                    if( selectedOthers.containsKey( (String)selectedIndexes.elementAt(i) ) ) {
                        _output.print("<other " + "index=\"" + (String)selectedIndexes.elementAt(i) + "\">");
                        _output.print((String)selectedOthers.get( (String)selectedIndexes.elementAt(i) ) );
                        _output.println("</other>");
                    } else {
                        _output.print("<item>");
                        _output.print( (String)selectedIndexes.elementAt(i) );
                        _output.println("</item>");
                    }
                }
            }
        }
    }
}
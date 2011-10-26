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
public class DescriptiveQuestion extends NDGQuestion {

    private int length;
    private Vector choices = new Vector();
    private Vector others;
    private Vector othersText;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getChoiceText(int index) {
        if(index<choices.size()){
            return (String)choices.elementAt(index);
        }
        return "";
    }

    public Vector getChoices() {
        return choices;
    }

    public void setChoices(Vector choices) {
        this.choices = choices;
    }

    public void addChoice(String choices) {
        this.choices.addElement( choices );
    }

    public Vector getOthers() {
        return others;
    }

    public void setOthers(Vector others) {
        this.others = others;
    }

    public void setOthersText(Vector othersText) {
        this.othersText = othersText;
    }

    public Vector getOthersText() {
        return othersText;
    }

    public NDGAnswer getAnswerModel() {
        return new DescriptiveAnswer();
    }
}

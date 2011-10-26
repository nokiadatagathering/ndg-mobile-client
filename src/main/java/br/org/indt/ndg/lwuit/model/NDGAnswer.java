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

/**
 *
 * @author alexandre martini
 */
public abstract class NDGAnswer {
    private int answer_id;
    private String type;
    private boolean visited;

    private Object value;


    public Object getValue(){
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setType(String _type) {
        type = _type;
    }

    public void setId( int _id ) {
        answer_id = _id;
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return answer_id;
    }

    public boolean getVisited() {
        return visited;
    }

    public void setVisited(String _boolean) {
        if (_boolean.equals("true")) visited=true;
        else visited=false;
    }

    public void setVisited( boolean  aVisited ) {
        visited = aVisited;
    }

    abstract public void save( PrintStream _output );
}

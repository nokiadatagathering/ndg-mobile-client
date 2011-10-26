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

/**
 *
 * @author mluz
 */
public class Result implements DisplayableItem, CheckableItem {

    private String name;
    private String fileName;
    private boolean isChecked = false;

    public Result( String aName ) {
        name = aName;
    }

    public String getName() {
        return name;
    }

    public void setPhisicallyFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getPhisicallyFileName(){
        return fileName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayableName() {
        return getName();
    }

    public void setChecked( boolean check ) {
        isChecked = check;
    }

    public boolean isChecked() {
        return isChecked;
    }
}

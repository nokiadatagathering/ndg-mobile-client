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
 * @author alexandre martini
 */
public abstract class NDGQuestion implements DisplayableItem{

    private int question_id;
    private boolean visited = false;
    private boolean firstTime = true;
    private boolean isNew = true;
    private boolean editable = true;
    private boolean skiped = false;
    private String description;
    private String type;
    private String categoryId;
    private String categoryName;

    public void setSkiped(boolean _val) {
        skiped = _val;
    }

    public boolean getSkiped() {
        return skiped;
    }

    public void setIdNumber(int _id_number) {
        this.question_id = _id_number;
    }

    public void setType(String _type) {
        this.type = _type;
    }

    public void setVisited(boolean _visited) {
        this.visited = _visited;
    }

    public void setVisited(boolean _visited, int selected) {
        this.visited = _visited;

        if (firstTime && visited) {
            firstTime = false;
        }
    }

    public void setFirstTime() {
        firstTime = true;
    }

    public void setIsNew(boolean _isNew) {
        isNew = _isNew;
    }

    public void setEdit(String _edit) {
        if (_edit.equals("false")) this.editable = false;
    }

    public void setDescription(String _description) {
        this.description = _description;
    }

    public int getIdNumber() {
        return this.question_id;
    }

    public String getType() {
        return this.type;
    }

    public boolean getVisited() {
        return this.visited;
    }

    public boolean getFirstTime() {
        return this.firstTime;
    }

    public boolean isNew() {
        return isNew;
    }

    public boolean getEdit() {
        return this.editable;
    }

    public String getDescription() {
        return this.description;
    }

    public void setCategoryId(String _val) {
        categoryId = _val;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryName(String _val) {
        categoryName = _val;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getName() {
        return getDescription();//name;
    }

    public String getDisplayableName() {
        return getName();
    }

    abstract public NDGAnswer getAnswerModel();
}

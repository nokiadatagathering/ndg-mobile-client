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

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.ui.CategoryList;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;


public class AcceptCategoryConditionalCommand extends BackCommand {

    private static AcceptCategoryConditionalCommand instance;

    protected Command createCommand() {
        return new Command(Resources.NEWUI_NEXT);//change resource name
    }

    protected void doAction(Object parameter) {
       SurveysControl.getInstance().setSelectedSubCategoryIndex(0);
       AppMIDlet.getInstance().setDisplayable( CategoryList.class );
    }

    public static AcceptCategoryConditionalCommand getInstance() {
        if (instance == null)
            instance = new AcceptCategoryConditionalCommand();
        return instance;
    }
}

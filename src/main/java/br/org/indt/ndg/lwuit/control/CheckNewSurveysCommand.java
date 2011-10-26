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

import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.download.DownloadNewSurveys;
import com.sun.lwuit.Command;

/**
 *
 * @author mluz
 */
public class CheckNewSurveysCommand extends CommandControl {

    private static CheckNewSurveysCommand instance;

    protected Command createCommand() {
        return new Command(Resources.CHECK_NEW_SURVEYS);
    }

    protected void doAction(Object parameter) {
        DownloadNewSurveys.getInstance().check();
    }

    public static CheckNewSurveysCommand getInstance() {
        if (instance == null)
            instance = new CheckNewSurveysCommand();
        return instance;
    }
}

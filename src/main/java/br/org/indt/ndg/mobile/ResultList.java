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

package br.org.indt.ndg.mobile;

import br.org.indt.ndg.lwuit.model.Result;
import java.util.Vector;
import java.util.Enumeration;


public class ResultList {
    private FileSystem fs;
    private Vector xmlResultFile;
    public Vector displayName;

    public ResultList() {
        fs = AppMIDlet.getInstance().getFileSystem();
        xmlResultFile = fs.getXmlResultFile();
        displayName = new Vector();
        Enumeration e = xmlResultFile.elements();
        while (e.hasMoreElements()) {
             displayName.addElement( new Result(((XmlResultFile) e.nextElement()).getDisplayName()));
        }
    }

    public Vector getList() {
        return displayName;
    }
}

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

import br.org.indt.ndg.lwuit.model.CheckableListModel;
import br.org.indt.ndg.lwuit.ui.SentResultList;
import br.org.indt.ndg.mobile.AppMIDlet;
import com.sun.lwuit.Command;
import br.org.indt.ndg.mobile.FileSystem;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.XmlResultFile;
import java.util.Enumeration;
import java.util.Vector;

/**
 *
 * @author Alexandre Martini
 */
public class DeleteSentResultCommand extends CommandControl{

    private static DeleteSentResultCommand instance = new DeleteSentResultCommand();

    private DeleteSentResultCommand(){}

    public static DeleteSentResultCommand getInstance(){
        return instance;
    }

    protected Command createCommand() {
        return new Command(Resources.CMD_DELETE);
    }

    protected void doAction(Object parameter) {
        // mark the old screen with selected checkboxes
        CheckableListModel model = (CheckableListModel) parameter;
        boolean[] listFlags = model.getSelectedFlags();

        FileSystem fs = AppMIDlet.getInstance().getFileSystem();
        fs.useResults(FileSystem.USE_SENT_RESULTS);
        Vector xmlResultFile = fs.getXmlSentFile();
        Vector selectedFiles = new Vector();
        
        int size = listFlags.length;
        for (int i=0; i < size; i++){
            if (listFlags[i]) {
                selectedFiles.addElement(((XmlResultFile) xmlResultFile.elementAt(i)).getFileName());
            }
        }
        DeleteResultRunnable drr = new DeleteResultRunnable( selectedFiles );
        Thread t = new Thread(drr);
        t.start();
    }


    class DeleteResultRunnable implements Runnable {
        Vector selectedFiles;

        public DeleteResultRunnable( Vector _selectedFiles )
        {
            this.selectedFiles = _selectedFiles;
        }

        public void run(){
            Enumeration e = selectedFiles.elements();
            String fName;
            while (e.hasMoreElements()) {
                fName = (String) e.nextElement();
                AppMIDlet.getInstance().getFileSystem().removeDisplayName(fName);
                AppMIDlet.getInstance().getFileSystem().deleteFile(fName);

                String name = fName.substring( "s_".length() );
                AppMIDlet.getInstance().getFileSystem().deleteDir( "b_" + name );//will delete binary files releated with selected result
            }
            AppMIDlet.getInstance().getFileSystem().loadSentFiles();
            AppMIDlet.getInstance().setDisplayable(SentResultList.class);
        }
    }
}

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
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.FileSystem;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.XmlResultFile;
import br.org.indt.ndg.lwuit.ui.WaitingScreen;
import br.org.indt.ndg.mobile.ResultList;
import com.sun.lwuit.Command;
import java.util.Vector;

/**
 *
 * @author Alexandre Martini
 */
public class MoveToUnsentCommand extends CommandControl{
    private static MoveToUnsentCommand instance = new MoveToUnsentCommand();

    private MoveToUnsentCommand(){}

    public static MoveToUnsentCommand getInstance(){
        return instance;
    }

    protected Command createCommand() {
        return new Command(Resources.CMD_MOVETOUNSENT);
    }

    protected void doAction(Object parameter) {
        CheckableListModel model = (CheckableListModel) parameter;
        boolean[] listFlags = model.getSelectedFlags();
        FileSystem fs = AppMIDlet.getInstance().getFileSystem();
        Vector xmlResultFile = fs.getXmlSentFile();
        int size = listFlags.length;
        for (int i=0; i < size; i++){
            if (listFlags[i]) {
                String xmlFileName = ((XmlResultFile) xmlResultFile.elementAt(i)).getFileName();
                if(xmlFileName.startsWith("s_")) {
                    fs.moveUnsentResult(xmlFileName);
                }
            }
        }

        WaitingScreen.show(Resources.LOADING_RESULTS);
        UnsentResultRunnable urr = new UnsentResultRunnable();
        Thread t = new Thread(urr);  //create new thread to compensate for waitingform
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();

    }

    class UnsentResultRunnable implements Runnable {
        public void run() {
            //markAsUnsent();
            AppMIDlet.getInstance().getFileSystem().loadResultFiles();
            AppMIDlet.getInstance().setResultList( new ResultList() );
            AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.ResultList.class);
        }
    }
}

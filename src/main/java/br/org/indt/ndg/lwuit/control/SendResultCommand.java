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

import br.org.indt.ndg.lwuit.ui.StatusScreen;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.FileSystem;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.submit.SubmitResultRunnable;
import br.org.indt.ndg.mobile.submit.SubmitServer;
import com.sun.lwuit.Command;

/**
 *
 * @author mluz
 */
public class SendResultCommand extends CommandControl {

    private static SendResultCommand instance;

    protected Command createCommand() {
        return new Command(Resources.NEWUI_SEND_RESULTS);
    }

    protected void doAction(Object parameter) {
        AppMIDlet.getInstance().getFileSystem().useResults(FileSystem.USE_NOT_SENT_RESULTS);
        SubmitResultRunnable srr = new SubmitResultRunnable(AppMIDlet.getInstance().getFileSystem().getResultFilename());
        AppMIDlet.getInstance().setSubmitServer( new SubmitServer() );
        srr.setSubmitServer( AppMIDlet.getInstance().getSubmitServer() );
        AppMIDlet.getInstance().setDisplayable(StatusScreen.class );
        try { Thread.sleep(500); } catch (InterruptedException ex) {}
        Thread t = new Thread(srr);
        t.start();
    }

    public static SendResultCommand getInstance() {
        if (instance == null)
            instance = new SendResultCommand();
        return instance;
    }
}
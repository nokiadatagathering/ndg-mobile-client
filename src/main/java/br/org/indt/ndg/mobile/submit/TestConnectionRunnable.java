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

package br.org.indt.ndg.mobile.submit;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.Utils;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

public class TestConnectionRunnable implements Runnable {
    public static final int TEST_GPRS = 1;

    private boolean isCanceled = false;


    public void run() {
            testGPRS();
    }

    public void setCanceled(boolean _val) {
        isCanceled = _val;
    }


    private void testGPRS() {
        String urlServlet = AppMIDlet.getInstance().getSettings().getStructure().getServerUrl(Utils.NDG_FORMAT);
        HttpConnection httpConn = null;
        try {
            httpConn = (HttpConnection) Connector.open(urlServlet);
            httpConn.setRequestMethod(HttpConnection.GET);
            int responseCode = httpConn.getResponseCode();
            //int responseCode = httpConn.HTTP_OK;
            if (!isCanceled) {
                if (responseCode == httpConn.HTTP_OK) {
                    TestConnection.getInstance().UpdateForm(Resources.GPRS_LABEL, Resources.CONNECTION_OK + "!");
                }
                else {
                    TestConnection.getInstance().UpdateForm(Resources.GPRS_LABEL, Resources.CONNECTION_FAILED+ "!");
                }
            }

        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            if (!isCanceled) {
                TestConnection.getInstance().UpdateForm(Resources.GPRS_LABEL, Resources.CONNECTION_FAILED+ "!");
            }
        }
        catch (SecurityException ex )
        {
            if (!isCanceled) {
                TestConnection.getInstance().UpdateForm(Resources.GPRS_LABEL, Resources.HTTP_UNAUTHORIZED + "!");
            }
        }
        catch ( IllegalArgumentException ex )
        {
            if (!isCanceled) {
                TestConnection.getInstance().UpdateForm(Resources.GPRS_LABEL, Resources.CONNECTION_FAILED+ "!");
            }
        }
    }
}

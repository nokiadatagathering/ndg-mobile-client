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

package br.org.indt.ndg.mobile.download;

import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import br.org.indt.ndg.mobile.Resources;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

/**
 *
 * @author damian.janicki
 */
public class DownloadUtils {

    private static int MTU = 1024;

    public static boolean getViaServlet(String url, Hashtable headers, OutputStream output){
        if(output == null){
            return false;
        }

        boolean         downloaded  = false;
        HttpConnection  httpConn    = null;
        InputStream     inputStream = null;

        try{
            httpConn = (HttpConnection)Connector.open(url);
            httpConn.setRequestMethod(HttpConnection.GET);

            if(headers != null){
                Enumeration enumeration = headers.keys();

                String key = null;
                String val = null;
                while(enumeration.hasMoreElements()){
                    key = (String)enumeration.nextElement();
                    val = (String)headers.get(key);
                    httpConn.setRequestProperty(key, val);
                }
            }

            inputStream = httpConn.openInputStream();
            if(httpConn.getResponseCode() == HttpConnection.HTTP_OK){
                transferData(inputStream, output);
                downloaded = true;
            }else if(httpConn.getResponseCode() == HttpConnection.HTTP_UNAUTHORIZED){
                GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
                GeneralAlert.getInstance().showCodedAlert(Resources.NETWORK_FAILURE, Resources.HTTP_UNAUTHORIZED, GeneralAlert.ERROR);
                downloaded = false;
            }
        }catch(IOException ex){
            GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
            GeneralAlert.getInstance().showCodedAlert(Resources.NETWORK_FAILURE, ex.getMessage().trim(), GeneralAlert.ERROR);
            downloaded = false;
        }finally{
            try{
                if(inputStream != null){
                    inputStream.close();
                }
                if(httpConn != null){
                    httpConn.close();
                }
            }catch(IOException ex){}
        }

        return downloaded;
    }

    public static void transferData(InputStream input, OutputStream output) throws IOException{
        int bytesread = 0;
        byte[] databyte = new byte[MTU];

        while ( (bytesread = input.read(databyte, 0, MTU)) != -1) {
            output.write(databyte, 0, bytesread);
        }
    }
}

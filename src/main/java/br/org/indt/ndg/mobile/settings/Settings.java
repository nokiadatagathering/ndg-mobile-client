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


package br.org.indt.ndg.mobile.settings;

import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.NdgConsts;
import br.org.indt.ndg.mobile.xmlhandle.Parser;

public class Settings {
    private SettingsStructure settingsStructure = new SettingsStructure();
    
    public Settings() {
        loadSettingsInfo();
    }
    
    public void writeSettings() {
        String filename = AppMIDlet.getInstance().getRootDir() + NdgConsts.SETTINGS_FILE;
        FileConnection fileConnection = null;
        OutputStream outputStream = null;
        PrintStream printStream = null;
        try {
            fileConnection = (FileConnection) Connector.open(filename);
            if( !fileConnection.exists() ) {
                fileConnection.create();
            }  else {
                fileConnection.delete();
                fileConnection.create();
            }
            outputStream = fileConnection.openOutputStream();
            printStream = new PrintStream(outputStream);
            settingsStructure.saveSettings(printStream);
        } catch (ConnectionNotFoundException e) {
            GeneralAlert.getInstance().addCommand( GeneralAlert.DIALOG_OK, true);
            GeneralAlert.getInstance().show(e);
        } catch(IOException e) {
            GeneralAlert.getInstance().addCommand( GeneralAlert.DIALOG_OK, true);
            GeneralAlert.getInstance().show(e);
        } finally {
            try {
                if (printStream != null)
                    printStream.close();
                if (outputStream != null)
                    outputStream.close();
                if (fileConnection != null)
                    fileConnection.close();
            } catch (IOException ex) {
                //ignore
            }
        }
    }
    
    public void loadSettingsInfo() {
        createSettingsFile();
        parseSettingsFile();
        if (!settingsStructure.getAppVersion().equals(AppMIDlet.getInstance().getAppVersion())) {
            deleteSettingsFile();
            createSettingsFile();
            parseSettingsFile();
            // workaround for loosing registration and MSISDN info on update
            // WARN: MSISDN info will be still lost on update until reimplemented on server! (== dynamic instead of static jad on update)
            settingsStructure.setRegisteredFlag(SettingsStructure.REGISTERED);
            writeSettings();
        }
    }
    
    public SettingsStructure getStructure() {
        return settingsStructure;
    }
    
    private void parseSettingsFile() {
        SettingsHandler sh = new SettingsHandler();
        sh.setSettingsStructure(settingsStructure);

        Parser parser = new Parser(sh);
        parser.parseFile(AppMIDlet.getInstance().getRootDir() + NdgConsts.SETTINGS_FILE);
    }
    
    private void deleteSettingsFile() {
        try {
            FileConnection conn = (FileConnection) Connector.open(AppMIDlet.getInstance().getRootDir() + NdgConsts.SETTINGS_FILE);
            if(conn.exists()) {
                conn.delete();
                conn.close();
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void createSettingsFile() {
        try {
            FileConnection conn = (FileConnection) Connector.open(AppMIDlet.getInstance().getRootDir() + NdgConsts.SETTINGS_FILE);
            if(!conn.exists()){
                conn.create();
                OutputStream outputStream = conn.openDataOutputStream();
                PrintStream printStream = new PrintStream(outputStream);
                settingsStructure.createDefaultSettings(printStream);
                printStream.close();
                outputStream.close();
                conn.close();
            }
             } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

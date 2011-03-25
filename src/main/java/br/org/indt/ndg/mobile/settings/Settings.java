
package br.org.indt.ndg.mobile.settings;

import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.xmlhandle.Parser;

public class Settings {
    private SettingsStructure settingsStructure = new SettingsStructure();
    
    public Settings() {
        loadSettingsInfo();
    }
    
    public void writeSettings() {
        String filename = Resources.ROOT_DIR + Resources.SETTINGS_FILE;
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
        }
    }
    
    public SettingsStructure getStructure() {
        return settingsStructure;
    }
    
    private void parseSettingsFile() {
        SettingsHandler sh = new SettingsHandler();
        sh.setSettingsStructure(settingsStructure);

        Parser parser = new Parser(sh);
        parser.parseFile(Resources.ROOT_DIR + Resources.SETTINGS_FILE);
    }
    
    private void deleteSettingsFile() {
        try {
            FileConnection conn = (FileConnection) Connector.open(Resources.ROOT_DIR + Resources.SETTINGS_FILE);
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
            FileConnection conn = (FileConnection) Connector.open(Resources.ROOT_DIR + Resources.SETTINGS_FILE);
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

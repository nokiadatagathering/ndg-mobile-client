
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
        
        try {
            FileConnection connection = (FileConnection) Connector.open(filename);
            if(!connection.exists()) connection.create();
            else {
                connection.delete();
                connection.create();
            }
            
            OutputStream out = connection.openOutputStream();
            PrintStream output = new PrintStream(out);
            
            output.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            
            output.print("<settings");
            output.print(" agree=\"" + settingsStructure.getAgreeFlag() + "\"");
            output.print(" splash=\"" + settingsStructure.getSplashTime() + "\"");
            output.println(" language=\"" + settingsStructure.getLanguage() + "\">");
            
            settingsStructure.writeGpsSettings(output);
            settingsStructure.writeCategoryEnableSettings(output);
            settingsStructure.writeLogSettings(output);
            settingsStructure.writeServerSettings(output);
            settingsStructure.writeVersionSettings(output);
            
            output.println("</settings>");
            
            output.close();
            out.close();
            connection.close();
            
        } catch (ConnectionNotFoundException e) {
            GeneralAlert.getInstance().addCommand( GeneralAlert.DIALOG_OK, true);
            GeneralAlert.getInstance().show(e);
        } catch(IOException e) {
            GeneralAlert.getInstance().addCommand( GeneralAlert.DIALOG_OK, true);
            GeneralAlert.getInstance().show(e);
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
                String defaultServerUrl = AppMIDlet.getInstance().getDefaultServerUrl();
                String defaultAppLanguage = AppMIDlet.getInstance().getDefaultAppLanguage();
                String[] defaultServelts = AppMIDlet.getInstance().getDefaultServlets();
                
                String settings = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<settings agree=\"1\" splash=\"8\" language=\"" + defaultAppLanguage + "\">\n" +
                        "<gps configured=\"yes\"/>\n" +
                        "<categoryView enabled=\"no\"/>\n" +
                        "<log active=\"no\"/>\n" +
                        "<server compression=\"on\">\n" +
                        "<url_compress>" + defaultServerUrl + defaultServelts[0] + defaultServelts[1] + "</url_compress>\n" +
                        "<url_normal>" + defaultServerUrl + defaultServelts[0] + defaultServelts[1] + "</url_normal>\n" +
                        "<url_receive_survey>" + defaultServerUrl + defaultServelts[0] + defaultServelts[2] + "</url_receive_survey>\n" +
                        "<url_update_check>" + defaultServerUrl + defaultServelts[0] + defaultServelts[3] + "</url_update_check>\n" +
                        "<url_register_imei>" + defaultServerUrl + defaultServelts[0] + defaultServelts[4] + "</url_register_imei>\n" +
                        "</server>\n" +
                        "<version application=\"" + AppMIDlet.getInstance().getAppVersion() + "\"/>\n" +
                        "</settings>";
                conn.create();                
                OutputStream out = conn.openDataOutputStream();
                
                out.write(settings.getBytes("UTF-8"));
                out.flush();
                out.close();                
                conn.close();
            }
             } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}

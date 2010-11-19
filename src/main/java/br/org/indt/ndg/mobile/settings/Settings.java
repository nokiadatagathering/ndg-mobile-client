
package br.org.indt.ndg.mobile.settings;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.xmlhandle.Parser;

public class Settings extends List implements CommandListener {
    
    private SettingsStructure settingsStructure = new SettingsStructure();
    
    public Settings() {
        super("Settings", Choice.IMPLICIT);

        loadSettingsInfo();
        
        this.setFitPolicy(this.TEXT_WRAP_ON);
    }
    
    public void commandAction(Command c, Displayable d) {
        if (c == Resources.CMD_BACK) {
            AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getSurveyList());
        } else if (c == SELECT_COMMAND) {
            int index = this.getSelectedIndex();
            if (index == 0) {
                AppMIDlet.getInstance().setDisplayable(new GpsForm());
            }
        }
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
            settingsStructure.writeSmsSettings(output);
            settingsStructure.writeTransportSettings(output);
            settingsStructure.writeLogSettings(output);
            settingsStructure.writeServerSettings(output);
            settingsStructure.writeVersionSettings(output);
            
            output.println("</settings>");
            
            output.close();
            out.close();
            connection.close();
            
        } catch (ConnectionNotFoundException e) {
            AppMIDlet.getInstance().getGeneralAlert().showError(e);
        } catch(IOException e) {
            AppMIDlet.getInstance().getGeneralAlert().showError(e);
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
                String[] defaultSmsNumbers = AppMIDlet.getInstance().getDefaultSmsNumbers();
                String defaultAppLanguage = AppMIDlet.getInstance().getDefaultAppLanguage();
                String[] defaultServelts = AppMIDlet.getInstance().getDefaultServlets();
                
                String settings = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<settings agree=\"1\" splash=\"8\" language=\"" + defaultAppLanguage + "\">\n" +
                        "<gps configured=\"yes\"/>\n" +
                        "<sms country_code=\"" + defaultSmsNumbers[0] + "\" area_code=\"" + defaultSmsNumbers[1] + "\" phone_number=\"" + defaultSmsNumbers[2] + "\" receivingPort=\"50001\" sendingPort=\"50000\" number_of_char_per_sms=\"100\"/>\n" +
                        "<transport gprs=\"yes\" sms=\"no\"/>\n" +
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

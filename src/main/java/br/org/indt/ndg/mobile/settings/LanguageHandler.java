/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.mobile.settings;

import br.org.indt.ndg.lwuit.control.ExitCommand;
import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import java.io.IOException;

import java.util.Enumeration;
import javax.microedition.io.file.FileSystemRegistry;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

/**
 *
 * @author mturiel
 */
public class LanguageHandler {
    
    private String lang = "";
    private Settings settings = null;
        
    public LanguageHandler() {
        setRootDir();
        settings = new Settings();
        AppMIDlet.getInstance().setSettings(settings);
    }

    public String getLanguage() {
        String result = "";
        lang = settings.getStructure().getLanguage();
        if ( (lang != null) && (lang.equals("pt-BR") || lang.equals("en-US") || lang.equals("es-ES") ) ) {
            result = lang;
        }
        else {
            result = System.getProperty("microedition.locale");
        }
        return result;
    }
    
    private void setRootDir() {
        boolean sunWTKEmulator = false;
        Enumeration e = FileSystemRegistry.listRoots();
        while(e.hasMoreElements()){
            String drive = (String) e.nextElement();
            if (drive.equals("root1/")){
                sunWTKEmulator = true;
                break;
            }            
        }
        if(sunWTKEmulator){
            Resources.ROOT_DIR_CARD = "file:///root1/ndg/";
            Resources.ROOT_DIR_PHONE = "file:///root1/ndg/";
        }
        else{
            Resources.ROOT_DIR_PHONE = System.getProperty("fileconn.dir.photos") + "ndg/";
            Resources.ROOT_DIR_CARD = System.getProperty("fileconn.dir.memorycard") + "ndg/";
        }
        
        FileConnection fc;
        try {            
            fc = (FileConnection) Connector.open(Resources.ROOT_DIR_CARD);     
            if (!fc.exists()){
                fc.mkdir();                  
            }
            Resources.ROOT_DIR = Resources.ROOT_DIR_CARD;    
            fc.close();            
        } 
        catch (IOException ioe) {            
           try {            
                fc = (FileConnection) Connector.open(Resources.ROOT_DIR_PHONE);            
                if (!fc.exists())
                    fc.mkdir();                       
                Resources.ROOT_DIR = Resources.ROOT_DIR_PHONE;    
                fc.close();
            } 
            catch (IOException ioe2) {
                GeneralAlert.getInstance().addCommand( ExitCommand.getInstance());
                GeneralAlert.getInstance().show(Resources.ERROR_TITLE, Resources.IMPOSSIBLE_CREATE_ROOTDIR, GeneralAlert.ERROR );
                //this.ROOT_DIR = this.ROOT_DIR_PHONE;
            }
        }
    }

}

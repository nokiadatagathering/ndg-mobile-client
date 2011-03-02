/*
 * FileSystem.java
 *
 * Created on March 5, 2007, 12:11 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package br.org.indt.ndg.mobile;

import br.org.indt.ndg.lwuit.control.ExitCommand;
import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import br.org.indt.ndg.mobile.logging.Logger;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import br.org.indt.ndg.mobile.structures.FileSystemResultStructure;
import br.org.indt.ndg.mobile.structures.FileSystemSurveyStructure;
import br.org.indt.ndg.mobile.xmlhandle.FileSystemResultHandler;
import br.org.indt.ndg.mobile.xmlhandle.FileSystemSurveyHandler;
import br.org.indt.ndg.mobile.xmlhandle.Parser;
import br.org.indt.ndg.mobile.xmlhandle.kParser;


public class FileSystem {
    private String root = null;
    private boolean error = false;
    private int resultListIndex;

    private FileSystemSurveyStructure fsSurveyStructure;
    private FileSystemResultStructure fsResultStructure;
    private FileSystemResultStructure fsSentStructure;

    public void setResultListIndex(int _index)
    {
        resultListIndex = _index;
    }

    public int getResultListIndex() {
        return resultListIndex;
    }

    public void storeFilename(String displayName, String fileName) {
        fsResultStructure.addXmlResultFileObj(displayName, fileName);
    }

    public void removeDisplayName(String _fileName) {
        fsResultStructure.removeDisplay(_fileName);
    }

    public void removeFile(String _filename) {
        fsResultStructure.removeFile(_filename);
    }

    public void removeSelectedResult() {
        fsResultStructure.removeSelectedResult();
    }
    
    /** Creates a new instance of FileSystem */
    public FileSystem(String _root) {
        root = _root;
        resultListIndex = 0;

        fsSurveyStructure = new FileSystemSurveyStructure();
        fsResultStructure = new FileSystemResultStructure();
        fsSentStructure = new FileSystemResultStructure();

        this.loadSurveyFiles();
    }

    public void setLocalFile(boolean _bool) {
        fsResultStructure.setLocalFile(_bool);
    }

    public boolean getError() {
        return error;
    }

    public void setError(boolean _bool) {
        error = _bool;
    }

    public boolean isLocalFile() {
        return fsResultStructure.isLocalFile();
    }

    public void setSurveyCurrentIndex(int _index) {
        fsSurveyStructure.setCurrentIndex(_index);
    }

    public void setResultCurrentIndex(int _index) {
        fsResultStructure.setCurrentIndex(_index);
    }

    public Vector SurveyNames() {  //these are local mappings not real filenames
        return fsSurveyStructure.getNames();
    }

    public int getSurveysCount(){
        return fsSurveyStructure.getSurveysCount();
    }

    public Vector getXmlResultFile() {
        return fsResultStructure.getXmlResultFile();
    }

    public Vector getXmlSentFile() {
        return fsSentStructure.getXmlResultFile();
    }

    public String getSurveyDirName() { //surveys located within this directory
        return fsSurveyStructure.getDirName();
    }

    //returns filename using current index of list
    public String getResultFilename() {
        return fsResultStructure.getFilename();
    }

    //returns filename given index
    public String getResultFilename(int _index) {
        return fsResultStructure.getFilename(_index);
    }

    public void loadSurveyInfo(String _dirName) {
        FileSystemSurveyHandler sh = new FileSystemSurveyHandler();
        fsSurveyStructure.addFileName(_dirName);
        sh.setFileSystemSurveyStructure(fsSurveyStructure);

        kParser kparser = new kParser();
        kparser.setFileSystemSurveyStructure(fsSurveyStructure);
        kparser.parserSurveyFileInfo(root + _dirName + Resources.SURVEY_NAME);
    }

    public void deleteFile(String filename) {
        delete(filename, true);
    }

    private void delete(String filename, boolean removeFromMemory){
        if(removeFromMemory){
            this.removeDisplayName(filename);
            this.removeFile(filename);
        }
        try {
            FileConnection fc = (FileConnection) Connector.open(root + fsSurveyStructure.getDirName() + filename);
            if (fc.exists()) {
                fc.delete();
            }
            if(fc != null) {
                fc.close();
            }
        }
        catch (IOException ioe) {
            error = true;
            GeneralAlert.getInstance().addCommand( ExitCommand.getInstance());
            GeneralAlert.getInstance().show(Resources.ERROR_TITLE, Resources.EDELETE_RESULT, GeneralAlert.ERROR );
        }
    }

    public void deleteSurveyDir(String dirname) {
        FileConnection fcFile = null;
        try {
            FileConnection fc = (FileConnection) Connector.open(root + dirname);
            Enumeration filelist = fc.list("*", true);
            String fileName = null;
            while(filelist.hasMoreElements()) {
                fileName = (String) filelist.nextElement();
                fcFile = (FileConnection) Connector.open(root + dirname + fileName);
                if ( fcFile.exists()) {
                    if( fcFile.isDirectory() ) {
                        fcFile.close();
                        deleteDir( fileName );
                    } else {
                        fcFile.delete();
                        fcFile.close();
                    }
                }
            }

            if (fc.exists()) {
                fc.delete();
            }
            if(fc != null) {
                fc.close();
            }

            fsSurveyStructure.removeNameAndFileNameAtCurrentIndex();
        }
        catch (IOException ioe) {
            error = true;
            GeneralAlert.getInstance().addCommand( ExitCommand.getInstance());
            GeneralAlert.getInstance().show(Resources.ERROR_TITLE, Resources.EDELETE_RESULT, GeneralAlert.ERROR );
        }
    }

    public void deleteDir(String dirname) {
        FileConnection fcFile = null;
        FileConnection fc = null;
        if( !dirname.endsWith("/")) {
            dirname = dirname + '/';
        }
        try {
            fc = (FileConnection) Connector.open(root + fsSurveyStructure.getDirName() + dirname);
            if( fc.exists() ) {
                Enumeration filelist = fc.list("*", true);
                String fileName = null;
                while(filelist.hasMoreElements()) {
                    fileName = (String) filelist.nextElement();
                    fcFile = (FileConnection) Connector.open(root + fsSurveyStructure.getDirName() + dirname + fileName);
                    if (fcFile.exists()) {
                        try{ fcFile.delete(); }catch (IOException ex){}
                    }
                    if(fcFile != null) {
                        fcFile.close();
                        fcFile = null;
                    }
                }
                fc.delete();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            Logger.getInstance().logException( ioe.getMessage() );
        }
        finally {
            if(fc != null) {
                try {
                    fc.close();
                } catch (IOException ex) {}
            }
        }
    }

    public void moveSentResult(String _filename) {
        String path = root + fsSurveyStructure.getDirName() + _filename;
        moveResult(path, _filename, "s_");
    }

    public void moveSentResult(String fileDir, String fileName){
        String path = root + fileDir + fileName;
        moveResult(path, fileName, "s_");
    }

    private void moveResult(String path, String fileName, String prefix){
        try {
            FileConnection fc = (FileConnection) Connector.open(path);
            if (fc.exists()) {
                this.removeDisplayName(fileName);
                this.removeFile(fileName);
                fc.rename(prefix + fileName);
            }
            if(fc != null){
                fc.close();
            }
        } catch (IOException ioe) {
            GeneralAlert.getInstance().addCommand( ExitCommand.getInstance());
            GeneralAlert.getInstance().show(Resources.ERROR_TITLE, Resources.ERENAME, GeneralAlert.ERROR );
        }
    }

    public void moveUnsentResult(String _filename) {
        try {
            FileConnection fc = (FileConnection) Connector.open(root + fsSurveyStructure.getDirName() + _filename);
            if (fc.exists()) {
                if (_filename.startsWith("s_") ) {
                    String newname = _filename.substring(2);
                    fc.rename(newname);
                }
            }
            if(fc != null) {
                fc.close();
            }
        } catch (IOException ioe) {
            GeneralAlert.getInstance().addCommand( ExitCommand.getInstance());
            GeneralAlert.getInstance().show(Resources.ERROR_TITLE, Resources.ERENAME, GeneralAlert.ERROR );
        }
    }

    public void moveCorruptedResult(String _filename) {
        try {
            FileConnection fc = (FileConnection) Connector.open(root + fsSurveyStructure.getDirName() + _filename);
            if (fc.exists()) {
                fc.rename("c_" + _filename);
            }
        } catch (IOException ioe) {

            GeneralAlert.getInstance().addCommand( ExitCommand.getInstance());
            GeneralAlert.getInstance().show(Resources.ERROR_TITLE, Resources.ERENAME, GeneralAlert.ERROR );
        }
    }

    public Vector getSentFilenames() {
        Vector sentFilenames = new Vector();
        
        try {
            FileConnection fc = (FileConnection) Connector.open(root + fsSurveyStructure.getDirName());
            Enumeration filelist = fc.list("s_*", true);
            String fileName;
            
            while(filelist.hasMoreElements()) {
                fileName = (String) filelist.nextElement();
                if (fileName.startsWith("s_")) {
                    sentFilenames.addElement(fileName);
                }
            }
            fc.close();
        } catch (IOException ioe) {}

        return sentFilenames;
    }

    public void loadSurveyFiles() {
        try {
            FileConnection fc1 = (FileConnection) Connector.open(root);
            FileConnection fc2 = null;
            FileConnection fc3 = null;
            String dirName;
            
            Enumeration filelist1 = fc1.list("*", true);
            while(filelist1.hasMoreElements()) {
                dirName = (String) filelist1.nextElement();
                fc2 = (FileConnection) Connector.open(root + dirName);
                if (fc2.isDirectory() && dirName.substring(0, 6).equalsIgnoreCase("survey")){//(dirName.startsWith("survey")  || dirName.startsWith("SURVEY") || dirName.startsWith("Survey")) ) {
                    fc3 = (FileConnection) Connector.open(root + dirName + Resources.SURVEY_NAME);
                    if (fc3.exists()) {
                        this.loadSurveyInfo(dirName);
                    } else {
//                        error = true;
                    }
                }
            }
            if(fc1 != null)
                fc1.close();
            if(fc2 != null)
                fc2.close();
            if(fc3 != null)
                fc3.close();
            
        } catch (IOException ioe) {
            error = true;
            GeneralAlert.getInstance().addCommand( ExitCommand.getInstance());
            GeneralAlert.getInstance().show(Resources.ERROR_TITLE, Resources.ELOAD_SURVEYS, GeneralAlert.ERROR );
        }
    }

    public void loadResultInfo(String _filename) {
        FileSystemResultHandler rh = new FileSystemResultHandler();
        rh.setFileSystemResultStructure(fsResultStructure);
        rh.setFileSystemResultFilename(_filename);

        Parser parser = new Parser(rh);
        parser.parseFile(root + fsSurveyStructure.getDirName() + _filename);
    }

    private void loadSentInfo(String _filename) {
        FileSystemResultHandler rh = new FileSystemResultHandler();
        rh.setFileSystemResultStructure(fsSentStructure);
        rh.setFileSystemResultFilename(_filename);

        Parser parser = new Parser(rh);
        parser.parseFile(root + fsSurveyStructure.getDirName() + _filename);
    }

    public void loadSentFiles() {
        fsSentStructure.reset();
        loadSentFiles("s_");
    }

    private void loadSentFiles(String filter){
        try {
            FileConnection fc = (FileConnection) Connector.open(root + fsSurveyStructure.getDirName());
            Enumeration filelist = fc.list(filter + "*", true);
            String fileName;
            while(filelist.hasMoreElements()) {
                fileName = (String) filelist.nextElement();
                if (fileName.startsWith(filter)) this.loadSentInfo(fileName);
            }
            fc.close();
        } catch (IOException ioe) {
            error = true;
            GeneralAlert.getInstance().addCommand( ExitCommand.getInstance());
            GeneralAlert.getInstance().show(Resources.ERROR_TITLE, Resources.ELOAD_RESULTS, GeneralAlert.ERROR );
        }
    }

    public void loadResultFiles() {
        fsResultStructure.reset();

        try {
            FileConnection fc = (FileConnection) Connector.open(root + fsSurveyStructure.getDirName());
            Enumeration filelist = fc.list("r_*", true);
            String fileName=null;
            while(filelist.hasMoreElements()) {
                if (!getError()) {
                    fileName = (String) filelist.nextElement();
                    if ( ( fileName.startsWith("r_")) || (fileName.startsWith("s_") ) ) {
                        this.loadResultInfo(fileName);
                    }
                    if (getError()){ 
                        moveCorruptedResult(fileName);
                    }
                } else {
                    break;
                }
            }

            if (!getError()) {
                fsResultStructure.sortDisplayNames();
            }

            fc.close();
        } catch (IOException ioe) {
            error = true;
            GeneralAlert.getInstance().addCommand( ExitCommand.getInstance());
            GeneralAlert.getInstance().show(Resources.ERROR_TITLE, Resources.ELOAD_RESULTS, GeneralAlert.ERROR );
        }
    }
}

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
    // used to distinguish which result list should be used
    public static final int USE_SENT_RESULTS = 1;
    public static final int USE_NOT_SENT_RESULTS = 2;
    private int mCurrentlyUsed = FileSystem.USE_SENT_RESULTS;

    private FileSystemSurveyStructure fsSurveyStructure;
    private FileSystemResultStructure fsResultStructure;
    private FileSystemResultStructure fsSentStructure;

    // This is very poor solution, unfortunatelly only alternatives were major refactoring or code repetition
    // before using the FileSystem it has to be pointed eiter sent or not sent results should be used
    public void useResults( int resultsToUse ) {
        if ( resultsToUse != FileSystem.USE_SENT_RESULTS &&
             resultsToUse != FileSystem.USE_NOT_SENT_RESULTS ) {
            throw new IllegalArgumentException("Unkown result list requested");
        }
        mCurrentlyUsed = resultsToUse;
    }

    public int resultsInUse() {
        return mCurrentlyUsed;
    }

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
        if ( mCurrentlyUsed == FileSystem.USE_NOT_SENT_RESULTS) {
            fsResultStructure.setCurrentIndex(_index);
        } else {
            fsSentStructure.setCurrentIndex(_index);
        }
    }

    public Vector SurveyNames() {  //these are local mappings not real filenames
        return fsSurveyStructure.getNames();
    }

    public String getCurrentSurveyName(){
        return fsSurveyStructure.getCurrentName();
    }

    public int getSurveysCount(){
        return fsSurveyStructure.getSurveysCount();
    }

    public Vector getXmlResultFile() {
        Vector result = null;
        if ( mCurrentlyUsed == FileSystem.USE_NOT_SENT_RESULTS) {
            result = fsResultStructure.getXmlResultFile();
        } else {
            result = fsSentStructure.getXmlResultFile();
        }
        return result;
    }

    public Vector getXmlSentFile() {
        return fsSentStructure.getXmlResultFile();
    }

    public String getSurveyDirName() { //surveys located within this directory
        return fsSurveyStructure.getDirName();
    }

    //returns filename using current index of list
    public String getResultFilename() {
        String filename = null;
        if ( mCurrentlyUsed == FileSystem.USE_NOT_SENT_RESULTS) {
            filename = fsResultStructure.getFilename();
        } else {
            filename = fsSentStructure.getFilename();
        }
        return filename;
    }

    //returns filename given index
    public String getResultFilename(int _index) {
        String filename = null;
        if ( mCurrentlyUsed == FileSystem.USE_NOT_SENT_RESULTS) {
            filename = fsResultStructure.getFilename(_index);
        } else {
            filename = fsSentStructure.getFilename(_index);
        }
        return filename;
    }

    public void loadSurveyInfo(String _dirName) {
        FileSystemSurveyHandler sh = new FileSystemSurveyHandler();
        fsSurveyStructure.addFileName(_dirName);
        sh.setFileSystemSurveyStructure(fsSurveyStructure);

        kParser kparser = new kParser();
        kparser.setFileSystemSurveyStructure(fsSurveyStructure);
        kparser.parserSurveyFileInfo(root + _dirName + Resources.SURVEY_NAME);
        setError(kparser.getError());
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
        if( !dirname.endsWith("/")) {
            dirname = dirname + '/';
        }
        FileConnection directory = null;
        try {
            directory = (FileConnection) Connector.open(root + fsSurveyStructure.getDirName() + dirname);
            if( directory.exists() ) {
                Enumeration filelist = directory.list("*", true);
                String fileName = null;
                while ( filelist.hasMoreElements() ) {
                    fileName = (String) filelist.nextElement();
                    FileConnection file = null;
                    try {
                        file = (FileConnection) Connector.open(root + fsSurveyStructure.getDirName() + dirname + fileName);
                        if (file.exists()) {
                            file.delete();
                        }
                    } catch ( IOException ex ) {
                    } finally {
                        try {
                            if ( file != null ) {
                                file.close();
                                file = null;
                            }
                        } catch ( IOException ex ) {
                            file = null;
                        }
                    }
                }
                directory.delete();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            Logger.getInstance().logException( ioe.getMessage() );
        }
        finally {
            if(directory != null) {
                try {
                    directory.close();
                } catch (IOException ex) {}
            }
        }
    }

    public void moveSentResult(String _filename) {
        String path = root + fsSurveyStructure.getDirName() + _filename;
        moveResult(path, _filename, "s_");
    }

    private void moveResult(String path, String fileName, String prefix){
        FileConnection fc = null;
        try {
            fc = (FileConnection) Connector.open(path);
            if (fc.exists()) {
                this.removeDisplayName(fileName);
                this.removeFile(fileName);
                fc.rename(prefix + fileName);
            }
        } catch (IOException ioe) {
            GeneralAlert.getInstance().addCommand( ExitCommand.getInstance());
            GeneralAlert.getInstance().show(Resources.ERROR_TITLE, Resources.ERENAME, GeneralAlert.ERROR );
        } finally {
            if(fc != null){
                try {
                    fc.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
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
                if (fc2.isDirectory() && (AppMIDlet.getInstance().isNdgDir(dirName) ||
                                          AppMIDlet.getInstance().isXformDir(dirName))){//(dirName.startsWith("survey")  || dirName.startsWith("SURVEY") || dirName.startsWith("Survey")) ) {
                    fc3 = (FileConnection) Connector.open(root + dirName + Resources.SURVEY_NAME);
                    if (fc3.exists() ) {
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
        useResults(FileSystem.USE_SENT_RESULTS);
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
        useResults(FileSystem.USE_NOT_SENT_RESULTS);
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

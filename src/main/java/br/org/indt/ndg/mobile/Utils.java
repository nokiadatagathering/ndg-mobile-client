package br.org.indt.ndg.mobile;

import br.org.indt.ndg.mobile.xmlhandle.Conversor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Enumeration;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;

public class Utils {

    public static final int INVALID_FORMAT = 0;
    public static final int NDG_FORMAT = 1;
    public static final int OPEN_ROSA_FORMAT = 2;

    public static int resolveSurveyFormatFromDirName( String dirName ) {
        int result = INVALID_FORMAT;
        if ( Utils.isNdgDir(dirName) ) {
            result = NDG_FORMAT;
        } else if (  dirName.startsWith("xforms")) {
            result = OPEN_ROSA_FORMAT;
        }
        return result;
    }

    public static boolean isNdgDir(String surveyDirName){
        if ( surveyDirName.substring(0, 6).equalsIgnoreCase(NdgConsts.SURVEY) ) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isXformDir(String surveyDirName){
        if ( surveyDirName.substring(0, 5).equalsIgnoreCase(NdgConsts.XFORM) ) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isCurrentDirXForm(){
        return Utils.isXformDir(AppMIDlet.getInstance().getFileSystem().getSurveyDirName());
    }

    public static boolean isS40() {
        boolean s40 = false;
        try {
            Player player = Manager.createPlayer("capture://image");
            player.deallocate();
            player.close();
            s40 = true;
        } catch( Exception ex ) {
            s40 = false;
        }
        return s40;
    }

    public static String x2u(String _value) {
        Conversor unicode = new Conversor();
        return unicode.xml2uni(_value);
    }

    public static String u2x(String _value) {
        Conversor unicode = new Conversor();
        return unicode.uni2xml(_value);
    }

    public static void moveFile(String oldPath, String newPath) {
        FileConnection oldConn = null;
        FileConnection newConn = null;

        InputStream oldFileInput = null;
        OutputStream newFileOutput = null;

        try {
            oldConn = (FileConnection) Connector.open(oldPath, Connector.READ_WRITE);

            if (oldConn.exists()) {
                newConn = (FileConnection) Connector.open(newPath, Connector.WRITE);
                newConn.create();

                oldFileInput = oldConn.openInputStream();
                newFileOutput = newConn.openOutputStream();

                int len = 0;
                byte[] buf = new byte[1024];

                while ((len = oldFileInput.read(buf)) > 0) {
                    newFileOutput.write(buf, 0, len);
                }
                oldConn.delete();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (oldFileInput != null) {
                    oldFileInput.close();
                }
                if (oldConn != null) {
                    oldConn.close();
                }
                if (newFileOutput != null) {
                    newFileOutput.close();
                }
                if (newConn != null) {
                    newConn.close();
                }
            } catch (IOException ex) {
            }
        }
    }

    public static byte[] bytesFromFile(String file) throws IOException {

        byte[] bytes = null;
        InputStream in = null;
        FileConnection fc = null;
        try {
            fc = (FileConnection) Connector.open(file, Connector.READ_WRITE);
            if (fc.exists()) {
                in = fc.openInputStream();
                long length = fc.fileSize();

                bytes = new byte[(int) length];
                int offset = 0;
                int numRead = 0;
                while (offset < bytes.length && (numRead = in.read(bytes,
                        offset, bytes.length - offset)) >= 0) {
                    offset += numRead;
                }
            }
        } catch (IOException ex) {
        } finally {
            if (in != null) {
                in.close();
            }
            if (fc != null) {
                fc.close();
            }
        }
        return bytes;
    }

    public static String loadFile(String file) {
        FileConnection fc = null;
        InputStream is = null;
        String content = null;
        try {
            fc = (FileConnection) Connector.open(file, Connector.READ);

            if (fc.exists()) {
                is = fc.openInputStream();
                StringBuffer sb = new StringBuffer();

                int chars = 0;

                while ((chars = is.read()) != -1) {
                    sb.append((char) chars);
                }
                content = sb.toString();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                if (fc != null) {
                    fc.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
        return content;
    }

    public static void saveFile( String aFile, String aContent ) {
        FileConnection fc = null;
        PrintStream os = null;
        try {
            fc = (FileConnection)Connector.open( aFile, Connector.READ_WRITE );

            if ( fc.exists() ) {
                fc.delete();
            }
            fc.create();

            os = new PrintStream( fc.openOutputStream() );
            os.print( aContent == null ? "" : aContent );
            os.flush();
        } catch ( IOException ex ) {
        } finally {
            if (os != null) {
                os.close();
            }
            try {
                if (fc != null) {
                    fc.close();
                }
            } catch (IOException ex) {
            }
        }
    }

    public static boolean removeFile(String file) {
        boolean deleted = false;
        FileConnection fc = null;
        try {
            fc = (FileConnection) Connector.open(file, Connector.READ_WRITE);
            if (fc.exists()) {
                //recursivly delete
                if (fc.isDirectory()) {
                    Enumeration e = fc.list("*", true);
                    String f = null;
                    while (e.hasMoreElements()) {
                        f = (String) e.nextElement();
                        removeFile(file + f);
                    }
                }

                fc.setWritable(true);
                fc.delete();
                deleted = true;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (fc != null) {
                try {
                    fc.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return deleted;
    }

    public static void createDirectory(String path) {
        FileConnection fc = null;
        try {
            fc = (FileConnection) Connector.open(path, Connector.READ_WRITE);
            if (!fc.exists()) {
                fc.mkdir();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fc.close();
            } catch (IOException ex) {
            }
        }
    }

    public static boolean fileExists(String file) {
        FileConnection fc = null;
        boolean exists = false;
        try {
            fc = (FileConnection) Connector.open(file, Connector.READ);
            if (fc.exists()) {
                exists = true;
            } else {
                exists = false;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (fc != null) {
                try {
                    fc.close();
                } catch (IOException ex) {
                }
            }
        }
        return exists;
    }

    public static String prepereMessagesPath(String locale){
        return AppMIDlet.getInstance().getRootDir() +
                NdgConsts.LANGUAGE_DIR +
                NdgConsts.MESSAGES_PREFIX + "_" + locale.substring(0, 2) + NdgConsts.MESSAGES_EXT;
    }

}

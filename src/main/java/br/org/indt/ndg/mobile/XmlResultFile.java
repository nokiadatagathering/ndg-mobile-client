package br.org.indt.ndg.mobile;

/**
 * This class encapsulates a file entity.
 * It contains its
 */
public class XmlResultFile {
    private String fileDisplayName;
    private String fileNameInDisc;
    
    public XmlResultFile(String displayName, String fileName){
        this.fileDisplayName = displayName;
        this.fileNameInDisc = fileName;
    }

    public String getDisplayName(){
        return fileDisplayName;
    }

    public String getFileName(){
        return fileNameInDisc;
    }

    public void setDisplayName(String name){
        fileDisplayName = name;
    }

    public void setFileName(String name){
        fileNameInDisc = name;
    }
}

package br.org.indt.ndg.mobile.xmlhandle;

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import br.org.indt.ndg.mobile.structures.FileSystemResultStructure;

public class FileSystemResultHandler extends DefaultHandler {
    
    private FileSystemResultStructure structure;
    private Stack tagStack = new Stack();
    private String filename;
    
    public FileSystemResultHandler() {
    }
    
    public void setFileSystemResultStructure(FileSystemResultStructure _structure) {
        structure = _structure;
    }
    
    public void setFileSystemResultFilename(String _filename) {
        this.filename = _filename;
    }
    
    public void startDocument() throws SAXException {}
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        tagStack.push(qName);
    }
    
    public void characters(char[] ch, int start, int length) throws SAXException {
        String chars = new String(ch, start, length).trim();
        
        if (chars.length() > 0) {
            String qName = (String)tagStack.peek();
            if (qName.equals("title")) {
                structure.addXmlResultFileObj(chars, filename);
                throw new DoneParsingException();
            }
        } 
    }
    
    public void endElement(String uri, String localName, String qName) throws SAXException {}
    
    public void endDocument() throws SAXException {}
}
